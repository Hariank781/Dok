package com.example.doktor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class MainActivity10 extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private TextView detailsTextView;
    private TextView doctorsTextView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main10);

        firestore = FirebaseFirestore.getInstance();
        doctorsTextView = findViewById(R.id.doctorsDetails);
        detailsTextView = findViewById(R.id.patientDetails);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_audio:
                    startActivity(new Intent(MainActivity10.this, MainActivity11.class));
                    return true;
                case R.id.menu_video:
                    startActivity(new Intent(MainActivity10.this, MainActivity12.class));
                    return true;
                case R.id.menu_tools:
                    startActivity(new Intent(MainActivity10.this, MainActivity13.class));
                    return true;
                default:
                    return false;
            }
        });

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user != null) {
            String loggedInPatientEmail = user.getEmail();

            firestore.collection("Patients")
                    .whereEqualTo("Email", loggedInPatientEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                String fullName = document.getString("Full name");
                                String email = document.getString("Email");
                                String medicalHistory = document.getString("Medical history");
                                String dob = document.getString("Date of birth");
                                String gender = document.getString("Gender");
                                String insurance = document.getString("Insurance");

                                String patientDetails = "Full Name: " + fullName + "\n"
                                        + "Email: " + email + "\n"
                                        + "Medical History: " + medicalHistory + "\n"
                                        + "Date of Birth: " + dob + "\n"
                                        + "Gender: " + gender + "\n"
                                        + "Insurance: " + insurance + "\n\n";

                                detailsTextView.setText(patientDetails);
                            }
                        } else {
                            Toast.makeText(MainActivity10.this, "Failed to fetch patient data.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(MainActivity10.this, "User not logged in.", Toast.LENGTH_SHORT).show();
        }

        firestore.collection("Doctors").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder doctorsData = new StringBuilder();
                        for (DocumentSnapshot document : task.getResult()) {
                            Doctor doctor = document.toObject(Doctor.class);
                            doctorsData.append("Full Name: ").append(doctor.getFullName()).append("\n");
                            doctorsData.append("Email: ").append(doctor.getEmail()).append("\n");
                            doctorsData.append("Specialization: ").append(doctor.getSpecialization()).append("\n");
                            doctorsData.append("Doctor ID: ").append(doctor.getDoctorID()).append("\n");
                            doctorsData.append("Date of Birth: ").append(doctor.getDateOfBirth()).append("\n");
                            doctorsData.append("Gender: ").append(doctor.getGender()).append("\n");
                            doctorsData.append("\n");
                        }
                        doctorsTextView.setText(doctorsData.toString());
                    } else {
                        Toast.makeText(MainActivity10.this, "Failed to fetch doctor data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}