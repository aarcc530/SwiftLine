package com.seteam7.SwiftLine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NextCallingForInfo extends AppCompatActivity {
    Button fiveMin;
    Button tenMin;
    Button fifteenMin;
    Button twentyMin;
    Button thirtyMin;
    Button fortyFiveMin;
    Button submit;
    ImageButton team1;
    ImageButton team2;
    ImageButton backButton;
    String storage1;
    int storage2;
    String mapsID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout2);

        fiveMin = (Button) findViewById(R.id.button2);
        tenMin = (Button) findViewById(R.id.button3);
        fifteenMin = (Button) findViewById(R.id.button4);
        twentyMin = (Button) findViewById(R.id.button5);
        thirtyMin = (Button) findViewById(R.id.button6);
        fortyFiveMin = (Button) findViewById(R.id.button7);
        submit = (Button) findViewById(R.id.button8);
        team1 = (ImageButton) findViewById(R.id.imageButton);
        team2 = (ImageButton) findViewById(R.id.imageButton2);
        backButton = (ImageButton) findViewById(R.id.imageButton4);

        //TODO

        team1.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                storage1 = "team1";
            }
        });

        team2.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                storage1 = "team2";
            }
        });

        fiveMin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                storage2 = 5;
            }
        });

        tenMin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                storage2 = 10;
            }
        });

        fifteenMin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                storage2 = 15;
            }
        });

        twentyMin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                storage2 = 20;
            }
        });

        thirtyMin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                storage2 = 30;
            }
        });

        fortyFiveMin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                storage2 = 45;
            }
        });

        //MapsID

        submit.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                DatabaseCtl.sendReport(storage1, storage2, mapsID);
                Intent intent = new Intent(NextCallingForInfo.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(NextCallingForInfo.this, CallingForInfo.class);
                startActivity(intent);
            }
        });
    }
}
