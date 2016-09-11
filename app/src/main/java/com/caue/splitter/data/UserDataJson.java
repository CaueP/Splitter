package com.caue.splitter.data;

import android.util.Log;

import com.caue.splitter.utils.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Caue on 9/11/2016.
 */
public class UserDataJson {

    public final String PHP_SERVER = "http://localhost:444/splitter/user/"; // String para API dos usuarios

    HashMap userData;

    // constructors
    public UserDataJson(){
    }

    public UserDataJson(String userId, String userLogin, String userPassword, String userName, int userCPF){
        userData = new HashMap();
        userData.put("id", userId);
        userData.put("login", userLogin);
        userData.put("password",userPassword);
        userData.put("name", userName);
        userData.put("cpf",userCPF);
    }


    // adding a new item to the database
    //display toast (need context)
    public void createUser(Map<String, ?> item){
        final JSONObject json;
        if(item != null) {
            json = new JSONObject(item);
        }else json = null;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String url = PHP_SERVER + "add";
                MyUtility.sendHttPostRequest(url, json);
            }
        };
        new Thread(runnable).start();
    }


    public void deleteUser(Map<String, ?> item){
        final JSONObject json;
        if(item != null) {
            json = new JSONObject(item);
        }else json = null;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String url = PHP_SERVER + "delete";
                MyUtility.sendHttPostRequest(url, json);
            }
        };
        new Thread(runnable).start();
    }

    public static HashMap downloadUserJson(String url){

        String jsonString = null;
        jsonString =  MyUtility.downloadJSONusingHTTPGetRequest(url);
        Log.d("downloadUserJson", url);
        if(jsonString != null){
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                JSONObject userJsonObj = jsonArray.getJSONObject(0);
                if (userJsonObj != null) {

                    String id = userJsonObj.optString("id");
                    String login = userJsonObj.optString("login");
                    String password = userJsonObj.optString("password");
                    String name = userJsonObj.optString("name");
                    int cpf = Integer.parseInt(userJsonObj.optString("cpf"));

                    Log.d("UserDataJson",
                            "Id: " + id +
                                    "name: " + name +
                                    "login: " + login+
                                    "cpf: " + cpf + "\n");

                    // return the created movie
                    return createUser(id, login, password, name, cpf);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else Log.d("MovieDataJson", "jsonString was null");
        return null;
    }

    private static HashMap createUser(String userId, String userLogin, String userPassword, String userName, int userCPF) {

        HashMap userData = new HashMap();
        userData.put("id", userId);
        userData.put("login", userLogin);
        userData.put("password",userPassword);
        userData.put("name", userName);
        userData.put("cpf",userCPF);
        return userData;
    }

}
