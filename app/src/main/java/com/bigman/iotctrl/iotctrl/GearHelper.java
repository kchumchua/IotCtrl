package com.bigman.iotctrl.iotctrl;
import android.app.Activity;
import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.support.v4.content.LocalBroadcastManager;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;
/**
 * Created by BIGMAN on 25/2/2560.
 */

public class GearHelper implements MicrogearEventListener {
    public final static String LOG_GEAR = "[GEAR]";
    private Context mcontex;
    private String mname;
    private Microgear mgearInst;

    private void DestroyObject()
    {
        mgearInst = null;
        mcontex = null;
    }

    GearHelper()
    {

    }
    GearHelper(Context ctx, String name)
    {
        mname = name;
        mcontex = ctx;
        mgearInst = new Microgear(ctx);
        mgearInst.setCallback(this);
    }

    public void GearDestroy(){
        if(mgearInst != null){
            Log.d("GearEvent", "GearDestroy");
            mgearInst.disconnect();
        }
    }

    public void GearResume(){
        if(mgearInst != null){
            Log.d("GearEvent", "GearResume");
            //mgearInst.bindServiceResume();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            DestroyObject();
        } catch (Throwable ex)
        {
            throw ex;
        }finally {
            super.finalize();
        }

    }

    public Microgear geGearInst() {
        return mgearInst;
    }

    Handler mhandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            String strData = bundle.getString("gear");
            if(strData != null && !strData.isEmpty()) {
                //Local Broadcast message
                Intent intent = new Intent("GearCast");
                intent.putExtra("msg", strData);
                LocalBroadcastManager.getInstance(mcontex.getApplicationContext()).sendBroadcast(intent);
                //Log.i("HandleMsg", strData);
                Debug.out(Debug.LOG_CASTMSG+"==>%s",strData);

                //          try {
                //              Log.i("HandleMsg", strData);
                //          }catch (Exception e){
                //              e.printStackTrace();
                //          }
            }
        }

    };

    @Override
    public void onConnect() {
        Message msg = mhandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("gear", "Now I'm connected with netpie");
        msg.setData(bundle);
        mhandler.sendMessage(msg);
        //Log.i("Connected","Now I'm connected with netpie");
    }

    @Override
    public void onMessage(String topic, String message) {
        Message msg = mhandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("gear", topic+" : "+message);
        msg.setData(bundle);
        mhandler.sendMessage(msg);
        //Log.i("Message",topic+" : "+message);
    }

    @Override
    public void onPresent(String token) {
        Message msg = mhandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("gear", "New friend Connect :"+token);
        msg.setData(bundle);
        mhandler.sendMessage(msg);
        //Log.i("present","New friend Connect :"+token);
    }

    @Override
    public void onAbsent(String token) {
        Message msg = mhandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("gear", "Friend lost :"+token);
        msg.setData(bundle);
        mhandler.sendMessage(msg);
        //Log.i("absent","Friend lost :"+token);
    }

    @Override
    public void onDisconnect() {
        Message msg = mhandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("gear", "Disconnected");
        msg.setData(bundle);
        mhandler.sendMessage(msg);
        //Log.i("disconnect","Disconnected");
    }

    @Override
    public void onError(String error) {
        Message msg = mhandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("gear", "Exception : "+error);
        msg.setData(bundle);
        mhandler.sendMessage(msg);
        //Log.i("exception","Exception : "+error);
    }

    @Override
    public void onInfo(String info) {
        Message msg = mhandler.obtainMessage();
        Bundle bundle = new Bundle();
        bundle.putString("gear", "info : "+info);
        msg.setData(bundle);
        mhandler.sendMessage(msg);
        //Log.i("info","info : "+info);
    }



}
