package nu.nerd.nerdusage;


import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerAbstract {

    private UUID uuid;
    private String name;

    public PlayerAbstract(Player player) {
        uuid = player.getUniqueId();
        name = player.getName();
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

}
