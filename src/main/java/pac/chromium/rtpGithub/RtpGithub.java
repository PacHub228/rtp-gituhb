package pac.chromium.rtpGithub;

import org.bukkit.plugin.java.JavaPlugin;
import pac.chromium.rtpGithub.command.RtpCommand;
import pac.chromium.rtpGithub.config.ConfigManager;
import pac.chromium.rtpGithub.config.LangManager;
import pac.chromium.rtpGithub.gui.RtpGui;
import pac.chromium.rtpGithub.manager.RtpManager;

public final class RtpGithub extends JavaPlugin {

    private ConfigManager configManager;
    private LangManager langManager;
    private RtpManager rtpManager;
    private RtpGui rtpGui;

    @Override
    public void onEnable() {
        configManager = new ConfigManager(this);
        langManager   = new LangManager(this, configManager);
        rtpManager    = new RtpManager(this, configManager, langManager);
        rtpGui        = new RtpGui(this, rtpManager, langManager, configManager);

        RtpCommand rtpCommand = new RtpCommand(this, rtpManager, rtpGui, langManager, configManager);
        getCommand("rtp").setExecutor(rtpCommand);
        getCommand("rtp").setTabCompleter(rtpCommand);

        getServer().getPluginManager().registerEvents(rtpGui, this);

        getLogger().info("RTP plugin enabled! Language: " + configManager.getLanguage());
    }

    @Override
    public void onDisable() {
        getLogger().info("RTP plugin disabled!");
    }

    public ConfigManager getConfigManager() { return configManager; }
    public LangManager getLangManager()     { return langManager; }
    public RtpManager getRtpManager()       { return rtpManager; }
    public RtpGui getRtpGui()               { return rtpGui; }
}
