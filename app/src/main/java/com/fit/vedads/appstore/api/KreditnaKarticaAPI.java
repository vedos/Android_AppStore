package com.fit.vedads.appstore.api;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fit.vedads.appstore.helper.Config;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.helper.url_connection.HttpManager;
import com.fit.vedads.appstore.helper.url_connection.MojApiRezultat;
import com.fit.vedads.appstore.model.KreditnaKartica;

import org.apache.http.message.BasicNameValuePair;

/**
 * Created by vedad on 17.1.2016.
 */
public class KreditnaKarticaAPI {
    public static void CheckKreditnaKartica(final Context context, final String brRacuna,final String cvv, final MyRunnable<KreditnaKartica> onSuccess)
    {
        final String url = Config.url + "Payment/GetCreditCard";
        new AsyncTask<Void,Void,MojApiRezultat<KreditnaKartica>>()
        {
            @Override
            protected MojApiRezultat<KreditnaKartica> doInBackground(Void... params) {
                return HttpManager.get(url,KreditnaKartica.class,
                        new BasicNameValuePair("brRacuna",brRacuna),
                        new BasicNameValuePair("cvv",cvv));
            }

            @Override
            protected void onPostExecute(MojApiRezultat<KreditnaKartica> r) {
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
