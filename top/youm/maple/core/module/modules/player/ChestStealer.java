package top.youm.maple.core.module.modules.player;


import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.player.inventory.ContainerLocalMenu;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import top.youm.maple.Maple;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.events.Render2DEvent;
import top.youm.maple.common.events.WorldEvent;
import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.common.settings.impl.NumberSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.TimerUtil;
import top.youm.maple.utils.liquidbounce.Rotation;
import top.youm.maple.utils.liquidbounce.RotationUtils;
import top.youm.maple.utils.player.RotationUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ChestStealer extends Module {

    private final NumberSetting delay = new NumberSetting("Delay", 80, 300, 0, 10);
    private final BoolSetting aura = new BoolSetting("Aura", false);
    private final NumberSetting auraRange = new NumberSetting("Aura Range", 3, 6, 1, 1);
    public static BoolSetting stealingIndicator = new BoolSetting("Stealing Indicator", false);
    public static BoolSetting titleCheck = new BoolSetting("Title Check", true);
    public static BoolSetting freeLook = new BoolSetting("Free Look", true);
    private final BoolSetting reverse = new BoolSetting("Reverse", false);
    public static final BoolSetting silent = new BoolSetting("Silent", false);
    private final BoolSetting smart = new BoolSetting("Smart", false);

    private final List<BlockPos> openedChests = new ArrayList<>();
    private final List<Item> items = new ArrayList<>();
    private final TimerUtil timer = new TimerUtil();
    public static boolean stealing;
    private InvManager invManager;
    private boolean clear;

    public ChestStealer() {
        super("Chest Stealer", ModuleCategory.PLAYER, Keyboard.KEY_NONE);
        auraRange.addParent(aura, BoolSetting::getValue);
        stealingIndicator.addParent(silent, BoolSetting::getValue);
        this.addSetting(delay, aura, auraRange, stealingIndicator, titleCheck, freeLook, reverse, silent, smart);
    }

    @EventTarget
    public void onMotionEvent(MotionEvent e) {
        if (e.getState() == Event.State.PRE) {
            if (invManager == null) invManager = Maple.getInstance().getModuleManager().getModuleByClass(InvManager.class);
            if (aura.getValue()) {
                final int radius = auraRange.getValue().intValue();
                for (int x = -radius; x < radius; x++) {
                    for (int y = -radius; y < radius; y++) {
                        for (int z = -radius; z < radius; z++) {
                            final BlockPos pos = new BlockPos(mc.thePlayer.posX + x, mc.thePlayer.posY + y, mc.thePlayer.posZ + z);
                            if (pos.getBlock() == Blocks.chest && !openedChests.contains(pos)) {
                                if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, EnumFacing.UP, new Vec3(pos))) {
                                    mc.thePlayer.swingItem();
                                    final float[] rotations = RotationUtils.getRotations1(pos.getX(), pos.getY(), pos.getZ());
                                    e.setRotations(rotations[0], rotations[1]);
                                    RotationUtil.setRotations(rotations[0], rotations[1]);
                                    openedChests.add(pos);
                                }
                            }
                        }
                    }
                }
            }
            if (mc.thePlayer.openContainer instanceof ContainerChest) {
                ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
                IInventory chestInv = chest.getLowerChestInventory();
                if (titleCheck.getValue() && (!(chestInv instanceof ContainerLocalMenu) || !((ContainerLocalMenu) chestInv).realChest))
                    return;
                clear = true;

                List<Integer> slots = new ArrayList<>();
                for (int i = 0; i < chestInv.getSizeInventory(); i++) {
                    ItemStack is = chestInv.getStackInSlot(i);
                    if (is != null && (!smart.getValue() || items.contains(is.getItem()))){
                        slots.add(i);
                    }
                }

                if (reverse.getValue()) {
                    Collections.reverse(slots);
                }

                slots.forEach(s -> {
                    ItemStack is = chestInv.getStackInSlot(s);
                    Item item = is != null ? is.getItem() : null;
                    if (item != null && !items.contains(item) && (delay.getValue().longValue() == 0 || timer.hasTimeElapsed(delay.getValue().longValue(), true))) {
                        if (smart.getValue() && !(item instanceof ItemBlock)) {
                            items.add(is.getItem());
                        }
                        mc.playerController.windowClick(chest.windowId, s, 0, 1, mc.thePlayer);
                    }
                });

                if (slots.isEmpty() || isInventoryFull()) {
                    items.clear();
                    clear = false;
                    stealing = false;
                    mc.thePlayer.closeScreen();
                }
            } else if (clear) {
                items.clear();
                clear = false;
            }
        }
    }

    @EventTarget
    public void onRender2DEvent(Render2DEvent event) {
        if (stealingIndicator.getValue() && stealing) {
            ScaledResolution sr = new ScaledResolution(mc);
            FontLoaders.robotoB22.drawStringWithShadow("§lStealing...", sr.getScaledWidth() / 2.0F - FontLoaders.robotoB22.getStringWidth("§lStealing...") / 2.0F, sr.getScaledHeight() / 2.0F + 10,-1);
        }
    }

    @Override
    public void onEnable() {
        openedChests.clear();
        super.onEnable();
    }

    private boolean isInventoryFull() {
        for (int i = 9; i < 45; i++) {
            if (mc.thePlayer.inventoryContainer.getSlot(i).getStack() == null) {
                return false;
            }
        }
        return true;
    }

    public static boolean canSteal() {
        if (Maple.getInstance().getModuleManager().getModuleByClass(ChestStealer.class).isToggle() && mc.currentScreen instanceof GuiChest) {
            ContainerChest chest = (ContainerChest) mc.thePlayer.openContainer;
            IInventory chestInv = chest.getLowerChestInventory();
            return !titleCheck.getValue() || (chestInv instanceof ContainerLocalMenu && ((ContainerLocalMenu) chestInv).realChest);
        }
        return false;
    }
    @EventTarget
    public void onWorldEvent(WorldEvent event) {
        if (event instanceof WorldEvent.Load) {
            openedChests.clear();
        }
    }

}
