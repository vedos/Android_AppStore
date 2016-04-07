package com.fit.vedads.appstore.SectionKategorije;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.fit.vedads.appstore.R;
import com.fit.vedads.appstore.api.KategorijeAPI;
import com.fit.vedads.appstore.helper.MyRunnable;
import com.fit.vedads.appstore.model.AplikacijaVM;
import com.fit.vedads.appstore.model.KategorijeVM;

public class KategorijeFragment extends Fragment {
    public  ListView lvKategorije;
    private KategorijeVM kategorije;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_kategorija,container,false);
        lvKategorije = (ListView) view.findViewById(R.id.list_kategorije);

        KategorijeAPI.GetAll(getActivity(),new MyRunnable< KategorijeVM>()
        {
            @Override
            public void run(KategorijeVM result) {
                kategorije = result;
                kategorijeListBind(result);
            }
        });

        lvKategorije.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fm = getFragmentManager();
                Fragment fragment = new PregledAplikacijaFragment();
                Bundle arg =new Bundle();
                KategorijeVM.KategorijaInfo kat = kategorije.listKategorije.get(position);
                arg.putSerializable("kat",kat);
                fragment.setArguments(arg);
                fm.beginTransaction().replace(R.id.frame_container,fragment).addToBackStack(null).commit();
            }
        });

        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();

        getActivity().setTitle("Kategorije");
    }

    private void kategorijeListBind(final KategorijeVM result) {
        String[] nazivi = new String[result.listKategorije.size()];

        for (int i = 0; i < nazivi.length; i++) {
            nazivi[i] = result.listKategorije.get(i).Naziv;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, nazivi);
        lvKategorije.setAdapter(adapter);
    }
}
