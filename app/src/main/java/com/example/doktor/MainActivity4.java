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

public class MainActivity4 extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private TextView patientDetailsTextView;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main4);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        patientDetailsTextView = findViewById(R.id.patientDetailsTextView);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_audio:
                    startActivity(new Intent(MainActivity4.this, MainActivity5.class));
                    return true;
                case R.id.menu_video:
                    startActivity(new Intent(MainActivity4.this, MainActivity6.class));
                    return true;
                case R.id.menu_tools:
                    startActivity(new Intent(MainActivity4.this, MainActivity7.class));
                    return true;
                default:
                    return false;
            }
        });

        // Get the logged-in doctor's user ID
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String doctorId = currentUser.getUid();
            // Query the Patients collection for patients with matching DoctorID
            firestore.collection("Patients")
                    .whereEqualTo("DoctorID", "456")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            StringBuilder detailsBuilder = new StringBuilder();

                            // Iterate over the resulting documents and append patient details to the StringBuilder
                            for (DocumentSnapshot document : task.getResult()) {
                                detailsBuilder.append("Full Name: ").append(document.getString("Full name")).append("\n");
                                detailsBuilder.append("Email: ").append(document.getString("Email")).append("\n");
                                detailsBuilder.append("Medical History: ").append(document.getString("Medical history")).append("\n");
                                detailsBuilder.append("Date of Birth: ").append(document.getString("Date of birth")).append("\n");
                                detailsBuilder.append("Gender: ").append(document.getString("Gender")).append("\n\n");
                            }

                            String patientDetails = detailsBuilder.toString();
                            if (!patientDetails.isEmpty()) {
                                // Display the patient details in the TextView
                                patientDetailsTextView.setText(patientDetails);
                            } else {
                                // No matching patients found
                                patientDetailsTextView.setText("No patients found.");
                            }
                        } else {
                            Toast.makeText(MainActivity4.this, "Failed to fetch patient details.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
