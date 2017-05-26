package com.sac.reach.maintencehero;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class login extends AppCompatActivity implements View.OnClickListener {
    Button login;
    EditText name,passw;
    String dude=null,passy=null,freak=null,desig=null;
    PrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button)findViewById(R.id.btn_log);
        login.setOnClickListener(this);
        name  = (EditText)findViewById(R.id.et_name);
        passw = (EditText)findViewById(R.id.et_pass);
         pref = new PrefManager(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        dude = name.getText().toString();
        passy = passw.getText().toString();
        BackGround b = new BackGround();
        b.execute(dude,passy);


    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String nam = params[0];
            String password = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://192.168.43.133/mh/login.php");
                String urlParams = "name="+nam+"&password="+password;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while((tmp=is.read())!=-1){
                    data+= (char)tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: "+e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err=null;
            try {
                JSONObject root = new JSONObject(s);
                JSONObject user_data = root.getJSONObject("user_data");
                freak = user_data.getString("name");
                desig = user_data.getString("desig");
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }

            if (freak!=null && !freak.isEmpty()){
                pref.setname(freak);
                pref.setkey(desig);


                Intent i = new Intent(login.this,main.class);


                startActivity(i);
            }
            else {
                Toast.makeText(login.this, "Wrong Credentials", Toast.LENGTH_LONG).show();

            }
        }
    }
}
