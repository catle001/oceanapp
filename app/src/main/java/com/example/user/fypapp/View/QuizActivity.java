package com.example.user.fypapp.View;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.fypapp.Presenter.QuizPresenter;
import com.example.user.fypapp.R;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by user on 1/2/2017.
 */
public class QuizActivity extends AppCompatActivity {

    private int seekbarid = 2000;
    private Button btnSubmit;
    private PrefManager prefManager;
    private QuizPresenter mQuizPresenter;
    private SharedPreferences prefs;

    ProgressDialog progress;
    private static final String TAG = "QuizActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);


        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        setContentView(R.layout.activity_quiz);

        init();
        btnSubmit = (Button) findViewById(R.id.btn_submit);
        mQuizPresenter = new QuizPresenter();
        mQuizPresenter.onAttachActivity(savedInstanceState, this);


        changeStatusBarColor();
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnSubmit.setEnabled(false);
                calculatequizresult();
                mQuizPresenter.startCalculation();


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

    public void reloadbutton() {
        btnSubmit.setEnabled(true);
    }

    private void calculatequizresult() {
        ArrayList<Integer> quizscore = new ArrayList<Integer>();
        int[] quizArray = new int[35];
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // Checking for first time launch - before calling setContentView()
        prefManager = new PrefManager(this);
        String[] allFactor={"Extraverted", "Energetic", "Talkative", "Bold", "Active", "Assertive", "Adventurous",
                "Warm", "Kind", "Cooperative", "Selfless", "Agreeable", "Trustable", "Generous",
                "Organized", "Responsible", "Dedicated", "Practical", "Thorough", "Hardworking", "Rule Abiding",
                "Calm", "Relaxed", "At ease", "Not envious", "Stable", "Contented", "Unemotional",
                "Intelligent", "Analytical", "Reflective", "Curious", "Imaginative", "Creative", "Cultured"};

        int count = 0;
        for (int i = 1; i <= 35; i++) {
            SeekBar sb = (SeekBar) findViewById(seekbarid + i);
            int value=sb.getProgress() + 1;
            count += value;
            quizArray[i-1]=value;
            if (i % 7 == 0) {
                quizscore.add(count);
                count = 0;
            }
        }

        SharedPreferences.Editor editor = prefs.edit();

        editor.putFloat(getString(R.string.qattr1), ((float) (quizscore.get(0) / 63.0)));
        editor.putFloat(getString(R.string.qattr2), ((float) (quizscore.get(1) / 63.0)));
        editor.putFloat(getString(R.string.qattr3), ((float) (quizscore.get(2) / 63.0)));
        editor.putFloat(getString(R.string.qattr4), ((float) ((63 - quizscore.get(3)) / 63.0))); //quiz result is emotional stability, we need neuroticism
        editor.putFloat(getString(R.string.qattr5), ((float) (quizscore.get(4) / 63.0)));

        for(int i=0; i<35; i++){
            editor.putInt(allFactor[i], quizArray[i] );
        }
        editor.commit();
        System.out.println("scores : " + quizscore);
        progress = new ProgressDialog(QuizActivity.this,
                R.style.AppTheme_Dark_Dialog);

        progress.setIndeterminate(true);
        progress.setMessage("Submitting your results...");
        progress.show();
    }


