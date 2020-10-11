package com.example.moneypay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;

public class HomeSCreen extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
private DrawerLayout drawer;
    TextView navName;
    TextView navEmail;
    TextView navAddress;
    TextView navMobile;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //-------Recycler view
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    List<ListItem> listItems;
    String myResponse;
    LinearLayoutManager manager;
    Boolean isScrolling = true;
    int currentItems,totalItems,scrollOutItems;
    ProgressBar progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_s_creen);
//        Toolbar toolbar =findViewById(R.id.toolbar);
        Toolbar toolbar =findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer=findViewById(R.id.draw_layer);
        NavigationView navigationView = findViewById(R.id.nav_view);

        View headerView= navigationView.getHeaderView(0);
         navName = headerView.findViewById(R.id.txt_name);


        navEmail =headerView.findViewById(R.id.txt_email);


        navAddress= headerView.findViewById(R.id.txt_address);


        navMobile = headerView.findViewById(R.id.txt_mobile);


        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
         databaseReference =FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInput userInput = snapshot.getValue(UserInput.class);
                navName.setText(userInput.getFirstname()+"\t"+userInput.getLastname());
                navEmail.setText(userInput.getEmail());
                navAddress.setText(userInput.getAddress());
                navMobile.setText(userInput.getMobile());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(HomeSCreen.this, error.getCode(), Toast.LENGTH_SHORT).show();
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle =new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
drawer.addDrawerListener(toggle);
toggle.syncState();


//========= Recycler view update ==============
        recyclerView=findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        manager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        listItems=new ArrayList<>();
        recyclerView.smoothScrollToPosition(0);
         progress=findViewById(R.id.progress);
        listItems.clear();

       // ============ Network Check ===================

        ConnectivityManager connectivityManager= (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
        if (networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable()) {
            //internet is inactive
            Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.custom_dialog);
            //outside touch
            dialog.setCanceledOnTouchOutside(false);
            //width and height
            dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT
                    , WindowManager.LayoutParams.WRAP_CONTENT);
            //Set transparent background
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//animation
            dialog.getWindow().getAttributes().windowAnimations = android.R.style.Animation_Dialog;
            Button btTryAgain = dialog.findViewById(R.id.bt_try_again);
            btTryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recreate();
                }
            });
            dialog.show();
        }
        else
        {
            RequestApi();
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.log_out:
                AlertDialog.Builder builder=new AlertDialog.Builder(this);
                builder.setTitle("LOG OUT!!!");
                builder.setMessage("Do You Want To LogOut ?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mAuth.signOut();
                        Intent intent = new Intent(HomeSCreen.this,Login_Page.class);
                        startActivity(intent);
                        Toast.makeText(HomeSCreen.this, "Log Out", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // DO SOMETHING HERE
                        dialog.cancel();

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();



                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START))
        {
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }


    }

      public void RequestApi()
     {
         listItems.clear();
         OkHttpClient client = new OkHttpClient();
         final ProgressDialog progressDialog = new ProgressDialog(this);
         progressDialog.setMessage("Please Wait!\nLoading Data");
         progressDialog.show();

         okhttp3.Request request=new okhttp3.Request.Builder()
                 .url("https://reqres.in/api/users?page=1")
                 .get()
                 .build();

         Log.i("HTTPS ","Request");

         client.newCall(request).enqueue(new Callback() {
             @Override
             public void onFailure(@NotNull Call call, @NotNull IOException e) {
                 Log.i("Onfailed","Failed");
             }

             @Override
             public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                 if (response.isSuccessful())
                 {
                     Log.i("OnResponse","SuccessFull");

                     progressDialog.dismiss();

                     myResponse=response.body().string();
                     try {

                         JSONObject jsonObject =new JSONObject(myResponse);
                         JSONArray array=jsonObject.getJSONArray("data");
                         if (array.length()==0) {
                             Toast.makeText(HomeSCreen.this, "Nothing Found in the 'API'", Toast.LENGTH_SHORT).show();
                         }
                         else
                         {
                             for (int i = 0; i < array.length(); i++)
                             {
                                 JSONObject o = array.getJSONObject(i);

                                 Log.i("id",o.getString("id"));
                                 Log.i("email",o.getString("email"));
                                 Log.i("first",o.getString("first_name"));
                                 Log.i("last",  o.getString("last_name"));
                                 Log.i("img",o.getString("avatar"));
                                // Log.i("id",o.getString("id"));

                                 ListItem item = new ListItem(
                                         o.getString("id"),
                                         o.getString("email"),
                                         o.getString("first_name"),
                                         o.getString("last_name"),
                                         o.getString("avatar")

                                 );

                                 listItems.add(item);
                             }
                             HomeSCreen.this.runOnUiThread(new Runnable() {
                                 @Override
                                 public void run() {
                                     Collections.sort(listItems, new Comparator<ListItem>() {
                                         @Override
                                         public int compare(ListItem o1, ListItem o2) {
                                             return o1.getFirst_Name().compareTo(o2.getFirst_Name());
                                         }
                                     });
                                     adapter = new MyAdapter(listItems, getApplicationContext());
                                     recyclerView.setAdapter(adapter);
                                 }
                             });


                         }


                     }catch (Exception e)
                     {

                     }


                 }
                 else
                 {
                     Log.i("Skipped","Error");
                 }
             }
         });



     }
}