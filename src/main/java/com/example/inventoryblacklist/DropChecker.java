package com.example.inventoryblacklist;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

public class DropChecker {

    public static boolean isDropAllowed(ItemStack stack, int slotIndex) {
        if (stack.isEmpty()) return true;

        var config = InventoryBlacklistClient.CONFIG;

        if (config.slotBlacklistEnabled && config.blacklistedSlots.contains(slotIndex)) {
            notifyPlayer("Dieses Inventar-Slot ist geschützt!");
            return false;
        }

        if (config.itemBlacklistEnabled) {
            String itemId = Registries.ITEM.getId(stack.getItem()).toString();
            if (config.blacklistedItems.contains(itemId)) {
                notifyPlayer("Dieses Item ist auf der Blacklist!");
                return false;
            }
        }

        return true;
    }

    private static void notifyPlayer(String message) {
        var client = MinecraftClient.getInstance();
        if (client.player != null && InventoryBlacklistClient.CONFIG.showDropNotifications) {
            client.player.sendMessage(Text.literal("§c[Blacklist] §7" + message), true);
        }
    }
}
