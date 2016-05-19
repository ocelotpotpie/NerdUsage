package nu.nerd.nerdusage.database;


import com.avaje.ebean.Query;
import com.avaje.ebean.SqlUpdate;
import nu.nerd.nerdusage.NerdUsage;

import java.util.*;

public class PlayerMetaTable {


    private NerdUsage plugin;


    public PlayerMetaTable(NerdUsage plugin) {
        this.plugin = plugin;
    }


    public List<PlayerMeta> getAllPlayers() {
        try {
            return plugin.getDatabase().find(PlayerMeta.class).orderBy("time").findList();
        } catch (Exception ex) {
            ex.printStackTrace();
            return new ArrayList<PlayerMeta>();
        }
    }


    public PlayerMeta getPlayer(UUID uuid) {
        try {
            Query<PlayerMeta> query =  plugin.getDatabase().find(PlayerMeta.class).where().eq("uuid", uuid.toString()).query();
            if (query != null) {
                return query.findUnique();
            } else {
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }


    public void cleanUpPlayerMeta() {
        try {
            long minTime = 1000 * 60 * 10;
            String sql = "DELETE FROM players WHERE time < :min";
            SqlUpdate stmt = plugin.getDatabase().createSqlUpdate(sql);
            stmt.setParameter("min", minTime);
            int res = stmt.execute();
            if (res > 0) {
                plugin.getLogger().info(String.format("Removed %d players with less than 10 minutes of play time from the usage stats.", res));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void save(PlayerMeta meta) {
        plugin.getDatabase().save(meta);
    }


    public void update(PlayerMeta meta) {
        plugin.getDatabase().update(meta);
    }


    public void delete(PlayerMeta meta) {
        plugin.getDatabase().delete(meta);
    }


}
