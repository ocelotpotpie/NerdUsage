package nu.nerd.nerdusage.database;


import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlUpdate;
import nu.nerd.nerdusage.NerdUsage;
import nu.nerd.nerdusage.QueuedPlayer;

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


    public HashMap<UUID, PlayerMeta> getMetaForQueuedPlayers() {
        Set<String> ids = new HashSet<String>();
        for (QueuedPlayer p : plugin.getPlayerUpdateQueue()) {
            ids.add(p.getUuid().toString());
        }
        Query<PlayerMeta> query = plugin.getDatabase().find(PlayerMeta.class).where().in("uuid", ids).query();
        if (query != null) {
            HashMap<UUID, PlayerMeta> map = new HashMap<UUID, PlayerMeta>();
            for (PlayerMeta pm : query.findList()) {
                map.put(UUID.fromString(pm.getUuid()), pm);
            }
            return map;
        } else {
            return new HashMap<UUID, PlayerMeta>();
        }
    }


    public void updatePlayerMeta(PlayerMeta meta, QueuedPlayer queued, long time) {
        meta.setPitch(queued.getPitch());
        meta.setYaw(queued.getYaw());
        meta.setTime(meta.getTime() + queued.getTimeIncrement());
        Calendar today = Calendar.getInstance();
        today.setTimeInMillis(time);
        Calendar seen = Calendar.getInstance();
        seen.setTime(meta.getSeen());
        if (today.get(Calendar.YEAR) != seen.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) != seen.get(Calendar.DAY_OF_YEAR)) {
            meta.setDays(meta.getDays() + 1);
        }
        meta.setSeen(today.getTime());
        meta.setName(queued.getName());
        update(meta);
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
