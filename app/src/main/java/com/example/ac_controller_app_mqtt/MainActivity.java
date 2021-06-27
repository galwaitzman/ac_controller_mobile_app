package com.example.ac_controller_app_mqtt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;

import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

//import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.example.ac_controller_app_mqtt.Services.setCurrentFanSpeedImages;

public class MainActivity extends AppCompatActivity {
    static MqttAsyncClient mqttClient;
    static MediaPlayer soundPlayer;
    private SharedPreferences sp;
    static Integer current_temp = 0;
    static AtomicBoolean userAwaitsReply;
    private String clientId;
    private String username;
    private String password;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch power_switch;
    private TextView desired_temp_text;
    private Button temp_up;
    private Button temp_down;
    private ImageButton fan1;
    private ImageButton fan2;
    private ImageButton fan3;
    private ImageButton fan4;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        userAwaitsReply = new AtomicBoolean(false);
          AudioAttributes aa  = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .build();
        soundPlayer = MediaPlayer.create(getApplicationContext(), R.raw.beep2, aa,1);
        soundPlayer.setVolume(0.01f,0.01f);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        sp = getApplicationContext().getSharedPreferences("mqttUserPassword",MODE_PRIVATE);
        if (sp.getBoolean("didUserSignIn",false)) { //if username, password and clientId are already saved
            clientId = sp.getString("clientId", null);
            username = sp.getString("username", null);
            password = sp.getString("password", null);
            startMqttConnection(clientId,username,password);
            Log.d("signin","no");
        }
        else {
            startActivityForResult(new Intent(MainActivity.this, login.class),1);

            Log.d("signin","startingactivity");
        }

