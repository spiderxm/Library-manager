package com.example.librarymanager;

        import androidx.annotation.NonNull;
        import androidx.appcompat.app.AppCompatActivity;

        import android.content.Intent;
        import android.os.Bundle;
        import android.text.method.HideReturnsTransformationMethod;
        import android.text.method.PasswordTransformationMethod;
        import android.util.Log;
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
        import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    EditText email, Password;
    Button signIn;
    ImageButton hideShow;
    TextView signUp;
    FirebaseAuth loginFirebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    String uid, status = "show";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginFirebaseAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.editTextMail);
        Password = findViewById(R.id.editTextPass);
        signIn = findViewById(R.id.buttonSignIn);
        signUp = findViewById(R.id.textViewSignUp);
        hideShow = findViewById(R.id.buttonHideShow);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        hideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status == "show") {
                    Password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    status = "hide";
                    hideShow.setImageDrawable(getResources().getDrawable(R.drawable.hide));
                }
                else if(status == "hide")
                {
                    Password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    status = "show";
                    hideShow.setImageDrawable(getResources().getDrawable(R.drawable.show));
                }
            }
        });

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null)
                {
                    Toast.makeText(getApplicationContext(), "You are logged in!", Toast.LENGTH_SHORT).show();
                    uid = firebaseUser.getUid();
                    Log.e("User id is", uid);
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail = email.getText().toString();
                String pwd = Password.getText().toString();

                if(mail.isEmpty() && pwd.isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "Fields are empty", Toast.LENGTH_SHORT).show();
                }
                else if(mail.isEmpty())
                {
                    email.setError("Please enter Email ID!");
                    email.requestFocus();
                }
                else if(pwd.isEmpty())
                {
                    Password.setError("Please enter your password!");
                    Password.requestFocus();
                }
                else if(!(mail.isEmpty() && pwd.isEmpty()))
                {
                    loginFirebaseAuth.signInWithEmailAndPassword(mail, pwd).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(), "Login Successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loginFirebaseAuth.addAuthStateListener(authStateListener);
    }
}

