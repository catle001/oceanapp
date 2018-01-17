package com.example.user.fypapp.Presenter;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.SeekBar;
import android.widget.Toast;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.mobile.DynamoDBManager;
import com.amazonaws.mobile.DynamoDBManager.*;
import com.amazonaws.mobile.ManagerClass;
import com.amazonaws.mobileconnectors.dynamodbv2.dynamodbmapper.DynamoDBMapper;
import com.amazonaws.models.nosql.DataCollectionTableDO;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.example.user.fypapp.Model.App;
import com.example.user.fypapp.R;
import com.example.user.fypapp.View.PrefManager;
import com.example.user.fypapp.View.QuizActivity;

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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


/**
 * Created by user on 14/2/2017.
 */
public class QuizPresenter extends AppCompatActivity {

    private QuizActivity mActivity;
    HashMap<String, HashMap<String, Integer> > catUsage;
    HashMap<String, App> appHm;
    SharedPreferences sharedPref;
//    SharedPreferences sharedPref2;
    String mostusedapp;
    public static final String TAG = "QuizPresenter";
    DynamoDBMapper mapper;
    public static ManagerClass managerClass = null;

    public QuizPresenter() {
    }

    public void onAttachActivity(@NonNull Bundle savedInstanceState, @NonNull QuizActivity activity) {
        mActivity = activity;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(mActivity);
        AWSMobileClient.initializeMobileClientIfNecessary(mActivity);

//        managerClass = new ManagerClass(this);
//        AmazonClientException lastException = null;

    }

    public void onDetachActivity() {
        mActivity = null;
    }


