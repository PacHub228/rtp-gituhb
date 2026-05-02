package pac.chromium.rtpGithub.config;

import org.bukkit.Location;
import org.bukkit.World;
import pac.chromium.rtpGithub.RtpGithub;

public class ConfigManager {

    private final RtpGithub plugin;

    private String language;
    private int maxRadius;
    private int minRadius;
    private int cooldown;
    private int maxAttempts;

    // Spawn
    private String spawnWorld;
    private double spawnX;
    private double spawnY;
    private double spawnZ;
    private float spawnYaw;
    private float spawnPitch;

    public ConfigManager(RtpGithub plugin) {
        this.plugin = plugin;
        load();
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();

        language    = plugin.getConfig().getString("language", "ru").toLowerCase();
        maxRadius   = plugin.getConfig().getInt("rtp.max-radius", 5000);
        minRadius   = plugin.getConfig().getInt("rtp.min-radius", 100);
        cooldown    = plugin.getConfig().getInt("rtp.cooldown", 15);
        maxAttempts = plugin.getConfig().getInt("rtp.max-attempts", 50);

        spawnWorld = plugin.getConfig().getString("spawn.world", "world");
        spawnX     = plugin.getConfig().getDouble("spawn.x", 0.5);
        spawnY     = plugin.getConfig().getDouble("spawn.y", 64.0);
        spawnZ     = plugin.getConfig().getDouble("spawn.z", 0.5);
        spawnYaw   = (float) plugin.getConfig().getDouble("spawn.yaw", 0.0);
        spawnPitch = (float) plugin.getConfig().getDouble("spawn.pitch", 0.0);

        // Валидация языка
        if (!language.matches("ru|en|pl|uk")) {
            plugin.getLogger().warning("Unknown language '" + language + "', falling back to 'ru'.");
            language = "ru";
        }
    }

    /**
     * Сохраняет координаты спавна в config.yml.
     */
    public void saveSpawn(Location loc) {
        plugin.getConfig().set("spawn.world", loc.getWorld().getName());
        plugin.getConfig().set("spawn.x", loc.getX());
        plugin.getConfig().set("spawn.y", loc.getY());
        plugin.getConfig().set("spawn.z", loc.getZ());
        plugin.getConfig().set("spawn.yaw", (double) loc.getYaw());
        plugin.getConfig().set("spawn.pitch", (double) loc.getPitch());
        plugin.saveConfig();

        // Обновляем кэш
        spawnWorld = loc.getWorld().getName();
        spawnX     = loc.getX();
        spawnY     = loc.getY();
        spawnZ     = loc.getZ();
        spawnYaw   = loc.getYaw();
        spawnPitch = loc.getPitch();
    }

    /**
     * Возвращает Location спавна или null если мир не найден.
     */
    public Location getSpawnLocation() {
        World world = plugin.getServer().getWorld(spawnWorld);
        if (world == null) return null;
        return new Location(world, spawnX, spawnY, spawnZ, spawnYaw, spawnPitch);
    }

    public String getLanguage()    { return language; }
    public int getMaxRadius()      { return maxRadius; }
    public int getMinRadius()      { return minRadius; }
    public int getCooldown()       { return cooldown; }
    public int getMaxAttempts()    { return maxAttempts; }
}
