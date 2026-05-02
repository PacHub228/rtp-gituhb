package pac.chromium.rtpGithub.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pac.chromium.rtpGithub.RtpGithub;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class LangManager {

    private final RtpGithub plugin;
    private final ConfigManager configManager;
    private FileConfiguration lang;

    public LangManager(RtpGithub plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        load();
    }

    public void load() {
        String langCode = configManager.getLanguage();
        String fileName = "lang/" + langCode + ".yml";

        // Сохраняем файл из ресурсов если его нет
        File langFile = new File(plugin.getDataFolder(), fileName);
        if (!langFile.exists()) {
            plugin.saveResource(fileName, false);
        }

        // Загружаем файл с диска
        lang = YamlConfiguration.loadConfiguration(langFile);

        // Дополняем дефолтными значениями из jar (на случай если файл неполный)
        InputStream defaultStream = plugin.getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
                    new InputStreamReader(defaultStream, StandardCharsets.UTF_8)
            );
            lang.setDefaults(defaults);
        }
    }

    /**
     * Возвращает строку по ключу с заменой плейсхолдеров.
     * Плейсхолдеры передаются парами: "ключ", "значение", ...
     */
    public String get(String key, Object... placeholders) {
        String value = lang.getString(key, "§c[Missing: " + key + "]");
        value = value.replace("&", "§");

        for (int i = 0; i + 1 < placeholders.length; i += 2) {
            value = value.replace("{" + placeholders[i] + "}", String.valueOf(placeholders[i + 1]));
        }
        return value;
    }

    /**
     * Возвращает список строк по ключу с заменой плейсхолдеров.
     */
    public List<String> getList(String key, Object... placeholders) {
        List<String> list = lang.getStringList(key);
        return list.stream().map(line -> {
            line = line.replace("&", "§");
            for (int i = 0; i + 1 < placeholders.length; i += 2) {
                line = line.replace("{" + placeholders[i] + "}", String.valueOf(placeholders[i + 1]));
            }
            return line;
        }).collect(Collectors.toList());
    }

    /**
     * Возвращает префикс из языкового файла.
     */
    public String prefix() {
        return get("prefix");
    }
}
