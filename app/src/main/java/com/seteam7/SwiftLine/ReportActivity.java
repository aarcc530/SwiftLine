package com.seteam7.SwiftLine;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

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
    ImageView teamBreakdown;
    TextView restName;
    String teamStorage = null;
    int waitTimeStorage = -1;


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
        backButton = (ImageButton) findViewById(R.id.ReportBackButton);
        teamBreakdown = (ImageView) findViewById(R.id.ReportRestIcon);
        restName = (TextView) findViewById(R.id.ReportRestName);

        fillScreen(getIntent().getStringExtra("id"), getIntent().getDoubleExtra("ratio", 0.5), getIntent().getIntExtra("waitTime", -1));

        team1.setImageResource(this.getResources().getIdentifier("team_icon1", "drawable", this.getPackageName()));
        team2.setImageResource(this.getResources().getIdentifier("team_icon2", "drawable", this.getPackageName()));

        team1.setOnClickListener(v -> {
            teamStorage = "team1";
            team1.setImageResource(this.getResources().getIdentifier("select_team_icon1", "drawable", this.getPackageName()));
            team2.setImageResource(this.getResources().getIdentifier("team_icon2", "drawable", this.getPackageName()));
        });

        team2.setOnClickListener(v -> {
            teamStorage = "team2";
            team1.setImageResource(this.getResources().getIdentifier("team_icon1", "drawable", this.getPackageName()));
            team2.setImageResource(this.getResources().getIdentifier("select_team_icon2", "drawable", this.getPackageName()));
        });

        fiveMin.setOnClickListener(v -> {
            waitTimeStorage = 5;
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = this.getTheme();
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
            Resources.Theme theme = this.getTheme();
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
            Resources.Theme theme = this.getTheme();
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
            Resources.Theme theme = this.getTheme();
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
            Resources.Theme theme = this.getTheme();
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
            Resources.Theme theme = this.getTheme();
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
            DatabaseCtl.sendReport(teamStorage, waitTimeStorage, getIntent().getStringExtra("id"));
            Intent intent = new Intent(ReportActivity.this, MapsActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            intent.putExtra("ratio", getIntent().getDoubleExtra("ratio", 0.5));
            intent.putExtra("waitTime", getIntent().getIntExtra("waitTime", -1));
            startActivity(intent);
        });

        backButton.setOnClickListener(v -> {
            Intent intent = new Intent(ReportActivity.this, RestaurantActivity.class);
            intent.putExtra("id", getIntent().getStringExtra("id"));
            intent.putExtra("ratio", getIntent().getDoubleExtra("ratio", 0.5));
            intent.putExtra("waitTime", getIntent().getIntExtra("waitTime", -1));
            startActivity(intent);
        });
    }

    public void fillScreen(String id, double ratio, int calcWaitTime) {
        Log.d("SCREEN", "Filling Screen");
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(id, placeFields);
        DatabaseCtl.placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            this.restName.setText(place.getName());
            this.teamBreakdown.setImageResource(DatabaseCtl.getCorrectIconReport(ratio, this));
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e("DATA", "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
            }
        });
    }
}
