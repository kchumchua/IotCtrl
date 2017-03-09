package com.bigman.iotctrl.iotctrl;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.netpie.microgear.Microgear;


/**
 * A simple {@link Fragment} subclass.
 */

public class cmdFrag extends Fragment {

    private MainActivity mainActivity;
    private View rootView;
    private UnitDebug Debug = new UnitDebug(true);
    public cmdFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Debug.log(Debug.LOG_EVENT +"onCreateView");
        mainActivity = (MainActivity)getActivity();
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_cmd, container, false);

        Button btn = (Button) rootView.findViewById(R.id.navCmd_btnConn);
        btn.setOnClickListener(OnClickHandle);
        btn = (Button) rootView.findViewById(R.id.navCmd_btnPub);
        btn.setOnClickListener(OnClickHandle);
/*
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Microgear microgear = mainActivity.mGear1.geGearInst();
                microgear.resettoken();

                microgear.connect("NETPIECHAT","sHFgjf4lfvtC5tZ","216qG0v0f8IKhvsoPVYIbAlzq","android");
                microgear.subscribe("/topic_node");
                microgear.subscribe("/topic_user");

                //Log.i("Connecting","Gear Connecting");
                Debug.log(GearHelper.LOG_GEAR+"Connecting");

            }
        });
*/

        return rootView;
    }

    public void TestGearConnect(){
        Microgear microgear = mainActivity.mGear1.geGearInst();
        microgear.resettoken();

        microgear.connect("NETPIECHAT","sHFgjf4lfvtC5tZ","216qG0v0f8IKhvsoPVYIbAlzq","android");
        microgear.subscribe("/topic_node");
        microgear.subscribe("/topic_user");

        //Log.i("Connecting","Gear Connecting");
        Debug.log(GearHelper.LOG_GEAR+"Connecting");
    }
    public void TestGearDisConnect(){
        Microgear microgear = mainActivity.mGear1.geGearInst();
        microgear.disconnect();

        Debug.log(GearHelper.LOG_GEAR+"Disonnect");
    }

    public void TestGearPublish(){
        Microgear microgear = mainActivity.mGear1.geGearInst();
        String strDate = (String) DateFormat.format("dd-MM-yy hh:mm:ss",System.currentTimeMillis());
        //SimpleDateFormat formatter = new SimpleDateFormat("hh.mm.ss");
        //String strDate = formatter.format(Calendar.getInstance().getTime());
        EditText txt = (EditText) rootView.findViewById(R.id.navCmd_InpCmd);
        String PubString = txt.getText().toString();
        if (PubString.isEmpty()){
            PubString = "nodata";
        }
        PubString += ":"+strDate;
        microgear.publish("/topic_node",PubString);

        Debug.log(GearHelper.LOG_GEAR+"/topic_node"+"==>"+PubString);
    }


    public View.OnClickListener OnClickHandle = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           switch(v.getId()) {
               case R.id.navCmd_btnConn:
                   TestGearConnect();
                   break;
               case R.id.navCmd_btnPub:
                   TestGearPublish();
                   break;
           }

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        //Log.i("CmdFrag","onStart");
        Debug.log(Debug.LOG_EVENT +"onStart");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("GearCast");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mUpdateReceiver,intentFilter);
    }


    @Override
    public void onResume() {
        Debug.log(Debug.LOG_EVENT +"onResume");
        super.onResume();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Debug.log("[Load Preference][App id]"+sharedPrefs.getString("txtpref_appid","no data"));
        Debug.log("[Load Preference][key]"+sharedPrefs.getString("txtpref_key","no data"));
        Debug.log("[Load Preference][secret]"+sharedPrefs.getString("txtpref_secret","no data"));
        //Log.i("CmdFrag","onResume");
        TestGearConnect();

    }

    @Override
    public void onDestroy() {
        Debug.log(Debug.LOG_EVENT+"onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Debug.log(Debug.LOG_EVENT+"onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onPause() {
        Debug.log(Debug.LOG_EVENT+"onPause");
        TestGearDisConnect();
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        //Log.i("CmdFrag","onStop");
        Debug.log(Debug.LOG_EVENT+"onStop");
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mUpdateReceiver);
    }

    @Override
    public void onDetach() {
        Debug.log(Debug.LOG_EVENT+"onDetach");
        super.onDetach();
    }

    private BroadcastReceiver mUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if( intent.getAction().equalsIgnoreCase("GearCast")){
                String str = intent.getStringExtra("msg");
                if(str != null && !str.isEmpty()) //NULL String Check
                {
                    //Log.d("GearCast",str);
                    Debug.log(Debug.LOG_CASTMSG+"<==%s",str);
                    TextView cmdLog = (TextView)getActivity().findViewById(R.id.navCmd_txtLog);
                    cmdLog.append(str+"\n");
                    final ScrollView scrlLog = (ScrollView)getActivity().findViewById(R.id.navCmd_scrlView);
                    scrlLog.post(new Runnable() {
                        @Override
                        public void run() {
                            scrlLog.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    });
                }
                //Log.d("IOT",intent.getStringExtra("msg"));
            }
        }
    };

}
