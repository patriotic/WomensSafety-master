package com.creative.womensafety.receiver;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.creative.womensafety.service.GCMIntentService;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver{

	@Override
    public void onReceive(Context context, Intent intent) {
        // Attach component of GCMIntentService that will handle the intent in background thread
        ComponentName comp = new ComponentName(context.getPackageName(),
                GCMIntentService.class.getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
