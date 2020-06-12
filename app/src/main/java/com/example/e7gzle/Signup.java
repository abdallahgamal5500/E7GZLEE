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

import Models.User;

public class Signup extends AppCompatActivity {

    private TextInputLayout fname_layout, lname_layout ,email_layout, pass_layout, con_pass_layout;
    private String fname_value, lname_value, full_name_value, email_value, pass_value, con_pass_value, uid;
    private Button btn;
    private ProgressBar progressBar;
    private Circle circle;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        fname_layout=findViewById(R.id.signup_layout_fname);
        lname_layout=findViewById(R.id.signup_layout_lname);
        email_layout=findViewById(R.id.signup_layout_email);
        pass_layout=findViewById(R.id.signup_layout_pass);
        con_pass_layout=findViewById(R.id.signup_layout_con_pass);
        btn=findViewById(R.id.signup_btn);
        progressBar=findViewById(R.id.signup_progress);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        circle = new Circle();
        progressBar.setIndeterminateDrawable(circle);
        progressBar.setVisibility(View.INVISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validationFname()) {
                } else if (!validationlname()) {
                } else if (!validationEmail()) {
                } else if (!validationPass()) {
                } else if (!validationCon_pass()) {
                } else if (!pass_value.equals(con_pass_value)) {
                    pass_layout.setError("The two passwords should be equals");
                    con_pass_layout.setError("The two passwords should be equals");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    full_name_value = fname_value + " " + lname_value;
                    mAuth.createUserWithEmailAndPassword(email_value,pass_value)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        mAuth.getCurrentUser().sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            uid = mAuth.getUid();
                                                            User user = new User(full_name_value,fname_value,lname_value,email_value);
                                                            myRef.child(uid).child("Personal_Info").setValue(user)
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {
                                                                        progressBar.setVisibility(View.INVISIBLE);
                                                                        Toast.makeText(Signup.this, "Done verify your email", Toast.LENGTH_SHORT).show();
                                                                        Intent intent = new Intent(getApplicationContext(), Login.class);
                                                                        startActivity(intent);
                                                                        finishAffinity();
                                                                    } else {
                                                                        String masse = task.getException().toString();
                                                                        Toast.makeText(Signup.this, "" + masse, Toast.LENGTH_LONG).show();
                                                                    }
                                                                }
                                                            });
                                                        } else {
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                            Toast.makeText(Signup.this, "Verification error", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    } else {
                                        if (Internet_Connection.getInstance(getApplicationContext()).isOnline()) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            email_layout.setError("This email is already exist");
                                            email_layout.requestFocus();
                                        } else {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getApplicationContext(),"No internet connection open WIFI or DATA",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                            });
                }
            }
        });
    }

    private final static Pattern PATTERNNAME = Pattern.compile("[\u0600-\u065F\u066A-\u06EF\u06FA-\u06FFa-zA-Z ]+[\u0600-\u065F\u066A-\u06EF\u06FA-\u06FFa-zA-Z- ]");

    public boolean validationFname() {
        fname_value = fname_layout.getEditText().getText().toString();
        if (fname_value.isEmpty()) {
            fname_layout.setError("Please enter your first name");
            fname_layout.requestFocus();
            return false;
        } else if (!PATTERNNAME.matcher(fname_value).matches()) {
            fname_layout.setError("Please enter alphabet letters only");
            return false;
        } else {
            fname_layout.setError(null);
            return true;
        }
    }

    public boolean validationlname() {
        lname_value = lname_layout.getEditText().getText().toString();
        if (lname_value.isEmpty()) {
            lname_layout.setError("Please enter your last name");
            lname_layout.requestFocus();
            return false;
        } else if (!PATTERNNAME.matcher(lname_value).matches()) {
            lname_layout.setError("Please enter alphabet letters only");
            return false;
        } else {
            lname_layout.setError(null);
            return true;
        }
    }

    public boolean validationEmail() {
        email_value = email_layout.getEditText().getText().toString();

        if (email_value.isEmpty()) {
            email_layout.setError("Enter your email");
            email_layout.requestFocus();
            return false;
        } else if (!isEmailValid(email_value)) {
            email_layout.setError("Please enter a correct email");
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

    public boolean validationCon_pass() {
        con_pass_value = con_pass_layout.getEditText().getText().toString();

        if (con_pass_value.isEmpty()) {
            con_pass_layout.setError("Confirm your password");
            con_pass_layout.requestFocus();
            return false;
        } else if (con_pass_value.length() < 6) {
            con_pass_layout.setError("Password should be more than 5 letters");
            con_pass_layout.requestFocus();
            return false;
        } else {
            con_pass_layout.setError(null);
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