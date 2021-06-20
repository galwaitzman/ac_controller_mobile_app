package com.example.ac_controller_app_mqtt;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class login extends AppCompatActivity {
    private Button saveButton;
    private EditText passwordText;
    private EditText usernameText;
    private  EditText cliendIdText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
    }
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.d("extra","activity started");
        saveButton = findViewById(R.id.save_button);
        passwordText = findViewById(R.id.passwordText);
        usernameText = findViewById(R.id.userNameText);
        cliendIdText = findViewById(R.id.clientIdText);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("extra","onclick started");
                Intent loginParamsIntent = new Intent();
                loginParamsIntent.putExtra("clientId", cliendIdText.getText().toString());
                loginParamsIntent.putExtra("username", usernameText.getText().toString());
                loginParamsIntent.putExtra("password", passwordText.getText().toString());
                Log.d("extra","putted extra");
                setResult(0, loginParamsIntent);
                Log.d("extra","before finish");
                finish();
                Log.d("extra","finished");
            }
        });
    }
}