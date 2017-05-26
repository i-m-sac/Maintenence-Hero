package com.sac.reach.maintencehero;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class view extends Activity implements View.OnClickListener {
    /** Called when the activity is first created. */
    String contents;
    Button det,up,complaint;
    String NAME,model,loca,price,buy_date,desc,vendor,status,freak,desig,admin="admin";
    PrefManager pref;


    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        det=(Button)findViewById(R.id.btnd);
        det.setOnClickListener(this);
        complaint = (Button)findViewById(R.id.btn_complaint);
        complaint.setOnClickListener(this);
        up = (Button)findViewById(R.id.btn_up);
        up.setOnClickListener(this);
        contents=getIntent().getStringExtra("id");
        pref = new PrefManager(getApplicationContext());
        freak = pref.get_name();
        desig = pref.get_key();

        if (contents!=null){
            BackGround b = new BackGround();
            b.execute(contents);

        }
        if (model==null){
            det.setEnabled(false);
            up.setEnabled(false);
        }
    }



    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(view.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    act.startActivity(intent);
                } catch (ActivityNotFoundException anfe) {

                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");

               // Toast toast = Toast.makeText(this, "Content:" + contents + " Format:" + format, Toast.LENGTH_LONG);
                //toast.show();
                BackGround b = new BackGround();
                b.execute(contents);
            }
        }
    }

    class BackGround extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            String id = params[0];

            String data="";
            int tmp;

            try {
                URL url = new URL("http://192.168.43.133/mh/item.php");
                String urlParams = "id="+id;

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
            String date = null;
            String prod = null,custid=null;
            try {
                JSONObject root = new JSONObject(s);
                JSONObject item_data = root.getJSONObject("item_data");
                NAME = item_data.getString("id");
                model = item_data.getString("model");
                loca = item_data.getString("loca");

                buy_date = item_data.getString("buy_date");
                desc = item_data.getString("desc");
                vendor = item_data.getString("vendor");
                status = item_data.getString("status");
                price = item_data.getString("price");


            } catch (JSONException e) {
                e.printStackTrace();
                err = "Exception: "+e.getMessage();
            }

            if (NAME!=null && !NAME.isEmpty()){
                Toast.makeText(view.this,"Scanned Succesfully",Toast.LENGTH_SHORT).show();
                if (desig.equals(admin)) {

                    up.setEnabled(true);
                }
                det.setEnabled(true);
            }
            else {
                Toast.makeText(view.this, "Incorrect Key", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnd)
        {
            Intent i = new Intent(view.this, details.class);
            i.putExtra("name", NAME);
            i.putExtra("model", model);
            i.putExtra("loca", loca);
            i.putExtra("price", price);
            i.putExtra("buy_date", buy_date);
            i.putExtra("desc", desc);
            i.putExtra("vendor", vendor);
            i.putExtra("status", status);

            startActivity(i);
        }
        else if (v.getId()==R.id.btn_up){
            Intent i = new Intent(view.this, update.class);
            i.putExtra("name", NAME);
            i.putExtra("model", model);
            i.putExtra("loca", loca);
            i.putExtra("price", price);
            i.putExtra("buy_date", buy_date);
            i.putExtra("desc", desc);
            i.putExtra("vendor", vendor);
            i.putExtra("status", status);

            startActivity(i);

        }
        else if (v.getId()==R.id.btn_complaint)
        {
            Intent i = new Intent(view.this, complaint.class);
            i.putExtra("id", contents);
            startActivity(i);

        }

    }
}
