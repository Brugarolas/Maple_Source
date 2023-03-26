package me.youm.rocchi.core.ui;

import me.youm.rocchi.core.module.ModuleCategory;
import me.youm.rocchi.utils.render.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.util.Locale;

public class TabUI {

    public Minecraft mc = Minecraft.getMinecraft();
    public ModuleCategory currentCategory = ModuleCategory.COMBAT;

    public void render(){
        GlStateManager.pushMatrix();
        RenderUtil.drawRect(5,20,55,78,new Color(0,0,0,120));
        int yOffset = 0;
        for (ModuleCategory category : ModuleCategory.values()) {
            this.mc.fontRendererObj.drawStringWithShadow(StringUtils.capitalize(category.name().toLowerCase(Locale.ROOT)),10,22 + yOffset,Theme.fontColor.getRGB());
            yOffset += this.mc.fontRendererObj.FONT_HEIGHT + 2;
        }
        GlStateManager.popMatrix();
    }

}
