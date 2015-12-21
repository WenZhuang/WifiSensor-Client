
package com.bupt.wifisensor;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.bupt.Utils.HttpUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity {

    private AutoCompleteTextView textView;
    private EditText editText;
    private String username = null;
    private String password = null;
    private CheckBox checkBox1;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        textView = (AutoCompleteTextView) findViewById(R.id.username) ;
        editText = (EditText) findViewById(R.id.password);
        checkBox1 = (CheckBox) findViewById(R.id.checkbox1);

        preferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);


        if (preferences.getBoolean("isRemember",false)){
            textView.setText(preferences.getString("username",null));
            editText.setText(preferences.getString("password",null));
            checkBox1.setChecked(true);
        }

        Button SignInBtn = (Button)findViewById(R.id.sign_in_button);
        SignInBtn.setOnClickListener(new OnClickListener(){
            @Override
            public void onClick(View v) {
               new LoginTask().execute(HttpUtils.LOGIN_URL);
            }
        });

        Button SignUpBtn = (Button)findViewById(R.id.sign_up_button);
        SignUpBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(intent);
            }
        });
    }

    class LoginTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... params) {
            String response = "";

            username = textView.getText().toString();
            password = editText.getText().toString();
            Map<String,String> map = new HashMap<>();
            map.put( "username" , username);
            map.put( "password" , password);

            response = HttpUtils.doPost(params[0],map);
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);

            if(response.trim().equals("OK")){
                editor = preferences.edit();
                if (checkBox1.isChecked()){
                    editor.putString("username",username);
                    editor.putString("password",password);
                    editor.putBoolean("isRemember",true);
                }else {
                    editor.putBoolean("isRemember",false);
                }
                editor.apply();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }else {
                Toast.makeText(LoginActivity.this , "用户名或密码错误"+response,Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this , "用户名或密码错误"+response,Toast.LENGTH_SHORT).show();
            }
        }
    }

}



