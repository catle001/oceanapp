package com.example.user.fypapp.View;



        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.preference.PreferenceManager;
        import android.support.annotation.NonNull;
        import android.support.v7.app.AppCompatActivity;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.support.v7.widget.Toolbar;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuItem;
        import android.widget.TextView;
        import android.widget.Toast;


        import com.example.user.fypapp.Model.App;
        import com.example.user.fypapp.Presenter.AppListPresenter;
        import com.example.user.fypapp.Presenter.ListAdapter;
        import com.example.user.fypapp.R;

        import java.util.ArrayList;
        import java.util.Collections;


public class MainActivity extends AppCompatActivity {



    private static final String TAG = "MainActivity";
    private AppListPresenter mAppPresenter;
    SharedPreferences prefs;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        toolbar.setTitle("OCEAN");
        setSupportActionBar(toolbar);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        mAppPresenter = new AppListPresenter();
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


    public ArrayList<String> getCategories() {
        ArrayList<String> arrCategories= new ArrayList<>();


        String str = prefs.getString(getString(R.string.Max1), "");

        System.out.println(str);

        String HighestAttr = prefs.getString(getString(R.string.Max1),"");
        String SecondHAttr = prefs.getString(getString(R.string.Max2),"");
        if (HighestAttr.equals(getString(R.string.AttrOne)) || SecondHAttr.equals(getString(R.string.AttrOne))) {
            Collections.addAll(arrCategories, getResources().getStringArray(R.array.arrExtraversion));
        }
        if (HighestAttr.equals(getString(R.string.AttrTwo)) || SecondHAttr.equals(getString(R.string.AttrTwo))) {
            Collections.addAll(arrCategories, getResources().getStringArray(R.array.arrAgreeableness));
        }
        if (HighestAttr.equals(getString(R.string.AttrThree)) || SecondHAttr.equals(getString(R.string.AttrThree))) {
            Collections.addAll(arrCategories, getResources().getStringArray(R.array.arrConscientiousness));
        }
        if (HighestAttr.equals(getString(R.string.AttrFour)) || SecondHAttr.equals(getString(R.string.AttrFour))) {
            Collections.addAll(arrCategories, getResources().getStringArray(R.array.arrNeuroticism));
        }
        if (HighestAttr.equals(getString(R.string.AttrFive)) || SecondHAttr.equals(getString(R.string.AttrFive))) {
            Collections.addAll(arrCategories, getResources().getStringArray(R.array.arrOpenness));
        }

        TextView txtHighest = (TextView) findViewById(R.id.txtHighestScore);
        txtHighest.setText("You score highest on " + HighestAttr + " and " + SecondHAttr);



        return arrCategories;

    }

    public String getFeaturedCategory(String str) {

        switch (str){
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

        return str;

    }

    public void setFeaturedAppList(@NonNull final ArrayList<App> appList) {

        mAdapter = new ListAdapter(this,appList);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        mRecyclerView.setLayoutManager(mLayoutManager);




        TextView txtfeatured = (TextView) findViewById(R.id.featured);
//        txtfeatured.setText("Because you spend most of your time on " + getFeaturedCategory(prefs.getString(getString(R.string.Max1Usage), "")) + " applications...");
        txtfeatured.setText("Featured Applications");


    }

    public void setAppList(@NonNull final ArrayList<App> appList) {

        mAdapter = new ListAdapter(this,appList);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view2);

        mRecyclerView.setAdapter(mAdapter);
        mLayoutManager = new LinearLayoutManager(
                getApplicationContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );
        mRecyclerView.setLayoutManager(mLayoutManager);



        TextView txtsuggested = (TextView) findViewById(R.id.suggested);
        txtsuggested.setText("Suggested Applications");



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case android.R.id.home:
                    onBackPressed();
                    return true;
                case R.id.profile:
                    Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                    startActivity(intent);

            }


        return super.onOptionsItemSelected(item);
    }


    public String getURL() {

        return getResources().getString(R.string.portUrl);
    }

    public void showToast(String str) {

        Toast.makeText(this, str, Toast.LENGTH_SHORT)
                .show();
    }

}




