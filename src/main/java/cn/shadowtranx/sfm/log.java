package cn.shadowtranx.sfm;


import java.text.SimpleDateFormat;
import java.util.Date;

public class log {
    public static void msg(String smsg)
    {


        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
        Date date = new Date(System.currentTimeMillis());
        String ot = "[SFM $" + formatter.format(date) + "]" + smsg;
        System.out.println(ot);
    }
}

