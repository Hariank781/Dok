package com.example.doktor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity3 extends AppCompatActivity {

    FirebaseFirestore firestore;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button register_button_doctorregister;
    EditText fullname, username, password, retypepassword, specialization, medicallicenseno, dd, mm, yyyy;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main3);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        register_button_doctorregister = findViewById(R.id.button1page3);
        fullname = findViewById(R.id.doctorFullname);
        username = findViewById(R.id.doctorUsername);
        password = findViewById(R.id.doctorPassword);
        retypepassword = findViewById(R.id.doctorRetypepassword);
        specialization = findViewById(R.id.doctorSpecialization);
        medicallicenseno = findViewById(R.id.doctorMedicallicenseno);
        dd = findViewById(R.id.doctorDD);
        mm = findViewById(R.id.doctorMM);
        yyyy = findViewById(R.id.doctorYYYY);

        radioGroup = findViewById(R.id.radioGroup);

        register_button_doctorregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Fullname = fullname.getText().toString().trim();
                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Retypepassword = retypepassword.getText().toString().trim();
                String Specialization = specialization.getText().toString().trim();
                String Medicallicenseno = medicallicenseno.getText().toString().trim();
                String DD = dd.getText().toString().trim();
                String MM = mm.getText().toString().trim();
                String YYYY = yyyy.getText().toString().trim();

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String Radiobutton = radioButton.getText().toString().trim();

                // Check if any field is empty
                if (Fullname.isEmpty() || Username.isEmpty() || Password.isEmpty() || Retypepassword.isEmpty() ||
                        Specialization.isEmpty() || Medicallicenseno.isEmpty() || Radiobutton.isEmpty() ||
                        DD.isEmpty() || MM.isEmpty() || YYYY.isEmpty()) {
                    Toast.makeText(MainActivity3.this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password and retype password match
                if (!Password.equals(Retypepassword)) {
                    Toast.makeText(MainActivity3.this, "Passwords do not match. Please retype the password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create user with email and password
                auth.createUserWithEmailAndPassword(Username, Password)
                        .addOnCompleteListener(MainActivity3.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registration successful
                                    Toast.makeText(MainActivity3.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    insertData();  // Save additional data to Firestore

                                    // Proceed to MainActivity2 (login page)
                                    Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                                    startActivity(intent);
                                    finish();  // Optional: Prevents returning to the registration page when pressing back
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        // User with the same email already exists
                                        Toast.makeText(MainActivity3.this, "Email is already registered.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Registration failed
                                        Toast.makeText(MainActivity3.this, "Registration failed. Make sure password is atleast 6 characters long.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    public void insertData() {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Map<String, String> items = new HashMap<>();
        items.put("Full name", fullname.getText().toString().trim());
        items.put("Email", username.getText().toString().trim());
        items.put("Password", password.getText().toString().trim());
        items.put("Specialization", specialization.getText().toString().trim());
        items.put("Doctor ID", medicallicenseno.getText().toString().trim());
        items.put("Date of birth", dd.getText().toString().trim() + "/" + mm.getText().toString().trim() + "/" + yyyy.getText().toString().trim());
        items.put("Gender", radioButton.getText().toString().trim());
        firestore.collection("Doctors").add(items);
    }
}
