package com.sac.reach.maintencehero;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class    details extends AppCompatActivity {
    String id;
    String one = "1";
    String model,loca,price,buy_date,desc,vendor,status;
    TextView i,mod,loc,pri,buy,des,ven,stat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
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
        loc= (TextView)findViewById(R.id.tv_loca);
        pri= (TextView)findViewById(R.id.tv_price);
        buy= (TextView)findViewById(R.id.tv_buy);
        des= (TextView)findViewById(R.id.tv_desc);
        ven= (TextView)findViewById(R.id.tv_vendor);
        stat= (TextView)findViewById(R.id.tv_status);

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
}
