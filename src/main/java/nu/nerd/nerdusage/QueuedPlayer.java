package nu.nerd.nerdusage;


import org.bukkit.entity.Player;

import java.util.UUID;

public class QueuedPlayer {


    private UUID uuid;
    private String name;
    private float pitch;
    private float yaw;
    private long timeIncrement;


    public QueuedPlayer(Player player, long timeIncrement) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
        this.pitch = player.getLocation().getPitch();
        this.yaw = player.getLocation().getYaw();
        this.timeIncrement = timeIncrement;
    }


    public UUID getUuid() {
        return uuid;
    }


    public String getName() {
        return name;
    }


    public float getPitch() {
        return pitch;
    }


    public float getYaw() {
        return yaw;
    }


    public long getTimeIncrement() {
        return timeIncrement;
    }


}
