package com.example.evanmartnez.whowantstobeamillionarie;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class PlayActivity extends AppCompatActivity{

    private static final String XML_QUESTION_TAG = "question";
    private static final String XML_QUESTION_TEXT = "text";
    private static final String XML_QUESTION_RIGHT = "right";
    private static final String XML_QUESTION_PHONE = "phone";
    private static final String XML_QUESTION_NUMBER = "number";
    private static final String XML_QUESTION_FIFTY1 = "fifty1";
    private static final String XML_QUESTION_FIFTY2 = "fifty2";
    private static final String XML_QUESTION_AUDIENCE = "audience";
    private static final String XML_QUESTION_ANSWER1 = "answer1";
    private static final String XML_QUESTION_ANSWER2 = "answer2";
    private static final String XML_QUESTION_ANSWER3 = "answer3";
    private static final String XML_QUESTION_ANSWER4 = "answer4";

    static ArrayList<Question> questions = new ArrayList<Question>();
    static Button btnAns1, btnAns2, btnAns3, btnAns4, btnHelpCrowd, btnHelpCall, btnHelp50;
    static TextView txtQuestion;
    static int question = 0;
    static int safePrize = 0;
    boolean crowdUsed = false, phoneUsed = false, fiftyUsed = false;
    static int[] prizes = {0, 100, 200, 300, 500, 1000, 2000, 4000, 8000, 16000, 32000, 64000, 125000, 250000, 500000, 1000000};
    static String right, crowd, phone, fifty1, fifty2;
    static SharedPreferences prefs;
    static SQLiteDatabase db;
    static String currentLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        myToolbar.setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        currentLanguage = Locale.getDefault().toString();

        ScoresSQLiteHelper sqlhelper = new ScoresSQLiteHelper(this, "DBScores", null, 6);
        db = sqlhelper.getWritableDatabase();

        txtQuestion = (TextView)findViewById(R.id.textView);

        btnAns1 = (Button)findViewById(R.id.btnAns1);
        btnAns2 = (Button)findViewById(R.id.btnAns2);
        btnAns3 = (Button)findViewById(R.id.btnAns3);
        btnAns4 = (Button)findViewById(R.id.btnAns4);

        btnHelpCrowd = (Button)findViewById(R.id.btnHelpCrowd);
        btnHelpCall = (Button)findViewById(R.id.btnHelpCall);
        btnHelp50 = (Button)findViewById(R.id.btnHelp50);

        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        question = prefs.getInt("index", 0);
        safePrize = prefs.getInt("safePrize", 0);
        crowdUsed = prefs.getBoolean("crowdUsed", false);
        phoneUsed = prefs.getBoolean("phoneUsed", false);
        fiftyUsed = prefs.getBoolean("fiftyUsed", false);

        generateQuestionList();

        helpUsed();

        loadNextQuestion();
    }

    @Override
    public boolean	onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_play, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)	{
        switch	(item.getItemId())	{
            case R.id.action_quit:
                safePrize = question;
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.quit) + " " + prizes[safePrize] +
                        " " + getResources().getString(R.string.coin), Toast.LENGTH_SHORT).show();
                gameOver();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void loadNextQuestion(){

        SharedPreferences.Editor editor = prefs.edit();

        btnAns1.setVisibility(View.VISIBLE);
        btnAns2.setVisibility(View.VISIBLE);
        btnAns3.setVisibility(View.VISIBLE);
        btnAns4.setVisibility(View.VISIBLE);

        if (question < 15) {
            if (question == 5) {
                safePrize = 5;
                editor.putInt("safePrize", 5);
                editor.commit();
            } else if (question == 10) {
                safePrize = 10;
                editor.putInt("safePrize", 10);
                editor.commit();
            }

            Question q = questions.get(question);

            txtQuestion.setText((question+1) + ". " + q.getText() + " (" + prizes[question+1] + " " +
                    getResources().getString(R.string.coin) + ")");

            btnAns1.setText("1. " + q.getAnswer1());
            btnAns2.setText("2. " + q.getAnswer2());
            btnAns3.setText("3. " + q.getAnswer3());
            btnAns4.setText("4. " + q.getAnswer4());

            right = q.getRight();
            crowd = q.getAudience();
            phone = q.getPhone();
            fifty1 = q.getFifty1();
            fifty2 = q.getFifty2();
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.win) + " " + prizes[15] +
                    getResources().getString(R.string.coin), Toast.LENGTH_SHORT).show();
            safePrize = 15;
            gameOver();
            finish();
        }
    }

    public void onClickAnswer(View v){

        int buttonID = v.getId();

        switch(buttonID){
            case R.id.btnAns1:
                if (Integer.parseInt(right) == 1){
                    question++;
                    loadNextQuestion();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong) + " " + prizes[safePrize]
                            + " " + getResources().getString(R.string.coin), Toast.LENGTH_SHORT).show();
                    gameOver();
                    finish();
                }
                break;
            case R.id.btnAns2:
                if (Integer.parseInt(right) == 2){
                    question++;
                    loadNextQuestion();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong) + " " + prizes[safePrize]
                            + " " + getResources().getString(R.string.coin), Toast.LENGTH_SHORT).show();
                    gameOver();
                    finish();
                }
                break;
            case R.id.btnAns3:
                if (Integer.parseInt(right) == 3){
                    question++;
                    loadNextQuestion();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong) + " " + prizes[safePrize]
                            + " " + getResources().getString(R.string.coin), Toast.LENGTH_SHORT).show();
                    gameOver();
                    finish();
                }
                break;
            case R.id.btnAns4:
                if (Integer.parseInt(right) == 4){
                    question++;
                    loadNextQuestion();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.wrong) + " " + prizes[safePrize]
                            + " " + getResources().getString(R.string.coin), Toast.LENGTH_SHORT).show();
                    gameOver();
                    finish();
                }
                break;
        }

    }

    public void onClickHelp(View v){
        SharedPreferences.Editor editor = prefs.edit();
        int buttonID = v.getId();

        switch (buttonID){
            case R.id.btnHelpCrowd:
                btnHelpCrowd.setVisibility(View.INVISIBLE);
                editor.putBoolean("crowdUsed", true);
                editor.commit();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.audience) + " " +
                        crowd, Toast.LENGTH_LONG).show();
                break;
            case R.id.btnHelpCall:
                btnHelpCall.setVisibility(View.INVISIBLE);
                editor.putBoolean("phoneUsed", true);
                editor.commit();
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.phone) + " " +
                        phone, Toast.LENGTH_LONG).show();
                break;
            case R.id.btnHelp50:
                btnHelp50.setVisibility(View.INVISIBLE);
                editor.putBoolean("fiftyUsed", true);
                editor.commit();
                switch (fifty1){
                    case "1":
                        btnAns1.setVisibility(View.INVISIBLE);
                        break;
                    case "2":
                        btnAns2.setVisibility(View.INVISIBLE);
                        break;
                    case "3":
                        btnAns3.setVisibility(View.INVISIBLE);
                        break;
                    case "4":
                        btnAns4.setVisibility(View.INVISIBLE);
                        break;
                }

                switch (fifty2){
                    case "1":
                        btnAns1.setVisibility(View.INVISIBLE);
                        break;
                    case "2":
                        btnAns2.setVisibility(View.INVISIBLE);
                        break;
                    case "3":
                        btnAns3.setVisibility(View.INVISIBLE);
                        break;
                    case "4":
                        btnAns4.setVisibility(View.INVISIBLE);
                        break;
                }
                break;
        }
    }

    public void generateQuestionList() {

        try {

            BufferedReader fin = null;

            if (currentLanguage.equalsIgnoreCase("es_ES")) {
                fin = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.questions_es)));
            } else {
                fin = new BufferedReader(new InputStreamReader(getResources().openRawResource(R.raw.questions)));
            }

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser(); //Creamos una instancia parser
            parser.setInput(fin); //Para leer del fichero

            String number = "", text = "", answer1 = "", answer2 = "", answer3 = "", answer4 = "", right = "", audience = "",
                    phone = "", fifty1 = "", fifty2 = "";


            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals(XML_QUESTION_TAG)) {
                            text = parser.getAttributeValue(null, XML_QUESTION_TEXT);
                            right = parser.getAttributeValue(null, XML_QUESTION_RIGHT);
                            phone = parser.getAttributeValue(null, XML_QUESTION_PHONE);
                            number = parser.getAttributeValue(null, XML_QUESTION_NUMBER);
                            fifty1 = parser.getAttributeValue(null, XML_QUESTION_FIFTY1);
                            fifty2 = parser.getAttributeValue(null, XML_QUESTION_FIFTY2);
                            audience = parser.getAttributeValue(null, XML_QUESTION_AUDIENCE);
                            answer1 = parser.getAttributeValue(null, XML_QUESTION_ANSWER1);
                            answer2 = parser.getAttributeValue(null, XML_QUESTION_ANSWER2);
                            answer3 = parser.getAttributeValue(null, XML_QUESTION_ANSWER3);
                            answer4 = parser.getAttributeValue(null, XML_QUESTION_ANSWER4);


                            questions.add(new Question(number, text, answer1, answer2, answer3, answer4, right, audience, phone, fifty1, fifty2));
                        }
                        break;
                    default:
                        break;
                }
                parser.next();
                eventType = parser.getEventType();
            }
            fin.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void helpUsed(){
        if (crowdUsed){
            btnHelpCrowd.setVisibility(View.INVISIBLE);
        }

        if (phoneUsed){
            btnHelpCall.setVisibility(View.INVISIBLE);
        }

        if (fiftyUsed){
            btnHelp50.setVisibility(View.INVISIBLE);
        }
    }

    public void gameOver(){

        SharedPreferences defaultprefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        ContentValues data = new ContentValues();
        data.put("name",defaultprefs.getString("user", "default"));
        data.put("score", prizes[safePrize]);
        data.put("longitude", 0);
        data.put("latitude", 0);

        db.insert("Scores", null, data);

        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("safePrize", 0);
        editor.putInt("index", 0);
        editor.putBoolean("crowdUsed", false);
        editor.putBoolean("phoneUsed", false);
        editor.putBoolean("fiftyUsed", false);
        editor.apply();

        question = 0;
        safePrize = 0;

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("safePrize", safePrize);
        editor.putInt("index", question);

        editor.apply();
    }

}
