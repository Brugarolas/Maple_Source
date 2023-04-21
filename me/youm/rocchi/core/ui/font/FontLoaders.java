/*
 * Decompiled with CFR 0_132.
 */
package me.youm.rocchi.core.ui.font;

import java.awt.Font;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class FontLoaders {
    public static CFontRenderer comfortaaR16 = new CFontRenderer(FontLoaders.getComfortaa(16,Roughness.Regular), true, true);
    public static CFontRenderer comfortaaR18 = new CFontRenderer(FontLoaders.getComfortaa(18,Roughness.Regular), true, true);
    public static CFontRenderer comfortaaR22 = new CFontRenderer(FontLoaders.getComfortaa(22,Roughness.Regular), true, true);

    public static CFontRenderer comfortaaR30 = new CFontRenderer(FontLoaders.getComfortaa(30,Roughness.Regular), true, true);

    public static CFontRenderer comfortaaT16 = new CFontRenderer(FontLoaders.getComfortaa(16,Roughness.THIN), true, true);
    public static CFontRenderer comfortaaT18 = new CFontRenderer(FontLoaders.getComfortaa(18,Roughness.THIN), true, true);
    public static CFontRenderer comfortaaB16 = new CFontRenderer(FontLoaders.getComfortaa(16,Roughness.Bold), true, true);
    public static CFontRenderer comfortaaB18 = new CFontRenderer(FontLoaders.getComfortaa(18,Roughness.Bold), true, true);
    public static CFontRenderer comfortaaB20 = new CFontRenderer(FontLoaders.getComfortaa(20,Roughness.Bold), true, true);
    public static CFontRenderer comfortaaB22 = new CFontRenderer(FontLoaders.getComfortaa(22,Roughness.Bold), true, true);

    public static CFontRenderer comfortaaB30 = new CFontRenderer(FontLoaders.getComfortaa(30,Roughness.Bold), true, true);

    private static Font getComfortaa(int size,Roughness roughness) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Rocchi/font/comfortaa/comfortaa_"+roughness.text+".ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    public enum Roughness{
        THIN("thin"),
        Regular("regular"),
        Bold("bold");

        private final String text;
        Roughness(String text){
            this.text = text;
        }

        public String getText() {
            return text;
        }
    }
}

