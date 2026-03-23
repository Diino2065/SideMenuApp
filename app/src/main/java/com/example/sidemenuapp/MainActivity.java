package com.example.sidemenuapp;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.sidemenuapp.R;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private LinearLayout menuLayout;
    private FrameLayout contentFrame;
    private ActionBarDrawerToggle drawerToggle;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuLayout = findViewById(R.id.left_drawer);
        contentFrame = findViewById(R.id.content_frame);

        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override public void handleOnBackPressed() {
                if (drawerLayout == null) return;

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        setupDrawerToggle();
        setupMenu();
        setupContent();
    }

    private void setupContent() {
        TextView contentText = new TextView(this);
        contentText.setText("Welcome to Side Menu App");
        contentText.setTextSize(24);
        contentText.setPadding(40,40,40,40);
        contentFrame.addView(contentText);
    }

    private void setupDrawerToggle() {
        drawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.app_name,
                R.string.app_name );
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (drawerLayout != null) {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupMenu() {
        addProfileHeader();
        addMenuDivider();

        String[][] menuItems = {
                {"Home", "Main dashboard"},
                {"Profile", "View your profile"},
                {"Settings", "App preferences"},
                {"Favorites", "Your saved items"},
                {"Notifications", "Message center"},
                {"Logout", "Sign out"}
        };

        for (String[] item : menuItems) {
            addMenuItemWithDescription(item[0], item[1]);
        }
    }

    private void addProfileHeader() {
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(LinearLayout.VERTICAL);
        headerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT ));
        headerLayout.setBackgroundColor(Color.parseColor("#2196F3"));
        headerLayout.setPadding(40,30,40,30);

        TextView nameText = new TextView(this);
        nameText.setText("John Doe");
        nameText.setTextSize(20);
        nameText.setTextColor(Color.WHITE);
        nameText.setGravity(GravityCompat.START);
        headerLayout.addView(nameText);

        TextView emailText = new TextView(this);
        emailText.setText("john.doe@example.com");
        emailText.setTextSize(12);
        emailText.setTextColor(Color.parseColor("#E3F2FD"));
        emailText.setPadding(0,10,0,0);
        headerLayout.addView(emailText);

        menuLayout.addView(headerLayout);
    }

    private void addMenuDivider() {
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2 ));
        divider.setBackgroundColor(Color.parseColor("#E0E0E0"));
        menuLayout.addView(divider);
    }

    private void addMenuItemWithDescription(String title, String description) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT ));
        itemLayout.setPadding(40,20,40,20);
        itemLayout.setClickable(true);

        itemLayout.setOnClickListener(v -> {
            Toast.makeText(this, title + " clicked", Toast.LENGTH_SHORT).show();
            drawerLayout.closeDrawers();
        });

        TextView titleText = new TextView(this);
        titleText.setText(title);
        titleText.setTextSize(16);
        titleText.setTextColor(Color.parseColor("#212121"));
        titleText.setGravity(GravityCompat.START);
        itemLayout.addView(titleText);

        TextView descText = new TextView(this);
        descText.setText(description);
        descText.setTextSize(12);
        descText.setTextColor(Color.parseColor("#757575"));
        descText.setPadding(0,5,0,0);
        descText.setGravity(GravityCompat.START);
        itemLayout.addView(descText);

        menuLayout.addView(itemLayout);
    }
}
