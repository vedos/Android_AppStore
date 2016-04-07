package com.fit.vedads.appstore.api;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fit.vedads.appstore.model.AutentifikacijaProvjeraVM;
import com.fit.vedads.appstore.helper.Config;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.helper.url_connection.HttpManager;
import com.fit.vedads.appstore.helper.url_connection.MojApiRezultat;

import org.apache.http.message.BasicNameValuePair;

public class AutentifikacijaAPI {

    public static void Provjera(final Context context, final String username, final String password, final MyRunnable<AutentifikacijaProvjeraVM> onSuccess) {
        final String url = Config.url + "Autentifikacija/Provjera";
        final ProgressDialog dialog = ProgressDialog.show(context, "Pristup podacima", "U toku");
        dialog.show();

        new AsyncTask<Void, Void, MojApiRezultat<AutentifikacijaProvjeraVM>>() {


            @Override
            protected MojApiRezultat<AutentifikacijaProvjeraVM> doInBackground(Void... params) {

                return HttpManager.get(url, AutentifikacijaProvjeraVM.class,
                        new BasicNameValuePair("username", username),
                        new BasicNameValuePair("password", password)
                );
            }

            @Override
            protected void onPostExecute(MojApiRezultat<AutentifikacijaProvjeraVM> rezultat) {
                if (rezultat.isError) {
                    Toast.makeText(context, "Greška u komunikaciji sa serverom: " + rezultat.errorMessage, Toast.LENGTH_LONG).show();

                } else {
                    onSuccess.run(rezultat.value);
                }
                dialog.dismiss();
            }
        }.execute();
    }
}
