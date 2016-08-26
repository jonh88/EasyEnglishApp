package com.jonh.easyenglish.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by jonh on 17/08/16.
 */
public class FechaManager {

    private Date c;

    public FechaManager(){
        c = new Date();
    }

    public int getNow(){
        SimpleDateFormat formateador = new SimpleDateFormat("yyyyMMdd");
        String f = formateador.format(this.c);
        return Integer.parseInt(f);
    }


}
