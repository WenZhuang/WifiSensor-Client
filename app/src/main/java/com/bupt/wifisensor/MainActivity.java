package com.bupt.wifisensor;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends ActionBarActivity {

    private static TextView textView;
    private MyHandler handler;
    private static NotificationCompat.Builder mBuilder;
    private static NotificationManager manager;
    private static boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new MyHandler();
        textView = (TextView) findViewById(R.id.textView);

        mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.abc_ab_share_pack_mtrl_alpha)
                .setContentTitle("欢迎来到640！")
                .setContentText("请点击查看今日任务")
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);

        Intent intent = new Intent(MainActivity.this, TaskActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MainActivity.class);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        mBuilder.setContentIntent(resultPendingIntent);

        manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        new Thread(new MyThread()).start();
    }

    public static class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            textView.setText("level:" + msg.arg1);
            super.handleMessage(msg);

            if ((boolean)msg.obj && !flag) {
                manager.notify(1000, mBuilder.build());
                flag = true;
            }
        }
    }

    public class MyThread implements Runnable {
        int count = 1;


        @Override
        public void run() {

            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = Message.obtain();
                message.obj = isWifiExist("D-Link_640_5G");
                message.arg1 = getWiFiLevel("D-Link_640_5G");

                count++;
                handler.sendMessage(message);
            }
        }
    }

    boolean isWifiExist(String name){
        boolean isWifi = false;

        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> list = wifiManager.getScanResults();

        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).SSID).equals(name)) {
                isWifi = true;
                break;
            }
        }

        return isWifi;
    }

    int getWiFiLevel(String name) {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> list = wifiManager.getScanResults();

        int level = -99;
        for (int i = 0; i < list.size(); i++) {
            if ((list.get(i).SSID).equals(name)) {
                level = list.get(i).level;
            }
        }
        return level;
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
}
