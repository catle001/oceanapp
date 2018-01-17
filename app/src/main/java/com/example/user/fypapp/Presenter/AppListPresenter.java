package com.example.user.fypapp.Presenter;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.user.fypapp.Model.App;
import com.example.user.fypapp.View.MainActivity;
import com.example.user.fypapp.R;
import com.example.user.fypapp.View.ResponseListener;

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
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by user on 14/2/2017.
 */
public class AppListPresenter {

    ArrayList<App> items;
    ArrayList<App> featureditems;
    private MainActivity mActivity;
    private String subcat = "";
    ArrayList<String> arrCategories;
    public static final String TAG = "AppListPresenter";
    private boolean suggestedappsdone = false;

    public AppListPresenter() {
    }

    public void onAttachActivity(@NonNull Bundle savedInstanceState, @NonNull MainActivity activity) {
        mActivity = activity;
        loadData();
    }

    public void onDetachActivity() {
        mActivity = null;
    }


    private void loadData() {


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(mActivity);
        subcat = prefs.getString(mActivity.getString(R.string.Max1Usage), "");
        Log.e(TAG, "Most Utilized Category - " + subcat);
        items = new ArrayList<>();
        featureditems = new ArrayList<>();


        new GetCustomizedList(fresponseListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, mActivity.getFeaturedCategory(subcat));


    }

    private void updateUI(@NonNull final ArrayList<App> appList, @NonNull final int type) {
        if (mActivity != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (type == 2)
                        mActivity.setFeaturedAppList(appList);
                    else
                        mActivity.setAppList(appList);
                }
            });
        }
    }


    private ResponseListener sresponseListener = new ResponseListener() {
        @Override
        public void onResponse(List<App> newItems) {

            Random randomGenerator = new Random();

            try {
                while (newItems.size() > 0) {
                    int index = randomGenerator.nextInt(newItems.size());
                    items.add(newItems.get(index));
                    newItems.remove(index);
                }
                updateUI(items, 1);
            } catch (NullPointerException e) {
                Toast.makeText(mActivity, "Error", Toast.LENGTH_SHORT)
                        .show();
            }


        }
    };

    // The FetchItemsTask delivers the new data to this listener in the main thread.
    private ResponseListener fresponseListener = new ResponseListener() {
        @Override
        public void onResponse(List<App> newItems) {


            Random randomGenerator = new Random();

            try {
                if (newItems.size() > 0) {
                    for (int i = 0; i < 30; i++) {
                        int index = randomGenerator.nextInt(newItems.size());

                        featureditems.add(newItems.get(index));
                        newItems.remove(index);
                    }
                    updateUI(featureditems, 2);
                    activeProgressDialog();
                }
            } catch (NullPointerException e) {
                Toast.makeText(mActivity, "Error", Toast.LENGTH_SHORT)
                        .show();
            }


        }
    };

    private class GetCustomizedList extends AsyncTask<String, Void, String> {

        private ResponseListener responseListener;
        ArrayList<App> items;
        boolean flag = false;


        public GetCustomizedList(ResponseListener listener) {
            this.responseListener = listener;
        }


        @Override
        protected String doInBackground(String... params) {
            String APIUrl = mActivity.getURL() + "process_getappbycategory";
            StringBuilder sb = new StringBuilder();
            int resCode;
            InputStream in;
            int count = 0;
            for (String key : params) {
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
                            .appendQueryParameter("post_category", key)
                            .appendQueryParameter("post_num", "50");
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = httpConn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();

                    httpConn.connect();

                    resCode = httpConn.getResponseCode();

                    if (resCode == HttpURLConnection.HTTP_OK) {
                        in = httpConn.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                                in, "UTF-8"));

                        String line;
                        flag = true;


                        while ((line = reader.readLine()) != null) {
                            if (count == 0) {
                                sb.append(line.substring(0, line.length() - 1));
                            } else {
                                sb.append(",");
                                sb.append(line.substring(1, line.length() - 1));
                            }

                        }

                        in.close();
                    } else {
                        flag = false;
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Fail to retrieve applications");
                    flag = false;
                    e.printStackTrace();
                }
                count++;
            }
            sb.append("]");
            return sb.toString();
        }

        @Override
        protected void onPostExecute(String result) {


            if (flag) {
                try {
                    ArrayList<App> appArray = jsonArrayToObj(result);

                    items = new ArrayList<>();

                    for (int i = 0; i < appArray.size(); i++) {
                        items.add(i, appArray.get(i));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

                if (!suggestedappsdone) {
                    arrCategories = mActivity.getCategories();
                    String[] arr = arrCategories.toArray(new String[arrCategories.size()]);
                    new GetCustomizedList(sresponseListener).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, arr);
                    suggestedappsdone = true;
                }


                responseListener.onResponse(items);
            } else {
                Log.e(TAG, "Fail to retrieve applications");
                mActivity.showToast("Please check if you are connected to the internet");
            }
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    ArrayList<App> jsonArrayToObj(String jsonString) throws JSONException {
        JSONArray jsonArray = new JSONArray(jsonString);
        ArrayList<App> appArr = new ArrayList<>();
        Log.e(TAG, "Retrieved " + jsonArray.length() + " number of apps");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject explrObject = jsonArray.getJSONObject(i);
            App appobj = new App(explrObject.getString("url"), explrObject.getString("appId"), explrObject.getString("title"), explrObject.getString("developer"), explrObject.getDouble("score"), explrObject.getDouble("price"), explrObject.getBoolean("free"), explrObject.getString("icon"), explrObject.getString("genre"));

            appobj.setSubCategory(getsubcategory(explrObject.getString("description"), explrObject.getString("genre")));

            appArr.add(appobj);

        }


        return appArr;
    }

    public String getsubcategory(String desc, String category) {
        ArrayList<String> subcategory = new ArrayList<>();
        ArrayList<String> keywordlist = new ArrayList<>();
        ArrayList<Integer> subcategoryscore = new ArrayList<>();
        switch (category) {
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
                    return subcategory.get(0);
                case 1:
                    return subcategory.get(1);
                case 2:
                    return subcategory.get(2);
            }
        }
        return "";
    }

    public String stemtext(String str) {
        String newstring = "";
        List<String> myList = new ArrayList<String>(Arrays.asList(str.split(",")));
        Stemmer s = new Stemmer();
        for (int i = 0; i < myList.size(); i++) {
            s.add(myList.get(i).toCharArray(), myList.get(i).length());
            s.stem();
            newstring += s.toString();

        }
        return newstring;
    }

    protected void activeProgressDialog() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(mActivity);
        dialog.setTitle("Usability Survey");
        dialog.setMessage("Please complete a survey in the next page");

        dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.setNegativeButton("Remind me later", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                activeProgressDialog();
            }
        });
        dialog.setNeutralButton("Done it!", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Toast.makeText(mActivity, "Thanks for participating!", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        dialog.setCancelable(false);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                dialog.show();
            }
        }, 100000);
    }

}
