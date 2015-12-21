package com.example.evanmartnez.whowantstobeamillionarie;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher);

    }

    @Override
    public boolean	onCreateOptionsMenu(Menu menu)	{
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)	{
        switch	(item.getItemId())	{
            case R.id.action_credits:
                Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onClickMainMenu(View v){

        int buttonID = v.getId();
        Intent intent;

        switch(buttonID){
            case R.id.buttonPlay:
                intent = new Intent(MainActivity.this, PlayActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonScores:
                intent = new Intent(MainActivity.this, ScoresActivity.class);
                startActivity(intent);
                break;
            case R.id.buttonSettings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                break;
        }

    }
}
