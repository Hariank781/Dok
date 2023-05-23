package com.example.doktor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity2 extends AppCompatActivity {
    Button login_button_doctorlogin, register_button_doctorlogin;
    TextView username, password;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main2);

        auth = FirebaseAuth.getInstance();

        login_button_doctorlogin = findViewById(R.id.button1page2);
        register_button_doctorlogin = findViewById(R.id.button2page2);
        username = findViewById(R.id.username_page2);
        password = findViewById(R.id.password_page2);

        login_button_doctorlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();

                // Check if username and password are provided
                if (Username.isEmpty() || Password.isEmpty()) {
                    Toast.makeText(MainActivity2.this, "Please enter username and password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Sign in the user with email and password
                auth.signInWithEmailAndPassword(Username, Password)
                        .addOnCompleteListener(MainActivity2.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Login successful
                                    Toast.makeText(MainActivity2.this, "Login successful", Toast.LENGTH_SHORT).show();

                                    // Proceed to MainActivity4
                                    Intent intent = new Intent(MainActivity2.this, MainActivity4.class);
                                    startActivity(intent);
                                    finish();  // Optional: Prevents returning to the login page when pressing back
                                } else {
                                    // Login failed, display an error message
                                    Toast.makeText(MainActivity2.this, "Invalid username or password.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        register_button_doctorlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                startActivity(intent);
            }
        });
    }
}
