package com.example.moneypay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
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
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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

public class Login_Page extends AppCompatActivity {
    private ImageButton btRegister;
    private TextView tvLogin;
    Button btn_Login;
    EditText txtEmail,txtpassword;
    FirebaseAuth mAuth;
    ProgressBar Progress_Login_in;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    String Name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__page);
        tvLogin=findViewById(R.id.tvLogin);
     btRegister=findViewById(R.id.btRegisterAdd);
        btn_Login=findViewById(R.id.btn_Login);
        txtEmail=findViewById(R.id.txtEmail);
        txtpassword=findViewById(R.id.txtpassword);
        Progress_Login_in=findViewById(R.id.Progress_Login_in);

        mAuth= FirebaseAuth.getInstance();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() !=null)
        {

            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    UserInput userInput = snapshot.getValue(UserInput.class);
                    Name = userInput.getFirstname();
                    Log.i("Name",userInput.getFirstname());
                    Toast.makeText(Login_Page.this, "Hello "+Name, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Login_Page.this, error.getCode(), Toast.LENGTH_SHORT).show();
                }
            });
            Intent intent = new Intent(Login_Page.this,HomeSCreen.class);
            startActivity(intent);
        }
    }
    public void onClick(View v)
    {
        if (v==btRegister){
            Intent intent   = new Intent(Login_Page.this,MainActivity.class);
            Pair[] pairs    = new Pair[1];
            pairs[0] = new Pair<View,String>(tvLogin,"tvLogin");
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(Login_Page.this,pairs);
            startActivity(intent,activityOptions.toBundle());
        }
    }
    public void LoginOnClick(View v)
    {

if (TextUtils.isEmpty(txtEmail.getText()))
{
    txtEmail.setError("Please Enter Email");
    txtEmail.requestFocus();
}
else if (TextUtils.isEmpty(txtpassword.getText()))
{
    txtpassword.setError("Please Enter Password");
    txtpassword.requestFocus();
}else
{
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
        txtEmail.setEnabled(false);
        txtpassword.setEnabled(false);
        Progress_Login_in.setVisibility(View.VISIBLE);
        btn_Login.setEnabled(false);
        btn_Login.setText("");
        String Email = txtEmail.getText().toString().trim();
        String Password = txtpassword.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful())
                {
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getUid());
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserInput userInput = snapshot.getValue(UserInput.class);
                            Name = userInput.getFirstname();
                            Log.i("Name",userInput.getFirstname());
                            Toast.makeText(Login_Page.this, "Hello "+Name, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Login_Page.this, error.getCode(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    Intent intent =new Intent(Login_Page.this,HomeSCreen.class);
                    startActivity(intent);

                }
                else
                {
                    txtEmail.setEnabled(true);
                    txtpassword.setEnabled(true);
                    Progress_Login_in.setVisibility(View.GONE);
                    btn_Login.setEnabled(true);
                    btn_Login.setText("Login");
                    Toast.makeText(Login_Page.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}

    }
}