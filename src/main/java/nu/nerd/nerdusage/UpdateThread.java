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
            processLoadQueue();
            processUpdateQueue();
            writeJson();
        }
        processLoadQueue();
        processUpdateQueue();
        writeJson();
    }


    private void processLoadQueue() {
        if (plugin.getPlayerLoadQueue().size() < 1) return;
        plugin.getDatabase().beginTransaction();
        try {
            Iterator<PlayerAbstract> iterator = plugin.getPlayerLoadQueue().iterator();
            while (iterator.hasNext()) {
                PlayerAbstract player = iterator.next();
                UUID uuid = player.getUuid();
                PlayerMeta meta = plugin.getPlayerMetaTable().getPlayer(uuid);
                if (meta != null) {
                    plugin.getPlayerMetaCache().put(uuid, meta);
                } else {
                    PlayerMeta newMeta = new PlayerMeta(player.getUuid().toString(), player.getName());
                    plugin.getPlayerMetaTable().save(newMeta);
                    plugin.getPlayerMetaCache().put(uuid, newMeta);
                }
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().info(String.format("Loading player %s", player.getName()));
                }
                iterator.remove();
            }
            plugin.getDatabase().commitTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            plugin.getDatabase().endTransaction();
        }
    }


    private void processUpdateQueue() {
        if (plugin.getPlayerUpdateQueue().size() < 1) return;
        plugin.getDatabase().beginTransaction();
        try {
            Iterator<PlayerMeta> iterator = plugin.getPlayerUpdateQueue().iterator();
            while (iterator.hasNext()) {
                PlayerMeta meta = iterator.next();
                plugin.getPlayerMetaTable().update(meta);
                if (plugin.getConfig().getBoolean("debug", false)) {
                    plugin.getLogger().info(String.format("Updating player %s", meta.getName()));
                }
                iterator.remove();
            }
            plugin.getDatabase().commitTransaction();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            plugin.getDatabase().endTransaction();
        }
    }


    private void writeJson() {

        if (!plugin.getConfig().getBoolean("write_json_file", true)) return;
        if (!plugin.getJsonSemaphore().get()) return;

        long start = System.currentTimeMillis();
        List<String> online = new ArrayList<String>();
        for (PlayerMeta pm : plugin.getPlayerMetaCache().values()) {
            online.add(pm.getName());
        }

        List<PlayerMeta> playerMetas = plugin.getPlayerMetaTable().getAllPlayers();
        Storage storage = new Storage(playerMetas, online);
        HashMap<String, Object> rootElement = new HashMap<String, Object>();
        rootElement.put("storage", storage);

        try {
            Gson gson;
            File path = new File(plugin.getDataFolder(), "usage.json");
            Writer writer = new OutputStreamWriter(new FileOutputStream(path), "UTF-8");
            if (plugin.getConfig().getBoolean("debug", false)) {
                gson = new GsonBuilder().setPrettyPrinting().create();
            } else {
                gson = new GsonBuilder().create();
            }
            gson.toJson(rootElement, writer);
            writer.close();
        } catch (Exception ex) {
            if (plugin.getConfig().getBoolean("debug", false)) {
                ex.printStackTrace();
            } else {
                plugin.getLogger().warning("Error writing JSON: " + ex.getMessage());
            }
        }

        plugin.getJsonSemaphore().set(false);

        if (plugin.getConfig().getBoolean("debug", false)) {
            plugin.getLogger().info(String.format("Wrote usage.json in %dms", System.currentTimeMillis() - start));
        }

    }


}
