package com.example.moneypay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout rlayout;
    private Animation animation;
    private Button btn_Sign_Up;
    private FirebaseAuth mAuth;
    EditText txtfirstname, txtlastname, txtemail, txtpassword, txtretrypassword, txtmobilenumber, address;
ProgressBar Register_progress_bar;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.bgHeader);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rlayout = findViewById(R.id.rlayout);
        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        rlayout.setAnimation(animation);
        //-------------finding---------------
        btn_Sign_Up = findViewById(R.id.btn_Sign_Up);
        txtfirstname = findViewById(R.id.txtfirstname);
        txtlastname = findViewById(R.id.txtlastname);
        txtemail = findViewById(R.id.txtemail);
        txtpassword = findViewById(R.id.txtpassword);
        txtretrypassword = findViewById(R.id.txtretrypassword);
        txtmobilenumber = findViewById(R.id.txtmobilenumber);
        address = findViewById(R.id.address);
//Firebase----------------
        mAuth = FirebaseAuth.getInstance(); // Initialised
        Register_progress_bar = findViewById(R.id.Register_progress_bar);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void Sign_Up_On_Clicked(View v) {
        if (TextUtils.isEmpty(txtfirstname.getText())) {
            txtfirstname.setError("Please Enter Your First Name");
            txtfirstname.requestFocus();
        } else if (TextUtils.isEmpty(txtlastname.getText())) {
            txtlastname.setError("Please Enter Your Last Name");
            txtlastname.requestFocus();
        } else if (TextUtils.isEmpty(txtemail.getText())) {
            txtemail.setError("Please Enter an Email");
            txtemail.requestFocus();
        } else if (TextUtils.isEmpty(txtpassword.getText())) {
            txtpassword.setError("Enter a password");
            txtpassword.requestFocus();
        } else if (TextUtils.isEmpty(txtretrypassword.getText())) {
            txtretrypassword.setError("Enter a password");
            txtretrypassword.requestFocus();
        } else if (!txtpassword.getText().toString().equals(txtretrypassword.getText().toString())) {
            txtpassword.setError("Password and Retype Password not matching!!!");
            txtpassword.requestFocus();
        } else if (TextUtils.isEmpty(txtmobilenumber.getText())) {
            txtmobilenumber.setError("Enter a password");
            txtmobilenumber.requestFocus();
        } else if (TextUtils.isEmpty(address.getText())) {
            address.setError("Enter a password");
            address.requestFocus();
        }

        //============= Original Code ================
        else {
            ConnectivityManager connectivityManager= (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo=connectivityManager.getActiveNetworkInfo();
            if (networkInfo==null||!networkInfo.isConnected()||!networkInfo.isAvailable())
            {
                //internet is inactive
                Dialog dialog=new Dialog(this);
                dialog.setContentView(R.layout.custom_dialog);
                //outside touch
                dialog.setCanceledOnTouchOutside(false);
                //width and height
                dialog.getWindow().setLayout(WindowManager.LayoutParams.WRAP_CONTENT
                        ,WindowManager.LayoutParams.WRAP_CONTENT);
                //Set transparent background
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//animation
                dialog.getWindow().getAttributes().windowAnimations=android.R.style.Animation_Dialog;
                Button btTryAgain=dialog.findViewById(R.id.bt_try_again);
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
                Register_progress_bar.setVisibility(View.VISIBLE);
                btn_Sign_Up.setText("");
                btn_Sign_Up.setEnabled(false);
                txtfirstname.setEnabled(false);
                txtlastname.setEnabled(false);
                txtemail.setEnabled(false);
                txtpassword.setEnabled(false);
                txtretrypassword.setEnabled(false);
                txtmobilenumber.setEnabled(false);
                address.setEnabled(false);

                final String First_Name = txtfirstname.getText().toString().trim();
                final String Last_Name = txtlastname.getText().toString().trim();
                final String Email = txtemail.getText().toString().trim();
                String Password = txtpassword.getText().toString();
                final String Mobile = txtmobilenumber.getText().toString();
                final String Address = address.getText().toString().trim();

                mAuth.createUserWithEmailAndPassword(Email, Password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    //We Will store the additional fields in firebase database
                                    UserInput userInput = new UserInput(
                                            First_Name,
                                            Last_Name,
                                            Email,
                                            Mobile,
                                            Address
                                    );
                                    FirebaseDatabase.getInstance().getReference("Users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(userInput).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(MainActivity.this, "User Register Successfully ", Toast.LENGTH_SHORT).show();
                                                Intent intent =new Intent(MainActivity.this,HomeSCreen.class);
                                                firebaseDatabase = FirebaseDatabase.getInstance();
                                                databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());
                                                databaseReference.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                        UserInput userInput = snapshot.getValue(UserInput.class);
                                                        Name = userInput.getFirstname();
                                                        Log.i("Name",userInput.getFirstname());
                                                        Toast.makeText(MainActivity.this, "Hello "+ Name, Toast.LENGTH_SHORT).show();
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {
                                                        Toast.makeText(MainActivity.this, error.getCode(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                                startActivity(intent);
                                                Register_progress_bar.setVisibility(View.INVISIBLE);
                                            } else {
                                                Toast.makeText(MainActivity.this, "Please Try Again... With Differnet Email ID ", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }

                                else {
                                    Register_progress_bar.setVisibility(View.INVISIBLE);
                                    txtfirstname.setEnabled(true);
                                    txtlastname.setEnabled(true);
                                    txtemail.setEnabled(true);
                                    txtpassword.setEnabled(true);
                                    txtretrypassword.setEnabled(true);
                                    txtmobilenumber.setEnabled(true);
                                    address.setEnabled(true);
                                    btn_Sign_Up.setText("Sign up");
                                    btn_Sign_Up.setEnabled(true);
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }



        }

    }
}