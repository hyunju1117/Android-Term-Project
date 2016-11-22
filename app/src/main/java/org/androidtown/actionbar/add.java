package org.androidtown.actionbar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class add extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        Intent singleTop = new Intent();
        singleTop.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

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
