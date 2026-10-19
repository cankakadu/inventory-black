package com.example.inventoryblacklist;

import com.example.inventoryblacklist.gui.BlacklistScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class InventoryBlacklistClient implements ClientModInitializer {
    public static ConfigManager CONFIG;
    public static KeyBinding OPEN_MENU_KEY;

    @Override
    public void onInitializeClient() {
        CONFIG = ConfigManager.load();

        OPEN_MENU_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.inventoryblacklist.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_B,
                "category.inventoryblacklist.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_MENU_KEY.wasPressed()) {
                client.setScreen(new BlacklistScreen());
            }
        });
    }
}
