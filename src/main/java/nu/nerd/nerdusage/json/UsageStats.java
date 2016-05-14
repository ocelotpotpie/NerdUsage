package nu.nerd.nerdusage.json;


import nu.nerd.nerdusage.database.PlayerMeta;

import java.util.HashMap;
import java.util.List;

public class UsageStats {

    public HashMap<String, PlayerObj> players;

    public UsageStats() {}

    public UsageStats(List<PlayerMeta> playerMetaList) {
        this.players = new HashMap<String, PlayerObj>();
        for (PlayerMeta pm : playerMetaList) {
            players.put(pm.getUuid(), new PlayerObj(pm));
        }
    }

}
