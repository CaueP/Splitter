package com.caue.splitter.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.caue.splitter.MainPageActivity;
import com.caue.splitter.data.UserDataJson;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by Caue on 9/16/2016.
 */
public class MyDownloadUserDataJson extends AsyncTask<String, Void, HashMap> {
    private final WeakReference<MainPageActivity> activityReference;

    public MyDownloadUserDataJson (MainPageActivity activity){
        activityReference = new WeakReference<MainPageActivity>(activity);
    }

    @Override
    protected HashMap doInBackground(String... urls) {
        HashMap user = null;
        for(String url: urls){
            user = UserDataJson.downloadUserJson(url);
        }
        return user;
    }

    @Override
    protected void onPostExecute(HashMap user){

        final MainPageActivity mActivity = activityReference.get();
        if(mActivity != null) {
            Log.d("MyDownloadUserDataJson", "Registered User");
            mActivity.userDataDownloaded(user);
        }
    }
}

