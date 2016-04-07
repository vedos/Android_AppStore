package com.fit.vedads.appstore.api;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.fit.vedads.appstore.helper.Config;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.helper.url_connection.HttpManager;
import com.fit.vedads.appstore.helper.url_connection.MojApiRezultat;
import com.fit.vedads.appstore.model.KategorijeVM;

public class KategorijeAPI {

    public static void  GetAll(final Context context, final MyRunnable<KategorijeVM> onSuccess)
    {
        final String url = Config.url + "Kategorije/GetAll";
        new AsyncTask<Void,Void,MojApiRezultat<KategorijeVM>>()
        {
            @Override
            protected MojApiRezultat<KategorijeVM> doInBackground(Void... params) {
                return HttpManager.get(url, KategorijeVM.class);
            }

            @Override
            protected void onPostExecute(MojApiRezultat<KategorijeVM> r) {
                if(r.isError)
                {
                    Toast.makeText(context,"Greška pri učitavanju: "+ r.errorMessage,Toast.LENGTH_SHORT).show();
                }else
                {
                    onSuccess.run(r.value);
                }
            }
        }.execute();
    }
}
