package com.example.ac_controller_app_mqtt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class RemoteFragment extends Fragment {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch power;
    private TextView desired_temp_text;
    private Button temp_down;
    private Button temp_up;
    private ImageButton fan1;
    private ImageButton fan2;
    private ImageButton fan3;
    private ImageButton fan4;
    private TimeFromClickThread timeFromClickThd;
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_remote, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        power = view.findViewById(R.id.switch1);
        temp_down = view.findViewById(R.id.temp_down);
        temp_up = view.findViewById(R.id.temp_up);
        fan1 = view.findViewById(R.id.fan_level_1);
        fan2 = view.findViewById(R.id.fan_level_2);
        fan3 = view.findViewById(R.id.fan_level_3);
        fan4 = view.findViewById(R.id.fan_level_4);
        desired_temp_text = view.findViewById(R.id.editTextNumber);

       power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               temp_down.setEnabled(false);
               temp_up.setEnabled(false);
               fan1.setEnabled(false);
               fan2.setEnabled(false);
               fan3.setEnabled(false);
               fan4.setEnabled(false);
               if (buttonView.isPressed()) { //if user triggered
                   if (isChecked) {
                       countTimeFromClickThenAction(getActivity(), Actions.TURN_ON, null);
                   } else {
                       countTimeFromClickThenAction(getActivity(), Actions.TURN_OFF, null);
                   }
               }
           }

        });
       temp_up.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               int desired_temp_text_int = Integer.parseInt(desired_temp_text.getText().toString());
               if (desired_temp_text_int == 29) {
                   temp_up.setEnabled(false);
               }
               power.setEnabled(false);
               fan1.setEnabled(false);
               fan2.setEnabled(false);
               fan3.setEnabled(false);
               fan4.setEnabled(false);
               temp_down.setEnabled(true);
               desired_temp_text.setText((String.valueOf(desired_temp_text_int+1)));
               desired_temp_text.setTextColor(Color.GRAY);
               Integer new_temp = desired_temp_text_int +1;
               countTimeFromClickThenAction(getActivity(),Actions.TEMP_UPDATE,new_temp);
           }
       });
        temp_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                power.setEnabled(false);
                fan1.setEnabled(false);
                fan2.setEnabled(false);
                fan3.setEnabled(false);
                fan4.setEnabled(false);
                temp_up.setEnabled(true);
                int desired_temp_text_int = Integer.parseInt(desired_temp_text.getText().toString());
                if (desired_temp_text_int == 17) {
                    temp_down.setEnabled(false);
                }
                desired_temp_text.setText(String.valueOf(desired_temp_text_int-1));
                desired_temp_text.setTextColor(Color.GRAY);
                Integer new_temp = desired_temp_text_int - 1;
                countTimeFromClickThenAction(getActivity(),Actions.TEMP_UPDATE, new_temp);
            }
        });
        fan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                power.setEnabled(false);
                temp_up.setEnabled(false);
                temp_down.setEnabled(false);
                //fan1.setEnabled(false);
                //fan2.setEnabled(false);
                //fan3.setEnabled(false);
                //fan4.setEnabled(false);
                fan1.setImageResource(R.drawable.fan_level_1_selected);
                fan2.setImageResource(R.drawable.fan_level_2_disabled);
                fan3.setImageResource(R.drawable.fan_level_3_disabled);
                fan4.setImageResource(R.drawable.fan_level_4_disabled);
                countTimeFromClickThenAction(getActivity(),Actions.FAN_SPEED_1, null);
            }
        });
        fan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                power.setEnabled(false);
                temp_up.setEnabled(false);
                temp_down.setEnabled(false);
                //fan1.setEnabled(false);
                //fan2.setEnabled(false);
                //fan3.setEnabled(false);
                //fan4.setEnabled(false);
                fan1.setImageResource(R.drawable.fan_level_1_selected);
                fan2.setImageResource(R.drawable.fan_level_2_selected);
                fan3.setImageResource(R.drawable.fan_level_3_disabled);
                fan4.setImageResource(R.drawable.fan_level_4_disabled);
                countTimeFromClickThenAction(getActivity(),Actions.FAN_SPEED_2, null);
            }
        });
        fan3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                power.setEnabled(false);
                temp_up.setEnabled(false);
                temp_down.setEnabled(false);
                //fan1.setEnabled(false);
                //fan2.setEnabled(false);
                //fan3.setEnabled(false);
                //fan4.setEnabled(false);
                fan1.setImageResource(R.drawable.fan_level_1_selected);
                fan2.setImageResource(R.drawable.fan_level_2_selected);
                fan3.setImageResource(R.drawable.fan_level_3_selected);
                fan4.setImageResource(R.drawable.fan_level_4_disabled);
                countTimeFromClickThenAction(getActivity(),Actions.FAN_SPEED_3, null);
            }
        });
        fan4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                power.setEnabled(false);
                temp_up.setEnabled(false);
                temp_down.setEnabled(false);
                //fan1.setEnabled(false);
                //fan2.setEnabled(false);
                //fan3.setEnabled(false);
                //fan4.setEnabled(false);
                fan1.setImageResource(R.drawable.fan_level_1_selected);
                fan2.setImageResource(R.drawable.fan_level_2_selected);
                fan3.setImageResource(R.drawable.fan_level_3_selected);
                fan4.setImageResource(R.drawable.fan_level_4_selected);
                countTimeFromClickThenAction(getActivity(),Actions.FAN_SPEED_4, null);
            }
        });
        /*{
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(RemoteFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });*/
    }
    public void countTimeFromClickThenAction(Activity activity, Actions action, Integer tempParam) {
        Button temp_up = activity.findViewById(R.id.temp_up);
        Button temp_down = activity.findViewById(R.id.temp_down);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch power = activity.findViewById(R.id.switch1);

        if (timeFromClickThd == null) {
            timeFromClickThd = new TimeFromClickThread(activity,action, tempParam);
            timeFromClickThd.start();
        }
        else {
            if (!timeFromClickThd.isInterrupted()) {
                timeFromClickThd.interrupt();
            }
            timeFromClickThd = new TimeFromClickThread(activity, action, tempParam);
            timeFromClickThd.start();
        }
    }
}