package com.example.ac_controller_app_mqtt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
/*import org.eclipse.paho.android.service.*;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;*/

import static com.example.ac_controller_app_mqtt.MainActivity.mqttClient;
import static com.example.ac_controller_app_mqtt.MainActivity.userAwaitsReply;

public class RequestActionThread extends Thread {
    private final Integer tempParam;
    private Actions action;
    private final Activity activity;
    private final Button temp_up;
    private final Button temp_down;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private final Switch power;

    public RequestActionThread(Activity activity, Button temp_up, Button temp_down, @SuppressLint("UseSwitchCompatOrMaterialCode") Switch power, Actions action, Integer tempParam) {
        this.activity = activity;
        this.temp_up = temp_up;
        this.temp_down = temp_down;
        this.power = power;
        this.action = action;
        this.tempParam = tempParam;

    }
    public void run() {
        String subTopic = "";
        String commandMsgContent = "";
        switch (action) {
            case TEMP_UPDATE:
                subTopic = "temp";
                commandMsgContent = String.valueOf(tempParam);
                break;
            case TURN_OFF:
                subTopic = "onoff";
                commandMsgContent = "TURN_OFF";
                break;
            case TURN_ON:
                subTopic = "onoff";
                commandMsgContent = "TURN_ON";
                break;
            case FAN_SPEED_1:
                subTopic = "fan";
                commandMsgContent = "FAN_SPEED_1";
                break;
            case FAN_SPEED_2:
                subTopic = "fan";
                commandMsgContent = "FAN_SPEED_2";
                break;
            case FAN_SPEED_3:
                subTopic = "fan";
                commandMsgContent = "FAN_SPEED_3";
                break;
            case FAN_SPEED_4:
                subTopic = "fan";
                commandMsgContent = "FAN_SPEED_4";
                break;
        }

        try {
            //TODO: e4:5f:01:0e:0d:df
            mqttClient.publish("commands/controller_e4:5f:01:0e:0d:df/"+subTopic, new MqttMessage(commandMsgContent.getBytes()));
            userAwaitsReply.set(true);
            ResponseTimeOutThread timeOutThread = new ResponseTimeOutThread(activity, 2000, userAwaitsReply);
            timeOutThread.start();
            Log.d("published","sdfsdf");
        } catch (MqttException | InterruptedException e) {
            e.printStackTrace();
        }

    }

}
