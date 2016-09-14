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
        if(user != null){
            final MainPageActivity mActivity = activityReference.get();
            if(mActivity != null){
                Log.d("MyDownloadUserDataJson", "user Json is NOT null");
                mActivity.newUser();
            }
        }
        else{
            Log.d("MyDownloadUserDataJson", "user Json is null");
        }
    }
}

