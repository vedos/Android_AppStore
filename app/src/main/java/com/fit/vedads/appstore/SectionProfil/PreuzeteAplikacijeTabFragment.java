package com.fit.vedads.appstore.SectionProfil;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fit.vedads.appstore.SectionAplikacije.DetaljiAppActivity;
import com.fit.vedads.appstore.R;
import com.fit.vedads.appstore.api.PreuzeteAplikacijeAPI;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.helper.Sesija;
import com.fit.vedads.appstore.model.AplikacijaVM;

/**
 * Created by vedad on 13.1.2016.
 */
public class PreuzeteAplikacijeTabFragment extends Fragment {
    ListView lvApps;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.listview_aplikacija, container, false);
        ((TextView)view.findViewById(R.id.profil_list_headerText)).setText("Preuzete aplikacije");
        lvApps = (ListView) view.findViewById(R.id.listViewAplikacije);

        PreuzeteAplikacijeAPI.GetApps(getActivity(), Sesija.getLogiraniKorisnik().KorisnikID, new MyRunnable<AplikacijaVM>() {
            @Override
            public void run(AplikacijaVM result) {
                if(result.aplikacijeList.size()!=0) {
                    listViewBind(result);
                }
            }
        });

        return view;
    }

    private void listViewBind(final AplikacijaVM result) {
        lvApps.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return result.aplikacijeList.size();
            }

            @Override
            public Object getItem(int position) {
                return result.aplikacijeList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                AplikacijaVM.AplikacijaInfo x = result.aplikacijeList.get(position);
                if(view==null)
                {
                    final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.listitem_stavka_aplikacije,parent,false);
                }
                ((TextView)view.findViewById(R.id.txtVNazivApp)).setText(x.naziv);
                ((TextView)view.findViewById(R.id.txtVOpis)).setVisibility(View.GONE);
                ((TextView)view.findViewById(R.id.txtVBrPreuzimanja)).setText("Preuzimanja: " + x.brojPreuzimanja);
                ImageView imageview = (ImageView) view.findViewById(R.id.imageViewThumb);

                Bitmap defaultImage = BitmapFactory.decodeResource(getActivity().getResources(),
                        R.drawable.icon_app);
                imageview.setImageBitmap(defaultImage);
                if(x.slikaThumbnail != null) {
                    if (!x.slikaThumbnail.equals("empty")) {
                        byte[] bytes = Base64.decode(x.slikaThumbnail, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        imageview.setImageBitmap(bitmap);
                    }
                }
                return view;
            }
        });
        lvApps.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AplikacijaVM.AplikacijaInfo x = result.aplikacijeList.get(position);
                startActivity(new Intent(getActivity(), DetaljiAppActivity.class).putExtra("app_detalji",x));
            }
        });
    }


}
