package com.example.user.fypapp.Presenter;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.user.fypapp.Model.App;
import com.example.user.fypapp.View.DetailActivity;
import com.example.user.fypapp.R;

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

/**
 * Created by user on 14/2/2017.
 */
public class AppDetailsPresenter {

    App item;
    private DetailActivity mActivity;

    public AppDetailsPresenter() {
    }

    public void onAttachActivity(@NonNull Bundle savedInstanceState, @NonNull DetailActivity activity) {
        mActivity = activity;
        loadData();
    }

    public void onDetachActivity() {
        mActivity = null;
    }


    private void loadData() {
        String id = mActivity.getIntent().getExtras().getString("id");
        new DownloadAppInfo().execute(id);
    }


    private class DownloadAppInfo extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... appid) {

            String APIUrl = mActivity.getResources().getString(R.string.portUrl) + "process_getappbyid";

            String result = null;
            int resCode;
            InputStream in;
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
                        .appendQueryParameter("app_id", appid[0]);
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
                    System.out.println("inside httpok");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            in, "UTF-8"));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    in.close();
                    result = sb.toString();

                } else {
                    System.out.println(resCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                final App application = jsonArrayToObj(result);
                mActivity.populateData(application);
                mActivity.dismissProgressDialog();

            } catch (JSONException e) {
                e.printStackTrace();

            }

        }

        @Override
        protected void onPreExecute() {
            mActivity.showProgressDialog();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    App jsonArrayToObj(String jsonString) throws JSONException {

        JSONObject explrObject = new JSONObject(jsonString);
        App appobj = new App(explrObject.getString("title"), explrObject.optString("summary"), explrObject.optString("icon"), explrObject.optDouble("price"), explrObject.optBoolean("free"), explrObject.optInt("minInstalls"), explrObject.optInt("maxInstalls"), explrObject.optDouble("score"), explrObject.optInt("reviews"), explrObject.optString("developer"), explrObject.optString("developerEmail"), explrObject.optString("developerWebsite"), explrObject.optString("updated"), explrObject.optString("version"), explrObject.optString("genre"), explrObject.optString("genreId"), explrObject.optString("size"), explrObject.optString("description"), explrObject.optString("descriptionHTML"), explrObject.optString("histogram"), explrObject.optBoolean("offersIAP"), explrObject.optBoolean("adSupported"), explrObject.optString("androidVersionText"), explrObject.optString("androidVersion"), explrObject.optString("contentRating"), explrObject.optString("screenshots"), explrObject.optString("video"), explrObject.optString("comments"), explrObject.optString("recentChanges"), explrObject.optString("url"), explrObject.optString("appId"));

        return appobj;
    }

}
