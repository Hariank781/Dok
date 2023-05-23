package com.example.doktor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity10 extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private TextView detailsTextView;
    private TextView doctorsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main10);

        firestore = FirebaseFirestore.getInstance();
        doctorsTextView = findViewById(R.id.doctorsDetails);
        detailsTextView = findViewById(R.id.patientDetails);

        firestore.collection("Patients").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder details = new StringBuilder();

                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
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

                                details.append(patientDetails);
                            }
                        }

                        detailsTextView.setText(details.toString());
                    } else {
                        Toast.makeText(MainActivity10.this, "Failed to fetch patient data.", Toast.LENGTH_SHORT).show();
                    }
                });
        firestore.collection("Doctors").get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        StringBuilder doctorsData = new StringBuilder();
                        for (QueryDocumentSnapshot document : task.getResult()) {
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
                    }
                    else {
                        Toast.makeText(MainActivity10.this, "Failed to fetch doctor data.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}