/*
 * Decompiled with CFR 0_132.
 */
package top.youm.rocchi.core.ui.font;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBarrier;
import net.minecraft.block.BlockGlass;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStainedGlass;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.util.BlockPos;

public class FontUtils {
    public static boolean fuck = true;
    private static Minecraft mc = Minecraft.getMinecraft();

    public static boolean isContainerEmpty(Container container) {
        int i = 0;
        int slotAmount = container.inventorySlots.size() == 90 ? 54 : 27;
        while (i < slotAmount) {
            if (container.getSlot(i).getHasStack()) {
                return false;
            }
            ++i;
        }
        return true;
    }

    public static Minecraft getMinecraft() {
        return mc;
    }

    public static boolean canBlock() {
        if (mc == null) {
            mc = Minecraft.getMinecraft();
        }
        if (FontUtils.mc.thePlayer.getHeldItem() == null) {
            return false;
        }
        if (FontUtils.mc.thePlayer.isBlocking() || FontUtils.mc.thePlayer.isUsingItem() && FontUtils.mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            return true;
        }
        if (FontUtils.mc.thePlayer.getHeldItem().getItem() instanceof ItemSword && Minecraft.getMinecraft().gameSettings.keyBindUseItem.isPressed()) {
            return true;
        }
        return false;
    }

    public static String getMD5(String input) {
        StringBuilder res = new StringBuilder();
        try {
            byte[] md5;
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(input.getBytes());
            byte[] arrby = md5 = algorithm.digest();
            int n = arrby.length;
            int n2 = 0;
            while (n2 < n) {
                byte aMd5 = arrby[n2];
                String tmp = Integer.toHexString(255 & aMd5);
                if (tmp.length() == 1) {
                    res.append("0").append(tmp);
                } else {
                    res.append(tmp);
                }
                ++n2;
            }
        }
        catch (NoSuchAlgorithmException algorithm) {
            // empty catch block
        }
        return res.toString();
    }

    public static void breakAnticheats() {
        FontUtils.mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(FontUtils.mc.thePlayer.posX + FontUtils.mc.thePlayer.motionX, FontUtils.mc.thePlayer.posY - 110.0, FontUtils.mc.thePlayer.posZ + FontUtils.mc.thePlayer.motionZ, true));
    }

    public static int add(int number, int add, int max) {
        return number + add > max ? max : number + add;
    }

    public static int remove(int number, int remove, int min) {
        return number - remove < min ? min : number - remove;
    }

    public static int check(int number) {
        return number <= 0 ? 1 : (number > 255 ? 255 : number);
    }

    public static double getDist() {
        double distance = 0.0;
        double i = FontUtils.mc.thePlayer.posY;
        while (i > 0.0) {
            if (i < 0.0) break;
            Block block = FontUtils.mc.theWorld.getBlockState(new BlockPos(FontUtils.mc.thePlayer.posX, i, FontUtils.mc.thePlayer.posZ)).getBlock();
            if (block.getMaterial() != Material.air && block.isCollidable() && (block.isFullBlock() || block instanceof BlockSlab || block instanceof BlockBarrier || block instanceof BlockStairs || block instanceof BlockGlass || block instanceof BlockStainedGlass)) {
                if (block instanceof BlockSlab) {
                    i -= 0.5;
                }
                distance = i;
                break;
            }
            i -= 0.1;
        }
        return FontUtils.mc.thePlayer.posY - distance;
    }
}

