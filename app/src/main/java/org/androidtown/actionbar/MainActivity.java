package org.androidtown.actionbar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity{

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
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
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new month()).addToBackStack(null).commit();
                return true;
            case R.id.week:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new week()).addToBackStack(null).commit();
                return true;
            case R.id.day:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,new day()).addToBackStack(null).commit();
                return true;
            case R.id.add:
                startActivity(new Intent(this,add.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
