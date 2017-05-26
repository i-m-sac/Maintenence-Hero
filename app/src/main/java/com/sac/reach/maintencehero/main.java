package com.sac.reach.maintencehero;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class main extends AppCompatActivity implements View.OnClickListener {
    Button view, unres, id;
    String freak, desig = null,admin = "admin",ide;
    EditText enter;


    PrefManager pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        view = (Button) findViewById(R.id.btn_scan);
        view.setOnClickListener(this);
        enter = (EditText)findViewById(R.id.et_entid);

        pref = new PrefManager(getApplicationContext());
        unres = (Button) findViewById(R.id.btn_unres);
        id = (Button) findViewById(R.id.btn_id);
        id.setOnClickListener(this);
        freak = pref.get_name();
        desig = pref.get_key();


       Toast.makeText(main.this,"Welcome "+freak, Toast.LENGTH_LONG).show();
       if(!desig.equals(admin))
            unres.setEnabled(false);;
        unres.setOnClickListener(this);




    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_scan) {
            Intent i = new Intent(main.this, view.class);
            startActivity(i);
        }
        else if(v.getId()==R.id.btn_unres)
        {
            Intent i = new Intent(main.this,unresolved.class);
            startActivity(i);
        }
        else if (v.getId() == R.id.btn_id)
        {
            ide = enter.getText().toString();
            Intent i = new Intent(main.this,view.class);
            i.putExtra("id",ide);
            startActivity(i);
        }
    }
}
