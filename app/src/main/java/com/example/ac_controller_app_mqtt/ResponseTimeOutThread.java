package com.example.ac_controller_app_mqtt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ResponseTimeOutThread extends Thread {
    private Button temp_up;
    private Button temp_down;
    private Switch power;
    private ImageButton fan1;
    private ImageButton fan2;
    private ImageButton fan3;
    private ImageButton fan4;
    private final ProgressBar waitForStatusBar;

    public ResponseTimeOutThread(Activity activity, int timeout, AtomicBoolean userAwaitsResponse) throws InterruptedException {
        temp_up = activity.findViewById(R.id.temp_up);
        temp_down = activity.findViewById(R.id.temp_down);
        power = activity.findViewById(R.id.switch1);
        fan1 = activity.findViewById(R.id.fan_level_1);
        fan2 = activity.findViewById(R.id.fan_level_2);
        fan3 = activity.findViewById(R.id.fan_level_3);
        fan4 = activity.findViewById(R.id.fan_level_4);
        waitForStatusBar = activity.findViewById(R.id.wait_for_status_bar);
        TimeUnit.MILLISECONDS.sleep(timeout);
        AtomicInteger desired_temp_int = new AtomicInteger();
        activity.runOnUiThread(() -> desired_temp_int.set(Integer.parseInt(((TextView) (activity.findViewById(R.id.editTextNumber))).getText().toString())));

        if (userAwaitsResponse.get()) {
            activity.runOnUiThread(() -> {
                if (desired_temp_int.get() != 16)
                {
                    temp_down.setEnabled(true);
                }
                if (desired_temp_int.get() != 30)
                {
                    temp_up.setEnabled(true);
                }
                power.setEnabled(true);
                fan1.setEnabled(true);
                fan2.setEnabled(true);
                fan3.setEnabled(true);
                fan4.setEnabled(true);
                waitForStatusBar.setVisibility(View.GONE);
                fan1.setImageResource(R.drawable.fan_level_1_disabled);
                fan2.setImageResource(R.drawable.fan_level_2_disabled);
                fan3.setImageResource(R.drawable.fan_level_3_disabled);
                fan4.setImageResource(R.drawable.fan_level_4_disabled);
                Toast.makeText(activity.getApplicationContext(), "no response from controller", Toast.LENGTH_LONG).show();
            });
            }
    }
}
