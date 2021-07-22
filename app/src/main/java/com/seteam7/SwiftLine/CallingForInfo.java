package com.seteam7.SwiftLine;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class CallingForInfo extends AppCompatActivity {
    TextView phone;
    Button report;
    TextView name;
    TextView address;
    TextView openingTiming;
    TextView website;
    TextView openClose;
    TextView review;
    RatingBar rate;
    ImageButton backButton;


    public void setData(String address, String phone, String name, String openingTiming,
                        String website, String openClose, float rating) {
        this.address.setText(address);
        this.phone.setText(phone);
        this.name.setText(name);
        this.openingTiming.setText(openingTiming);
        this.website.setText(website);
        this.openClose.setText(openClose);
        this.rate.setRating(rating);
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout1);

        report = (Button)findViewById(R.id.ReportButton);
        phone = (TextView)findViewById(R.id.RestPhone);
        name = (TextView)findViewById(R.id.RestName);
        address = (TextView)findViewById(R.id.TestAddress);
        openingTiming = (TextView)findViewById(R.id.RestOpenTime);
        website = (TextView)findViewById(R.id.RestWebsiteURL);
        openClose = (TextView)findViewById(R.id.RestStatus);
        rate = (RatingBar)findViewById(R.id.ratingBar);
        backButton = (ImageButton)findViewById(R.id.RestBackButton);




        report.setOnClickListener(v -> {
            Intent intent = new Intent(CallingForInfo.this, NextCallingForInfo.class);
            startActivity(intent);
        });

        backButton.setOnClickListener(new View.OnClickListener() {

              public void onClick(View v) {
                  Intent intent = new Intent(CallingForInfo.this, MapsActivity.class);
                  startActivity(intent);
              }
          });

        fillRestScreen(getIntent().getStringExtra("id"));
    }
    public void fillRestScreen(String id) {
        Log.d("SCREEN", "Filling Screen");
        final List<Place.Field> placeFields = Arrays.asList(Place.Field.ID,
                Place.Field.ADDRESS,
                Place.Field.ADDRESS_COMPONENTS,
                Place.Field.BUSINESS_STATUS,
                Place.Field.LAT_LNG,
                Place.Field.NAME,
                Place.Field.OPENING_HOURS,
                Place.Field.PHONE_NUMBER,
                Place.Field.RATING,
                Place.Field.WEBSITE_URI,
                Place.Field.UTC_OFFSET);
        final FetchPlaceRequest request = FetchPlaceRequest.newInstance(id, placeFields);

        DatabaseCtl.placesClient.fetchPlace(request).addOnSuccessListener((response) -> {
            Place place = response.getPlace();
            OpeningHours oh = place.getOpeningHours();
            String hours;
            String open;
            if (oh == null) {
                hours = "Unknown";
                open = "Unknown";
            } else{
                List<String> weekdayText = place.getOpeningHours().getWeekdayText();
                hours = weekdayText.get(LocalDate.now().getDayOfWeek().getValue());
                open = (place.isOpen() ? "Open" : "Closed");
            }
            Uri uri = place.getWebsiteUri();
            String uri_str = (uri == null ? "Unknown" : uri.toString());
            float rating = (place.getRating() == null ? 0f : place.getRating().floatValue());
            setData(place.getAddress(), place.getPhoneNumber(), place.getName(),
                    hours, uri_str, open, rating);
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e("DATA", "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
            }
        });
    }
}
