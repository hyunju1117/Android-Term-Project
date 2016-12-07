package com.example.calendar_1394020_1394040;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    MyDBHelper helper;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(Html.fromHtml("<font color='#808080'>Main</font>"));



    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.month:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout,new MonthFragment()).addToBackStack(null).commit();
                return true;
            case R.id.week:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout,new WeekFragment()).addToBackStack(null).commit();
                return true;
            case R.id.day:
                getSupportFragmentManager().beginTransaction().replace(R.id.mainlayout,new DayFragment()).addToBackStack(null).commit();
                return true;
            case R.id.edit:
                startActivity(new Intent(this,EditActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



}
