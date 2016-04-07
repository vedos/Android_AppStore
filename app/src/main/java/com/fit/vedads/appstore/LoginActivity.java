package com.fit.vedads.appstore;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.fit.vedads.appstore.api.AutentifikacijaAPI;
import com.fit.vedads.appstore.model.AutentifikacijaProvjeraVM;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.helper.Sesija;

public class LoginActivity extends ActionBarActivity {

    private EditText etUsername;
    private EditText etPassword;
    final private static String ARG_onPossitiveDismiss = "ARG_onPossitiveDismiss";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.txtUsername);
        etPassword = (EditText) findViewById(R.id.txtPassword);
         Button btnLogin = (Button) findViewById(R.id.btnLogin);

        ImageButton btnRegistracija = (ImageButton) findViewById(R.id.imageButtonRegister);
        btnRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegistracijaActivity.class));
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                do_btnLogin_click();
            }
        });
    }

    private void do_btnLogin_click() {
        AutentifikacijaAPI.Provjera(this, etUsername.getText().toString(), etPassword.getText().toString(), new MyRunnable<AutentifikacijaProvjeraVM>() {
            @Override
            public void run(AutentifikacijaProvjeraVM result) {
                Sesija.setLogiraniKorisnik(result);
                if (result == null) {
                    Toast.makeText(LoginActivity.this, "Username ili password nije validan", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(LoginActivity.this, result.KorisnickoIme, Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                }
            }
        });
    }
}
