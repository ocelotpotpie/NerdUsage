package nu.nerd.nerdusage.json;


import nu.nerd.nerdusage.database.PlayerMeta;

import java.util.List;

public class Storage {

    public UsageStats usagestats;
    public long lastUpdate;
    public String[] online;

    public Storage() {}

    public Storage(List<PlayerMeta> playerMetaList) {
        this.lastUpdate = System.currentTimeMillis();
        this.usagestats = new UsageStats(playerMetaList);
    }

}
