package com.example.doktor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity7 extends AppCompatActivity {

    private ImageView leftImage1, leftImage2, leftImage3;
    private ImageView rightImage4, rightImage5, rightImage6;

    private ImageView selectedLeftImage;
    private ImageView selectedRightImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main7);

        leftImage1 = findViewById(R.id.Image1);
        leftImage2 = findViewById(R.id.Image2);
        leftImage3 = findViewById(R.id.Image3);
        rightImage4 = findViewById(R.id.Image4);
        rightImage5 = findViewById(R.id.Image5);
        rightImage6 = findViewById(R.id.Image6);

        leftImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRightImage == rightImage5) {
                    matchImages(leftImage1, rightImage5);
                } else {
                    selectedLeftImage = leftImage1;
                }
            }
        });

        leftImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRightImage == rightImage6) {
                    matchImages(leftImage2, rightImage6);
                } else {
                    selectedLeftImage = leftImage2;
                }
            }
        });

        leftImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedRightImage == rightImage4) {
                    matchImages(leftImage3, rightImage4);
                } else {
                    selectedLeftImage = leftImage3;
                }
            }
        });

        rightImage4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLeftImage == leftImage3) {
                    matchImages(leftImage3, rightImage4);
                } else {
                    selectedRightImage = rightImage4;
                }
            }
        });

        rightImage5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLeftImage == leftImage1) {
                    matchImages(leftImage1, rightImage5);
                } else {
                    selectedRightImage = rightImage5;
                }
            }
        });

        rightImage6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedLeftImage == leftImage2) {
                    matchImages(leftImage2, rightImage6);
                } else {
                    selectedRightImage = rightImage6;
                }
            }
        });
    }

    private void matchImages(ImageView leftImage, ImageView rightImage) {
        leftImage.setVisibility(View.INVISIBLE);
        rightImage.setVisibility(View.INVISIBLE);
        Toast.makeText(MainActivity7.this, "Matched!", Toast.LENGTH_SHORT).show();
        selectedLeftImage = null;
        selectedRightImage = null;
    }
}
