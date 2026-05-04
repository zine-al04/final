package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Signup extends AppCompatActivity {
EditText mFullName,mEmailAdress,mPassword;
Button mCreatebutton;
TextView mSignInButton;
FirebaseAuth fAuth ;
ProgressBar progressBar ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);


mFullName = findViewById(R.id.FullName);
mEmailAdress = findViewById(R.id.EmailAddress);
mPassword = findViewById(R.id.password);
mCreatebutton = findViewById(R.id.CreateButton);
mSignInButton = findViewById(R.id.SignInButton);

fAuth = FirebaseAuth.getInstance();
progressBar = findViewById(R.id.progressBar);


mCreatebutton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String email = mEmailAdress.getText().toString().trim();
        String password =mPassword.getText().toString().trim();

        // input empty
        if(TextUtils.isEmpty(email)){
            mEmailAdress.setError("Email is required");
            return;
        }
        if(TextUtils.isEmpty(password)){
            mPassword.setError("Password is required");
            return;
        }


        // password less than 6
        if (password.length() < 6){
            mPassword.setError("Password very short !!");
            return;
        }


        progressBar.setVisibility(View.VISIBLE);


        //firebase
        Task<AuthResult> accountCreatedSuccessfully = fAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Signup.this, "account created successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), Listetask.class));
                } else {
                    Toast.makeText(Signup.this, "error !" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
});

        //    signin
        mSignInButton.setOnClickListener(v -> {
            Intent intent =new Intent(Signup.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}