package com.example.user.fypapp.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.user.fypapp.Model.App;
import com.example.user.fypapp.Presenter.AppDetailsPresenter;
import com.example.user.fypapp.Presenter.ImageLoader;
import com.example.user.fypapp.R;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by user on 28/11/2016.
 */
public class DetailActivity extends AppCompatActivity {


    ProgressDialog progress;
    private AppDetailsPresenter mAppPresenter;
    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.application_info);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();

        mAppPresenter = new AppDetailsPresenter();
        mAppPresenter.onAttachActivity(savedInstanceState, this);


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

    public void init() {
        progress = new ProgressDialog(DetailActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progress.setIndeterminate(true);

        final ExpandableTextView expandableTextView = (ExpandableTextView) findViewById(R.id.txtAppsummary);
        final Button buttonToggle = (Button) findViewById(R.id.button_toggle);

        // set animation duration via code, but preferable in your layout files by using the animation_duration attribute
        expandableTextView.setAnimationDuration(1000);

        // set interpolators for both expanding and collapsing animations
        expandableTextView.setInterpolator(new AccelerateDecelerateInterpolator());

        // or set them separately
        expandableTextView.setExpandInterpolator(new AccelerateDecelerateInterpolator());
        expandableTextView.setCollapseInterpolator(new AccelerateDecelerateInterpolator());


        // toggle the ExpandableTextView
        buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                expandableTextView.toggle();
                buttonToggle.setText(expandableTextView.isExpanded() ? "expand" : "collapse");
            }
        });


    }


    private static class URLSpanNoUnderline extends URLSpan {
        public URLSpanNoUnderline(String p_Url) {
            super(p_Url);
        }

        public void updateDrawState(TextPaint p_DrawState) {
            super.updateDrawState(p_DrawState);
            p_DrawState.setUnderlineText(false);
        }
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

    public void populateData(final App application) {
        getSupportActionBar().setTitle(application.getTitle());
        TextView tf1 = (TextView) findViewById(R.id.txtAppname);
        TextView tf2 = (TextView) findViewById(R.id.txtAppid);
        ExpandableTextView tf3 = (ExpandableTextView) findViewById(R.id.txtAppsummary);
        tf1.setText(application.getTitle());
        tf2.setText(application.getAppId());
        tf3.setText(application.getSummary() + "\n\n" + application.getDescription());
        RatingBar rb = (RatingBar) findViewById(R.id.ratingBar);
        RatingBar ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);
        System.out.println(String.valueOf(application.getScore()));
        rb.setRating(Float.valueOf(String.valueOf(application.getScore())));

        ImageView imageView = (ImageView) findViewById(R.id.imageView1);
        ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);
        imageView.setVisibility(View.GONE);

        if (!application.getIcon().contains("http")) {
            application.setIcon("http:" + application.getIcon());
        }

        new ImageLoader(application, imageView, pb).execute();
        System.out.println(application.getComments());
        LinearLayout layout = (LinearLayout) findViewById(R.id.linear);
        ArrayList<String> screenshots = new ArrayList<String>(Arrays.asList(application.getScreenshots().substring(1, application.getScreenshots().length() - 1).split(",")));
        for (int i = 0; i < screenshots.size(); i++) {
            ImageButton tempimgview = new ImageButton(getApplicationContext());
            String url = screenshots.get(i).replace("\"", "");
            url = url.replace("\\/", "/");
            tempimgview.setId(i);
            tempimgview.setPadding(10, 10, 10, 10);

            tempimgview.setScaleType(ImageView.ScaleType.CENTER_CROP);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(500, 800);
            tempimgview.setLayoutParams(layoutParams);


            if (!url.contains("http")) {
                application.setIcon("http:" + url);
            }

            new ImageLoader(application, tempimgview, pb).execute();

            layout.addView(tempimgview);
        }

        TextView txtupdated = (TextView) findViewById(R.id.txtupdated);
        txtupdated.setText(application.getUpdated());
        TextView txtinstalls = (TextView) findViewById(R.id.txtinstalls);
        txtinstalls.setText(application.getMaxInstalls() + " - " + application.getMaxInstalls());
        TextView txtversion = (TextView) findViewById(R.id.txtcurrentversion);
        txtversion.setText(application.getVersion());
        TextView txtversrequire = (TextView) findViewById(R.id.txtversionrequire);
        txtversrequire.setText(application.getAndroidVersion());
        TextView txtoffered = (TextView) findViewById(R.id.txtoffered);
        txtoffered.setText(application.getDeveloper());
        TextView txtdeveloper = (TextView) findViewById(R.id.txtdeveloperwebsite);
        txtdeveloper.setClickable(true);
        txtdeveloper.setMovementMethod(LinkMovementMethod.getInstance());

        String text = "<a href='" + application.getDeveloperWebsite() + "'> Website </a>";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            txtdeveloper.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtdeveloper.setText(Html.fromHtml(text));
        }
        if (txtdeveloper != null) {
            removeUnderlines((Spannable) txtdeveloper.getText());
        }
        TextView txtdeveloperemail = (TextView) findViewById(R.id.txtdeveloperemail);
        txtdeveloperemail.setText(application.getDeveloperEmail());
        Button btninstall = (Button) findViewById(R.id.button_install);
        btninstall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                startNewActivity(getApplicationContext(), application.getAppId());
            }
        });
    }

    public void showProgressDialog() {
        progress.setMessage("retrieving application...");
        progress.show();
    }

    public void dismissProgressDialog() {
        progress.dismiss();
    }
//

    public void startNewActivity(Context context, String packageName) {
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        if (intent == null) {
            // Bring user to the market or let them choose an app?
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("market://details?id=" + packageName));
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void removeUnderlines(Spannable p_Text) {
        URLSpan[] spans = p_Text.getSpans(0, p_Text.length(), URLSpan.class);

        for (URLSpan span : spans) {
            int start = p_Text.getSpanStart(span);
            int end = p_Text.getSpanEnd(span);
            p_Text.removeSpan(span);
            span = new URLSpanNoUnderline(span.getURL());
            p_Text.setSpan(span, start, end, 0);
        }
    }

}
