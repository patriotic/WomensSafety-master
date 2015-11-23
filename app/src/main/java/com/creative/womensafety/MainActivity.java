package com.creative.womensafety;

import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.creative.womensafety.appdata.AppController;
import com.creative.womensafety.receiver.ScreenOnOffReceiver;
import com.creative.womensafety.userview.LoginOrSingupActivity;
import com.creative.womensafety.utils.CheckDeviceConfig;
import com.creative.womensafety.utils.GPSTracker;
import com.creative.womenssafety.R;
import com.creative.womensafety.appdata.AppConstant;
import com.creative.womensafety.service.LockScreenService;
import com.creative.womensafety.sharedprefs.SaveManager;
import com.creative.womensafety.drawer.Drawer_list_adapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private CheckDeviceConfig checkDeviceConfig;


    ScreenOnOffReceiver mScreenStateReceiver;

    private SaveManager saveManager;

    private String gcmRegId;

    private Button btn_helpMe;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private ExpandableListView drawer_list;
    private Drawer_list_adapter drawer_adapter_custom;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;

    private Toolbar toolbar;

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


//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void init() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btn_helpMe = (Button) findViewById(R.id.btnHelpMe);
        btn_helpMe.setOnClickListener(this);


        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_icon_inactive);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
        ) {
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_icon_inactive);
            }
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_icon_active);
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        setDrawer();


    }




    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data

        listDataHeader.add("HOW TO USE");
        listDataHeader.add("INFORMATION");
        listDataHeader.add("LOGOUT");
        listDataHeader.add("EXIT");
        // Adding child data
        List<String> info = new ArrayList<String>();
        info.add("HOSPITAL");
        info.add("POLICE");
        info.add("FIRE SERVICE");


        listDataChild.put(listDataHeader.get(1), info); // Header, Child data
//        listDataChild.put(listDataHeader.get(1), others);
    }
    protected void setDrawer(){
        prepareListData();
        drawer_list = (ExpandableListView) findViewById(R.id.left_drawer);
        LayoutInflater inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup) inflater.inflate(R.layout.drawer_list_header, drawer_list, false);


        drawer_adapter_custom = new Drawer_list_adapter(this,listDataHeader,listDataChild);
        drawer_list.addHeaderView(header, null, false);
        drawer_list.setAdapter(drawer_adapter_custom);

        drawer_list.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });
        drawer_list.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                Toast.makeText(getApplicationContext(), "" + listDataHeader.get(i), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }
}

