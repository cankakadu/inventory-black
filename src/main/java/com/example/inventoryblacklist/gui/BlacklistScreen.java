package com.example.inventoryblacklist.gui;

import com.example.inventoryblacklist.InventoryBlacklistClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class BlacklistScreen extends Screen {
    private TextFieldWidget searchBox;
    private final List<Item> filteredItems = new ArrayList<>();
    private int page = 0;
    private static final int ITEMS_PER_PAGE = 28;

    public BlacklistScreen() {
        super(Text.literal("Inventory Blacklist"));
    }

    @Override
    protected void init() {
        super.init();

        int centerX = this.width / 2;
        int centerY = this.height / 2;

        this.searchBox = new TextFieldWidget(this.textRenderer, centerX - 100, centerY - 80, 200, 20, Text.literal("Suche..."));
        this.searchBox.setChangedListener(s -> updateSearch());
        this.addDrawableChild(this.searchBox);

        this.addDrawableChild(ButtonWidget.builder(Text.literal("Fertig"), button -> {
            InventoryBlacklistClient.CONFIG.save();
            this.close();
        }).dimensions(centerX - 75, centerY + 90, 150, 20).build());

        updateSearch();
    }

    private void updateSearch() {
        filteredItems.clear();
        String query = searchBox != null ? searchBox.getText().toLowerCase() : "";
        for (Item item : Registries.ITEM) {
            ItemStack stack = new ItemStack(item);
            if (stack.getName().getString().toLowerCase().contains(query)) {
                filteredItems.add(item);
            }
        }
        page = 0;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fillGradient(0, 0, this.width, this.height, 0xAA101010, 0xEE181818);
        context.drawCenteredTextWithShadow(this.textRenderer, this.title, this.width / 2, 20, 0xFFFFFF);

        super.render(context, mouseX, mouseY, delta);

        int startX = this.width / 2 - 120;
        int startY = this.height / 2 - 50;
        int index = 0;

        for (int i = page * ITEMS_PER_PAGE; i < Math.min(filteredItems.size(), (page + 1) * ITEMS_PER_PAGE); i++) {
            Item item = filteredItems.get(i);
            int x = startX + (index % 7) * 36;
            int y = startY + (index / 7) * 36;

            ItemStack stack = new ItemStack(item);
            String itemId = Registries.ITEM.getId(item).toString();
            boolean isBlacklisted = InventoryBlacklistClient.CONFIG.blacklistedItems.contains(itemId);

            int bgColor = isBlacklisted ? 0xFF2E7D32 : 0xFF333333;
            context.fill(x - 2, y - 2, x + 18, y + 18, bgColor);

            context.drawItem(stack, x, y);

            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
                context.drawTooltip(this.textRenderer, stack, mouseX, mouseY);
            }

            index++;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        int startX = this.width / 2 - 120;
        int startY = this.height / 2 - 50;
        int index = 0;

        for (int i = page * ITEMS_PER_PAGE; i < Math.min(filteredItems.size(), (page + 1) * ITEMS_PER_PAGE); i++) {
            Item item = filteredItems.get(i);
            int x = startX + (index % 7) * 36;
            int y = startY + (index / 7) * 36;

            if (mouseX >= x - 2 && mouseX <= x + 18 && mouseY >= y - 2 && mouseY <= y + 18) {
                String itemId = Registries.ITEM.getId(item).toString();
                if (InventoryBlacklistClient.CONFIG.blacklistedItems.contains(itemId)) {
                    InventoryBlacklistClient.CONFIG.blacklistedItems.remove(itemId);
                } else {
                    InventoryBlacklistClient.CONFIG.blacklistedItems.add(itemId);
                }
                InventoryBlacklistClient.CONFIG.save();
                return true;
            }
            index++;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
