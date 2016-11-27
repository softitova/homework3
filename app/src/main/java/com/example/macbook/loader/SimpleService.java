package com.example.macbook.loader;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public final class SimpleService extends Service implements Runnable {

    private File f;
    private final String myUrl = "http://design-kmv.ru/wp-content/uploads/2014/05/postoynstvopamyti.jpghttp://design-kmv.ru/wp-content/uploads/2014/05/postoynstvopamyti.jpg";
    private static final String TAG = SimpleService.class.getSimpleName();




    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        f = new File(getFilesDir(), MainActivity.FILE_NAME);
        Log.d(TAG," start");
        if (!f.exists()) {
            new Thread(this).start();
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void run() {

        InputStream in = null;
        FileOutputStream out = null;

        try {
            in = new BufferedInputStream(new URL(myUrl).openStream());
            out = new FileOutputStream(f);
            int curBite;
            byte[] buff = new byte[1024];
            while ((curBite = in.read(buff)) != -1) {
                out.write(buff, 0, curBite);
            }
            sendBroadcast(new Intent(MainActivity.BROADCAST));
        } catch (IOException e) {
            e.printStackTrace();
            f.delete();
        } finally {
            try {
                if (in != null)
                    in.close();
                if (out != null)
                    out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG," finish");

    }
}