package com.seteam7.SwiftLine;

import android.content.Intent;
import android.media.Rating;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.libraries.places.api.model.OpeningHours;
import com.google.type.LatLng;

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

    public void setData(String address, String phone, String name, String openingTiming,
                        String website, String openClose, String review, float rating) {
        this.address.setText(address);
        this.phone.setText(phone);
        this.name.setText(name);
        this.openingTiming.setText(openingTiming);
        this.website.setText(website);
        this.openClose.setText(openClose);
        this.review.setText(review);
        this.rate.setRating(rating);
    }

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout1);

        report = (Button)findViewById(R.id.button);
        phone = (TextView)findViewById(R.id.TextPhone);
        name = (TextView)findViewById(R.id.TextTextPersonName);
        address = (TextView)findViewById(R.id.TextTextEmailAddress2);
        openingTiming = (TextView)findViewById(R.id.TextTime);
        website = (TextView)findViewById(R.id.TextTextPersonName2);
        openClose = (TextView)findViewById(R.id.TextTextPersonName3);
        review = (TextView)findViewById(R.id.TextTextMultiLine);
        rate = (RatingBar) findViewById(R.id.ratingBar);


        //TODO

        report.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v)
            {
                Intent intent = new Intent(CallingForInfo.this, NextCallingForInfo.class);
                startActivity(intent);
            }
        });
    }
}
