package com.example.inventoryblacklist.mixin;

import com.example.inventoryblacklist.DropChecker;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {
    @Shadow protected Slot focusedSlot;

    @Inject(method = "onMouseClick", at = @At("HEAD"), cancellable = true)
    private void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType, CallbackInfo ci) {
        if (slot != null && (actionType == SlotActionType.THROW)) {
            if (!DropChecker.isDropAllowed(slot.getStack(), slot.id)) {
                ci.cancel();
            }
        }
    }
}
