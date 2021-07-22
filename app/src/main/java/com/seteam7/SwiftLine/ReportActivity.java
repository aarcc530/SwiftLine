package com.seteam7.SwiftLine;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class ReportActivity extends AppCompatActivity {
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
    String teamStorage;
    int waitTimeStorage;
    String mapsID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        fiveMin = (Button) findViewById(R.id.Button5Minutes);
        tenMin = (Button) findViewById(R.id.Button10Minutes);
        fifteenMin = (Button) findViewById(R.id.Button15Minutes);
        twentyMin = (Button) findViewById(R.id.Button20Minutes);
        thirtyMin = (Button) findViewById(R.id.Button30Minutes);
        fortyFiveMin = (Button) findViewById(R.id.Button45Minutes);
        submit = (Button) findViewById(R.id.submitReportButton);
        team1 = (ImageButton) findViewById(R.id.team1Button);
        team2 = (ImageButton) findViewById(R.id.team2Button);
        backButton = (ImageButton) findViewById(R.id.RefreshButton);

        //TODO

        team1.setOnClickListener(v -> {
            teamStorage = "team1";
        });

        team2.setOnClickListener(v -> {
            teamStorage = "team2";
        });

        fiveMin.setOnClickListener(v -> {
            waitTimeStorage = 5;
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getApplicationContext().getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int primary = typedValue.data;
            theme.resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true);
            int primaryVariant = typedValue.data;
            fiveMin.setBackgroundColor(primaryVariant);
            tenMin.setBackgroundColor(primary);
            fifteenMin.setBackgroundColor(primary);
            twentyMin.setBackgroundColor(primary);
            thirtyMin.setBackgroundColor(primary);
            fortyFiveMin.setBackgroundColor(primary);
        });

        tenMin.setOnClickListener(v -> {
            waitTimeStorage = 10;
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getApplicationContext().getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int primary = typedValue.data;
            theme.resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true);
            int primaryVariant = typedValue.data;
            fiveMin.setBackgroundColor(primary);
            tenMin.setBackgroundColor(primaryVariant);
            fifteenMin.setBackgroundColor(primary);
            twentyMin.setBackgroundColor(primary);
            thirtyMin.setBackgroundColor(primary);
            fortyFiveMin.setBackgroundColor(primary);
        });

        fifteenMin.setOnClickListener(v -> {
            waitTimeStorage = 15;

            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getApplicationContext().getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int primary = typedValue.data;
            theme.resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true);
            int primaryVariant = typedValue.data;
            fiveMin.setBackgroundColor(primary);
            tenMin.setBackgroundColor(primary);
            fifteenMin.setBackgroundColor(primaryVariant);
            twentyMin.setBackgroundColor(primary);
            thirtyMin.setBackgroundColor(primary);
            fortyFiveMin.setBackgroundColor(primary);
        });

        twentyMin.setOnClickListener(v -> {
            waitTimeStorage = 20;

            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getApplicationContext().getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int primary = typedValue.data;
            theme.resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true);
            int primaryVariant = typedValue.data;
            fiveMin.setBackgroundColor(primary);
            tenMin.setBackgroundColor(primary);
            fifteenMin.setBackgroundColor(primary);
            twentyMin.setBackgroundColor(primaryVariant);
            thirtyMin.setBackgroundColor(primary);
            fortyFiveMin.setBackgroundColor(primary);
        });

        thirtyMin.setOnClickListener(v -> {
            waitTimeStorage = 30;

            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getApplicationContext().getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int primary = typedValue.data;
            theme.resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true);
            int primaryVariant = typedValue.data;
            fiveMin.setBackgroundColor(primary);
            tenMin.setBackgroundColor(primary);
            fifteenMin.setBackgroundColor(primary);
            twentyMin.setBackgroundColor(primary);
            thirtyMin.setBackgroundColor(primaryVariant);
            fortyFiveMin.setBackgroundColor(primary);
        });

        fortyFiveMin.setOnClickListener(v -> {
            waitTimeStorage = 45;

            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = getApplicationContext().getTheme();
            theme.resolveAttribute(R.attr.colorPrimary, typedValue, true);
            int primary = typedValue.data;
            theme.resolveAttribute(R.attr.colorPrimaryVariant, typedValue, true);
            int primaryVariant = typedValue.data;
            fiveMin.setBackgroundColor(primary);
            tenMin.setBackgroundColor(primary);
            fifteenMin.setBackgroundColor(primary);
            twentyMin.setBackgroundColor(primary);
            thirtyMin.setBackgroundColor(primary);
            fortyFiveMin.setBackgroundColor(primaryVariant);
        });

        submit.setOnClickListener(v -> {
            DatabaseCtl.sendReport(teamStorage, waitTimeStorage, mapsID);
            Intent intent = new Intent(ReportActivity.this, MapsActivity.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ReportActivity.this, RestaurantActivity.class);
            startActivity(intent);
        });
    }
}
