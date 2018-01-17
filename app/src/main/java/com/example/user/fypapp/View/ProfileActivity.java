package com.example.user.fypapp.View;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.ProgressDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.*;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.DataCollectionTableDO;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.user.IdentityManager;
import com.amazonaws.models.nosql.DataCollectionTableDO;
import com.example.user.fypapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by user on 15/12/2016.
 */
public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";
    HashMap<String, String> hmPhoneUsage;
    TextRoundCornerProgressBar dOpenness, dExtraversion, dAgreeableness, dNeuroticism, dConscientiousness;
    TextRoundCornerProgressBar qOpenness, qExtraversion, qAgreeableness, qNeuroticism, qConscientiousness;
    ArrayList<String> totalscores;
    TextView lblmapp, lblmcapp;
    String jsonText;
    Button copyButton;
    private android.content.ClipboardManager myClipboard;
    private ClipData myClip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        totalscores = new ArrayList<>();

        hmPhoneUsage = new HashMap<>();
        dExtraversion = (TextRoundCornerProgressBar) findViewById(R.id.dExtraversion);
        dAgreeableness = (TextRoundCornerProgressBar) findViewById(R.id.dAgreeableness);
        dConscientiousness = (TextRoundCornerProgressBar) findViewById(R.id.dConscientiousness);
        dNeuroticism = (TextRoundCornerProgressBar) findViewById(R.id.dNeuroticism);
        dOpenness = (TextRoundCornerProgressBar) findViewById(R.id.dOpenness);

        dExtraversion.setProgressColor(Color.parseColor("#ffffff"));
        dExtraversion.setProgressBackgroundColor(Color.parseColor("#000000"));
        dAgreeableness.setProgressColor(Color.parseColor("#ffffff"));
        dAgreeableness.setProgressBackgroundColor(Color.parseColor("#000000"));
        dConscientiousness.setProgressColor(Color.parseColor("#ffffff"));
        dConscientiousness.setProgressBackgroundColor(Color.parseColor("#000000"));
        dNeuroticism.setProgressColor(Color.parseColor("#ffffff"));
        dNeuroticism.setProgressBackgroundColor(Color.parseColor("#000000"));
        dOpenness.setProgressColor(Color.parseColor("#ffffff"));
        dOpenness.setProgressBackgroundColor(Color.parseColor("#000000"));

        dExtraversion.setMax(1);
        dExtraversion.setProgress(prefs.getFloat(getString(R.string.dattr1), (float) 0.0));
        dAgreeableness.setMax(1);
        dAgreeableness.setProgress(prefs.getFloat(getString(R.string.dattr2), (float) 0.0));
        dConscientiousness.setMax(1);
        dConscientiousness.setProgress(prefs.getFloat(getString(R.string.dattr3), (float) 0.0));
        dNeuroticism.setMax(1);
        dNeuroticism.setProgress(prefs.getFloat(getString(R.string.dattr4), (float) 0.0));
        dOpenness.setMax(1);
        dOpenness.setProgress(prefs.getFloat(getString(R.string.dattr5), (float) 0.0));

        qExtraversion = (TextRoundCornerProgressBar) findViewById(R.id.qExtraversion);
        qAgreeableness = (TextRoundCornerProgressBar) findViewById(R.id.qAgreeableness);
        qConscientiousness = (TextRoundCornerProgressBar) findViewById(R.id.qConscientiousness);
        qNeuroticism = (TextRoundCornerProgressBar) findViewById(R.id.qNeuroticism);
        qOpenness = (TextRoundCornerProgressBar) findViewById(R.id.qOpenness);

        qExtraversion.setProgressColor(Color.parseColor("#ffffff"));
        qExtraversion.setProgressBackgroundColor(Color.parseColor("#000000"));
        qAgreeableness.setProgressColor(Color.parseColor("#ffffff"));
        qAgreeableness.setProgressBackgroundColor(Color.parseColor("#000000"));
        qConscientiousness.setProgressColor(Color.parseColor("#ffffff"));
        qConscientiousness.setProgressBackgroundColor(Color.parseColor("#000000"));
        qNeuroticism.setProgressColor(Color.parseColor("#ffffff"));
        qNeuroticism.setProgressBackgroundColor(Color.parseColor("#000000"));
        qOpenness.setProgressColor(Color.parseColor("#ffffff"));
        qOpenness.setProgressBackgroundColor(Color.parseColor("#000000"));

        qExtraversion.setMax(1);
        qExtraversion.setProgress(prefs.getFloat(getString(R.string.qattr1), (float) 0.0));
        qAgreeableness.setMax(1);
        qAgreeableness.setProgress(prefs.getFloat(getString(R.string.qattr2), (float) 0.0));
        qConscientiousness.setMax(1);
        qConscientiousness.setProgress(prefs.getFloat(getString(R.string.qattr3), (float) 0.0));
        qNeuroticism.setMax(1);
        qNeuroticism.setProgress(prefs.getFloat(getString(R.string.qattr4), (float) 0.0));
        qOpenness.setMax(1);
        qOpenness.setProgress(prefs.getFloat(getString(R.string.qattr5), (float) 0.0));



        lblmapp = (TextView) findViewById(R.id.lblmapp);
        lblmapp.setText(prefs.getString("mostusedapp", "Not Available").toUpperCase());
        lblmcapp = (TextView) findViewById(R.id.lblmcapp);
        lblmcapp.setText(getFeaturedCategory(prefs.getString(getString(R.string.Max1Usage), "")));

        //value to DB

        try {
            JSONObject finalJSON = new JSONObject();

                JSONObject appJSON = new JSONObject();
                appJSON.put("Openness", Math.round(prefs.getFloat(getString(R.string.dattr5), (float) 0.0)*100));
                appJSON.put("Extraversion", Math.round(prefs.getFloat(getString(R.string.dattr1), (float) 0.0)*100));
                appJSON.put("Agreeableness", Math.round(prefs.getFloat(getString(R.string.dattr2), (float) 0.0)*100));
                appJSON.put("Neuroticism",Math.round(prefs.getFloat(getString(R.string.dattr4), (float) 0.0)*100));
                appJSON.put("Conscientiousness", Math.round(prefs.getFloat(getString(R.string.dattr3), (float) 0.0)*100));

                JSONObject quizJSON = new JSONObject();
                quizJSON.put("Openness", Math.round(prefs.getFloat(getString(R.string.qattr5), (float) 0.0)*100));
                quizJSON.put("Extraversion", Math.round(prefs.getFloat(getString(R.string.qattr1), (float) 0.0)*100));
                quizJSON.put("Agreeableness", Math.round(prefs.getFloat(getString(R.string.qattr2), (float) 0.0)*100));
                quizJSON.put("Neuroticism", Math.round(prefs.getFloat(getString(R.string.qattr4), (float) 0.0)*100));
                quizJSON.put("Conscientiousness", Math.round(prefs.getFloat(getString(R.string.qattr3), (float) 0.0)*100));

                JSONObject timeJSON = new JSONObject();
                timeJSON.put("Social",Math.round(prefs.getFloat(getString(R.string.time1), (float) 0.0)*1000));
                timeJSON.put("Game", Math.round(prefs.getFloat(getString(R.string.time2), (float) 0.0)*1000));
                timeJSON.put("Communicate", Math.round(prefs.getFloat(getString(R.string.time3), (float) 0.0)*1000));
                timeJSON.put("Personalize", Math.round(prefs.getFloat(getString(R.string.time4), (float) 0.0)*1000));
                timeJSON.put("Productivity", Math.round(prefs.getFloat(getString(R.string.time5), (float) 0.0)*1000));
                timeJSON.put("Shop", Math.round(prefs.getFloat(getString(R.string.time6), (float) 0.0)*1000));
                timeJSON.put("Music", Math.round(prefs.getFloat(getString(R.string.time7), (float) 0.0)*1000));
                timeJSON.put("Video", Math.round(prefs.getFloat( getString(R.string.time8), (float) 0.0)*1000 ));

                JSONObject mostUsedJSON=new JSONObject();
                mostUsedJSON.put("Most Used App",prefs.getString("mostusedapp", "Not Available").toUpperCase());
                mostUsedJSON.put("FeaturedCategory",getFeaturedCategory(prefs.getString(getString(R.string.Max1Usage), "")));

            String[] allFactor={"Extraverted", "Energetic", "Talkative", "Bold", "Active", "Assertive", "Adventurous",
                    "Warm", "Kind", "Cooperative", "Selfless", "Agreeable", "Trustable", "Generous",
                    "Organized", "Responsible", "Dedicated", "Practical", "Thorough", "Hardworking", "Rule Abiding",
                    "Calm", "Relaxed", "At ease", "Not envious", "Stable", "Contented", "Unemotional",
                    "Intelligent", "Analytical", "Reflective", "Curious", "Imaginative", "Creative", "Cultured"};

                JSONObject quizAnsJSON=new JSONObject();

                for(int i=0;i<35;i++){
                    quizAnsJSON.put(allFactor[i],prefs.getInt(allFactor[i], 5));
                }

            finalJSON.put("app", appJSON);
            finalJSON.put("quizResult",quizJSON);
            finalJSON.put("time",timeJSON);
            finalJSON.put("mostUsedApp",mostUsedJSON);
            finalJSON.put("quizAns", quizAnsJSON);

            jsonText = finalJSON.toString();
            System.out.print(jsonText);

        } catch (JSONException e) {
            Log.e("MYAPP", "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }

        copyButton= (Button) findViewById(R.id.copybutton);
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                myClipboard = (android.content.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String text;
                text = jsonText;

                myClip = ClipData.newPlainText("label", text);
                myClipboard.setPrimaryClip(myClip);

                Toast.makeText(getApplicationContext(), "Text Copied",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.v(TAG, "++ ON START ++");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.v(TAG, "+ ON RESUME +");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.v(TAG, "- ON PAUSE -");

    }

    @Override
    public void onStop() {
        super.onStop();
        Log.v(TAG, "-- ON STOP --");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "- ON DESTROY -");
    }

    public String getFeaturedCategory(String str) {

        switch (str) {
            case "Messenger":
            case "Browser":
            case "CMail":
                return "COMMUNICATION";
            case "Tool":
            case "Calendar":
            case "PMail":
                return "PRODUCTIVITY";
            case "PostSocial":
            case "MediaSocial":
            case "Stream":
                return "SOCIAL";
            case "Video":
            case "Tools":
            case "VRecorder":
                return "VIDEO_PLAYERS";
            case "Wallpaper":
            case "Fonts":
            case "Keyboard":
                return "PERSONALIZATION";
            case "Music":
            case "Radio":
            case "MRecorder":
                return "MUSIC_AND_AUDIO";

        }
        return "Not Available";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

        }


        return super.onOptionsItemSelected(item);
    }

}






