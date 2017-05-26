package com.sac.reach.maintencehero;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class getdet extends AppCompatActivity implements View.OnClickListener {
    String name="",comp,id,freak,datee,tym;
    TextView na,ide,def,time,date;
    Button done;
    CheckBox res;

    PrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getdet);
        na = (TextView)findViewById(R.id.tv_by);
        ide = (TextView)findViewById(R.id.tv_c_id);
        time = (TextView)findViewById(R.id.tv_time);
        date = (TextView)findViewById(R.id.tv_date);
        res = (CheckBox)findViewById(R.id.cb_res);
        id=getIntent().getStringExtra("id");
        pref = new PrefManager(getApplicationContext());
        ide.setText(id);
        freak = pref.get_name();


        def = (TextView)findViewById(R.id.tv_defect);
        done = (Button)findViewById(R.id.btn_resdone);
        done.setOnClickListener(this);
        BackGround b = new BackGround();
        b.execute(id);
    }

    @Override
    public void onClick(View view) {
        if (res.isChecked())
        {
            Intent i = new Intent(getdet.this,uploader.class);
            i.putExtra("id",id);
            startActivity(i);
            BackGround_2 b = new BackGround_2();
            b.execute(id);
        }
        else
        { Intent i = new Intent(getdet.this,main.class);
        startActivity(i);}


    }


    class BackGround_2 extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String name = params[0];


            String data="";
            int tmp;

            try {
                URL url = new URL("http://192.168.43.133/mh/complain_res.php");
                String urlParams = "id="+id+"&member="+freak;

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
            if(s.equals("")){
                s="Resolved successfully.";

            }
            Toast.makeText(getdet.this, s, Toast.LENGTH_LONG).show();
        }
    }


    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String nam = params[0];
           // String password = params[1];
            String data="";
            int tmp;

            try {
                URL url = new URL("http://192.168.43.133/mh/unres.php");
                String urlParams = "id="+nam;

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
                JSONObject user_data = root.getJSONObject("item_data");
                name = user_data.getString("name");
                comp = user_data.getString("comp");
                tym = user_data.getString("time");
                datee = user_data.getString("date");
            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }

            if (name!=null && !name.isEmpty()){
                na.setText(name);
                def.setText(comp);
                time.setText(tym);
                date.setText(datee);




            }
            else {
                Toast.makeText(getdet.this, "Incorrect ID", Toast.LENGTH_LONG).show();

            }
        }
    }

}

