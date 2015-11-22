package com.creative.womensafety;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creative.womensafety.appdata.AppController;
import com.creative.womensafety.drawer.Drawer_Data;
import com.creative.womensafety.receiver.ScreenOnOffReceiver;
import com.creative.womensafety.userview.LoginOrSingupActivity;
import com.creative.womensafety.utils.CheckDeviceConfig;
import com.creative.womensafety.utils.GPSTracker;
import com.creative.womenssafety.R;
import com.creative.womensafety.appdata.AppConstant;
import com.creative.womensafety.service.LockScreenService;
import com.creative.womensafety.sharedprefs.SaveManager;
import com.creative.womensafety.drawer.Drawer_item;
import com.creative.womensafety.drawer.Drawer_list_adapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private CheckDeviceConfig checkDeviceConfig;


    ScreenOnOffReceiver mScreenStateReceiver;

    private SaveManager saveManager;

    private String gcmRegId;

    private Button btn_helpMe;

    private ListView drawer_list;
    private Drawer_list_adapter drawer_adapter_custom;

    GPSTracker gps;

    //private  checkDeviceConfig

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkDeviceConfig = new CheckDeviceConfig(this);

        saveManager = new SaveManager(this);

        gps = new GPSTracker(this);

        // start service for observing intents
        startService(new Intent(this, LockScreenService.class));


        init();


    }


    private void registerReceiverForHomeButtonAction() {

        mScreenStateReceiver = new ScreenOnOffReceiver();

        //registerReceiver(mScreenStateReceiver, new IntentFilter(Intent.ACTION_SCREEN_ON));
        //registerReceiver(mScreenStateReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));


        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(mScreenStateReceiver, filter);
    }


    private void init() {

        btn_helpMe = (Button) findViewById(R.id.btnHelpMe);
        btn_helpMe.setOnClickListener(this);
        setDrawer();
    }

    protected void setDrawer(){

        ArrayList<Drawer_item> item_list = new ArrayList<>();
        for(int i=0;i< Drawer_Data.drawer_list_text.length;i++){
            item_list.add(new Drawer_item(Drawer_Data.drawer_list_text[i],Drawer_Data.drawer_list_image[i]));
        }

        drawer_list = (ListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.drawer_list_header, drawer_list, false);


        drawer_adapter_custom = new Drawer_list_adapter(this,R.layout.drawer_list_item,item_list);
        drawer_list.addHeaderView(header, null, false);
        drawer_list.setAdapter(drawer_adapter_custom);

        drawer_list.setOnItemClickListener(new DrawerItemClickListener());
    }
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }
    private void selectItem(int position){
        switch(position){
            case 1:
                Toast.makeText(this,"Hospital",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(this,"POLICE",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this,"HOW TO USE",Toast.LENGTH_SHORT).show();
                break;
            case 4:
                Toast.makeText(this,"LOGOUT",Toast.LENGTH_SHORT).show();
                break;
            case 5:
                Toast.makeText(this,"Exit",Toast.LENGTH_SHORT).show();
                break;

        }
    }

    @Override
    public void onClick(View v) {


        int id = v.getId();

        if (id == R.id.btnHelpMe) {
            if (checkDeviceConfig.isGoogelPlayInstalled()) {

                // Read saved registration id from shared preferences.
                gcmRegId = saveManager.getUserGcmRegId();

                String lat = String.valueOf(gps.getLatitude());

                String lng = String.valueOf(gps.getLongitude());


                this.sendBroadcast(new Intent("com.google.android.intent.action.GTALK_HEARTBEAT"));
                this.sendBroadcast(new Intent("com.google.android.intent.action.MCS_HEARTBEAT"));

                sendRequestToServer(AppConstant.getUrlForHelpSend(gcmRegId, lat, lng));


            }
        }
    }


    public void sendRequestToServer(String sentUrl) {

        StringRequest req = new StringRequest(Request.Method.GET, sentUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Toast.makeText(getApplicationContext(), response,
                                Toast.LENGTH_LONG).show();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        });

        AppController.getInstance().addToRequestQueue(req);

    }

    public void LogOut(View view) {
        saveManager.setIsLoggedIn(false);
        Intent home = new Intent(MainActivity.this, LoginOrSingupActivity.class);
        startActivity(home);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenStateReceiver);
    }
}

