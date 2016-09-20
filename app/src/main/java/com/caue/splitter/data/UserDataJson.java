package com.caue.splitter.data;

import android.util.Log;

import com.caue.splitter.utils.MyUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Caue on 9/11/2016.
 */
public class UserDataJson {

    //public static final String PHP_SERVER = "http://splitter.mybluemix.net/user/";    // hostname da API no IBM Bluemix
    public static final String PHP_SERVER = "http://api.splitter.cf/user/";         // hostname da API utilizando o dominio registrado
    // String para API dos usuarios, necessario alterar o ip de acordo com a rede

    HashMap userData;


    // constructors
    public UserDataJson(HashMap<String, ?> userData){
        this.userData = userData;
    }

    public UserDataJson(Integer id, String name, Long cpf, String dateOfBirth, String login, Long phone, String password){
        userData = new HashMap();
        userData.put("id", id);
        userData.put("name", name);
        userData.put("cpf",cpf);
        userData.put("dateOfBirth",dateOfBirth);
        userData.put("login", login);
        userData.put("phone", phone);
        userData.put("password", password);
    }

    // getters
    public HashMap getUserData(){
        return userData;
    }

    public String toString() {
        return "id: " + userData.get("id") +
                ", name: " + userData.get("name") +
                "', cpf: " + userData.get("cpf") +
                "', dateOfBirth: " + userData.get("dateOfBirth") +
                ", login: " + userData.get("login") +
                ", phone: "+ userData.get("phone") + "";
    }

    // adding a new user to the database
    //display toast (need context)
    public void createUser(Map<String, ?> user){
        final JSONObject json;
        if(user != null) {
            json = new JSONObject(user);
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

    // updating a user in the database
    public void updateUser(Map<String, ?> user){
        final JSONObject json;
        if(user != null) {
            json = new JSONObject(user);
        }else json = null;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                String url = PHP_SERVER + "update";
                MyUtility.sendHttPostRequest(url, json);
            }
        };
        new Thread(runnable).start();
    }
/*
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
*/
    // DownloadUserJson to be used in the MyDownloadUserDataJson to download the user data
    public static HashMap downloadUserJson(String url){


        String jsonString = null;
        jsonString =  MyUtility.downloadJSONusingHTTPGetRequest(url);
        Log.d("downloadUserJson", url);
        if(jsonString != null){
            try {
                JSONArray jsonArray = new JSONArray(jsonString);
                JSONObject userJsonObj = jsonArray.getJSONObject(0);
                if (userJsonObj != null) {

                    int id = Integer.parseInt(userJsonObj.optString("id"));
                    String name = userJsonObj.optString("name");
                    Long cpf = Long.parseLong(userJsonObj.optString("cpf"));
                    String dateOfBirth = userJsonObj.optString("dateOfBirth");
                    String login = userJsonObj.optString("login");
                    Long phone = Long.parseLong(userJsonObj.optString("phone"));
                    String password = userJsonObj.optString("password");

                    Log.d("downloadUserJson",
                            "id: " + id +
                                    " login: " + login +
                                    " dateOfBirth: " + dateOfBirth +
                                    "\n");

                    // return the created user
                    return createUser(id, name, cpf, dateOfBirth, login, phone, password);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else Log.d("downloadUserJson", "jsonString was null");
        return null;
    }

    private static HashMap createUser(int id, String name, Long cpf, String dateOfBirth, String login, Long phone,String password){
        HashMap userData = new HashMap();
        userData.put("id", id);
        userData.put("name", name);
        userData.put("cpf",cpf);
        userData.put("dateOfBirth",dateOfBirth);
        userData.put("login", login);
        userData.put("phone", phone);
        userData.put("password", password);

        return userData;
    }

}