        temp_down = findViewById(R.id.temp_down);
        temp_up = findViewById(R.id.temp_up);
        power_switch = findViewById(R.id.switch1);
        fan1 = findViewById(R.id.fan_level_1);
        fan2 = findViewById(R.id.fan_level_2);
        fan3 = findViewById(R.id.fan_level_3);
        fan4 = findViewById(R.id.fan_level_4);
        temp_up.setEnabled(false);
        temp_down.setEnabled(false);
        power_switch.setEnabled(false);
        fan1.setEnabled(false);
        fan2.setEnabled(false);
        fan3.setEnabled(false);
        fan4.setEnabled(false);
        desired_temp_text = findViewById(R.id.editTextNumber);


    }

    private void startMqttConnection(String clientId, String username, String password) {
        try {
            mqttClient = new MqttAsyncClient("tcp://ec2-3-142-144-189.us-east-2.compute.amazonaws.com:1883",clientId, new MemoryPersistence());

        } catch (MqttException e) {
            e.printStackTrace();
        }
        MqttConnectOptions options = new MqttConnectOptions();
        options.setUserName(username);
        options.setCleanSession(true);
        options.setPassword(password.toCharArray());
        Log.d("credentials", "pass: " + String.valueOf(options.getPassword()) + " user: " + options.getUserName());
        options.setAutomaticReconnect(true);
        mqttClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean reconnect, String serverURI) {
                try {
                    // e4:5f:01:0e:0d:df is mac of raspberry pi
                    mqttClient.subscribe("status/controller_e4:5f:01:0e:0d:df/#", 1);
                    //ask for last status
                    // e4:5f:01:0e:0d:df
                    mqttClient.publish("commands/controller_e4:5f:01:0e:0d:df",new MqttMessage("STATUS_REQUEST".getBytes()));
                    Log.d("publishing","pub");
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void connectionLost(Throwable cause) {
                //handled by auto reconnect
            }

            @SuppressLint("ShowToast")
            @Override
            public void messageArrived(String topic, MqttMessage message) throws Exception {
                boolean playSound = true;
                String msgContent = message.toString();
                if (topic.contains("STATUS_RESPONSE")) {
                    playSound = false;
                    if (topic.contains("temp")) {
                        current_temp = Integer.parseInt(msgContent);
                        runOnUiThread(() ->desired_temp_text.setText(msgContent));
                    } else {
                        if (topic.contains("onoff")) {
                            if (msgContent.equals("ON")) {
                                runOnUiThread(() -> power_switch.setText("ON"));
                                runOnUiThread(() -> power_switch.setChecked(true));
                                runOnUiThread(() ->desired_temp_text.setTextColor(Color.GREEN));
                            } else {
                                runOnUiThread(() -> power_switch.setText("OFF"));
                                runOnUiThread(() -> power_switch.setChecked(false));
                            }

                        }
                        else {
                            if (topic.contains("fan")) {
                                runOnUiThread(() -> setCurrentFanSpeedImages(MainActivity.this,msgContent));
                            }
                        }
                    }
                }
                else {
                    if (topic.contains("temp")) {
                        if (userAwaitsReply.get()) {
                            userAwaitsReply.set(false);
                            if (!message.toString().equals("-1")) {
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "temperature changed to " + msgContent, Toast.LENGTH_SHORT).show());
                            } else {
                                runOnUiThread(() -> Toast.makeText(getApplicationContext(), "temperature change FAILED, please try again", Toast.LENGTH_SHORT).show());
                            }
                        }
                        current_temp = Integer.parseInt(msgContent);
                        runOnUiThread(() -> {
                            desired_temp_text.setTextColor(Color.GREEN);
                            desired_temp_text.setText(msgContent);
                        });

                    }
                    else {
                        if (topic.contains("onoff")) {
                            Log.d("subs","gotmessage");
                            if (userAwaitsReply.get()) {
                                userAwaitsReply.set(false);
                                if (!message.toString().equals("-1")) {
                                    Log.d("make_msg","making");
                                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "AC is now " + msgContent, Toast.LENGTH_LONG).show());
                                } else {
                                    runOnUiThread(() -> Toast.makeText(getApplicationContext(), "ON/OFF command FAILED", Toast.LENGTH_LONG).show());
                                }
                            }
                            runOnUiThread(() ->power_switch.setText(msgContent));
                            if (msgContent.equals("OFF")) {
                                runOnUiThread(() -> power_switch.setChecked(false));
                                runOnUiThread(() -> desired_temp_text.setTextColor(Color.GRAY));
                            }
                            else { //ON
                                runOnUiThread(() -> power_switch.setChecked(true));
                            }
                        }
                        else {
                            if (topic.contains("fan")) {
                                runOnUiThread(() -> setCurrentFanSpeedImages(MainActivity.this, msgContent));
                                if (userAwaitsReply.get()) {
                                    userAwaitsReply.set(false);
                                    runOnUiThread(() -> {
                                        switch (msgContent) {
                                            case "FAN_SPEED_1":
                                                Toast.makeText(getApplicationContext(), "fan speed is now Low", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "FAN_SPEED_2":
                                                Toast.makeText(getApplicationContext(), "fan speed is now Medium", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "FAN_SPEED_3":
                                                Toast.makeText(getApplicationContext(), "fan speed is now High", Toast.LENGTH_SHORT).show();
                                                break;
                                            case "FAN_SPEED_4":
                                                Toast.makeText(getApplicationContext(), "fan speed is now Turbo", Toast.LENGTH_SHORT).show();
                                                break;

                                        }
                                    });
                                }
                            }
                        }
                    }
                }

                boolean finalPlaySound = playSound;
                runOnUiThread(() -> {
                    if (current_temp != 30 && power_switch.isChecked()) {
                        temp_up.setEnabled(true);
                    } else {
                        temp_up.setEnabled(false);
                    }
                    if (current_temp != 16 && power_switch.isChecked()) {
                        temp_down.setEnabled(true);
                    } else {
                        temp_down.setEnabled(false);
                    }
                    power_switch.setEnabled(true);
                    if (power_switch.isChecked()) {
                        fan1.setEnabled(true);
                        fan2.setEnabled(true);
                        fan3.setEnabled(true);
                        fan4.setEnabled(true);
                    }
                    else {
                        fan1.setEnabled(false);
                        fan2.setEnabled(false);
                        fan3.setEnabled(false);
                        fan4.setEnabled(false);
                        setCurrentFanSpeedImages(MainActivity.this,"FAN_SPEED_NONE");
                    }

                    if (finalPlaySound) {
                        soundPlayer.start();
                    }
                });
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken token) {

            }

        });
        try {
            Log.d("before_connect","before");
            mqttClient.connect(options).waitForCompletion(5000);
            Log.d("connected_to",mqttClient.getServerURI());
        } catch (MqttException e) {
            Toast.makeText(getApplicationContext(),"server unavailable", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1)
        {

            clientId = data.getStringExtra("clientId");
            username = data.getStringExtra("username");
            password = data.getStringExtra("password");
            sp.edit().putBoolean("didUserSignIn",true).apply();
            sp.edit().putString("clientId",clientId).apply();
            sp.edit().putString("username",username).apply();
            sp.edit().putString("password",password).apply();
            startMqttConnection(clientId,username,password);
        }
    }
}