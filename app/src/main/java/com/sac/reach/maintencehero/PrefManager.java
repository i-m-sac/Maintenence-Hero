package com.sac.reach.maintencehero;

/**
 * Created by Akash Bhaskaran on 13-07-2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

/**
 * Created by Lincoln on 05/05/16.
 */
public class PrefManager {
    SharedPreferences pref,key,enckey,name,phone,incorrect;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "name";
    private static final String PREF_KEY = "key1";
    private static final String PREF_ENCKEY = "enckey";
    private static final String PREF_PHONE = "phone";
    private static final String PREF_INCORRECT = "incorrect";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";


    public PrefManager(Context context) {
        this._context = context;
      //  pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        name = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        key = _context.getSharedPreferences(PREF_KEY,PRIVATE_MODE);
        enckey = _context.getSharedPreferences(PREF_ENCKEY, PRIVATE_MODE);
        phone = _context.getSharedPreferences(PREF_PHONE, PRIVATE_MODE);
        incorrect = _context.getSharedPreferences(PREF_INCORRECT, PRIVATE_MODE);
        //editor = name.edit();
    }




    public int getincorrectpass() {
       return incorrect.getInt("incorrect",0);
    }

    public void setname(String n) {
        name = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = name.edit();
        editor.putString("name", n);
        editor.commit();

    }
    public void setkey(String k) {
        key = _context.getSharedPreferences(PREF_KEY, PRIVATE_MODE);
        editor = key.edit();
        editor.putString("key1", k);
        editor.commit();

    }



    public String get_key() {
        return key.getString("key1", "");
    }
    public String get_name() {
        return name.getString("name", null);
    }

    public Long get_phone() {

        return phone.getLong("phone", 0);
    }
    public String get_enckey() {
        return enckey.getString("enckey",null);
    }


    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }

}