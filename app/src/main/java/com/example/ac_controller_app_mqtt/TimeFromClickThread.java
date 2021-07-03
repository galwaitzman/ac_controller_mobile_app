package com.example.ac_controller_app_mqtt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;


import java.util.concurrent.TimeUnit;

public class TimeFromClickThread extends Thread {
    private final Button temp_up;
    private final Button temp_down;
    private final Activity activity;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private final Switch power;
    private final ImageButton fan1;
    private final ImageButton fan2;
    private final ImageButton fan3;
    private final ImageButton fan4;
    private final ProgressBar waitForStatusBar;
    private final Actions action;
    private final Integer tempParam;

    public TimeFromClickThread(Activity activity, Actions action, Integer tempParam) {
        this.activity = activity;
        this.temp_up = activity.findViewById(R.id.temp_up);
        this.temp_down = activity.findViewById(R.id.temp_down);
        this.power = activity.findViewById(R.id.switch1);
        this.fan1 = activity.findViewById(R.id.fan_level_1);
        this.fan2 = activity.findViewById(R.id.fan_level_2);
        this.fan3 = activity.findViewById(R.id.fan_level_3);
        this.fan4 = activity.findViewById(R.id.fan_level_4);
        this.waitForStatusBar = activity.findViewById(R.id.wait_for_status_bar);
        this.action = action;
        this.tempParam = tempParam;
    }
        @Override
        public void run() {

                    try {
                        TimeUnit.MILLISECONDS.sleep(1000);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.runOnUiThread(() -> {
                                    waitForStatusBar.setVisibility(View.VISIBLE);
                                temp_up.setEnabled(false);
                                temp_down.setEnabled(false);
                                power.setEnabled(false);
                                fan1.setEnabled(false);
                                fan2.setEnabled(false);
                                fan3.setEnabled(false);
                                fan4.setEnabled(false);
                                });
                                RequestActionThread reqThd = new RequestActionThread(activity, temp_up,temp_down,power, action, tempParam);
                                reqThd.start();
                            }
                        });
                    } catch (InterruptedException e) { //stops counting
                        e.printStackTrace();
                    }
                }
        }
