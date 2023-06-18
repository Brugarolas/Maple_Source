/*
 * Decompiled with CFR 0_132.
 */
package top.youm.rocchi.core.ui.font;

import java.awt.Font;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class FontLoaders {
    public static CFontRenderer comfortaaR16 = new CFontRenderer(FontLoaders.getComfortaa(16,Roughness.Regular), true, true);
    public static CFontRenderer comfortaaR18 = new CFontRenderer(FontLoaders.getComfortaa(18,Roughness.Regular), true, true);
    public static CFontRenderer comfortaaR20 = new CFontRenderer(FontLoaders.getComfortaa(20,Roughness.Regular), true, true);

    public static CFontRenderer comfortaaR22 = new CFontRenderer(FontLoaders.getComfortaa(22,Roughness.Regular), true, true);
    public static CFontRenderer comfortaaR24 = new CFontRenderer(FontLoaders.getComfortaa(24,Roughness.Regular), true, true);

    public static CFontRenderer comfortaaR30 = new CFontRenderer(FontLoaders.getComfortaa(30,Roughness.Regular), true, true);

    public static CFontRenderer comfortaaT16 = new CFontRenderer(FontLoaders.getComfortaa(16,Roughness.THIN), true, true);
    public static CFontRenderer comfortaaT18 = new CFontRenderer(FontLoaders.getComfortaa(18,Roughness.THIN), true, true);
    public static CFontRenderer comfortaaB16 = new CFontRenderer(FontLoaders.getComfortaa(16,Roughness.Bold), true, true);
    public static CFontRenderer comfortaaB18 = new CFontRenderer(FontLoaders.getComfortaa(18,Roughness.Bold), true, true);
    public static CFontRenderer comfortaaB20 = new CFontRenderer(FontLoaders.getComfortaa(20,Roughness.Bold), true, true);
    public static CFontRenderer comfortaaB22 = new CFontRenderer(FontLoaders.getComfortaa(22,Roughness.Bold), true, true);

    public static CFontRenderer comfortaaB30 = new CFontRenderer(FontLoaders.getComfortaa(30,Roughness.Bold), true, true);

    public static CFontRenderer robotoB22 = new CFontRenderer(FontLoaders.getRoboto(22,Roughness.Bold), true, true);
    public static CFontRenderer robotoB24 = new CFontRenderer(FontLoaders.getRoboto(24,Roughness.Bold), true, true);
    public static CFontRenderer robotoB26 = new CFontRenderer(FontLoaders.getRoboto(26,Roughness.Bold), true, true);

    public static CFontRenderer robotoB32 = new CFontRenderer(FontLoaders.getRoboto(32,Roughness.Bold), true, true);

    public static CFontRenderer robotoB40 = new CFontRenderer(FontLoaders.getRoboto(40,Roughness.Bold), true, true);
    public static CFontRenderer robotoR18 = new CFontRenderer(FontLoaders.getRoboto(18,Roughness.Regular), true, true);

    public static CFontRenderer robotoR22 = new CFontRenderer(FontLoaders.getRoboto(22,Roughness.Regular), true, true);
    public static CFontRenderer robotoR34 = new CFontRenderer(FontLoaders.getRoboto(34,Roughness.Regular), true, true);
    public static CFontRenderer icon20 = new CFontRenderer(getFont("iconfont",20),true,true);
    public static CFontRenderer icon24 = new CFontRenderer(getFont("iconfont",24),true,true);
    public static CFontRenderer icon28 = new CFontRenderer(getFont("iconfont",28),true,true);
    public static CFontRenderer icon32 = new CFontRenderer(getFont("iconfont",32),true,true);
    public static GlyphPageFontRenderer chinese22;
    public static GlyphPageFontRenderer chinese18;
    public static Font getFont(String name ,int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Rocchi/font/"+name+".ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return font;
    }
    private static Font getComfortaa(int size,Roughness roughness) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Rocchi/font/comfortaa/comfortaa_"+roughness.text+".ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
        }
        return font;
    }
    private static Font getRoboto(int size,Roughness roughness) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Rocchi/font/roboto/roboto-"+roughness.text+".ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(Font.PLAIN, size);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", Font.PLAIN, size);
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

