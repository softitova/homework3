package com.example.macbook.loader;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public final class SimpleService extends Service {
    private static final String TAG = SimpleService.class.getSimpleName();

    Random random = new Random();
    private final String[] myUrl = new String[]{
            "https://upload.wikimedia.org/wikipedia/ru/archive/d/dd/20090517010837!The_Persistence_of_Memory.jpg",
            "http://www.jr1.ru/i/orig/476-2560x1600.jpg"
    };

    boolean startLoading = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, " start");

        if (startLoading) {
            return START_STICKY;
        }
        startLoading = true;
        final File f = new File(getFilesDir(), MainActivity.FILE_NAME);

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                HttpURLConnection con = null;
                InputStream in = null;
                FileOutputStream out = null;

                try {
                    con = (HttpURLConnection) new URL(
                            myUrl[random.nextInt(myUrl.length)]
                    ).openConnection();

                    con.setConnectTimeout(15 * 1000);
                    con.setReadTimeout(60 * 1000);

                    in = new BufferedInputStream(con.getInputStream());
                    out = new FileOutputStream(f);

                    int curBite;
                    byte[] buff = new byte[1024];

                    while ((curBite = in.read(buff)) != -1) {
                        out.write(buff, 0, curBite);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    f.delete();
                } finally {
                    try {
                        if (con != null)
                            con.disconnect();
                        if (in != null)
                            in.close();
                        if (out != null)
                            out.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                Log.d(TAG, " finish");
                sendBroadcast(new Intent(MainActivity.BROADCAST));
                startLoading = false;
                return null;
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}