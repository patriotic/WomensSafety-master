package com.creative.womensafety.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.creative.womensafety.appdata.AppController;
import com.creative.womensafety.sharedprefs.SaveManager;
import com.creative.womensafety.utils.CheckDeviceConfig;
import com.creative.womensafety.utils.GPSTracker;
import com.creative.womensafety.appdata.AppConstant;

import org.json.JSONObject;

import java.util.Calendar;

public class MyService extends Service {
    Calendar cur_cal = Calendar.getInstance();


    // GPS Location
    GPSTracker gps;

    // Connection detector class
    CheckDeviceConfig checkDeviceConfig;

    private SaveManager saveManager;
    private static String KEY_STATUS = "status";
    private static String KEY_SESSION_TOKEN = "sessionId";


    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        saveManager = new SaveManager(this);


        checkDeviceConfig = new CheckDeviceConfig(getApplicationContext());

        // creating GPS Class object
        gps = new GPSTracker(this);


    }

    @Override
    public void onStart(Intent intent, int startId) {
        // TODO Auto-generated method stub
        super.onStart(intent, startId);
        // your code for background process


        Log.d("DEBUG", "onStart");

        if (!saveManager.getUserGcmRegId().equals("0") && saveManager.getIsLoggedIn()) {

            Toast.makeText(this,"home btn logged in",Toast.LENGTH_SHORT);
            if (checkDeviceConfig.isConnectingToInternet()) {
                //Log.d("DEBUG", "user connextion ok");
                if (gps.canGetLocation()) {

                    //Log.d("DEBUG", "user gps ok");

                    String user_lat = String.valueOf(gps.getLatitude());
                    String user_lang = String.valueOf(gps.getLongitude());

                    String URL = AppConstant.getUrlForHelpSend(saveManager.getUserGcmRegId(), user_lat, user_lang);

                    Log.d("DEBUG_helpUrl", URL);

                    Toast.makeText(this,"home btn press sucess",Toast.LENGTH_SHORT);

                    hitUrl(URL);
                }
            }
        }

    }

    private void hitUrl(String uRL) {
        // TODO Auto-generated method stub
        final JsonObjectRequest jsObjRequest = new JsonObjectRequest(
                Request.Method.GET, uRL, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        //String textResult = response.toString();


                    }

                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });
        // TODO Auto-generated method stub
        AppController.getInstance().addToRequestQueue(jsObjRequest);
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        //Log.d("DEBUG", "onBind");
        return null;
    }
}
