package com.sac.reach.maintencehero;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class complaint extends AppCompatActivity implements View.OnClickListener {
    Button lodge;
    EditText comp;
    String complaint,id,freak;
    PrefManager pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaint);
        lodge = (Button)findViewById(R.id.btn_lod);
        pref = new PrefManager(getApplicationContext());
        lodge.setOnClickListener(this);
        comp = (EditText)findViewById(R.id.et_comp);
        id=getIntent().getStringExtra("id");
        freak = pref.get_name();


    }

    @Override
    public void onClick(View view) {
        complaint = comp.getText().toString();
        if (complaint.equals(null))
        {
            Toast.makeText(complaint.this,"Enter a complaint" , Toast.LENGTH_LONG).show();

        }
        else
        {
            BackGround b = new BackGround();
            b.execute(id,freak,complaint);
        }


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
                URL url = new URL("http://192.168.43.133/mh/complain.php");
                String urlParams = "id="+id+"&complaint="+complaint+"&name="+freak;

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
                s="Complaint Lodged successfully.";
                Intent i = new Intent(complaint.this,view.class);
                startActivity(i);
            }
            Toast.makeText(complaint.this, s, Toast.LENGTH_LONG).show();
        }
    }
}
