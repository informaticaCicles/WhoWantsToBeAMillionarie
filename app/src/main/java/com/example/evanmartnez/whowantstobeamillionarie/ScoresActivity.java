package com.example.evanmartnez.whowantstobeamillionarie;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ScoresActivity extends AppCompatActivity{

    static ListView lstLocal, lstFriends;
    static SQLiteDatabase db;
    static ArrayList<Score> scoresList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        ScoresSQLiteHelper sqlhelper = new ScoresSQLiteHelper(this, "DBScores", null, 6);

        db = sqlhelper.getWritableDatabase();

        scoresList = new ArrayList<Score>();
        loadScores();

        Collections.sort(scoresList);

        Resources res = getResources();

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AdaptadorScores adaptador = new AdaptadorScores(this, scoresList);
        lstLocal = (ListView)findViewById(R.id.LstLocalScores);
        lstLocal.setAdapter(adaptador);

        TabHost tabs = (TabHost)findViewById(android.R.id.tabhost);
        tabs.setup();

        TabHost.TabSpec spec = tabs.newTabSpec("mitab1");
        spec.setContent(R.id.tab1);
        spec.setIndicator(getString(R.string.tabLocal));
        tabs.addTab(spec);

        spec=tabs.newTabSpec("mitab2");
        spec.setContent(R.id.tab2);
        spec.setIndicator(getString(R.string.tabFriends));
        tabs.addTab(spec);

        tabs.setCurrentTab(0);

    }

    @Override
    public boolean	onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_scores, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)	{
        switch	(item.getItemId())	{
            case R.id.action_delete:
                db.execSQL("DELETE FROM Scores");
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadScores(){

        Cursor c = db.rawQuery("SELECT name, score, longitude, latitude FROM Scores", null);

        if (c != null && c.moveToFirst()) {

            do {

                Score score = new Score(c.getString(0), c.getInt(1), c.getFloat(2), c.getFloat(3));
                scoresList.add(score);

            } while (c.moveToNext());

        }

    }

    class AdaptadorScores extends ArrayAdapter<Score> {

        public AdaptadorScores(Context context, ArrayList<Score> datos)	{
            super(context, R.layout.listitem_scores, datos);
        }

        public View getView(int	position, View convertView, ViewGroup parent)	{
            LayoutInflater inflater = LayoutInflater.from(getContext());
            View item = inflater.inflate(R.layout.listitem_scores, null);

            TextView txtUsername = (TextView)item.findViewById(R.id.txtUserLocal);
            txtUsername.setText(scoresList.get(position).getUser());

            TextView txtLongitude = (TextView)item.findViewById(R.id.txtLongitudeLocal);
            txtLongitude.setText(String.valueOf(scoresList.get(position).getLongitude()));

            TextView txtLatitude = (TextView)item.findViewById(R.id.txtLatitudeLocal);
            txtLatitude.setText(String.valueOf(scoresList.get(position).getLatitude()));

            TextView txtScore = (TextView)item.findViewById(R.id.txtScoresLocal);
            txtScore.setText(String.valueOf(scoresList.get(position).getScore()));

            return(item);
        }
    }

    class Score implements Comparable{

        private String user;
        private int score;
        private float longitude;
        private float latitude;

        Score (){}

        public Score(String user, int score, float longitude, float latitude) {
            this.user = user;
            this.score = score;
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public int getScore() {
            return score;
        }

        public void setScore(int score) {
            this.score = score;
        }

        public float getLongitude() {
            return longitude;
        }

        public void setLongitude(float longitude) {
            this.longitude = longitude;
        }

        public float getLatitude() {
            return latitude;
        }

        public void setLatitude(float latitude) {
            this.latitude = latitude;
        }

        public int compareTo(Object o) {
            if (o != null && o instanceof Score){
                if (this.score > ((Score)o).score) {
                    return -1;
                } else if (this.score < ((Score)o).score) {
                    return 1;
                } else {
                    return 0;
                }
            } else {
                return 0;
            }
        }
    }
}
