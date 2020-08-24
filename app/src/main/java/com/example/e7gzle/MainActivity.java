package com.example.e7gzle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Button btn_signup,btn_login;
    private FirebaseUser firebaseUser;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setBackgroundDrawableResource(R.drawable.background_1);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        btn_signup=findViewById(R.id.main_signup);
        btn_login=findViewById(R.id.main_login);

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Signup.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
            }
        });


        if (Internet_Connection.getInstance(getApplicationContext()).isOnline()) {
            keepLogIN();
        } else {
            Toast.makeText(getApplicationContext(), "No internet connection open WIFI or DATA, then restart the application", Toast.LENGTH_LONG).show();
        }


    }

    private void keepLogIN () {
        if (firebaseUser != null) {
            if (firebaseUser.isEmailVerified()) {
                startActivity(new Intent(getApplicationContext(),Home.class));
                finishAffinity();
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
