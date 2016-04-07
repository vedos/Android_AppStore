package com.fit.vedads.appstore.api;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fit.vedads.appstore.helper.Config;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.helper.url_connection.HttpManager;
import com.fit.vedads.appstore.helper.url_connection.MojApiRezultat;
import com.fit.vedads.appstore.model.AplikacijaVM;
import com.fit.vedads.appstore.model.KorisnikAplikacija;

import org.apache.http.message.BasicNameValuePair;

public class PreuzeteAplikacijeAPI {
    public static void GetApps(final Context context,final int id, final MyRunnable<AplikacijaVM> onSuccess)
    {
        final String url = Config.url + "PreuzeteAplikacije/GetAppsByUserID";//posljednjih 50
        new AsyncTask<Void,Void,MojApiRezultat<AplikacijaVM>>()
        {
            @Override
            protected MojApiRezultat<AplikacijaVM> doInBackground(Void... params) {
                return HttpManager.get(url,AplikacijaVM.class,
                        new BasicNameValuePair("uId",id+""));
            }

            @Override
            protected void onPostExecute(MojApiRezultat<AplikacijaVM> r) {
                if(r.isError)
                {
                    Toast.makeText(context, "Greška u komunikaciji sa serverom: " + r.errorMessage, Toast.LENGTH_LONG).show();
                }else{
                    onSuccess.run(r.value);
                }
            }
        }.execute();
    }


    public static void PostPreuzetaApp(final Context context,final int uID,final int appID, final MyRunnable<KorisnikAplikacija> onSuccess)
    {
        final String url = Config.url + "PreuzeteAplikacije/PostPreuzetaApp";
        new AsyncTask<Void,Void,MojApiRezultat<KorisnikAplikacija>>()
        {
            @Override
            protected MojApiRezultat<KorisnikAplikacija> doInBackground(Void... params) {
                KorisnikAplikacija k = new KorisnikAplikacija();
                k.KorisnikId = uID;
                k.AplikacijaId = appID;
                return HttpManager.post(url,KorisnikAplikacija.class,k);
            }

            @Override
            protected void onPostExecute(MojApiRezultat<KorisnikAplikacija> r) {
                if(r.isError)
                {
                    Toast.makeText(context,"Došlo je do greške: " + r.errorMessage,Toast.LENGTH_SHORT).show();
                }else
                {
                    onSuccess.run(r.value);
                }
            }
        }.execute();
    }
}
