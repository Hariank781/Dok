//Patient Registration
package com.example.doktor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class MainActivity9 extends AppCompatActivity {
    FirebaseFirestore firestore;
    Spinner spinner_insurance;
    RadioGroup radioGroup;
    RadioButton radioButton;
    Button register_button_patientregister;
    EditText fullname, username, password, retypepassword, medicalhistory, dd, mm, yyyy;
    private FirebaseAuth auth;

    private String selectedInsurance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main9);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        register_button_patientregister = findViewById(R.id.button1page9);
        fullname = findViewById(R.id.patientFullname);
        username = findViewById(R.id.patientUsername);
        password = findViewById(R.id.patientPassword);
        retypepassword = findViewById(R.id.patientRetypepassword);
        medicalhistory = findViewById(R.id.patientMedicalhistory);
        dd = findViewById(R.id.patientDD);
        mm = findViewById(R.id.patientMM);
        yyyy = findViewById(R.id.patientYYYY);

        radioGroup = findViewById(R.id.radioGroup2);

        register_button_patientregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Fullname = fullname.getText().toString().trim();
                String Username = username.getText().toString().trim();
                String Password = password.getText().toString().trim();
                String Retypepassword = retypepassword.getText().toString().trim();
                String Medicalhistory = medicalhistory.getText().toString().trim();
                String DD = dd.getText().toString().trim();
                String MM = mm.getText().toString().trim();
                String YYYY = yyyy.getText().toString().trim();

                int radioId = radioGroup.getCheckedRadioButtonId();
                radioButton = findViewById(radioId);
                String Radiobutton = radioButton.getText().toString().trim();

                // Check if any field is empty
                if (Fullname.isEmpty() || Username.isEmpty() || Password.isEmpty() || Retypepassword.isEmpty() || Medicalhistory.isEmpty() || Radiobutton.isEmpty() ||
                        DD.isEmpty() || MM.isEmpty() || YYYY.isEmpty()) {
                    Toast.makeText(MainActivity9.this, "Please fill in all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check if password and retype password match
                if (!Password.equals(Retypepassword)) {
                    Toast.makeText(MainActivity9.this, "Passwords do not match. Please retype the password.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Create user with email and password
                auth.createUserWithEmailAndPassword(Username, Password)
                        .addOnCompleteListener(MainActivity9.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Registration successful
                                    Toast.makeText(MainActivity9.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                    String userId = auth.getCurrentUser().getUid(); // Get the user ID
                                    insertData(userId);  // Save additional data to Firestore using the user ID

                                    // Proceed to MainActivity8 (login page)
                                    Intent intent = new Intent(MainActivity9.this, MainActivity8.class);
                                    startActivity(intent);
                                    finish();  // Optional: Prevents returning to the registration page when pressing back
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        // User with the same email already exists
                                        Toast.makeText(MainActivity9.this, "Email is already registered.", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // Registration failed
                                        Toast.makeText(MainActivity9.this, "Registration failed. Make sure the password is at least 6 characters long.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }
        });

        spinner_insurance = findViewById(R.id.patientInsurance);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.insurance_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_insurance.setAdapter(adapter);
        spinner_insurance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedInsurance = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void checkButton(View v) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
    }

    public void insertData(String userId) {
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        Map<String, String> items = new HashMap<>();
        items.put("Full name", fullname.getText().toString().trim());
        items.put("Email", username.getText().toString().trim());
        items.put("Password", password.getText().toString().trim());
        items.put("Medical history", medicalhistory.getText().toString().trim());
        items.put("Date of birth", dd.getText().toString().trim() + "/" + mm.getText().toString().trim() + "/" + yyyy.getText().toString().trim());
        items.put("Gender", radioButton.getText().toString().trim());
        items.put("Insurance", selectedInsurance); // Use the selectedInsurance variable
        items.put("DoctorID", "");
        firestore.collection("Patients").document(userId).set(items); // Use the user ID as the document ID
    }
}