package com.bupt.wifisensor;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import com.bupt.Utils.*;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class TaskActivity extends ActionBarActivity {

    private static final String serverUrl = HttpUtils.Wifi_URL;
    private static final String picUrl = HttpUtils.PIC_URL;
    private TextView taskText;
    private ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        taskText = (TextView)findViewById(R.id.taskText);
        imageView = (ImageView)findViewById(R.id.image);
        new ARTask().execute(serverUrl);
        new ImageTask().execute(picUrl);
    }


    class ARTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            String result = "";
            result = HttpUtils.doGet(params[0]);

            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String inTime = Data.getTime(jsonObject.getString("inTime"));
                String outTime = Data.getTime(jsonObject.getString("outTime"));
                String dwellTime = jsonObject.getString("dwellTime");
                taskText.setText("进门时间：" + inTime + "\n" +
                                 "离开时间：" + outTime + "\n" +
                                 "停留时间：" + dwellTime +"s\n");
            }catch (JSONException e){
                e.printStackTrace();
            }

        }
    }

    class ImageTask extends AsyncTask<String,Void,Bitmap>{
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpRequest = new HttpGet(params[0]);
            try {
                HttpResponse httpResponse = httpClient.execute(httpRequest);
                if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                    bitmap = BitmapFactory.decodeStream(httpResponse.getEntity().getContent());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task, menu);
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
