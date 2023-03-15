package com.lyneon.splash;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;
import android.content.Intent;
import android.provider.Settings;
import android.net.Uri;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.WindowManager;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.ViewGroup;
import com.yhz.study.colorpicker.ColorPickView;
import android.widget.Toast;
import com.lyneon.customview.MarqueeTextView;
import android.widget.Switch;
import android.widget.CompoundButton;

public final class MainActivity extends AppCompatActivity {
    
    //final public static int GET_FONT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		final Toolbar main_toolbar=(Toolbar)findViewById(R.id.main_toolbar);
		setSupportActionBar(main_toolbar);


        final EditText main_edittext_message = findViewById(R.id.main_edittext_message);
        //final EditText main_edittext_color = findViewById(R.id.main_edittext_color);
        ColorPickView main_colorpickview_textcolor = findViewById(R.id.main_colorpickview_textcolor);
        final EditText main_edittext_size = findViewById(R.id.main_edittext_size);
        //final EditText main_edittext_speed = findViewById(R.id.main_edittext_speed);
        final TextView main_textview_message = findViewById(R.id.main_textview_message);
        //final Switch main_switch_marquee = findViewById(R.id.main_switch_marquee);
        final Button main_button_message = findViewById(R.id.main_button_message);
        final Button main_button_permission = findViewById(R.id.main_button_permission);
        final Button main_button_color = findViewById(R.id.main_button_color);
        final Button main_button_size = findViewById(R.id.main_button_size);
        //final Button main_button_speed = findViewById(R.id.main_button_speed);
        //final Button main_button_font = findViewById(R.id.main_button_font);
        final Button main_button_start = findViewById(R.id.main_button_start);
        final Button main_button_stop = findViewById(R.id.main_button_stop);

        final SharedPreferences shared_preferences = getSharedPreferences("message", Context.MODE_PRIVATE);
        main_textview_message.setText(shared_preferences.getString("message", "no message"));
        main_textview_message.setTextColor(shared_preferences.getInt("text_color", Color.parseColor("#000000")));
        
        main_textview_message.setTextSize(shared_preferences.getFloat("text_size",20));
        //main_textview_message.setSpeed(shared_preferences.getInt("text_speed",0));
        main_edittext_message.setText(shared_preferences.getString("message", "no message"));
        //main_edittext_color.setTextColor((shared_preferences.getInt("color", Color.parseColor("#000000"))));
        main_edittext_size.setText(String.valueOf(shared_preferences.getFloat("text_size",20)));
        //main_edittext_speed.setText(String.valueOf(shared_preferences.getInt("text_speed",0)));

        main_button_message.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View p1) {
                    final String message = main_edittext_message.getText().toString();
                    final SharedPreferences.Editor editor = shared_preferences.edit();
                    editor.putString("message", message);
                    editor.apply();
                    main_textview_message.setText(shared_preferences.getString("message", "no message"));
                    Toast.makeText(getApplication(), "保存成功", Toast.LENGTH_SHORT).show();
                }
            });
        main_button_color.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                    final String input = main_edittext_color.getText().toString();
                    if (input.length() ==  6) {
                        SharedPreferences.Editor editor = shared_preferences.edit();
                        editor.putString("text_color", input);
                        editor.commit();
                        main_textview_message.setTextColor(Color.parseColor("#" + shared_preferences.getString("text_color", "#000000")));
                        main_edittext_color.setTextColor(Color.parseColor("#" + shared_preferences.getString("text_color", "#000000")));
                    }else{
                        main_edittext_color.setError("格式错误");
                    }
                    */
                    final int input_color = main_textview_message.getCurrentTextColor();
                    final SharedPreferences.Editor editor = shared_preferences.edit();
                    editor.putInt("text_color", input_color);
                    editor.apply();
                    Toast.makeText(getApplication(), "保存成功", Toast.LENGTH_SHORT).show();
                }
            });
        main_button_size.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final float text_size = Float.parseFloat(main_edittext_size.getText().toString());
                    final SharedPreferences.Editor editor = shared_preferences.edit();
                    editor.putFloat("text_size",text_size);
                    editor.apply();
                    main_textview_message.setTextSize(shared_preferences.getFloat("text_size",10));
                    Toast.makeText(getApplication(), "保存成功", Toast.LENGTH_SHORT).show();
                }
            });
        main_button_permission.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())));
                }
            });
        main_button_start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                    final Intent i = new Intent(MainActivity.this,ShowService.class);
                    startService(i);
                    }catch(Exception e){
                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                            .setMessage(e.getMessage())
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dia, int which) {
                                    dia.dismiss();
                                }
                            })
                            .create();
                        dialog.show();
                    }
                }
        });
        main_button_stop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Intent i = new Intent(MainActivity.this,ShowService.class);
                    stopService(i);
                }
            });
        main_colorpickview_textcolor.setBarListener(new ColorPickView.OnColorBarListener(){
                @Override
                public void moveBar(int color) {
                    main_textview_message.setTextColor(color);
                }
        });
        /*main_button_font.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    
                }
            });
        */
        /*
        main_switch_marquee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        main_textview_message.setSpeed(shared_preferences.getInt("text_speed",0));
                    }else{
                        main_textview_message.setSpeed(0);
                    }
                }
        });
        */
        /*
        main_button_speed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int input_speed = Integer.valueOf(main_edittext_speed.getText().toString());
                    final SharedPreferences.Editor editor = shared_preferences.edit();
                    editor.putInt("text_speed",input_speed);
                    editor.apply();
                    main_textview_message.setSpeed(input_speed);
                }
            });
            */
    }

    
    
}
