package com.fit.vedads.appstore;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import com.fit.vedads.appstore.helper.Sesija;
import com.fit.vedads.appstore.model.AplikacijaVM;


public class MainActivity extends ActionBarActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

/*        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

      this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

       Thread timer = new Thread(){
            public void run(){
                try{
                    sleep(1000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    loginCheck();
                }
            }
        };
        timer.start();
    }

    private void loginCheck() {
        if(Sesija.getLogiraniKorisnik() == null)
        {
            Intent INTENT = new Intent("com.fit.vedads.appstore.LOGIN");
            startActivity(INTENT);
        }
        else
        {
           final Intent INTENT = new Intent(this, HomeActivity.class);
            startActivity(INTENT);
        }
    }
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

}
