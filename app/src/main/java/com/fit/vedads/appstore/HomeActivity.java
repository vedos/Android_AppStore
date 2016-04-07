package com.fit.vedads.appstore;

import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.fit.vedads.appstore.SectionAplikacije.PretragaAplikacijaFragment;
import com.fit.vedads.appstore.SectionKategorije.KategorijeFragment;
import com.fit.vedads.appstore.SectionProfil.ProfilFragment;

public class HomeActivity extends ActionBarActivity {

    private DrawerLayout navDrawerLayout;
    private ListView navDrawerListView;
    private ActionBarDrawerToggle navDrawerToggle;

    private CharSequence navDraweTitle;
    private CharSequence appTitle;
    private String[] navDrawerItemTitles;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        this.navDrawerItemTitles = this.getResources().getStringArray(R.array.nav_drawer_strings);
        this.navDraweTitle = this.getTitle();
        this.appTitle = this.getTitle();

        this.navDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navDrawerListView = (ListView) findViewById(R.id.list_navBar);

/*        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,navDrawerItemTitles);
        navDrawerListView.setAdapter(adapter);*/
        BindNavbar();
        navDrawerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DisplayOnScreen(position);
                navDrawerListView.setItemChecked(position,true);
                navDrawerLayout.closeDrawer(navDrawerListView);
            }
        });

        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        this.getSupportActionBar().setHomeButtonEnabled(true);
        //getSupportActionBar().setIcon(R.drawable.icon_app);
        this.navDrawerToggle = new ActionBarDrawerToggle(this,this.navDrawerLayout,R.drawable.ic_navmenu_button,R.string.app_name,R.string.app_name){
            @Override
            public void onDrawerOpened(View drawerView) {
               //getSupportActionBar().setTitle(navDraweTitle);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                //getSupportActionBar().setTitle(appTitle);
                invalidateOptionsMenu();
            }
        };
        this.navDrawerLayout.setDrawerListener(this.navDrawerToggle);
        if(savedInstanceState==null)
        {
            DisplayOnScreen(1);//pokaži pretragu na početnoj
            getSupportActionBar().setTitle("Home");
        }

    }

    private void DisplayOnScreen(int position) {
        Fragment fragment = null;
        if (position == 0)
            fragment = new ProfilFragment();
            //startActivity(new Intent(HomeActivity.this, ProfilActivity.class));
        if (position == 1)
            fragment = new PretragaAplikacijaFragment();
        if (position == 2)
            fragment = new KategorijeFragment();

        if(position == 3)
        {
            SharedPreferences preferences =getSharedPreferences("DatotekaZaSharedPreferences",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear();
            editor.commit();
            finish();
            startActivity(new Intent(HomeActivity.this,MainActivity.class));
        }

        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().addToBackStack(null).replace(R.id.frame_container, fragment).commit();
        } else
            Log.e("App Store", "Unable to create fragment");
    }

    private void BindNavbar() {
        navDrawerListView.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return navDrawerItemTitles.length;
            }

            @Override
            public Object getItem(int position) {
                return navDrawerItemTitles[position];
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView==null)
                {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(HomeActivity.this.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.navbar_item,parent,false);
                }
                TextView txtNavBar = (TextView) convertView.findViewById(R.id.navB_txtV);
                txtNavBar.setText(navDrawerItemTitles[position]);
                return convertView;
            }
        });
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        this.navDrawerToggle.syncState();
    }

    @Override
    public void setTitle(CharSequence title) {
        super.setTitle(title);
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(this.navDrawerToggle.onOptionsItemSelected(item))
        {
            return  true;
        }
        switch (item.getItemId())
        {
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }
}
