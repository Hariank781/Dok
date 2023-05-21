package com.example.doktor;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class MainActivity9 extends AppCompatActivity {
    Spinner spinner_insurance;
    RadioGroup radioGroup2;
    RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main9);
        radioGroup2=findViewById(R.id.radioGroup2);
        Button register=findViewById(R.id.button1page9);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int radioId=radioGroup2.getCheckedRadioButtonId();
                radioButton=findViewById(radioId);
            }
        });

        spinner_insurance=findViewById(R.id.spinner_insurance);
        ArrayAdapter<CharSequence>adapter=ArrayAdapter.createFromResource(this,R.array.insurance_option, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner_insurance.setAdapter(adapter);
        spinner_insurance.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String data=adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public void checkButton(View v)
    {
        int radioId=radioGroup2.getCheckedRadioButtonId();
        radioButton=findViewById(radioId);
    }
}