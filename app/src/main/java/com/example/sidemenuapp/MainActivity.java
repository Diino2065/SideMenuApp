package com.example.sidemenuapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.drawerlayout.widget.DrawerLayout;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private LinearLayout menuLayout;
    private FrameLayout contentFrame;
    private boolean isAltBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        menuLayout = findViewById(R.id.left_drawer);
        contentFrame = findViewById(R.id.content_frame);


        // ako se ne pronadju layoutovi zatvori aktivnost
        if (drawerLayout == null || menuLayout == null || contentFrame == null) {
            finish();
            return;
        }

        // Samo left swipe sa drzanjem
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        increaseDrawerEdgeSensitivity();

        // Back zatvara samo ako je neki otvoren
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                }
            }
        });

        setupMenu();
        setupContent();
    }

    private void increaseDrawerEdgeSensitivity() {
        try {
            Field leftDraggerField = DrawerLayout.class.getDeclaredField("mLeftDragger");
            leftDraggerField.setAccessible(true);
            ViewDragHelper leftDragger = (ViewDragHelper) leftDraggerField.get(drawerLayout);

            Field edgeSizeField = ViewDragHelper.class.getDeclaredField("mEdgeSize");
            edgeSizeField.setAccessible(true);

            int currentEdge = edgeSizeField.getInt(leftDragger);
            int newEdge = Math.max(currentEdge * 8, dpToPx(120));
            edgeSizeField.setInt(leftDragger, newEdge);
        } catch (Exception ignored) {

        }
    }

    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    private void setupContent() {
        TextView contentText = new TextView(this);
        contentText.setText("Welcome to Side Menu App");
        contentText.setTextSize(24);
        contentText.setPadding(40, 40, 40, 40);
        contentFrame.addView(contentText);
    }

    private void toggleContentBackground() {
        if (isAltBackground) {
            contentFrame.setBackgroundColor(Color.WHITE);
        } else {
            contentFrame.setBackgroundColor(Color.BLUE);
        }
        isAltBackground = !isAltBackground;
    }

    private void setupMenu() {
        menuLayout.setBackgroundColor(Color.parseColor("#F1F5F9"));

        addProfileHeader();
        addMenuDivider();

        Object[][] menuItems = {
                {android.R.drawable.ic_menu_view, "Home", "Main dashboard"},
                {android.R.drawable.ic_menu_myplaces, "Profile", "View your profile"},
                {android.R.drawable.ic_menu_manage, "Settings", "App preferences"},
                {android.R.drawable.ic_menu_edit, "Change Background", "Switch content background color"},
                {android.R.drawable.star_big_on, "Favorites", "Your saved items"},
                {android.R.drawable.ic_dialog_info, "Notifications", "Message center"},
                {android.R.drawable.ic_lock_power_off, "Logout", "Sign out"}
        };

        for (Object[] item : menuItems) {
            int iconRes = (int) item[0];
            String title = (String) item[1];
            String description = (String) item[2];
            addMenuItemWithDescription(iconRes, title, description);
        }
    }

    private void addProfileHeader() {
        LinearLayout headerLayout = new LinearLayout(this);
        headerLayout.setOrientation(LinearLayout.VERTICAL);
        headerLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        headerLayout.setBackgroundColor(Color.parseColor("#2196F3"));
        headerLayout.setPadding(40, 30, 40, 30);

        TextView nameText = new TextView(this);
        nameText.setText("Dino");
        nameText.setTextSize(20);
        nameText.setTextColor(Color.WHITE);
        nameText.setGravity(GravityCompat.START);
        headerLayout.addView(nameText);

        TextView emailText = new TextView(this);
        emailText.setText("dino@gmail.com");
        emailText.setTextSize(12);
        emailText.setTextColor(Color.parseColor("#E3F2FD"));
        emailText.setPadding(0, 10, 0, 0);
        headerLayout.addView(emailText);

        menuLayout.addView(headerLayout);
    }

    private void addMenuDivider() {
        View divider = new View(this);
        divider.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                2
        ));
        divider.setBackgroundColor(Color.parseColor("#E0E0E0"));
        menuLayout.addView(divider);
    }

    private void addMenuItemWithDescription(int iconRes, String title, String description) {
        LinearLayout itemLayout = new LinearLayout(this);
        itemLayout.setOrientation(LinearLayout.VERTICAL);
        itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        itemLayout.setPadding(40, 20, 40, 20);
        itemLayout.setClickable(true);

        itemLayout.setOnClickListener(v -> {
            drawerLayout.closeDrawer(GravityCompat.START);


            if ("Logout".equals(title)) {
                finishAffinity();
                return;
            }

            // handle za promjenu pozadine bez otvaranja nove aktivnosti
            if ("Change Background".equals(title)) {
                toggleContentBackground();
                return;
            }

            Intent intent = new Intent(MainActivity.this, MenuDetailActivity.class);
            intent.putExtra("menu_title", title); // prosljedjuje naslov i opis u MenuDetailActivity
            intent.putExtra("menu_description", description);
            startActivity(intent);
        });

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        row.setGravity(Gravity.CENTER_VERTICAL);

        TextView titleText = new TextView(this);
        titleText.setText(title);
        titleText.setTextSize(16);
        titleText.setTextColor(Color.parseColor("#212121"));
        titleText.setCompoundDrawablesWithIntrinsicBounds(iconRes, 0, 0, 0);
        titleText.setCompoundDrawablePadding(24);
        row.addView(titleText);

        itemLayout.addView(row);

        TextView descText = new TextView(this);
        descText.setText(description);
        descText.setTextSize(12);
        descText.setTextColor(Color.parseColor("#616161"));
        descText.setPadding(0, 8, 0, 0);
        itemLayout.addView(descText);

        menuLayout.addView(itemLayout);
    }
}

