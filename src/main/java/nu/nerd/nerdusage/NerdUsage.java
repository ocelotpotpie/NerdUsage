package nu.nerd.nerdusage;

import nu.nerd.nerdusage.database.PlayerMeta;
import nu.nerd.nerdusage.database.PlayerMetaTable;
import org.bukkit.plugin.java.JavaPlugin;

import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;


public class NerdUsage extends JavaPlugin {


    public static NerdUsage instance;
    private PlayerMetaTable playerMetaTable;
    private ConcurrentLinkedQueue<QueuedPlayer> playerUpdateQueue;


    public void onEnable() {

        NerdUsage.instance = this;
        saveDefaultConfig();

        setupDatabase();
        this.playerMetaTable = new PlayerMetaTable(this);
        this.playerUpdateQueue = new ConcurrentLinkedQueue<QueuedPlayer>();

        new UsageTask();
        new UpdateThread();

    }


    public void onDisable() {
        getServer().getScheduler().cancelTasks(this); //ensure queue finishes and database unlocks
        getPlayerMetaTable().cleanUpPlayerMeta(); //remove players with less than 10 minutes of play time
    }


    private void setupDatabase() {
        try {
            getDatabase().find(PlayerMeta.class).findRowCount();
        } catch (PersistenceException ex) {
            getLogger().info("Initializing database.");
            installDDL();
        }
    }


    @Override
    public ArrayList<Class<?>> getDatabaseClasses() {
        ArrayList<Class<?>> list = new ArrayList<Class<?>>();
        list.add(PlayerMeta.class);
        return list;
    }


    public PlayerMetaTable getPlayerMetaTable() {
        return playerMetaTable;
    }


    public ConcurrentLinkedQueue<QueuedPlayer> getPlayerUpdateQueue() {
        return playerUpdateQueue;
    }


}
