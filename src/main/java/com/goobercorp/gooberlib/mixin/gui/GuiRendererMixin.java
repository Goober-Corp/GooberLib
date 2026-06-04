package com.goobercorp.gooberlib.mixin.gui;

import com.goobercorp.gooberlib.screen.GooberScreen;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderPass;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.render.GuiRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiRenderer.class)
public class GuiRendererMixin {
    @WrapOperation(method = "enableScissor", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderPass;enableScissor(IIII)V"))
    void GooberLib$weHaveScissorAtHome(RenderPass instance, int i, int j, int k, int l, Operation<Void> original) {
        if (Minecraft.getInstance().screen instanceof GooberScreen screen) {
            ScreenRectangle badbadbad = screen.badbadbad;
            original.call(instance, badbadbad.position().x(), badbadbad.position().y(), badbadbad.width(), badbadbad.height());
        }
        original.call(instance, i, j, k, l);
    }
}
