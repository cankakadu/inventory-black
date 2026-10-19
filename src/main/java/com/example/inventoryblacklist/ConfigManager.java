package com.example.inventoryblacklist;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ConfigManager {
    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve("inventoryblacklist.json").toFile();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean itemBlacklistEnabled = true;
    public boolean slotBlacklistEnabled = true;
    public boolean visualHighlights = true;
    public boolean showDropNotifications = true;
    
    public Set<String> blacklistedItems = new HashSet<>();
    public Set<Integer> blacklistedSlots = new HashSet<>();

    public static ConfigManager load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                ConfigManager config = GSON.fromJson(reader, ConfigManager.class);
                if (config != null) return config;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ConfigManager config = new ConfigManager();
        config.save();
        return config;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
