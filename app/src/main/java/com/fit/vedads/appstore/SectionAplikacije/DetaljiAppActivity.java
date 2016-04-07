package com.fit.vedads.appstore.SectionAplikacije;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fit.vedads.appstore.R;
import com.fit.vedads.appstore.api.KupljeneAplikacijeAPI;
import com.fit.vedads.appstore.api.PreuzeteAplikacijeAPI;
import com.fit.vedads.appstore.helper.Config;
import com.fit.vedads.appstore.helper.MyApp;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.helper.Sesija;
import com.fit.vedads.appstore.helper.url_connection.HttpManager;
import com.fit.vedads.appstore.helper.url_connection.MojApiRezultat;
import com.fit.vedads.appstore.model.AplikacijaVM;
import com.fit.vedads.appstore.model.AutentifikacijaProvjeraVM;
import com.fit.vedads.appstore.model.KorisnikAplikacija;
import com.fit.vedads.appstore.model.KreditnaKartica;

import org.apache.http.message.BasicNameValuePair;


public class DetaljiAppActivity extends ActionBarActivity {
    private AplikacijaVM.AplikacijaInfo app;
    private boolean isBuyed;
    private Button btnPreuzmi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalji_app);

        TextView tvNaziv = (TextView) findViewById(R.id.txtVNaziv);
        ImageView imageview = (ImageView) findViewById(R.id.imageViewDetalji);
        TextView tvOpis = (TextView) findViewById(R.id.txtVO);
        TextView tvBrPreuzimanja = (TextView) findViewById(R.id.txtVpreuzimanja);
        TextView tvCijena = (TextView) findViewById(R.id.txtVCijena);
        //TextView tvVelicinaFajla = (TextView) findViewById(R.id.txtBVelicina);
        btnPreuzmi =(Button) findViewById(R.id.btnPreuzmi);

        app = ((AplikacijaVM.AplikacijaInfo) getIntent().getSerializableExtra("app_detalji"));
        getSupportActionBar().setTitle(app.naziv);

        checkIsBuyed();

        tvOpis.setText(app.opis);
        if(app.brojPreuzimanja>0)
        {
            tvBrPreuzimanja.setText("Broj preuzimanja: "+ app.brojPreuzimanja);

        }
        if(app.cijena>0) {
            tvCijena.setText((Float.toString(app.cijena)) + " KM");
        }else
        {
            tvCijena.setText("Besplatno");
        }
        tvNaziv.setText(app.naziv);
        Bitmap defaultImage = BitmapFactory.decodeResource(getResources(),
                R.drawable.icon_app);
        imageview.setImageBitmap(defaultImage);
        if(app.slikaThumbnail != null) {
            byte[] bytes = Base64.decode(app.slikaThumbnail, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            imageview.setImageBitmap(bitmap);
        }

        btnPreuzmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBtnPreuzmi();
            }
        });

    }

    private void checkIsBuyed() {
        isBuyed=true;
        if (app.cijena > 0) {
            btnPreuzmi.setText("Kupi");
            isBuyed = false;
            if(Sesija.getLogiraniKorisnik().kupljeneAplikacijeIds!=null) {
                if (Sesija.getLogiraniKorisnik().kupljeneAplikacijeIds.contains(app.ID)) {
                    btnPreuzmi.setText("Preuzmi");
                    isBuyed = true;
                }
            }
        }
    }

    private void doBtnPreuzmi() {
        new AsyncTask<Void, Void, MojApiRezultat<AplikacijaVM.AplikacijaInfo>>() {

            @Override
            protected MojApiRezultat<AplikacijaVM.AplikacijaInfo> doInBackground(Void... params) {

                return HttpManager.get(Config.url + "Aplikacija/Get", AplikacijaVM.AplikacijaInfo.class,
                        new BasicNameValuePair("id", Integer.toString(app.ID))
                );
            }
            @Override
            protected void onPostExecute(MojApiRezultat<AplikacijaVM.AplikacijaInfo> x) {
                if (x.isError) {
                    Toast.makeText(getApplicationContext(), "Greška u komunikaciji sa serverom: " + x.errorMessage, Toast.LENGTH_LONG).show();

                } else {
                    if(isBuyed) {
                        preuzmiAppDialog(x.value.fajl);
                    }else
                    {
                        kupiAppDialog(x.value);
                    }
                }
            }
        }.execute();

    }

    private void kupiAppDialog(final AplikacijaVM.AplikacijaInfo value) {
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setMessage("Da li želite kupiti ovu aplikaciju?\n" + app.cijena + " KM");
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Kupovina", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                int korisnikID = Sesija.getLogiraniKorisnik().KorisnikID;
                payment(korisnikID,value.ID);

            }

        });
        adb.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Prekinuto", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                // moveTaskToBack(true);
            }
        });
        adb.setCancelable(false);
        adb.show();
    }

    private void payment(final int korisnikID, final int ID) {

        final MyRunnable<KreditnaKartica> onPositiveDismiss = new MyRunnable<KreditnaKartica>() {
            @Override
            public void run(KreditnaKartica response) {
                Toast.makeText(getApplicationContext(),"Uspješno kupljena aplikacija " +app.naziv+ " " + app.cijena + " KM", Toast.LENGTH_SHORT).show();
                addappKupovina(korisnikID, ID);

            }
        };
        PaymentFragment.otvoriPaymentDialog(this,onPositiveDismiss);
    }

    private void preuzmiAppDialog(final String fajl) {//asinhrono pozivanje prebaciti
        final AlertDialog.Builder adb = new AlertDialog.Builder(this);
        adb.setTitle("Instaliranje...? ");
        adb.setMessage("Da li želite instalirati ovu aplikaciju?\n");
        adb.setIcon(android.R.drawable.ic_dialog_alert);
        adb.setPositiveButton("Da", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Preuzimanje", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                installNewApp(fajl);
                addappPreuzete(Sesija.getLogiraniKorisnik().KorisnikID,app.ID);
            }

        });
        adb.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(), "Prekinuto", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                // moveTaskToBack(true);
            }
        });
        adb.setCancelable(false);
        adb.show();
    }

    private void addappKupovina(int uID,final int appId) {

        KupljeneAplikacijeAPI.PostKupovinaApp(this, uID, appId, new MyRunnable<KorisnikAplikacija>() {
            @Override
            public void run(KorisnikAplikacija result) {
                AutentifikacijaProvjeraVM x = Sesija.getLogiraniKorisnik();
                x.kupljeneAplikacijeIds.add(0, appId);
                Sesija.setLogiraniKorisnik(x);
                checkIsBuyed();
            }
        });
    }

    private void addappPreuzete(int uID, int appid) {

        PreuzeteAplikacijeAPI.PostPreuzetaApp(this, uID, appid,new MyRunnable<KorisnikAplikacija>()
        {
            @Override
            public void run(KorisnikAplikacija result) {
                //uspješno preuzeto
            }
        });
    }

    private void installNewApp(String fajl)
    {
        if(fajl!= null) {
            try {
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(app.fajl));//make apk
                request.setDescription("Preuzimanje...");
                request.setTitle(app.naziv);
                // in order for this if to run, you must use the android 3.2 to compile your app
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    request.allowScanningByMediaScanner();
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                }
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, app.naziv);

                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);

                moveTaskToBack(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else
        {
            Toast.makeText(this,"Aplikacija nije dostupna",Toast.LENGTH_LONG).show();
        }
    }
}


