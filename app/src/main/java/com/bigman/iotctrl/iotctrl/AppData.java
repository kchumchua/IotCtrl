package com.bigman.iotctrl.iotctrl;

import android.app.Application;
import android.content.Context;

import io.netpie.microgear.Microgear;
import io.netpie.microgear.MicrogearEventListener;

/**
 * Created by BIGMAN on 25/2/2560.
 */

public class AppData extends Application {
    private Microgear gearInst;

    public void setGearInst(Microgear inst)
    {
        this.gearInst = inst;
    }
    public Microgear getGearInst()
    {
      return this.gearInst;
    }

}
