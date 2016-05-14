package nu.nerd.nerdusage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import nu.nerd.nerdusage.database.PlayerMeta;
import nu.nerd.nerdusage.json.Storage;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.*;


public class UpdateThread extends BukkitRunnable {


    private NerdUsage plugin;


    public UpdateThread() {
        plugin = NerdUsage.instance;
        this.runTaskAsynchronously(plugin);
    }


    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            processQueue();
        }
        processQueue();
    }


    private void processQueue() {

        if (plugin.getPlayerUpdateQueue().size() < 1) return;

        //todo: implement online players

        long startTime = System.currentTimeMillis();
        HashMap<UUID, PlayerMeta> playerMetaMap = plugin.getPlayerMetaTable().getMetaForQueuedPlayers();

        plugin.getDatabase().beginTransaction();
        try {
            Iterator<QueuedPlayer> iterator = plugin.getPlayerUpdateQueue().iterator();
            while (iterator.hasNext()) {
                QueuedPlayer queued = iterator.next();
                if (!playerMetaMap.containsKey(queued.getUuid())) {
                    PlayerMeta meta = new PlayerMeta(queued.getUuid().toString(), queued.getName());
                    meta.setTime(queued.getTimeIncrement());
                    plugin.getPlayerMetaTable().save(meta);
                } else {
                    PlayerMeta meta = playerMetaMap.get(queued.getUuid());
                    if (queued.getPitch() == meta.getPitch() && queued.getYaw() == meta.getYaw()) {
                        iterator.remove();
                        continue;
                    }
                    plugin.getPlayerMetaTable().updatePlayerMeta(meta, queued, startTime);
                }
                iterator.remove();
            }
            plugin.getDatabase().commitTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            plugin.getDatabase().endTransaction();
            writeJson();
            long runTime = System.currentTimeMillis() - startTime;
            if (plugin.getConfig().getBoolean("debug", false)) {
                plugin.getLogger().info(String.format("Updated usage in %dms", runTime));
            }
        }

    }


    private void writeJson() {

        if (plugin.getPlayerUpdateQueue().size() > 0) return;
        if (!plugin.getConfig().getBoolean("write_json_file", true)) return;

        List<PlayerMeta> playerMetas = plugin.getPlayerMetaTable().getAllPlayers();
        Storage storage = new Storage(playerMetas);
        HashMap<String, Object> rootElement = new HashMap<String, Object>();
        rootElement.put("storage", storage);

        try {
            File path = new File(plugin.getDataFolder(), "usage.json");
            Writer writer = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(rootElement, writer);
            writer.close();
        } catch (Exception ex) {
            if (plugin.getConfig().getBoolean("debug", false)) {
                ex.printStackTrace();
            } else {
                plugin.getLogger().warning("Error writing JSON: " + ex.getMessage());
            }
        }

    }


}
