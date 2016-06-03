package nu.nerd.nerdusage.json;


import nu.nerd.nerdusage.database.PlayerMeta;

import java.util.List;

public class Storage {

    public UsageStats usagestats;

    public Storage() {}

    public Storage(List<PlayerMeta> playerMetaList, List<String> online) {
        this.usagestats = new UsageStats(playerMetaList, online);
    }

}
