package com.example.remindtopay;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tfb.fbtoast.FBToast;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private EditText loginUsername,loginPassword;
    private LinearLayout progressbarLayout;

    private ProgressBar progressBarAnimation;
    private ObjectAnimator progressAnimator;

    private TextView loginlabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if(firebaseAuth.getCurrentUser() != null){
                    String action;
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }

            }
        };




        Button loginButton;
        TextView lblRegister;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = findViewById(R.id.login_btn);
        loginUsername=  (EditText)findViewById(R.id.login_txtUsername);
        loginPassword =  (EditText) findViewById(R.id.login_txtPassword);
        lblRegister = findViewById(R.id.login_registerLbl);
        progressbarLayout = findViewById(R.id.dotsLayout);




        lblRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(myIntent);

            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent myIntent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(myIntent);
                signIn();
            }
        });


        loginlabel = findViewById(R.id.lblLogin);
        loginlabel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = mAuth.getCurrentUser().getEmail()+ ": is the error";
                FBToast.warningToast(LoginActivity.this, user , Toast.LENGTH_LONG);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void signIn() {
        progressbarLayout.setVisibility(View.VISIBLE);
        final String email = loginUsername.getText().toString().trim();
        String password = loginPassword.getText().toString().trim();
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FBToast.successToast(LoginActivity.this,"Successfully Logged In" , FBToast.LENGTH_SHORT);
                    progressbarLayout.setVisibility(View.GONE);


                }
                else {
                    String error = task.getException().toString();
                    FBToast.errorToast(LoginActivity.this, "Error: " +error, Toast.LENGTH_SHORT);
                    progressbarLayout.setVisibility(View.GONE);
                }
            }
        });

    }

}
