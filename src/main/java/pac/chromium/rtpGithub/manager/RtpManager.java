package pac.chromium.rtpGithub.manager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import pac.chromium.rtpGithub.RtpGithub;
import pac.chromium.rtpGithub.config.ConfigManager;
import pac.chromium.rtpGithub.config.LangManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class RtpManager {

    private final RtpGithub plugin;
    private final ConfigManager config;
    private final LangManager lang;

    private final Map<UUID, Long> cooldowns = new HashMap<>();
    private final Random random = new Random();

    public RtpManager(RtpGithub plugin, ConfigManager config, LangManager lang) {
        this.plugin = plugin;
        this.config = config;
        this.lang   = lang;
    }

    /**
     * Возвращает оставшееся время кулдауна в секундах, или 0 если кулдаун истёк.
     */
    public int getCooldownRemaining(Player player) {
        if (player.hasPermission("rtp.admin")) return 0;

        UUID uuid = player.getUniqueId();
        if (!cooldowns.containsKey(uuid)) return 0;

        long elapsed = (System.currentTimeMillis() - cooldowns.get(uuid)) / 1000;
        int remaining = (int) (config.getCooldown() - elapsed);
        return Math.max(remaining, 0);
    }

    /**
     * Телепортирует игрока в случайное безопасное место в обычном мире.
     */
    public boolean teleport(Player player) {
        int cooldown = getCooldownRemaining(player);
        if (cooldown > 0) {
            player.sendMessage(lang.prefix() + lang.get("teleport-cooldown", "seconds", cooldown));
            return false;
        }

        World world = plugin.getServer().getWorlds().stream()
                .filter(w -> w.getEnvironment() == World.Environment.NORMAL)
                .findFirst()
                .orElse(null);

        if (world == null) {
            player.sendMessage(lang.prefix() + lang.get("teleport-no-world"));
            return false;
        }

        Location destination = findSafeLocation(world);
        if (destination == null) {
            player.sendMessage(lang.prefix() + lang.get("teleport-no-safe"));
            return false;
        }

        player.teleport(destination);
        cooldowns.put(player.getUniqueId(), System.currentTimeMillis());

        player.sendMessage(lang.prefix() + lang.get("teleport-success",
                "x", destination.getBlockX(),
                "y", destination.getBlockY(),
                "z", destination.getBlockZ()));
        return true;
    }

    /**
     * Ищет безопасное место в мире в радиусе min..max от 0,0.
     */
    private Location findSafeLocation(World world) {
        for (int attempt = 0; attempt < config.getMaxAttempts(); attempt++) {
            int x = randomCoord();
            int z = randomCoord();

            world.loadChunk(x >> 4, z >> 4);

            int y = world.getHighestBlockYAt(x, z);
            Location loc = new Location(world, x + 0.5, y + 1, z + 0.5);

            if (isSafe(loc)) {
                return loc;
            }
        }
        return null;
    }

    /**
     * Генерирует случайную координату в диапазоне [-max, -min] или [min, max].
     */
    private int randomCoord() {
        int value = config.getMinRadius() + random.nextInt(config.getMaxRadius() - config.getMinRadius());
        return random.nextBoolean() ? value : -value;
    }

    /**
     * Проверяет безопасность локации.
     */
    private boolean isSafe(Location loc) {
        World world = loc.getWorld();
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        Material ground = world.getBlockAt(x, y - 1, z).getType();
        Material feet   = world.getBlockAt(x, y, z).getType();
        Material head   = world.getBlockAt(x, y + 1, z).getType();

        if (!ground.isSolid()) return false;
        if (ground == Material.LAVA || ground == Material.WATER) return false;
        if (feet != Material.AIR) return false;
        if (head != Material.AIR) return false;

        return true;
    }
}
