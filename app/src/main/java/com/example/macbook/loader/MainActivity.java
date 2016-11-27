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

public class MainActivity extends AppCompatActivity {

    public static final String FILE_NAME = "pic.jpg";
    public static final String BROADCAST = "myBroadcast";

    private static final String TAG = MainActivity.class.getSimpleName();

    private ImageView image;
    private TextView error;
    private BroadcastReceiver imRes, actRes;

    private boolean fileExists = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFields();

        File f = new File(getFilesDir(), FILE_NAME);
        if (fileExists = f.exists()) {
            Log.d(TAG, " EXIST!!!!!!!!!!!!!");
            image.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
        }
        Log.d(TAG, " HERE!!!!!!!!!!!!!");
        makeVisible();

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
        if (f.exists()) {
            image.setImageBitmap(BitmapFactory.decodeFile(f.getAbsolutePath()));
            image.setVisibility(View.VISIBLE);
            error.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(actRes);
        unregisterReceiver(imRes);
    }

    private void makeVisible() {
        int imageVis = fileExists ? View.VISIBLE : View.INVISIBLE;
        int errorVis = fileExists ? View.INVISIBLE : View.VISIBLE;
        image.setVisibility(imageVis);
        error.setVisibility(errorVis);

    }

    private void setFields() {
        image = (ImageView) findViewById(R.id.picture);
        error = (TextView) findViewById(R.id.error_txt);
    }

}