    public void init() {
        TableLayout ll = (TableLayout) findViewById(R.id.qnE);

        String[] EFactor = {"Extraverted", "Energetic", "Talkative", "Bold", "Active", "Assertive", "Adventurous"};

        for (int i = 0; i < 7; i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            lp.setMargins(10, 50, 10, 50);
            row.setLayoutParams(lp);
            TextView qn = new TextView(this);
            TextThumbSeekBar sb = new TextThumbSeekBar(this);
            sb.setId(seekbarid + sb.generateViewId());
            sb.setMax(8);
            sb.setProgress(4);
            sb.setProgressDrawable(getResources().getDrawable(R.drawable.apptheme_scrubber_progress_horizontal_holo_light));
            sb.setThumb(getResources().getDrawable(R.drawable.apptheme_scrubber_control_normal));
            qn.setText(EFactor[i]);
            row.addView(qn);
            row.addView(sb);
            ll.addView(row, lp);

        }

        ll = (TableLayout) findViewById(R.id.qnA);
        String[] AFactor = {"Warm", "Kind", "Cooperative", "Selfless", "Agreeable", "Trustable", "Generous"};
        for (int i = 0; i < 7; i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView qn = new TextView(this);
            TextThumbSeekBar sb = new TextThumbSeekBar(this);
            sb.setId(seekbarid + sb.generateViewId());
            sb.layout(10, 10, 10, 10);
            sb.setMax(8);
            sb.setProgress(4);
            sb.setProgressDrawable(getResources().getDrawable(R.drawable.apptheme_scrubber_progress_horizontal_holo_light));
            sb.setThumb(getResources().getDrawable(R.drawable.apptheme_scrubber_control_normal));
            qn.setText(AFactor[i]);
            row.addView(qn);
            row.addView(sb);
            ll.addView(row, i + 2);

        }

        ll = (TableLayout) findViewById(R.id.qnC);
        String[] CFactor = {"Organized", "Responsible", "Dedicated", "Practical", "Thorough", "Hardworking", "Rule Abiding"};
        for (int i = 0; i < 7; i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView qn = new TextView(this);
            TextThumbSeekBar sb = new TextThumbSeekBar(this);
            sb.setId(seekbarid + sb.generateViewId());
            sb.layout(10, 10, 10, 10);
            sb.setMax(8);
            sb.setProgress(4);
            sb.setProgressDrawable(getResources().getDrawable(R.drawable.apptheme_scrubber_progress_horizontal_holo_light));
            sb.setThumb(getResources().getDrawable(R.drawable.apptheme_scrubber_control_normal));
            qn.setText(CFactor[i]);
            row.addView(qn);
            row.addView(sb);
            ll.addView(row, i + 2);

        }

        ll = (TableLayout) findViewById(R.id.qnN);
        String[] NFactor = {"Calm", "Relaxed", "At ease", "Not envious", "Stable", "Contented", "Unemotional"};
        for (int i = 0; i < 7; i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView qn = new TextView(this);
            TextThumbSeekBar sb = new TextThumbSeekBar(this);
            sb.setId(seekbarid + sb.generateViewId());
            sb.layout(10, 10, 10, 10);
            sb.setMax(8);
            sb.setProgress(4);
            sb.setProgressDrawable(getResources().getDrawable(R.drawable.apptheme_scrubber_progress_horizontal_holo_light));
            sb.setThumb(getResources().getDrawable(R.drawable.apptheme_scrubber_control_normal));
            qn.setText(NFactor[i]);
            row.addView(qn);
            row.addView(sb);
            ll.addView(row, i + 2);

        }

        ll = (TableLayout) findViewById(R.id.qnO);
        String[] OFactor = {"Intelligent", "Analytical", "Reflective", "Curious", "Imaginative", "Creative", "Cultured"};
        for (int i = 0; i < 7; i++) {

            TableRow row = new TableRow(this);
            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            row.setLayoutParams(lp);
            TextView qn = new TextView(this);
            TextThumbSeekBar sb = new TextThumbSeekBar(this);
            sb.setId(seekbarid + sb.generateViewId());
            sb.layout(10, 10, 10, 10);
            sb.setMax(8);
            sb.setProgress(4);
            sb.setProgressDrawable(getResources().getDrawable(R.drawable.apptheme_scrubber_progress_horizontal_holo_light));
            sb.setThumb(getResources().getDrawable(R.drawable.apptheme_scrubber_control_normal));


            qn.setText(OFactor[i]);
            row.addView(qn);
            row.addView(sb);
            ll.addView(row, i + 2);

        }

    }


    public void showProgressDialog(String warning) {


        progress.setMessage(warning);
        progress.setCancelable(false);
        progress.show();
    }

    public void setProgressMessage(String warning) {
        progress.setMessage(warning);
    }

    public void dismissProgressDialog() {
        progress.dismiss();
    }

    public void launchHomeScreen() {
        startActivity(new Intent(QuizActivity.this, MainActivity.class));
        finish();
    }


    /**
     * Making notification bar transparent
     */
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public String getURL() {

        return getResources().getString(R.string.portUrl);
    }

    public String getDBUrl() {

        return getResources().getString(R.string.dburl);
    }

    public ArrayList<String> getGamingCategory() {
        ArrayList<String> stringList = new ArrayList<String>(Arrays.asList(getResources().getStringArray(R.array.gamingcategory)));
        return stringList;
    }

    public void showToast(String str) {

        Toast.makeText(this, str, Toast.LENGTH_SHORT)
                .show();
    }
}


