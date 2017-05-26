package com.sac.reach.maintencehero;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class update extends AppCompatActivity implements View.OnClickListener {

    String id;
    String one = "1";
    String model,loca,price,buy_date,desc,vendor,status,freak;
    TextView i,mod,pri,buy,ven,stat;
    EditText loc,des;
    Button update;
    PrefManager pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        pref = new PrefManager(getApplicationContext());
        id=getIntent().getStringExtra("name");
        model = getIntent().getStringExtra("model");
        loca = getIntent().getStringExtra("loca");
        price = getIntent().getStringExtra("price");
        buy_date = getIntent().getStringExtra("buy_date");
        desc = getIntent().getStringExtra("desc");
        vendor = getIntent().getStringExtra("vendor");
        status = getIntent().getStringExtra("status");
        i = (TextView)findViewById(R.id.tv_id);
        mod = (TextView)findViewById(R.id.tv_model);
        loc= (EditText) findViewById(R.id.tv_loca);
        pri= (TextView)findViewById(R.id.tv_price);
        buy= (TextView)findViewById(R.id.tv_buy);
        des= (EditText) findViewById(R.id.tv_desc);
        ven= (TextView)findViewById(R.id.tv_vendor);
        stat= (TextView)findViewById(R.id.tv_status);
        update = (Button)findViewById(R.id.btn_update);
        freak = pref.get_name();

        update.setOnClickListener(this);

        i.setText(id);
        mod.setText(model);
        loc.setText(loca);
        pri.setText(price);
        buy.setText(buy_date);
        des.setText(desc);
        ven.setText(vendor);
        if (status.equals(one))
            stat.setText("Down for maintainence");
        else
            stat.setText("Working");

    }


    @Override
    public void onClick(View view) {
        loca = loc.getText().toString();
        desc = des.getText().toString();
        BackGround b = new BackGround();
        b.execute();
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
          //  String name = params[0];
           // String loo = params[1];
            //String deees = params[2];


            String data="";
            int tmp;

            try {
                URL url = new URL("http://192.168.43.133/mh/update.php");
                String urlParams = "id="+id+"&location="+loca+"&desc="+desc+"&tech="+freak;

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
                s="Update done successfully.";
                Intent i = new Intent(update.this,view.class);
                startActivity(i);
            }
            Toast.makeText(update.this, s, Toast.LENGTH_LONG).show();
        }
    }
}
