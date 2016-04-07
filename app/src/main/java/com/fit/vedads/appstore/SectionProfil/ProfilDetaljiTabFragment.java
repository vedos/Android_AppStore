package com.fit.vedads.appstore.SectionProfil;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fit.vedads.appstore.R;
import com.fit.vedads.appstore.api.KorisnikAPI;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.helper.Sesija;
import com.fit.vedads.appstore.model.Korisnik;

public class ProfilDetaljiTabFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profil_detalji, container, false);
        final TextView tvUsername = (TextView) view.findViewById(R.id.textViewUsername);
        final TextView tvIme = (TextView) view.findViewById(R.id.textViewImePrezime);
        final TextView tvEmail = (TextView) view.findViewById(R.id.textViewEmail);
        KorisnikAPI.Get(this.getActivity(), Sesija.getLogiraniKorisnik().KorisnikID, new MyRunnable<Korisnik>() {
            @Override
            public void run(Korisnik result) {
                if (result != null) {
                    tvUsername.setText("Korisničko ime: " + result.username);
                    tvIme.setText("Ime i prezime: " + result.ime + " " + result.prezime);
                    tvEmail.setText("Email: " + result.email);
                }
            }
        });



        return view;
        // /return super.onCreateView(inflater, container, savedInstanceState);
    }
}
