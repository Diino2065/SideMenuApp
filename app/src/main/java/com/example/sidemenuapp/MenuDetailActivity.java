package com.example.sidemenuapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;

public class MenuDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = getIntent().getStringExtra("menu_title");
        String description = getIntent().getStringExtra("menu_description");

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(40, 60, 40, 40);
        root.setBackgroundColor(Color.WHITE);

        TextView titleView = new TextView(this);
        titleView.setText(title != null ? title : "Menu");
        titleView.setTextSize(24);
        titleView.setTextColor(Color.parseColor("#212121"));
        titleView.setGravity(GravityCompat.START);
        root.addView(titleView);

        TextView descView = new TextView(this);
        descView.setText(description != null ? description : "Nema opisa za ovaj meni.");
        descView.setTextSize(16);
        descView.setTextColor(Color.parseColor("#616161"));
        descView.setPadding(0, 20, 0, 0);
        descView.setGravity(GravityCompat.START);
        root.addView(descView);

        setContentView(root);
    }
}
