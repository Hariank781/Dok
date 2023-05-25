package com.example.doktor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity10 extends AppCompatActivity {
    private FirebaseFirestore firestore;
    private TextView detailsTextView;
    private TextView doctorsTextView;
    private BottomNavigationView bottomNavigationView;
    private EditText enterDoctorID;
    private Button sendDoctorID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main10);

        firestore = FirebaseFirestore.getInstance();
        doctorsTextView = findViewById(R.id.doctorsDetails);
        detailsTextView = findViewById(R.id.patientDetails);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        enterDoctorID = findViewById(R.id.enterDoctorID);
        sendDoctorID = findViewById(R.id.sendDoctorID);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_audio:
                    startActivity(new Intent(MainActivity10.this, MainActivity5.class));
                    return true;
                case R.id.menu_video:
                    startActivity(new Intent(MainActivity10.this, MainActivity6.class));
                    return true;
                case R.id.menu_tools:
                    startActivity(new Intent(MainActivity10.this, MainActivity7.class));
                    return true;
                default:
                    return false;
            }
        });

        sendDoctorID.setOnClickListener(view -> {
            String doctorID = enterDoctorID.getText().toString().trim();
            if (!doctorID.isEmpty()) {
                // Check if the Doctor ID exists in the Doctor's field of Firestore
                firestore.collection("Doctors")
                        .whereEqualTo("doctorID", doctorID)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                if (task.getResult().isEmpty()) {
                                    Toast.makeText(MainActivity10.this, "Doctor ID not found.", Toast.LENGTH_SHORT).show();
                                } else {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (user != null) {
                                        String loggedInPatientEmail = user.getEmail();

                                        firestore.collection("Patients")
                                                .whereEqualTo("Email", loggedInPatientEmail)
                                                .get()
                                                .addOnCompleteListener(task1 -> {
                                                    if (task1.isSuccessful()) {
                                                        for (DocumentSnapshot document : task1.getResult()) {
                                                            firestore.collection("Patients")
                                                                    .document(document.getId())
                                                                    .update("DoctorID", doctorID)
                                                                    .addOnSuccessListener(aVoid -> {
                                                                        Toast.makeText(MainActivity10.this, "Doctor ID stored successfully.", Toast.LENGTH_SHORT).show();
                                                                    })
                                                                    .addOnFailureListener(e -> {
                                                                        Toast.makeText(MainActivity10.this, "Failed to store Doctor ID.", Toast.LENGTH_SHORT).show();
                                                                    });
                                                        }
                                                    } else {
                                                        Toast.makeText(MainActivity10.this, "Failed to fetch patient data.", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(MainActivity10.this, "User not logged in.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } else {
                                Toast.makeText(MainActivity10.this, "Failed to check Doctor ID.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(MainActivity10.this, "Please enter a valid Doctor ID.", Toast.LENGTH_SHORT).show();
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