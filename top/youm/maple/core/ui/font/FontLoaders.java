/*
 * Decompiled with CFR 0_132.
 */
package top.youm.maple.core.ui.font;

import java.awt.Font;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class FontLoaders {
    public static CFontRenderer comfortaaR24 = new CFontRenderer(FontLoaders.getComfortaa(24,Roughness.Regular), true, true);

    public static CFontRenderer comfortaaB18 = new CFontRenderer(FontLoaders.getComfortaa(18,Roughness.Bold), true, true);
    public static CFontRenderer comfortaaB22 = new CFontRenderer(FontLoaders.getComfortaa(22,Roughness.Bold), true, true);
    public static CFontRenderer comfortaaB30 = new CFontRenderer(FontLoaders.getComfortaa(30,Roughness.Bold), true, true);

    public static CFontRenderer robotoB22 = new CFontRenderer(FontLoaders.getRoboto(22,Roughness.Bold), true, true);
    public static CFontRenderer robotoB24 = new CFontRenderer(FontLoaders.getRoboto(24,Roughness.Bold), true, true);
    public static CFontRenderer robotoB26 = new CFontRenderer(FontLoaders.getRoboto(26,Roughness.Bold), true, true);

    public static CFontRenderer robotoB32 = new CFontRenderer(FontLoaders.getRoboto(32,Roughness.Bold), true, true);

    public static CFontRenderer robotoB40 = new CFontRenderer(FontLoaders.getRoboto(40,Roughness.Bold), true, true);
    public static CFontRenderer robotoR18 = new CFontRenderer(FontLoaders.getRoboto(18,Roughness.Regular), true, true);

    public static CFontRenderer robotoR22 = new CFontRenderer(FontLoaders.getRoboto(22,Roughness.Regular), true, true);
    public static CFontRenderer robotoR24 = new CFontRenderer(FontLoaders.getRoboto(24,Roughness.Regular), true, true);

    public static CFontRenderer icon24 = new CFontRenderer(getFont("maple_icon",24),true,true);
    public static CFontRenderer icon28 = new CFontRenderer(getFont("maple_icon",28),true,true);
    public static CFontRenderer icon40 = new CFontRenderer(getFont("maple_icon",40),true,true);

    public static CFontRenderer noti = new CFontRenderer(getFont("product_sans_regular",18),true,true);
    public static CFontRenderer title = new CFontRenderer(getFont("product_sans_regular",22),true,true);
    public static CFontRenderer neverlose = new CFontRenderer(getFont("neverlose",40),true,true);
    public static CFontRenderer moduleList = new CFontRenderer(getFont("neverlose",22),true,true);

    public static CFontRenderer statistics = new CFontRenderer(getFont("neverlose",26),true,true);
    public static CFontRenderer commonIcon = new CFontRenderer(getFont("common",38),true,true);
    public static CFontRenderer tenacity = new CFontRenderer(getFont("tenacity-bold",40),true,true);
    public static CFontRenderer notiTitle = new CFontRenderer(getFont("tenacity-bold",22),true,true);

    public static Font getFont(String name ,int size) {
        Font font;
        try {
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Maple/font/"+name+".ttf")).getInputStream();
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

            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Maple/font/comfortaa/comfortaa_"+roughness.text+".ttf")).getInputStream();

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
            InputStream is = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("Maple/font/roboto/roboto-"+roughness.text+".ttf")).getInputStream();
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

