package com.bupt.wifisensor;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bupt.Utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends ActionBarActivity {

    private String username = null;
    private String password = null;
    private String password_again = null;
    private AutoCompleteTextView textView;
    private EditText editText1;
    private EditText editText2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textView = (AutoCompleteTextView) findViewById(R.id.username) ;
        editText1 = (EditText) findViewById(R.id.password);
        editText2 = (EditText) findViewById(R.id.password_agin);


        Button ensureBtn = (Button) findViewById(R.id.ensure);

        ensureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                username = textView.getText().toString();
                password = editText1.getText().toString();
                password_again = editText2.getText().toString();


                if (verifyUser(username,password,password_again)){
                    new SignUpTask().execute(HttpUtils.SIGNUP_URL);

                }
            }
        });

    }

    boolean verifyUser(String username,String password,String password_again){
        boolean flag = false;
        if ( (username == null) ||( username.equals(""))){
            Toast.makeText(this , "请输入用户名1" , Toast.LENGTH_SHORT).show();
        }else if ( (password == null) ||( password.equals(""))){
            Toast.makeText(this , "请输入密码" , Toast.LENGTH_SHORT).show();
        }else if ( !(password.equals(password_again))){
            Toast.makeText(this , "两次输入密码不一致" , Toast.LENGTH_SHORT).show();
        }else{
            flag = true;
        }

        return flag;
    }

    class SignUpTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... params) {

            String response = "";

            Map<String,String> map = new HashMap<>();
            map.put("username",username);
            map.put("password",password);
            response = HttpUtils.doPost(params[0],map);

            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if(response.trim().equals("OK")){
                Toast.makeText(SignupActivity.this,"注册成功",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
            }else if(response.trim().equals("DENY")){
                Toast.makeText(SignupActivity.this,"用户名已存在",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(SignupActivity.this,"注册失败"+response,Toast.LENGTH_SHORT).show();
            }
         }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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
