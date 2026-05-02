package pac.chromium.rtpGithub.command;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import pac.chromium.rtpGithub.RtpGithub;
import pac.chromium.rtpGithub.config.ConfigManager;
import pac.chromium.rtpGithub.config.LangManager;
import pac.chromium.rtpGithub.gui.RtpGui;
import pac.chromium.rtpGithub.manager.RtpManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RtpCommand implements CommandExecutor, TabCompleter {

    private final RtpGithub plugin;
    private final RtpManager rtpManager;
    private final RtpGui rtpGui;
    private final LangManager lang;
    private final ConfigManager config;

    public RtpCommand(RtpGithub plugin, RtpManager rtpManager, RtpGui rtpGui,
                      LangManager lang, ConfigManager config) {
        this.plugin     = plugin;
        this.rtpManager = rtpManager;
        this.rtpGui     = rtpGui;
        this.lang       = lang;
        this.config     = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(lang.get("players-only"));
            return true;
        }

        // /rtp help
        if (args.length == 1 && args[0].equalsIgnoreCase("help")) {
            sendHelp(player);
            return true;
        }

        // /rtp setspawn — только для rtp.admin
        if (args.length == 1 && args[0].equalsIgnoreCase("setspawn")) {
            if (!player.hasPermission("rtp.admin")) {
                player.sendMessage(lang.prefix() + lang.get("no-permission", "permission", "rtp.admin"));
                return true;
            }
            config.saveSpawn(player.getLocation());
            player.sendMessage(lang.prefix() + lang.get("spawn-set"));
            return true;
        }

        // /rtp spawn
        if (args.length == 1 && args[0].equalsIgnoreCase("spawn")) {
            if (!player.hasPermission("rtp.user") && !player.hasPermission("rtp.admin")) {
                player.sendMessage(lang.prefix() + lang.get("no-permission", "permission", "rtp.user"));
                return true;
            }
            Location spawnLoc = config.getSpawnLocation();
            if (spawnLoc == null) {
                player.sendMessage(lang.prefix() + lang.get("teleport-no-world"));
                return true;
            }
            player.teleport(spawnLoc);
            player.sendMessage(lang.prefix() + lang.get("spawn-teleport"));
            return true;
        }

        // /rtp — открыть меню
        if (args.length == 0) {
            if (!player.hasPermission("rtp.user") && !player.hasPermission("rtp.admin")) {
                player.sendMessage(lang.prefix() + lang.get("no-permission", "permission", "rtp.user"));
                return true;
            }
            rtpGui.openMenu(player);
            return true;
        }

        player.sendMessage(lang.prefix() + lang.get("unknown-command"));
        return true;
    }

    private void sendHelp(Player player) {
        player.sendMessage(lang.get("help-header"));
        player.sendMessage(lang.get("help-title"));
        player.sendMessage(lang.get("help-separator"));
        player.sendMessage(lang.get("help-rtp"));
        player.sendMessage(lang.get("help-rtp-help"));
        player.sendMessage(lang.get("help-separator"));
        player.sendMessage(lang.get("help-perms-header"));
        player.sendMessage(lang.get("help-perm-user", "radius", config.getMaxRadius()));
        player.sendMessage(lang.get("help-perm-admin"));
        player.sendMessage(lang.get("help-header"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("rtp.admin")) {
                return Arrays.asList("help", "spawn", "setspawn");
            }
            return Arrays.asList("help", "spawn");
        }
        return Collections.emptyList();
    }
}