    public void startCalculation() {
//        subCatUsage = new HashMap<>();
        catUsage = new HashMap<>();
        appHm = new HashMap<>();
        ArrayList<String> toretrieve = getProcessName(); //getallappsused

        String[] arr = toretrieve.toArray(new String[toretrieve.size()]);


        new getUsageCategory().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, arr);
    }


    private ArrayList<String> getProcessName() {
        ArrayList<String> toretrieve = new ArrayList<>();
        ActivityManager activityManager = (ActivityManager) mActivity.getApplicationContext().getSystemService(mActivity.ACTIVITY_SERVICE);
        // Process running
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager mUsageStatsManager = (UsageStatsManager) mActivity.getApplicationContext().getSystemService(mActivity.USAGE_STATS_SERVICE);

            Calendar beginCal = Calendar.getInstance();

            beginCal.add(Calendar.DAY_OF_MONTH, -7); // apps usage within 7 days


            List<UsageStats> stats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, beginCal.getTimeInMillis(), System.currentTimeMillis());
            // Sort the stats by the last time used
            if (stats != null) {

                for (UsageStats usageStats : stats) {
                    if ((float) (usageStats.getTotalTimeInForeground() / 1000) != 0) {
                        App singleapp = null;
                        if (usageStats.getPackageName().contains("com.android")) { // if default app
                            if (usageStats.getPackageName().contains("calendar")) {
                                singleapp = new App(usageStats.getPackageName(), String.valueOf(usageStats.getTotalTimeInForeground() / 1000), "Productivity", "AndroidCalendar");
                            } else if (usageStats.getPackageName().contains("mms")) {
                                singleapp = new App(usageStats.getPackageName(), String.valueOf(usageStats.getTotalTimeInForeground() / 1000), "Communication", "AndroidMessaging");
                            } else if (usageStats.getPackageName().contains("incallui")) {
                                singleapp = new App(usageStats.getPackageName(), String.valueOf(usageStats.getTotalTimeInForeground() / 1000), "Communication", "AndroidCallUI");
                            } else if (usageStats.getPackageName().contains("email")) {
                                singleapp = new App(usageStats.getPackageName(), String.valueOf(usageStats.getTotalTimeInForeground() / 1000), "Communication", "AndroidEmail");
                            } else if (usageStats.getPackageName().contains("browser")) {
                                singleapp = new App(usageStats.getPackageName(), String.valueOf(usageStats.getTotalTimeInForeground() / 1000), "Communication", "AndroidBrowser");
                            } else if (usageStats.getPackageName().contains("calculator")) {
                                singleapp = new App(usageStats.getPackageName(), String.valueOf(usageStats.getTotalTimeInForeground() / 1000), "Productivity", "AndroidCalculator");
                            }
                            if (singleapp != null)
                                singleapp.setSubCategory("Default");
                        } else {
                            if (!usageStats.getPackageName().contains("fyp") && (!usageStats.getPackageName().contains("home"))) {
                                singleapp = new App();
                                singleapp.setAppId(usageStats.getPackageName());
//                                singleapp.setGenre("Local");
//                                singleapp.setSubCategory("Local");
                                singleapp.setForegroundtime(String.valueOf(usageStats.getTotalTimeInForeground() / 1000));
                                toretrieve.add(usageStats.getPackageName());
                            }
                        }
                        if (singleapp != null)
                            appHm.put(usageStats.getPackageName(), singleapp);//singleapp => Hashmap<Packagename,<Category,Foregroundtime,Title>>

                    }
                }
            }
        } else {
            @SuppressWarnings("deprecation") ActivityManager.RunningTaskInfo foregroundTaskInfo = activityManager.getRunningTasks(1).get(0);
            return null;

        }
        return toretrieve;
    }


    private class getUsageCategory extends AsyncTask<String, Integer, String> {

        boolean flag = true;
        int noofapps = 0;

        @Override
        protected String doInBackground(String... params) {

            Log.e(TAG, "Retrieving App Usage");
            String APIUrl = mActivity.getURL().concat("process_getappbyid");
            ArrayList<String> jsonresult = new ArrayList<>();
            String result = "{Applications : [";
            int resCode;
            int count = 1;
            InputStream in;
            noofapps = params.length;
            for (String key : params) { //array of apps to retrieve
                publishProgress(count * 100 / noofapps);
                try {
                    URL url = new URL(APIUrl);
                    URLConnection urlConn = url.openConnection();
                    HttpURLConnection httpConn = (HttpURLConnection) urlConn;
                    httpConn.setAllowUserInteraction(false);
                    httpConn.setInstanceFollowRedirects(true);
                    httpConn.setRequestMethod("POST");
                    httpConn.setDoInput(true);
                    httpConn.setDoOutput(true);

                    Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("app_id", key);
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = httpConn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    httpConn.connect();
                    if (key.contains("fyp")) {
                    }
                    resCode = httpConn.getResponseCode();

                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                in, "iso-8859-1"), 8);
                        StringBuilder sb = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            sb.append(line).append("\n");
                        }
                        in.close();
                        if (sb.toString().length() != 0) {
                            sb.insert(1, "\"appId\":\"" + key + "\",");
                            jsonresult.add(sb.toString());
                        }
//

                    } else {
                        Log.e(TAG, "Fail to retrieve");
                        appHm.remove(key);
                    }
                } catch (IOException e) {

                    Log.e(TAG, "IOException");
                    flag = false;
                    e.printStackTrace();
                }
                count++;
            }

            for (int i = 0; i < jsonresult.size(); i++) {
                if (i == 0) {
                    result += jsonresult.get(i);
                } else {
                    result += "," + jsonresult.get(i);
                }

            }
            result += "]}"; //complete json string
            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {

            if (progress[0] < 30) {
                mActivity.setProgressMessage("Android has background applications running all the time...");
            } else if (progress[0] < 80) {

                mActivity.setProgressMessage("Do you know you have used more than " + noofapps + " applications in the past 7 days... inclusive of default background applications.");

            } else {
                int maxforeground = 0;
                for (App singleapp : appHm.values()) {
                    if (Integer.valueOf(singleapp.getForegroundtime()) > maxforeground) {
                        maxforeground = Integer.valueOf(singleapp.getForegroundtime());
                        mostusedapp = singleapp.getAppId().substring(singleapp.getAppId().lastIndexOf(".") + 1);
                        mActivity.setProgressMessage("You spent the most time on " + singleapp.getAppId().substring(singleapp.getAppId().lastIndexOf(".") + 1));
                    }
                }
            }

        }

        @Override
        protected void onPostExecute(String result) {
            if (flag) {
                JSONObject jsnobject = null;
                String appusage = "";
                try {
                    jsnobject = new JSONObject(result);
                    JSONArray jsonArray = jsnobject.getJSONArray("Applications"); //retrieve from jsonstring

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject explrObject = jsonArray.getJSONObject(i);
//                    App singleapp = appHm.get(explrObject.getString("appId")); //get the app hashmap
                        App singleapp = appHm.get(explrObject.getString("appId"));
//                    singleapp.put("Title", explrObject.get("title").toString());
                        singleapp.setTitle(explrObject.get("title").toString());

                        singleapp.setDescription(explrObject.getString("description").toString());
                        if (mActivity.getGamingCategory().contains(explrObject.getString("genre").toString())) {
                            singleapp.setGenre("Game");
                            singleapp.setSubCategory(explrObject.getString("genre").toString());
                        } else {
                            singleapp.setGenre(explrObject.getString("genre").toString());
                            if (singleapp.getDescription().length() != 0) {
                                singleapp = retrievesubcategory(singleapp);
                            } else {
                                singleapp.setSubCategory("Local");
                            }
                        }

//                        singleapp.put("Description", explrObject.get("description").toString());
//                    singleapp.put("Category", explrObject.get("genre").toString());


                        appHm.put(explrObject.get("appId").toString(), singleapp);

                    }

                    for (String key : appHm.keySet()) { //key is packagename

                        App singleapp = appHm.get(key);
                        HashMap<String, Integer> subcat = new HashMap<>();
                        if (singleapp.getGenre() != null) {
                            appusage += singleapp.getGenre() + " : " + singleapp.getTitle() + " = " + singleapp.getForegroundtime() + "seconds\n";
                            if (catUsage.containsKey(singleapp.getGenre())) {
                                subcat = catUsage.get(singleapp.getGenre());

                                if (subcat.containsKey(singleapp.getSubCategory())) {
                                    Integer newUsage = subcat.get(singleapp.getSubCategory()).intValue() + Integer.parseInt(singleapp.getForegroundtime());
                                    subcat.put(singleapp.getSubCategory(), newUsage);
                                    catUsage.put(singleapp.getGenre(), subcat);
                                } else {
                                    subcat.put(singleapp.getSubCategory(), Integer.parseInt(singleapp.getForegroundtime()));
                                    catUsage.put(singleapp.getGenre(), subcat);
                                }

                            } else {

                                subcat.put(singleapp.getSubCategory(), Integer.parseInt(singleapp.getForegroundtime()));
                                catUsage.put(singleapp.getGenre(), subcat);
                            }
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                new calculateBigFive(catUsage).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            } else {
                mActivity.showToast("Please check if you are connected to the internet");
                mActivity.reloadbutton();
                mActivity.dismissProgressDialog();
            }

        }

        @Override
        protected void onPreExecute() {
            mActivity.showProgressDialog("Please wait... Checking App Usage");

        }

    }

    public App retrievesubcategory(App singleapp) {
        String desc = singleapp.getDescription();
        ArrayList<String> subcategory = new ArrayList<>();
        ArrayList<String> keywordlist = new ArrayList<>();
        ArrayList<Integer> subcategoryscore = new ArrayList<>();

        switch (singleapp.getGenre()) {
            case "Communication":
                subcategory.add("Messenger");
                subcategory.add("Browser");
                subcategory.add("CMail");
                keywordlist.add("messag");
                keywordlist.add("call");
                keywordlist.add("brows");
                keywordlist.add("web");
                keywordlist.add("mail");
                keywordlist.add("inbox");
                break;
            case "Productivity":
                subcategory.add("Tool");
                subcategory.add("Calendar");
                subcategory.add("PMail");
                keywordlist.add("creat");
                keywordlist.add("save");
                keywordlist.add("event");
                keywordlist.add("holiday");
                keywordlist.add("inbox");
                keywordlist.add("mail");
                break;
            case "Social":
                subcategory.add("PostSocial");
                subcategory.add("MediaSocial");
                subcategory.add("Stream");
                keywordlist.add("friend");
                keywordlist.add("post");
                keywordlist.add("photo");
                keywordlist.add("video");
                keywordlist.add("stream");
                keywordlist.add("broadcast");
                break;
            case "Video Players & Editors":
                subcategory.add("Video");
                subcategory.add("Tools");
                subcategory.add("VRecorder");
                keywordlist.add("watch");
                keywordlist.add("playlist");
                keywordlist.add("convert");
                keywordlist.add("edit");
                keywordlist.add("record");
                keywordlist.add("save");
                break;
            case "Personalization":
                subcategory.add("Wallpaper");
                subcategory.add("Fonts");
                subcategory.add("Keyboard");
                keywordlist.add("screen");
                keywordlist.add("theme");
                keywordlist.add("font");
                keywordlist.add("style");
                keywordlist.add("keyboard");
                keywordlist.add("emoji");
                break;
            case "Music & Audio":
                subcategory.add("Music");
                subcategory.add("Radio");
                subcategory.add("MRecorder");
                keywordlist.add("listen");
                keywordlist.add("playlist");
                keywordlist.add("radio");
                keywordlist.add("local");
                keywordlist.add("record");
                keywordlist.add("save");

                break;


        }
        desc = stemtext(desc);
        int count = 0;
        for (int y = 0; y < keywordlist.size(); y++) {
            String findStr = keywordlist.get(y);
            int lastIndex = 0;


            while (lastIndex != -1) {

                lastIndex = desc.indexOf(findStr, lastIndex);

                if (lastIndex != -1) {
                    count++;
                    lastIndex += findStr.length();
                }
            }
            if (y % 2 == 1) {
                subcategoryscore.add(count);
                count = 0;
            }

        }


        if (keywordlist.size() > 0) {
            switch (subcategoryscore.indexOf(Collections.max(subcategoryscore))) {
                case 0:
                    singleapp.setSubCategory(subcategory.get(0));
                    break;
                case 1:
                    singleapp.setSubCategory(subcategory.get(1));
                    break;
                case 2:
                    singleapp.setSubCategory(subcategory.get(2));
            }

        }
        return singleapp;
    }

    public String stemtext(String str) {
        String newstring = "";
        List<String> myList = new ArrayList<>(Arrays.asList(str.split(",")));
        Stemmer s = new Stemmer();
        for (int i = 0; i < myList.size(); i++) {
            s.add(myList.get(i).toCharArray(), myList.get(i).length());
            s.stem();
            newstring += s.toString();

        }
        return newstring;
    }


    private class calculateBigFive extends AsyncTask<String, Void, String> {
        HashMap<String, HashMap<String, Integer>> fullcatUsage;
        HashMap<String, Double> catUsage;
        Double ExtraversionSc = 0.1;
        Double AgreeablenessSc = 0.1;
        Double ConscientiousnessSc = 0.1;
        Double NeuroticismSc = 0.1;
        Double OpennessSc = 0.1;
        Double TotalUsageTime = 0.1;
        ArrayList<Double> Scores;

        float[] timeArray = new float[8];

        public calculateBigFive(HashMap<String, HashMap<String, Integer>> m) {
            fullcatUsage = m;
            catUsage = new HashMap<>();
            for (String key : m.keySet()) {//error

                HashMap<String, Integer> subcat = m.get(key);
                Double categoryusage = 0.0;
                for (String key2 : subcat.keySet()) {
                    TotalUsageTime += subcat.get(key2).doubleValue();
                    categoryusage += subcat.get(key2).doubleValue();
                }
                catUsage.put(key, categoryusage);
            }
            Scores = new ArrayList<>();
        }


        @Override
        protected String doInBackground(String... params) {
            String result = "";
            String Max1 = "", Max2 = "";

            Double socialtime = 0.1;
            Double gamingtime = 0.1;
            Double communicationtime = 0.1;
            Double personalizationtime = 0.1;
            Double productivitytime = 0.1;
            Double shoppingtime = 0.1;
            Double musicnaudiotime = 0.1;
            Double videoplayertime = 0.1;

            String[] arrGamesCategory = mActivity.getResources().getStringArray(R.array.gamingcategory);
            Double gametime = 0.0;
            for (int i = 0; i < arrGamesCategory.length; i++) {
                if (catUsage.containsKey(arrGamesCategory[i])) {
                    gametime += catUsage.get(arrGamesCategory[i]);
                }
            }

            gamingtime = gametime / TotalUsageTime;


            if (catUsage.get("Social") != null) {
                socialtime = catUsage.get("Social") / TotalUsageTime;
            }

            if (catUsage.get("Communication") != null) {
                communicationtime = catUsage.get("Communication") / TotalUsageTime;
            }

            if (catUsage.get("Productivity") != null) {
                productivitytime = catUsage.get("Productivity") / TotalUsageTime;
            }

            if (catUsage.get("Shopping") != null) {
                shoppingtime = catUsage.get("Shopping") / TotalUsageTime;
            }

            if (catUsage.get("Personalization") != null) {
                shoppingtime = catUsage.get("Personalization") / TotalUsageTime;
            }

            if (catUsage.get("Music & Audio") != null) {
                musicnaudiotime = catUsage.get("Music & Audio") / TotalUsageTime;
                ;
            }

            if (catUsage.get("Video Players & Editors") != null) {
                videoplayertime = catUsage.get("Video Players & Editors") / TotalUsageTime;

            }

            Double AppExtraversion = ((communicationtime * 0.25) + (productivitytime * 0.25) + (socialtime * 0.25) + ((1 - gamingtime) * 0.25));

            ExtraversionSc = AppExtraversion;
            if (!ExtraversionSc.isNaN()) {
                Scores.add(ExtraversionSc);
            } else {
                Scores.add(0.0);
            }

            Double AppAgreeableness = (((1 - musicnaudiotime) * 0.25) + (personalizationtime * 0.25) + (shoppingtime * 0.25) + ((1 - videoplayertime) * 0.25));

            AgreeablenessSc = AppAgreeableness;
            if (!AgreeablenessSc.isNaN()) {
                Scores.add(AgreeablenessSc);
            } else {
                Scores.add(0.0);
            }


            Double AppConscientiousness = (((1 - musicnaudiotime) * 0.25) + ((1 - gamingtime) * 0.25) + ((productivitytime) * 0.25) + ((1 - communicationtime) * 0.25));
            ConscientiousnessSc = AppConscientiousness;
            if (!ConscientiousnessSc.isNaN()) {
                Scores.add(ConscientiousnessSc);
            } else {
                Scores.add(0.0);
            }


            Double AppNeuroticism = ((gamingtime * 0.25) + ((1 - communicationtime) * 0.25) + (shoppingtime * 0.25) + (musicnaudiotime * 0.25));

            NeuroticismSc = AppNeuroticism;
            if (!NeuroticismSc.isNaN()) {
                Scores.add(NeuroticismSc);
            } else {
                Scores.add(0.0);
            }

            Double AppOpenness = (((1 - communicationtime) * 0.25) + ((1 - productivitytime) * 0.25)) + (musicnaudiotime * 0.25) + (videoplayertime * 0.25);
            OpennessSc = AppOpenness;
            if (!OpennessSc.isNaN()) {
                Scores.add(OpennessSc);
            } else {
                Scores.add(0.0);
            }

            timeArray[0] = socialtime.floatValue();
            timeArray[1]=gamingtime.floatValue();
            timeArray[2]=communicationtime.floatValue();
            timeArray[3]=personalizationtime.floatValue();
            timeArray[4]=productivitytime.floatValue();
            timeArray[5]=shoppingtime.floatValue();
            timeArray[6]=musicnaudiotime.floatValue();
            timeArray[7]=videoplayertime.floatValue();

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            SharedPreferences.Editor editor = sharedPref.edit();

            ArrayList<String> AttrList = new ArrayList<>();
            AttrList.add(mActivity.getString(R.string.AttrOne));
            AttrList.add(mActivity.getString(R.string.AttrTwo));
            AttrList.add(mActivity.getString(R.string.AttrThree));
            AttrList.add(mActivity.getString(R.string.AttrFour));
            AttrList.add(mActivity.getString(R.string.AttrFive));
            ArrayList<Double> tScores = Scores;
            editor.putFloat(mActivity.getString(R.string.dattr1), Scores.get(0).floatValue());
            editor.putFloat(mActivity.getString(R.string.dattr2), Scores.get(1).floatValue());
            editor.putFloat(mActivity.getString(R.string.dattr3), Scores.get(2).floatValue());
            editor.putFloat(mActivity.getString(R.string.dattr4), Scores.get(3).floatValue());
            editor.putFloat(mActivity.getString(R.string.dattr5), Scores.get(4).floatValue());
            editor.putBoolean("quizdone", true);
            editor.putString("mostusedapp", mostusedapp);

            Double max = Collections.max(tScores);
            int highest = tScores.indexOf(max);
            editor.putString(mActivity.getString(R.string.Max1), AttrList.get(highest));
//            editor.putString(mActivity.getString(R.string.Max1), "Extraversion");

            HashMap<String, Integer> subCatUsage = new HashMap<>();
//            fullcatUsage.get()
            for (HashMap<String, Integer> key : fullcatUsage.values()) {

                for (String key2 : key.keySet()) {
                    if (key2 != null)
                        subCatUsage.put(key2, key.get(key2));
                }

            }

            if (subCatUsage.containsKey("Default"))
                subCatUsage.remove("Default");
            int maxValueInMap=0;
            if (subCatUsage.size()!=0) {
                maxValueInMap = (Collections.max(subCatUsage.values()));  // This will return max value in the Hashmap

                for (HashMap.Entry<String, Integer> entry : subCatUsage.entrySet()) {  // Itrate through hashmap
                    if (entry.getValue() == maxValueInMap) {
                        editor.putString(mActivity.getString(R.string.Max1Usage), entry.getKey());
                    }
                }
            }else{
                editor.putString(mActivity.getString(R.string.Max1Usage), "");
            }


            Scores.remove(max);
            AttrList.remove(highest);
            Double max2 = Collections.max(tScores);
            highest = tScores.indexOf(max2);
            editor.putString(mActivity.getString(R.string.Max2), AttrList.get(highest));

            editor.putFloat(mActivity.getString(R.string.time1), timeArray[0]);
            editor.putFloat(mActivity.getString(R.string.time2), timeArray[1]);
            editor.putFloat(mActivity.getString(R.string.time3), timeArray[2]);
            editor.putFloat(mActivity.getString(R.string.time4), timeArray[3]);
            editor.putFloat(mActivity.getString(R.string.time5), timeArray[4]);
            editor.putFloat(mActivity.getString(R.string.time6), timeArray[5]);
            editor.putFloat(mActivity.getString(R.string.time7), timeArray[6]);
            editor.putFloat(mActivity.getString(R.string.time8), timeArray[7]);

            editor.commit();
            //new addtoDB().execute();

            mActivity.dismissProgressDialog();
            mActivity.launchHomeScreen();
        }

        @Override
        protected void onPreExecute() {

        }
    }

    private class addtoDB extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... Void) {

            DataCollectionTableDO appClass= new DataCollectionTableDO();
            appClass.setUserMatric(sharedPref.getString("matric", ""));
            appClass.setMExtraversion(String.valueOf(sharedPref.getFloat(mActivity.getString(R.string.dattr1), (float) 0.0)));
            appClass.setMAgreeableness(String.valueOf(sharedPref.getFloat(mActivity.getString(R.string.dattr2), (float) 0.0)));
            appClass.setMConscientiousness(String.valueOf(sharedPref.getFloat(mActivity.getString(R.string.dattr3), (float) 0.0)));
            appClass.setMNeuroticism(String.valueOf(sharedPref.getFloat(mActivity.getString(R.string.dattr4), (float) 0.0)));
            appClass.setMOpenness(String.valueOf(sharedPref.getFloat(mActivity.getString(R.string.dattr5), (float) 0.0)));
            appClass.setQExtraversion(String.valueOf(sharedPref.getFloat(mActivity.getString(R.string.qattr1), (float) 0.0)));
            appClass.setQAgreeableness(String.valueOf(sharedPref.getFloat(mActivity.getString(R.string.qattr2), (float) 0.0)));
            appClass.setQConscientiousness(String.valueOf(sharedPref.getFloat(mActivity.getString(R.string.qattr3), (float) 0.0)));
            appClass.setQNeuroticism(String.valueOf(sharedPref.getFloat(mActivity.getString(R.string.qattr4), (float) 0.0)));
            appClass.setQOpenness(String.valueOf(sharedPref.getFloat(mActivity.getString(R.string.qattr5), (float) 0.0)));

            DynamoDBManager.update(appClass);

            return 1;
        }


        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer ==1) {
                mActivity.showToast("Store successful");
            } else {
                mActivity.showToast("Please check if you are connected to the internet db");
                mActivity.reloadbutton();
                mActivity.dismissProgressDialog();
            }
        }

        @Override
        protected void onPreExecute() {
            mActivity.showProgressDialog("Please wait while we submit your survey..");
        }

    }

}
