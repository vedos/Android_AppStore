package com.fit.vedads.appstore;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fit.vedads.appstore.R;
import com.fit.vedads.appstore.api.KorisnikAPI;
import com.fit.vedads.appstore.helper.MyApp;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.model.Korisnik;

public class RegistracijaActivity extends ActionBarActivity {

    private EditText etUsername;
    private EditText etIme;
    private EditText etPrezime;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etPassword2;
    private TextView tvError;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registracija);

         etUsername = (EditText) findViewById(R.id.et_regUsername);
         etIme = (EditText) findViewById(R.id.et_regIme);
         etPrezime = (EditText) findViewById(R.id.et_regPrezime);
         etEmail = (EditText) findViewById(R.id.et_regEmail);
         etPassword = (EditText) findViewById(R.id.et_regPassword);
         etPassword2 = (EditText) findViewById(R.id.et_regPassword2);
         tvError = (TextView) findViewById(R.id.tv_regError);
        Button btnRegistruj = (Button) findViewById(R.id.btnRegistracija);


        btnRegistruj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(validate())
                {
                    doBtnregistracija();
                }
            }
        });
    }

    private void doBtnregistracija() {
        Korisnik k = new Korisnik();
        k.username = etUsername.getText().toString();
        k.ime = etIme.getText().toString();
        k.prezime = etPrezime.getText().toString();
        k.email = etEmail.getText().toString();
        k.password = etPassword.getText().toString();
        KorisnikAPI.PostKorisnik(this,k, new MyRunnable<Korisnik>() {
            @Override
            public void run(Korisnik result) {
                startActivity(new Intent(RegistracijaActivity.this,LoginActivity.class));
                Toast.makeText(MyApp.getContext(),"Prijavite se",Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean validate()
    {
        if(etUsername.getText().length()<=0) {
            tvError.setText("Unesite username");
            return false;
        }
        if(etIme.getText().length()<=0) {
            tvError.setText("Unesite ime");
            return false;
        }
        if(etPrezime.getText().length()<=0) {
            tvError.setText("Unesite prezime");
            return false;
        }
        if(!isValidEmail(etEmail.getText())) {
            tvError.setText("Unesite e-mail adresu: example@yourdomain.com");
            return false;
        }
        if(etPassword.getText().length()<4 || !etPassword.getText().toString().equals(etPassword2.getText().toString())) {
            tvError.setText("Unesite password u oba polja sa najmanje 4 karaktera");
            return false;
        }

        tvError.setText("");

        return true;
    }

    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }
}
