package com.example.librarymanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    EditText emailId, password;
    Button signUp;
    TextView login;
    FirebaseAuth firebaseAuth;
    ImageButton showHide;
    String s = "show";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        signUp = findViewById(R.id.buttonSignUp);
        login = findViewById(R.id.textViewLogin);
        showHide = findViewById(R.id.buttonShowHide);

        showHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(s == "show")
                {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    s = "hide";
                    showHide.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                }
                else if(s == "hide")
                {
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    s = "show";
                    showHide.setImageDrawable(getResources().getDrawable(R.drawable.show));
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = emailId.getText().toString();
                String pwd = password.getText().toString();

                if(mail.isEmpty() && pwd.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(mail.isEmpty())
                {
                    emailId.setError("Please enter Email ID");
                    emailId.requestFocus();
                    return;
                }
                if(pwd.isEmpty())
                {
                    password.setError("Please enter your password");
                    password.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
                {
                    emailId.setError("Please enter a valid mail Id");
                    emailId.requestFocus();
                    return;
                }
                if(!(mail.isEmpty() && pwd.isEmpty()))
                {
                    firebaseAuth.createUserWithEmailAndPassword(mail, pwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                {
                                    Toast.makeText(getApplicationContext(),"You're already registered!", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });

    }
}