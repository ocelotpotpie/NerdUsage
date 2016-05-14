package nu.nerd.nerdusage.json;


import nu.nerd.nerdusage.database.PlayerMeta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PlayerObj {

    public long min;
    public String last;
    public String name;
    public int days;

    public PlayerObj() {}

    public PlayerObj(PlayerMeta pm) {
        DateFormat df = new SimpleDateFormat("yyMMdd");
        this.min = pm.getTime();
        this.last = df.format(pm.getSeen());
        this.name = pm.getName();
        this.days = pm.getDays();
    }

}
