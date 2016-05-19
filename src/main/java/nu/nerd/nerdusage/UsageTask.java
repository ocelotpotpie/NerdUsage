package nu.nerd.nerdusage;


import nu.nerd.nerdusage.database.PlayerMeta;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class UsageTask extends BukkitRunnable {


    private NerdUsage plugin;
    private long lastRun;


    public UsageTask() {
        plugin = NerdUsage.instance;
        lastRun = System.currentTimeMillis();
        this.runTaskTimer(plugin, 1200L, 1200L);
    }


    public void run() {
        long start = System.currentTimeMillis();
        long timeIncrement = System.currentTimeMillis() - lastRun;
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            updatePlayer(player, timeIncrement);
        }
        cleanCache();
        lastRun = System.currentTimeMillis();
        if (plugin.getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info(String.format("Completed player iteration in %dms", start - System.currentTimeMillis()));
        }
    }


    private void updatePlayer(Player player, long timeIncrement)  {
        if (plugin.getPlayerMetaCache().containsKey(player.getUniqueId())) {
            PlayerMeta meta = plugin.getPlayerMetaCache().get(player.getUniqueId());
            if (player.getLocation().getPitch() != meta.getPitch() && player.getLocation().getYaw() != meta.getYaw()) {
                meta.setPitch(player.getLocation().getPitch());
                meta.setYaw(player.getLocation().getYaw());
                meta.setTime(meta.getTime() + timeIncrement);
                Calendar today = Calendar.getInstance();
                today.setTimeInMillis(System.currentTimeMillis());
                Calendar seen = Calendar.getInstance();
                seen.setTime(meta.getSeen());
                if (today.get(Calendar.YEAR) != seen.get(Calendar.YEAR) && today.get(Calendar.DAY_OF_YEAR) != seen.get(Calendar.DAY_OF_YEAR)) {
                    meta.setDays(meta.getDays() + 1);
                }
                meta.setSeen(today.getTime());
                meta.setName(player.getName());
                plugin.getPlayerUpdateQueue().add(meta);
            }
        } else {
            plugin.getPlayerLoadQueue().add(new PlayerAbstract(player));
        }
    }


    private void cleanCache() {
        Set<UUID> cached = new HashSet<UUID>();
        for (Player p : plugin.getServer().getOnlinePlayers()) {
            cached.add(p.getUniqueId());
        }
        for (UUID metaKey : plugin.getPlayerMetaCache().keySet()) {
            if (!cached.contains(metaKey)) plugin.getPlayerMetaCache().remove(metaKey);
        }
    }


}
