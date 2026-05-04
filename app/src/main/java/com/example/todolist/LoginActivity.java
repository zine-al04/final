package com.example.todolist;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText mEmail, mPassword ;
    Button mLoginButton ;
    TextView mSignUp ;
    FirebaseAuth fAuth ;
    ProgressBar progressBar ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

    mEmail = findViewById(R.id.EmailLogin);
    mPassword = findViewById(R.id.PasswordLogin);
    mLoginButton = findViewById(R.id.LoginButton);
    mSignUp = findViewById(R.id.SignUpLogin);

    fAuth = FirebaseAuth.getInstance();
    progressBar = findViewById(R.id.progressBar2);




    if(fAuth.getCurrentUser() != null){
        startActivity(new Intent(getApplicationContext(), Listetask.class));
        finish();
    }




    mLoginButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            String email = mEmail.getText().toString().trim();
            String password = mPassword.getText().toString().trim();

            // input empty
            if (TextUtils.isEmpty(email)) {
                mEmail.setError("Email is required");
                return;
            }
            if (TextUtils.isEmpty(password)) {
                mPassword.setError("Password is required");
                return;
            }


            // password less than 6
            if (password.length() < 6) {
                mPassword.setError("Password very short !!");
                return;
            }


            progressBar.setVisibility(View.VISIBLE);

            fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Listetask.class));
                    } else {
                        Toast.makeText(LoginActivity.this, "error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    });



        mSignUp.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), Signup.class));
        });


    }
    }
