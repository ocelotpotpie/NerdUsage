package nu.nerd.nerdusage.database;

import com.avaje.ebean.validation.NotNull;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.UUID;


@Entity()
@Table(name = "players")
public class PlayerMeta {


    @Id
    private Integer id;

    @NotNull
    private String uuid;
    private String name;
    private long time;
    private int days;
    private Date seen;
    private float pitch;
    private float yaw;


    public PlayerMeta() {}

    public PlayerMeta(String uuid, String name) {
        setUuid(uuid);
        setName(name);
        setTime(0);
        setDays(1);
        setSeen(new Date());
        setPitch(0f);
        setYaw(0f);
    }


    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Player getPlayer() {
        return Bukkit.getServer().getPlayer(UUID.fromString(uuid));
    }

    public void setPlayer(Player player) {
        uuid = player.getUniqueId().toString();
        name = player.getName();
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public Date getSeen() {
        return seen;
    }

    public void setSeen(Date seen) {
        this.seen = seen;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setFacing(Location loc) {
        setPitch(loc.getPitch());
        setYaw(loc.getYaw());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
