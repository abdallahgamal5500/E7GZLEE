package com.example.e7gzle;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends AppCompatActivity {

    private TextInputLayout email_layout, pass_layout;
    private String email_value, pass_value;
    private Button login_btn;
    private ProgressBar progressBar;
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");

        email_layout = findViewById(R.id.login_layout_email);
        pass_layout = findViewById(R.id.login_layout_pass);
        login_btn = findViewById(R.id.login_btn);
        progressBar=findViewById(R.id.login_progress);

        Circle circle = new Circle();
        progressBar.setIndeterminateDrawable(circle);
        progressBar.setVisibility(View.INVISIBLE);

        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validationEmail()) {
                } else if (!validationPass()) {
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(email_value,pass_value)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        if (mAuth.getCurrentUser().isEmailVerified()) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Intent intent = new Intent(getApplicationContext(), Home.class);
                                            startActivity(intent);
                                            finishAffinity();
                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(Login.this, "Please verify your email", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        if (Internet_Connection.getInstance(getApplicationContext()).isOnline()) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            email_layout.setError("email or password is incorrect");
                                            pass_layout.setError("email or password is incorrect");
                                            email_layout.requestFocus();
                                            pass_layout.requestFocus();
                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(), "No internet connection open WIFI or DATA", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });


    }

    public boolean validationEmail() {
        email_value = email_layout.getEditText().getText().toString();

        if (email_value.isEmpty()) {
            email_layout.setError("Enter your email");
            email_layout.requestFocus();
            return false;
        } else if (!isEmailValid(email_value)) {
            email_layout.setError("Please enter correct email");
            email_layout.requestFocus();
            return false;
        } else {
            email_layout.setError(null);
            return true;
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public boolean validationPass() {
        pass_value = pass_layout.getEditText().getText().toString();

        if (pass_value.isEmpty()) {
            pass_layout.setError("Enter your password");
            pass_layout.requestFocus();
            return false;
        } else if (pass_value.length() < 6) {
            pass_layout.setError("Password should be more than 5 letters");
            pass_layout.requestFocus();
            return false;
        } else {
            pass_layout.setError(null);
            return true;
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}