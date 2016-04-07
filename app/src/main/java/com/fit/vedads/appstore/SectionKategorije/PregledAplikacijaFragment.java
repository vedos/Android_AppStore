package com.fit.vedads.appstore.SectionKategorije;


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
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fit.vedads.appstore.R;
import com.fit.vedads.appstore.SectionAplikacije.DetaljiAppActivity;
import com.fit.vedads.appstore.api.AplikacijaAPI;
import com.fit.vedads.appstore.api.KategorijeAPI;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.model.AplikacijaVM;
import com.fit.vedads.appstore.model.KategorijeVM;


public class PregledAplikacijaFragment extends Fragment {

    private GridView gvPregledAplikacija;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_gvpregled_aplikacija,container,false);
        final TextView tvNazivKategorije = (TextView) view.findViewById(R.id.tv_gvNazivKategorije);
        gvPregledAplikacija = (GridView) view.findViewById(R.id.gv_aplikacije);
        Bundle bundle = getArguments();
        final KategorijeVM.KategorijaInfo x = (KategorijeVM.KategorijaInfo)bundle.get("kat");

        AplikacijaAPI.GetAppsByKat(getActivity(),x.ID,new MyRunnable<AplikacijaVM>()
        {
            @Override
            public void run(AplikacijaVM result) {
                gridviewBind(result);
            }
        });
        tvNazivKategorije.setText(x.Naziv);

        return view;
    }

    private void gridviewBind(final AplikacijaVM result) {
        gvPregledAplikacija.setAdapter(new BaseAdapter() {
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
            public View getView(int position, View convertView, ViewGroup parent) {
                AplikacijaVM.AplikacijaInfo x = result.aplikacijeList.get(position);
                if(convertView==null)
                {
                    LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.gridview_item_aplikacija,parent,false);
                }
                ImageView imageview = (ImageView) convertView.findViewById(R.id.gvimageView);
                TextView tvNazivApp = (TextView) convertView.findViewById(R.id.gvitem_nazivaplikacije);
                tvNazivApp.setText(x.naziv);
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
                return convertView;
            }
        });

        gvPregledAplikacija.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final AplikacijaVM.AplikacijaInfo x = result.aplikacijeList.get(position);
                startActivity(new Intent(getActivity(),DetaljiAppActivity.class).putExtra("app_detalji",x));
            }
        });
    }
}
