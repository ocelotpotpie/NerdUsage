package nu.nerd.nerdusage.json;


import nu.nerd.nerdusage.database.PlayerMeta;

import java.util.List;

public class Storage {

    public UsageStats usagestats;
    public long lastUpdate;
    public List<String> online;

    public Storage() {}

    public Storage(List<PlayerMeta> playerMetaList, List<String> online) {
        this.lastUpdate = System.currentTimeMillis();
        this.usagestats = new UsageStats(playerMetaList);
        this.online = online;
    }

}
