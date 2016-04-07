package com.fit.vedads.appstore.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by vedad on 16.1.2016.
 */
public class KategorijeVM implements Serializable {
    public class KategorijaInfo implements Serializable
    {
        public int ID;
        public String Naziv;
    }
    public List<KategorijaInfo> listKategorije;
}
