package com.example.macbook.loader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String FILE_NAME = "pic.jpg";
    public static final String BROADCAST = "myBroadcast";


    private ImageView image;
    private TextView error;
    private BroadcastReceiver imRes, actRes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFields();
        showImage();

        actRes = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                context.startService(new Intent(context, SimpleService.class));
            }
        };
        imRes = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                showImage();
            }
        };

        registerReceiver(actRes, new IntentFilter(Intent.ACTION_SCREEN_ON));
        registerReceiver(imRes, new IntentFilter(BROADCAST));
    }

    private void showImage() {
        File f = new File(getFilesDir(), FILE_NAME);
        boolean isError = true;

        if (f.exists()) {
            try {
                image.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(f)));
                isError = false;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        int imageVis = isError ? View.INVISIBLE : View.VISIBLE;
        int errorVis = isError ? View.VISIBLE : View.INVISIBLE;
        image.setVisibility(imageVis);
        error.setVisibility(errorVis);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(actRes);
        unregisterReceiver(imRes);
    }

    private void setFields() {
        image = (ImageView) findViewById(R.id.picture);
        error = (TextView) findViewById(R.id.error_txt);
    }

}
