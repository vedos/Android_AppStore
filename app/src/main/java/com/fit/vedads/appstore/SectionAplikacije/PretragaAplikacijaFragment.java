package com.fit.vedads.appstore.SectionAplikacije;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.fit.vedads.appstore.R;
import com.fit.vedads.appstore.api.AplikacijaAPI;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.model.AplikacijaVM;


public class PretragaAplikacijaFragment extends Fragment {
    private ListView lvPretraga;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        getActivity().setTitle("Pretraga");

        View view = inflater.inflate(R.layout.fragment_pretraga_aplikacija, container, false);
        lvPretraga = (ListView) view.findViewById(R.id.listView);
        final EditText etPretrazi = (EditText) view.findViewById(R.id.editTextPretrazi);
        ImageButton btnPretraga = (ImageButton) view.findViewById(R.id.imageButtonPretraga);
        btnPretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doBtnPretraga(etPretrazi);
            }
        });

        AplikacijaAPI.GetTop20(getActivity(), new MyRunnable<AplikacijaVM>() {
            @Override
            public void run(AplikacijaVM result) {
                listViewBind(result);

            }
        });

        return view;
    }


    private void doBtnPretraga(EditText editTextPretrazi) {
        final String param = editTextPretrazi.getText().toString(); //uneseni parametri po kojima pretrazujemo opis naziv
        AplikacijaAPI.Pretraga(getActivity(),param,new MyRunnable<AplikacijaVM>()
        {
            @Override
            public void run(AplikacijaVM result) {
                listViewBind(result);
            }
        });
    }

    private void listViewBind(final AplikacijaVM appsVM) {
        lvPretraga.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return appsVM.aplikacijeList.size();
            }

            @Override
            public Object getItem(int position) {
                return appsVM.aplikacijeList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View view, ViewGroup parent) {
                final AplikacijaVM.AplikacijaInfo x = appsVM.aplikacijeList.get(position);
                if(view == null) {
                    final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    view = inflater.inflate(R.layout.listitem_stavka_aplikacije, parent, false);
                }
                ImageView imageview = (ImageView) view.findViewById(R.id.imageViewThumb);
                TextView txtVNazivApp = (TextView) view.findViewById(R.id.txtVNazivApp);
                TextView txtVOpis = (TextView) view.findViewById(R.id.txtVOpis);
                TextView txtVBrPreuzimanja = (TextView) view.findViewById(R.id.txtVBrPreuzimanja);
                txtVNazivApp.setText(x.naziv);
                int takelength = 30;
                if(x.opis != null)
                    txtVOpis.setText(x.opis.substring(0,takelength>x.opis.length()?x.opis.length():takelength).replace("\n"," ")+"...");
                txtVBrPreuzimanja.setText("Broj preuzimanja: " + x.brojPreuzimanja);

                Bitmap defaultImage = BitmapFactory.decodeResource(getActivity().getResources(),
                        R.drawable.icon_app);
                imageview.setImageBitmap(defaultImage);
                if(x.slikaThumbnail != null) {
                    if(!x.slikaThumbnail.equals("empty")) {
                        byte[] bytes = Base64.decode(x.slikaThumbnail, Base64.DEFAULT);
                        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                        imageview.setImageBitmap(bitmap);
                    }
                }

                return view;
            }
        });
        lvPretraga.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AplikacijaVM.AplikacijaInfo x = appsVM.aplikacijeList.get(position);
                startActivity(new Intent(getActivity(),DetaljiAppActivity.class).putExtra("app_detalji",x));
            }
        });
    }

}
