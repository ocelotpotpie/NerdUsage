package nu.nerd.nerdusage.json;


import nu.nerd.nerdusage.database.PlayerMeta;

import java.util.HashMap;
import java.util.List;

public class UsageStats {

    public HashMap<String, PlayerObj> players;
    public long lastUpdate;
    public List<String> online;

    public UsageStats() {}

    public UsageStats(List<PlayerMeta> playerMetaList, List<String> online) {
        this.players = new HashMap<String, PlayerObj>();
        this.lastUpdate = System.currentTimeMillis();
        this.online = online;
        for (PlayerMeta pm : playerMetaList) {
            players.put(pm.getUuid(), new PlayerObj(pm));
        }
    }

}
