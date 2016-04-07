package com.fit.vedads.appstore.api;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fit.vedads.appstore.helper.Config;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.helper.url_connection.HttpManager;
import com.fit.vedads.appstore.helper.url_connection.MojApiRezultat;
import com.fit.vedads.appstore.model.Korisnik;

import org.apache.http.message.BasicNameValuePair;


public class KorisnikAPI {
    public static  void Get(final Context context,final int id,final MyRunnable<Korisnik> onSuccess)
    {
        final String url = Config.url + "Korisnik/Get";

        new AsyncTask<Void,Void, MojApiRezultat<Korisnik>>()
        {
            @Override
            protected MojApiRezultat<Korisnik> doInBackground(Void... params) {
                return HttpManager.get(url, Korisnik.class, new BasicNameValuePair("id", id + ""));

            }

            @Override
            protected void onPostExecute(MojApiRezultat<Korisnik> r) {
                if(r.isError)
                {
                    Toast.makeText(context,"Greška u komunikaciji sa serverom: " + r.errorMessage,Toast.LENGTH_LONG).show();
                }
                else
                {
                    onSuccess.run(r.value);
                }
            }
        }.execute();
    }

    public static void PostKorisnik (final Context context,final Korisnik k, final MyRunnable<Korisnik> onSuccess)
    {
        final String url = Config.url + "Korisnik/PostKorisnik";
        new AsyncTask<Void,Void,MojApiRezultat<Korisnik>>()
        {
            @Override
            protected MojApiRezultat<Korisnik> doInBackground(Void... params) {
                return HttpManager.post(url,Korisnik.class,k);
            }

            @Override
            protected void onPostExecute(MojApiRezultat<Korisnik> r) {
                if(r.isError) {
                    Toast.makeText(context,"Greška u komunikaciji sa serverom: " + r.errorMessage,Toast.LENGTH_SHORT ).show();
                }else
                {
                    if(r.value==null)
                    {
                        Toast.makeText(context,"Uspješno ste se registorvali",Toast.LENGTH_SHORT ).show();
                        onSuccess.run(r.value);
                    }else {
                        Toast.makeText(context,"Odabrani username već postoji.",Toast.LENGTH_SHORT ).show();
                    }
                }
            }
        }.execute();
    }
}
