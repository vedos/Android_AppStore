package com.fit.vedads.appstore.helper;

import android.content.SharedPreferences;

import com.fit.vedads.appstore.model.AutentifikacijaProvjeraVM;

/**
 * Created by vedad on 25.5.2015.
 */
public class Sesija {
    public static final String PREFS_NAME = "DatotekaZaSharedPreferences";
    //private static AutentifikacijaProvjeraVM logiraniKorisnik;

    public static AutentifikacijaProvjeraVM getLogiraniKorisnik(){
        //return logiraniKorisnik;
        if(MyApp.getContext() == null)
            return null;
        SharedPreferences settings = MyApp.getContext().getSharedPreferences(PREFS_NAME, 0);
        String str;
        str = settings.getString("logiraniKorisnikJson", "");
        if(str.length()==0)
            return null;
        final AutentifikacijaProvjeraVM vm = MyGson.build().fromJson(str,AutentifikacijaProvjeraVM.class);
        return vm;
    }

    public static void setLogiraniKorisnik(AutentifikacijaProvjeraVM x)
    {
        //logiraniKorisnik = x;
        final String str = MyGson.build().toJson(x);
        SharedPreferences settings = MyApp.getContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        editor.putString("logiraniKorisnikJson", str);

        // Commit the edits!
        editor.commit();
    }
}
