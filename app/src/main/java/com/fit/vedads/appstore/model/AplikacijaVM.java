package com.fit.vedads.appstore.model;

import com.fit.vedads.appstore.helper.Config;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
/**
 * Created by vedad on 24.8.2015.
 */
public class AplikacijaVM implements Serializable{
    public static String url = Config.url + "Aplikacija/GetAll";
    public class AplikacijaInfo implements Serializable{
        public int ID;
        public String naziv;
        public Date datumDodavanja;
        public String velicinaFajle;
        public String slikaThumbnail;
        public String fajl;
        public int brojPreuzimanja;
        public String opis;
        public float cijena;
        public int KategorijaID;
        public String KategorijaNaziv;
    }
    public List<AplikacijaInfo> aplikacijeList;
}
