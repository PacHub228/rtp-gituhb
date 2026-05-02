package pac.chromium.rtpGithub.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pac.chromium.rtpGithub.RtpGithub;
import pac.chromium.rtpGithub.config.ConfigManager;
import pac.chromium.rtpGithub.config.LangManager;
import pac.chromium.rtpGithub.manager.RtpManager;

import java.util.List;

public class RtpGui implements Listener {

    private static final int GUI_SIZE = 27;

    // Слоты
    private static final int SLOT_TELEPORT = 11;
    private static final int SLOT_INFO     = 13;
    private static final int SLOT_HELP     = 15;

    private final RtpGithub plugin;
    private final RtpManager rtpManager;
    private final LangManager lang;
    private final ConfigManager config;

    public RtpGui(RtpGithub plugin, RtpManager rtpManager, LangManager lang, ConfigManager config) {
        this.plugin     = plugin;
        this.rtpManager = rtpManager;
        this.lang       = lang;
        this.config     = config;
    }

    /**
     * Открывает GUI меню для игрока.
     */
    public void openMenu(Player player) {
        String title = lang.get("gui-title");
        Inventory inv = Bukkit.createInventory(null, GUI_SIZE, title);

        // Фон
        ItemStack filler = createItem(Material.GRAY_STAINED_GLASS_PANE,
                lang.get("gui-filler-name"), null);
        for (int i = 0; i < GUI_SIZE; i++) {
            inv.setItem(i, filler);
        }

        int cooldown = rtpManager.getCooldownRemaining(player);

        // Кнопка телепортации
        ItemStack tpButton;
        if (cooldown > 0) {
            tpButton = createItem(
                    Material.RED_BED,
                    lang.get("gui-tp-button-name-cooldown"),
                    lang.getList("gui-tp-button-lore-cooldown", "seconds", cooldown)
            );
        } else {
            tpButton = createItem(
                    Material.COMPASS,
                    lang.get("gui-tp-button-name-ready"),
                    lang.getList("gui-tp-button-lore-ready",
                            "radius", config.getMaxRadius(),
                            "cooldown", config.getCooldown())
            );
        }
        inv.setItem(SLOT_TELEPORT, tpButton);

        // Информация
        String status = cooldown > 0
                ? lang.get("gui-info-status-cooldown", "seconds", cooldown)
                : lang.get("gui-info-status-ready");

        ItemStack infoItem = createItem(
                Material.CLOCK,
                lang.get("gui-info-name"),
                lang.getList("gui-info-lore",
                        "cooldown", config.getCooldown(),
                        "radius", config.getMaxRadius(),
                        "status", status)
        );
        inv.setItem(SLOT_INFO, infoItem);

        // Помощь
        ItemStack helpItem = createItem(
                Material.BOOK,
                lang.get("gui-help-name"),
                lang.getList("gui-help-lore")
        );
        inv.setItem(SLOT_HELP, helpItem);

        player.openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = event.getView().getTitle();
        if (title == null || !title.equals(lang.get("gui-title"))) return;

        event.setCancelled(true);

        ItemStack clicked = event.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.GRAY_STAINED_GLASS_PANE) return;

        if (event.getSlot() == SLOT_TELEPORT) {
            player.closeInventory();
            Bukkit.getScheduler().runTask(plugin, () -> rtpManager.teleport(player));
        }
    }

    private ItemStack createItem(Material material, String name, List<String> lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(name);
            if (lore != null) meta.setLore(lore);
            item.setItemMeta(meta);
        }
        return item;
    }
}
