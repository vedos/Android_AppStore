package com.fit.vedads.appstore.SectionProfil;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fit.vedads.appstore.HomeActivity;
import com.fit.vedads.appstore.R;

public class ProfilFragment extends Fragment {

    private ViewPager viewpager;
    private ActionBar actionBar;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_profil,container,false);

        viewpager = (ViewPager) view.findViewById(R.id.viewpagerProfil);
        pripremiPager();
        viewpager.setOffscreenPageLimit(2);
        getActivity().setTitle("Profil");
        return view;
    }

    private void pripremiPager() {
        final FragmentManager fm = getChildFragmentManager();
        viewpager.setAdapter(new FragmentPagerAdapter(fm) {
            @Override
            public Fragment getItem(int position) {
                Fragment fragment = null;
                if(position==0)
                    fragment = new ProfilDetaljiTabFragment();
                if(position==1)
                    fragment = new KupljeneAplikacijeTabFragment();
                if(position==2)
                    fragment = new PreuzeteAplikacijeTabFragment();

                return  fragment;
            }

            @Override
            public int getCount() {
                return 3; //brojtabova
            }
        });
        viewpager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener()
        {
            @Override
            public void onPageSelected(int position)
            {
                 super.onPageSelected(position);
                    actionBar.setSelectedNavigationItem(position);

            }
        });
    }

    private void pripremiTabs() {
        actionBar = ((ActionBarActivity)getActivity()).getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowTitleEnabled(true);
        ActionBar.TabListener nesto = new ActionBar.TabListener()
        {
            @Override
            public void onTabSelected(Tab tab, FragmentTransaction fragmentTransaction)
            {
                int pozicija = tab.getPosition();
                viewpager.setCurrentItem(pozicija);
            }

            @Override
            public void onTabUnselected(Tab tab, FragmentTransaction fragmentTransaction)
            {

            }

            @Override
            public void onTabReselected(Tab tab, FragmentTransaction fragmentTransaction)
            {

            }
        };
        Tab tab1 = actionBar.newTab().setText("Profil").setTabListener(nesto);
        actionBar.addTab(tab1);

        Tab tab2 = actionBar.newTab().setText("Kupljene aplikacije").setTabListener(nesto);
        actionBar.addTab(tab2);

        Tab tab3 = actionBar.newTab().setText("Preuzete aplikacije").setTabListener(nesto);
        actionBar.addTab(tab3);
    }

    @Override
    public void onPause() {
        super.onPause();
        actionBar.removeAllTabs();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    }

    @Override
    public void onResume() {
        super.onResume();
        pripremiTabs();
    }
}
