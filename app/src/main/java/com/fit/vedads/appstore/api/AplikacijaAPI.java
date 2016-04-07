package com.fit.vedads.appstore.api;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fit.vedads.appstore.helper.Config;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.helper.url_connection.HttpManager;
import com.fit.vedads.appstore.helper.url_connection.MojApiRezultat;
import com.fit.vedads.appstore.model.AplikacijaVM;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by vedad on 18.1.2016.
 */
public class AplikacijaAPI {

    public static void GetAppsByKat(final Context context, final int katID, final MyRunnable<AplikacijaVM> onSuccess)
    {
        final String url = Config.url + "Aplikacija/GetAppsByKat";
        final ProgressDialog dialog = ProgressDialog.show(context, "Pristup podacima", "U toku");
        dialog.show();
        new AsyncTask<Void,Void,MojApiRezultat<AplikacijaVM>>()
        {
            @Override
            protected MojApiRezultat<AplikacijaVM> doInBackground(Void... params) {
                return HttpManager.get(url,AplikacijaVM.class,new BasicNameValuePair("kategorijaID",katID+""));
            }

            @Override
            protected void onPostExecute(MojApiRezultat<AplikacijaVM> r) {
                if(r.isError)
                {
                    Toast.makeText(context,"Došlo je do greške: " + r.errorMessage,Toast.LENGTH_SHORT);
                }else{
                    onSuccess.run(r.value);
                }
                dialog.dismiss();
            }
        }.execute();
    }


    public static void GetTop20(final Context context, final MyRunnable<AplikacijaVM> onSuccess)
    {
        final String url =Config.url +  "Aplikacija/GetTop20";
        final ProgressDialog dialog = ProgressDialog.show(context, "Učitavanje aplikacija", "U toku");
        dialog.show();
        new AsyncTask<Void, Void, MojApiRezultat<AplikacijaVM>>()
        {
            @Override
            protected MojApiRezultat<AplikacijaVM> doInBackground(Void... params) {
                return HttpManager.get(url, AplikacijaVM.class);
            }

            @Override
            protected void onPostExecute(MojApiRezultat<AplikacijaVM> r) {
                if (r.isError) {
                    Toast.makeText(context, "Greška u komunikaciji sa serverom: " + r.errorMessage, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(context, "Podaci su učitani", Toast.LENGTH_SHORT).show();
                    onSuccess.run(r.value);
                }
                dialog.dismiss();
            }
        }.execute();
    }


    public static void Pretraga (final Context context, final String param,final MyRunnable<AplikacijaVM> onSuccess)
    {
        final String url = Config.url + "Aplikacija/Pretraga";
        new AsyncTask<Void, Void, MojApiRezultat<AplikacijaVM>>() {
            @Override
            protected MojApiRezultat<AplikacijaVM> doInBackground(Void... params) {

                return HttpManager.get(url, AplikacijaVM.class, new BasicNameValuePair("param", param));
            }
            @Override
            protected void onPostExecute(MojApiRezultat<AplikacijaVM> r) {
                if (r.isError) {
                    Toast.makeText(context, "Greška u komunikaciji sa serverom: " + r.errorMessage, Toast.LENGTH_SHORT).show();

                } else {
                   onSuccess.run(r.value);
                }
            }
        }.execute();
    }
}
