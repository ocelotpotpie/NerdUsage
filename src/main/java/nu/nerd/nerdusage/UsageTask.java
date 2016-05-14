package nu.nerd.nerdusage;


import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class UsageTask extends BukkitRunnable {


    private NerdUsage plugin;
    private long lastRun;


    public UsageTask() {
        plugin = NerdUsage.instance;
        lastRun = System.currentTimeMillis();
        this.runTaskTimer(plugin, 1200L, 1200L);
    }


    public void run() {
        long timeIncrement = System.currentTimeMillis() - lastRun;
        for (Player player : plugin.getServer().getOnlinePlayers()) {
            plugin.getPlayerUpdateQueue().add(new QueuedPlayer(player, timeIncrement));
        }
        lastRun = System.currentTimeMillis();
    }


}
