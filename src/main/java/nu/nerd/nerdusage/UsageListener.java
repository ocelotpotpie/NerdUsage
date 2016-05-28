package nu.nerd.nerdusage;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;


public class UsageListener implements Listener {


    private NerdUsage plugin;


    public UsageListener() {
        this.plugin = NerdUsage.instance;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getPlayerLoadQueue().add(new PlayerAbstract(event.getPlayer()));
    }


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getPlayerMetaCache().remove(event.getPlayer().getUniqueId());
    }


    @EventHandler
    public void onKick(PlayerKickEvent event) {
        plugin.getPlayerMetaCache().remove(event.getPlayer().getUniqueId());
    }


}
