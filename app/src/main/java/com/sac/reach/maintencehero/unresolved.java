package com.sac.reach.maintencehero;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class unresolved extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    TextView un;
    String NAME,x="1";
    Spinner spinner;
Button show;
    String prod,id;
    String item,temp;
    List<String> pcs = new ArrayList<String>();

    List<String> items = new ArrayList<String>();

    ArrayAdapter<String> dataAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unresolved);

        BackGround b = new BackGround();
        b.execute(x);
        show = (Button)findViewById(R.id.btn_show);
        show.setOnClickListener(this);
        spinner = (Spinner)findViewById(R.id.spin);
        spinner.setOnItemSelectedListener(this);
        // Creating adapter for spinner
         dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);





    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // On selecting a spinner item
        prod = adapterView.getItemAtPosition(i).toString();

        id = prod;


    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onClick(View view) {
        Intent i = new Intent(unresolved.this,getdet.class);
        i.putExtra("id",id);
        startActivity(i);
       // Toast.makeText(unresolved.this,id, Toast.LENGTH_LONG).show();


    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String brand = params[0];

            String data = "";
            int tmp;


            try {
                URL url = new URL("http://192.168.43.133/mh/search.php");
                String urlParams = "brand=" + brand;

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setDoOutput(true);
                OutputStream os = httpURLConnection.getOutputStream();
                os.write(urlParams.getBytes());
                os.flush();
                os.close();

                InputStream is = httpURLConnection.getInputStream();
                while ((tmp = is.read()) != -1) {
                    data += (char) tmp;
                }

                is.close();
                httpURLConnection.disconnect();

                return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "Exception: " + e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String err = null;

            JSONArray results;

            try {
                JSONObject root = new JSONObject(s);
                results = root.getJSONArray("results");
                NAME = results.toString();


            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: " + e.getMessage();
            }


            if (NAME != "[]") {

                stringsetter();




            } else {

                Toast.makeText(unresolved.this, "\tNo Results Found \nTry again with a different combo " + err, Toast.LENGTH_SHORT).show();

            }
        }
    }
    void stringsetter()
    {
        int k=0,pos=0;
        temp = "";
        for(int j=0;NAME.charAt(j)!=']';j++){

            if((NAME.charAt(j)>='a'&& NAME.charAt(j)<='z') || (NAME.charAt(j)>='A' && NAME.charAt(j)<='Z') || NAME.charAt(j)=='_'|| (NAME.charAt(j)>='0' && NAME.charAt(j)<='9'))
            {
                temp=temp+NAME.charAt(j);


            }
            else if (NAME.charAt(j)=='"')
            {
                pos+=1;




            }
            if(pos==2) {
                items.add(temp);
                temp = "";
                pos =0;
            }

            spinner.setAdapter(dataAdapter);

        }


    }
}
