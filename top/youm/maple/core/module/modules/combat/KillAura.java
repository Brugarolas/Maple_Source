package top.youm.maple.core.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import de.florianmichael.viamcp.fixes.AttackOrder;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.*;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.potion.Potion;
import net.minecraft.util.*;
import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import top.youm.maple.Maple;
import top.youm.maple.common.events.*;
import top.youm.maple.common.settings.impl.BoolSetting;
import top.youm.maple.common.settings.impl.ModeSetting;
import top.youm.maple.common.settings.impl.NumberSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.module.modules.world.SafeScaffold;
import top.youm.maple.core.module.modules.visual.HUD;
import top.youm.maple.core.module.modules.world.Teams;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.AnimationUtils;
import top.youm.maple.utils.SlotComponent;
import top.youm.maple.utils.TimerUtil;
import top.youm.maple.utils.liquidbounce.Rotation;
import top.youm.maple.utils.liquidbounce.RotationUtils;
import top.youm.maple.utils.math.MathUtil;
import top.youm.maple.utils.network.PacketUtil;
import top.youm.maple.utils.player.RotationUtil;
import top.youm.maple.utils.render.RenderUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static net.optifine.CustomColors.random;
import static org.lwjgl.opengl.GL11.glColor4f;

/**
 * @author YouM
 */
public class KillAura extends Module {

    private float yaw = 0;
    private final BoolSetting interactAutoBlock = new BoolSetting("interactAutoBlock",true);
    private final NumberSetting reach = new NumberSetting("Reach", 3.3, 6, 1, 0.01);
    private final NumberSetting minCps = new NumberSetting("Min CPS", 6, 20, 1, 1);
    private final NumberSetting maxCps = new NumberSetting("Max CPS", 11, 20, 1, 1);
    private final NumberSetting minSpeed = new NumberSetting("Min Speed", 0.5, 2.0, 0, 0.1);
    private final NumberSetting maxSpeed = new NumberSetting("Max Speed", 0.6, 2.0, 0, 0.1);
    private final NumberSetting maxRotateSpeed = new NumberSetting("Max Rotate Speed", 180, 180, 0, 1);
    private final NumberSetting minRotateSpeed = new NumberSetting("Min Rotate Speed", 180, 180, 0, 1);
    private final ModeSetting autoBlockMode = new ModeSetting("AutoBlock Mode", "Vulcan", "Vulcan","Watchdog", "Interaction", "AAC", "Verus","New NCP","Old Intave");
    private final BoolSetting autoBlock = new BoolSetting("AutoBlock", false);
    private final BoolSetting footCircle = new BoolSetting("foot circle", true);
    private final NumberSetting footWidth = new NumberSetting("foot width", 2, 5, 1, 1);

    private final BoolSetting scanCircle = new BoolSetting("scan circle", true);
    private final NumberSetting scanWidth = new NumberSetting("scan width", 2, 5, 1, 1);

    BoolSetting players = new BoolSetting("Players", true);
    BoolSetting mobs = new BoolSetting("Mobs", false);
    BoolSetting animals = new BoolSetting("Animals", false);
    BoolSetting villager = new BoolSetting("Villager", false);
    BoolSetting invisible = new BoolSetting("Invisible", false);
    BoolSetting ticks = new BoolSetting("Ticks", true);
    BoolSetting nameTags = new BoolSetting("NameTags", true);
    BoolSetting packet = new BoolSetting("Packet", true);
    ModeSetting rotateMode = new ModeSetting("RotateMode", "Dynamic", "Dynamic", "Smooth", "Grim", "Vanilla");
    BoolSetting safeRotation = new BoolSetting("Safe Rotation", false);
    BoolSetting followRotation = new BoolSetting("Follow", false);

    private final BoolSetting autoDisable = new BoolSetting("Auto Disable", true);
    public static List<EntityLivingBase> targets = new ArrayList<>();
    public static EntityLivingBase target;
    public static boolean blocking;
    public static boolean attacking;
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil rotateTimer = new TimerUtil();
    private int hitTicks = 0;


    public KillAura() {
        super("Kill Aura", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        footWidth.addParent(footCircle, BoolSetting::getValue);
        scanWidth.addParent(scanCircle, BoolSetting::getValue);
        autoBlockMode.addParent(autoBlock, BoolSetting::getValue);
        maxSpeed.addParent(rotateMode, rotateMode -> rotateMode.getValue().equals("Smooth"));
        minSpeed.addParent(rotateMode, rotateMode -> rotateMode.getValue().equals("Smooth"));
        maxRotateSpeed.addParent(rotateMode, rotateMode -> rotateMode.getValue().equals("Grim"));
        minRotateSpeed.addParent(rotateMode, rotateMode -> rotateMode.getValue().equals("Grim"));
        this.addSetting(reach, minCps, maxCps, footCircle, footWidth, scanCircle, scanWidth, autoDisable,autoBlock, autoBlockMode, players,villager, invisible, mobs, animals, rotateMode, safeRotation, followRotation, maxSpeed, minSpeed, maxRotateSpeed, minRotateSpeed, ticks, packet, nameTags);
    }

    boolean direction;
    float offsetY = 0;
    private AnimationUtils animator = new AnimationUtils();


    private float size;
    private float headSize = 100;
    private float health = 120;
    private float healthFade = 0;
    private float armor = 120;
    @EventTarget
    public void onRender2D(Render2DEvent event){
        if(!targets.isEmpty() && target != null){
            if(target.hurtTime > 4){
                headSize -= 0.5f;
            }else{
                headSize += 0.5f;
            }
            if(headSize <= 90) headSize = 90;
            if(headSize >= 100) headSize = 100;

            size = animator.animate(100,size,0.1f);
            health = animator.animate((target.getHealth() / target.getMaxHealth()) * 120,health,0.1f);
            armor = animator.animate((target.getTotalArmorValue() / 20.0f) * 120,armor,0.1f);
        }else{
            health = animator.animate(0,health,0.1f);
            armor = animator.animate(0,armor,0.1f);
            if(health <= 0) {
                size = animator.animate(0,size,0.1f);
            }



        }

        ScaledResolution sr = new ScaledResolution(mc);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        int x = width - (width / 3);
        int y = height - (height / 3);
        RenderUtil.scale(x + (200 / 2.0f),y + (50 / 2.0f),size / 100,()->{
            RenderUtil.drawRect(x,y,180,50,new Color(0,0,0,120));
            ResourceLocation locationSkin;
            if(target != null) {

                if (mc.getNetHandler().getPlayerInfo(target.getUniqueID()) != null) {
                    locationSkin = mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getLocationSkin();
                } else {
                    locationSkin = DefaultPlayerSkin.getDefaultSkinLegacy();
                }

                RenderUtil.scale(x + 26, y + 26,headSize / 100.0f,()->{
                    RenderUtil.drawHead(locationSkin, x + 8, y + 8, 36, 36);
                    if(target.hurtTime > 4) {
                        RenderUtil.drawRect(x + 8, y + 8, 36, 36,new Color(255,0,0,120));
                    }
                });

                RenderUtil.drawRect(x + 50, y + 30,120, 5, new Color(60, 60, 60).getRGB());
                RenderUtil.drawRect(x + 50, y + 40,120, 5, new Color(60, 60, 60).getRGB());

                RenderUtil.drawRect(x + 50, y + 30, health, 5, new Color(81, 160, 81));
                RenderUtil.drawRect(x + 50, y + 30 + 10, armor,  5, new Color(88, 136, 171));
                FontLoaders.aovel32.drawStringWithShadow(target.getName(), x + 50, y + 8, -1);

            }
        });

    }

    @EventTarget
    public void onTick(TickEvent event) {
        this.setSuffixes(this.minCps.getValue().intValue() + "-" + this.maxCps.getValue().intValue() + " " + reach.getValue().floatValue());

        if (mc.thePlayer != null) {
            if ((mc.thePlayer.getHealth() <= 0.0f || mc.thePlayer.isDead) && this.autoDisable.getValue()) {
                this.setToggle(false);
            }
            if(this.autoBlock.getValue() && this.autoBlockMode.getValue().equals("Vulcan") && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword){
                if(targets.isEmpty()){
                    vulcanBlock(false);
                }else{
                    vulcanBlock(true);
                }

            }
            if (direction) {
                offsetY += 0.04;
                if (2 - offsetY < 0.02) {
                    direction = false;
                }
            } else {
                offsetY -= 0.04;
                if (offsetY < 0.02) {
                    direction = true;
                }
            }
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event) {

        if (footCircle.getValue()) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(
                    mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ
            );
            GlStateManager.enableBlend();
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GlStateManager.disableDepth();
            GlStateManager.disableTexture2D();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glLineWidth(footWidth.getValue().floatValue());
            GlStateManager.rotate(90F, 1F, 0F, 0F);
            RenderUtil.color(HUD.getHUDThemeColor().getRGB());
            GlStateManager.glBegin(GL11.GL_LINE_STRIP);

            for (int i = 0; i <= 360; i += 2) {
                GL11.glVertex2f(
                        (float) Math.cos(i * Math.PI / 180.0) * reach.getValue().floatValue(),
                        (float) Math.sin(i * Math.PI / 180.0) * reach.getValue().floatValue()
                );
            }
            GlStateManager.glEnd();

            GlStateManager.enableTexture2D();
            GlStateManager.enableDepth();
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
            glColor4f(1f, 1f, 1f, 1f);
            GL11.glLineWidth(2);
        }
        if (targets.size() == 0) {
            return;
        }
        if (targets.get(0).isDead || targets.get(0).getHealth() <= 0 || Maple.getInstance().getModuleManager().getModuleByClass(SafeScaffold.class).isToggle() || mc.thePlayer.isDead || mc.thePlayer.isSpectator())
            return;
        if (target != null && this.scanCircle.getValue()) {
            drawCircle(event);
            drawShadow(event);
        }
    }

    public void drawCircle(Render3DEvent event) {

        GlStateManager.pushMatrix();
        GlStateManager.enableBlend();
        GlStateManager.disableDepth();
        GlStateManager.disableTexture2D();
        GL11.glShadeModel(7425);
        GL11.glLineWidth(scanWidth.getValue().floatValue());
        double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * (double) event.getTicks() - mc.getRenderManager().viewerPosX;
        double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * (double) event.getTicks() - mc.getRenderManager().viewerPosY + offsetY;
        double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * (double) event.getTicks() - mc.getRenderManager().viewerPosZ;
        GlStateManager.glBegin(GL11.GL_LINE_STRIP);
        for (int i = 0; i <= 360; i++) {
            double c1 = i * Math.PI * 2 / 360;
            RenderUtil.color(HUD.getHUDThemeColor().getRGB());
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y, z + 0.5 * Math.sin(c1) * 1.2);
        }

        GlStateManager.glEnd();
        GlStateManager.enableTexture2D();
        GL11.glShadeModel(7424);
        GlStateManager.enableDepth();
        GlStateManager.disableBlend();

        GlStateManager.popMatrix();
        glColor4f(1f, 1f, 1f, 1f);
        GL11.glLineWidth(2);
    }

    public void drawShadow(Render3DEvent event) {
        GlStateManager.pushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(7425);
        double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * (double) event.getTicks() - mc.getRenderManager().viewerPosX;
        double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * (double) event.getTicks() - mc.getRenderManager().viewerPosY + offsetY;
        double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * (double) event.getTicks() - mc.getRenderManager().viewerPosZ;
        GL11.glBegin(GL11.GL_QUAD_STRIP);
        for (int i = 0; i <= 360; i++) {
            double c1 = i * Math.PI * 2 / 360;
            double c2 = (i + 1) * Math.PI * 2 / 360;
            GL11.glColor4f(HUD.red.getValue().floatValue() / 255f, HUD.green.getValue().floatValue() / 255f, HUD.blue.getValue().floatValue() / 255f, 0.8f);
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y, z + 0.5 * Math.sin(c1) * 1.2);
            GL11.glVertex3d(x + 0.5 * Math.cos(c2) * 1.2, y, z + 0.5 * Math.sin(c2) * 1.2);
            GL11.glColor4f(HUD.red.getValue().floatValue() / 255f, HUD.green.getValue().floatValue() / 255f, HUD.blue.getValue().floatValue() / 255f, 0.4f);
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y + (direction ? -0.3 : 0.3), z + 0.5 * Math.sin(c1) * 1.2);
            GL11.glVertex3d(x + 0.5 * Math.cos(c2) * 1.2, y + (direction ? -0.3 : 0.3), z + 0.5 * Math.sin(c2) * 1.2);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(7424);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GlStateManager.popMatrix();
        glColor4f(1f, 1f, 1f, 1f);
        GL11.glLineWidth(2);
    }

    private float[] randomRotation;

    @EventTarget
    public void onMotion(MotionEvent event) {
        sortTargets();
        if (targets.isEmpty()) {
            attacking = false;
            blocking = false;
            this.hitTicks = 0;
            return;
        }
        if(Maple.getInstance().getModuleManager().getModuleByClass(SafeScaffold.class).isToggle()){
            Maple.getInstance().getModuleManager().getModuleByClass(SafeScaffold.class).setToggle(false);
        }
        if(removeTarget()) return;

        target = targets.get(0);
        if (event.getState() == Event.State.PRE) rotation(event);

        if (!RotationUtil.isMouseOver(event.getYaw(), event.getPitch(), target, reach.getValue().floatValue()))
            return;

        if (autoBlock.getValue() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            autoBlock(event);
        }

        if (event.getState() == Event.State.PRE) {
            attack();
        }
        removeTarget();
        if (targets.isEmpty()) {
            attacking = false;
            blocking = false;
            this.hitTicks = 0;
        }

    }
    public boolean removeTarget(){
        if (targets.get(0).getHealth() <= 0 || targets.get(0).isDead || mc.thePlayer.isDead || mc.thePlayer.isSpectator()){
            targets.remove(0);
            return true;
        }
        return false;
    }
    public void attack(){
        attacking = true;
        if (timer.hasTimeElapsed((1000 / MathUtil.getRandomInRange(minCps.getValue().intValue(), maxCps.getValue().intValue())), true)) {
            AttackOrder.sendFixedAttack(mc.thePlayer, target);
            if (mc.thePlayer.fallDistance > 0 && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null) {
                mc.thePlayer.onCriticalHit(target);
            }
            this.hitTicks = 0;

        }
    }

    private void rotation(MotionEvent event){
        float[] smoothRotations = getRotationsToEnt(target);
        switch (rotateMode.getValue()) {
            case "Dynamic":
                if (rotateTimer.hasTimeElapsed(80,true)) {
                    smoothRotations[0] += MathUtil.getRandomInRange(-7, 7);
                    smoothRotations[1] += MathUtil.getRandomInRange(-5, 10);
                    randomRotation = smoothRotations;
                } else {
                    smoothRotations = randomRotation;
                }
                break;
            case "Grim":
                Rotation targetRotation = getTargetRotation(target);
                smoothRotations[0] = targetRotation.getYaw();
                smoothRotations[1] = targetRotation.getPitch();
                break;
            case "Vanilla":
                smoothRotations = getRotationsToEnt(target);
                break;
            default:
                smoothRotations = RotationUtil.getSmoothRotations(target, maxSpeed.getValue().floatValue(), minSpeed.getValue().floatValue());
        }

        event.setYaw(smoothRotations[0]);
        event.setPitch(smoothRotations[1]);
        this.yaw = event.getYaw();
        if (followRotation.getValue()) {
            mc.thePlayer.rotationYaw = smoothRotations[0];
            mc.thePlayer.rotationPitch = smoothRotations[1];
        }
        RotationUtil.setRotations(smoothRotations);
    }
    private void autoBlock(MotionEvent event){

        mc.playerController.syncCurrentPlayItem();
        hitTicks++;
        blocking = true;
        switch (autoBlockMode.getValue()) {
            case "Watchdog":
                if (event.getState() == Event.State.PRE && blocking && mc.thePlayer.ticksExisted % 4 == 0) {
                    PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    blocking = false;
                }

                if (event.getState() == Event.State.POST && !blocking) {
                    PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(BlockPos.ORIGIN, 255, mc.thePlayer.getHeldItem(), 255, 255, 255));
                    blocking = true;
                }
                break;
            case "Interaction":
                if (event.getState() == Event.State.POST) {
                    if (blocking) {
                        for (Entity current : targets) {
                            mc.playerController.interactWithEntitySendPacket(mc.thePlayer, current);
                        }
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                    } else {
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(
                                new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                        blocking = false;
                    }
                }
                break;
            case "AAC":
                if (event.getState() == Event.State.POST) {
                    if (blocking) {
                        mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
                    } else {
                        PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(
                                new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                        blocking = false;
                    }
                }
            case "New NCP":
                if (event.getState() == Event.State.PRE) {
                    if (blocking) {
                        PacketUtil.sendPacket(new C09PacketHeldItemChange(SlotComponent.getItemIndex() % 8 + 1));
                        PacketUtil.sendPacket(new C09PacketHeldItemChange(SlotComponent.getItemIndex()));
                        blocking = false;
                    }
                }
                if (event.getState() == Event.State.POST) {
                    this.block(true, false);
                }
            case "Old Intave":
                if (event.getState() == Event.State.PRE) {
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem % 8 + 1));
                    mc.getNetHandler().addToSendQueue(new C09PacketHeldItemChange(mc.thePlayer.inventory.currentItem));
                    blocking = false;
                }
                if(event.getState() == Event.State.POST){
                    startBlocking(target,interactAutoBlock.getValue());
                }
                break;
            default:
                if (event.getState() == Event.State.PRE) {
                    if (blocking) {
                        PacketUtil.sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.
                                Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                    }
                    PacketUtil.sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getHeldItem()));
                    blocking = true;
                }
                break;
        }
        PacketUtil.sendPacket(new C0APacketAnimation());

    }

    @EventTarget
    public void onJumpFixEvent(JumpFixEvent event) {
        if (!targets.isEmpty()) {
            event.setYaw(yaw);
        }
    }

    @EventTarget
    public void onPlayerMoveUpdateEvent(MoveInputEvent event) {
        if (!targets.isEmpty()) {
            event.setYaw(yaw);
        }
    }

    @EventTarget
    public void onPacketReceive(PacketReceiveEvent event) {
        if (packet.getValue() && target != null) {
            if (event.getPacket() instanceof S18PacketEntityTeleport) {
                S18PacketEntityTeleport s18PacketEntityTeleport = (S18PacketEntityTeleport) event.getPacket();
                if (target.getEntityId() == s18PacketEntityTeleport.getEntityId()) {
                    if (s18PacketEntityTeleport.getX() == mc.thePlayer.posX && s18PacketEntityTeleport.getY() == mc.thePlayer.posY && s18PacketEntityTeleport.getZ() == mc.thePlayer.posZ) {
                        targets.remove(target);
                    }
                }
            }
        }
    }
    @Override
    public void onDisable() {
        this.vulcanBlock(false);
        targets.clear();
        blocking = false;
        attacking = false;
    }

    private void block(final boolean check, final boolean interact) {
        if (!blocking || !check) {
            if (interact && target != null && mc.objectMouseOver.entityHit == target) {
                mc.playerController.interactWithEntitySendPacket(mc.thePlayer, target);
            }

            PacketUtil.send(new C08PacketPlayerBlockPlacement(SlotComponent.getItemStack()));
            blocking = true;
        }
    }
    public void sortTargets() {
        targets.clear();
        for (Entity entity : mc.theWorld.getLoadedEntityList()) {
            if (entity instanceof EntityLivingBase) {
                EntityLivingBase entLiving = (EntityLivingBase) entity;
                if (mc.thePlayer.getDistanceToEntity(entLiving) < reach.getValue().floatValue() && entLiving != mc.thePlayer && !entLiving.isDead && isValid(entLiving)) {
                    targets.add(entLiving);
                }
            }
        }
        targets.sort(Comparator.comparingDouble(mc.thePlayer::getDistanceToEntity));
    }

    public boolean isValid(EntityLivingBase entLiving) {
        AntiBot antiBot = Maple.getInstance().getModuleManager().getModuleByClass(AntiBot.class);
        if (entLiving == null) {
            return false;
        }
        if (Teams.isInTeam(entLiving)) return false;
        if (entLiving instanceof EntityPlayer && players.getValue() && !entLiving.isInvisible() && !antiBot.isNPC(entLiving)) {
            return true;
        }

        if (entLiving instanceof EntityPlayer && invisible.getValue() && entLiving.isInvisible()) {
            return true;
        }
        if (mobs.getValue() && (entLiving instanceof EntityMob || entLiving instanceof EntitySlime)) {
            return true;
        }
        if (villager.getValue() && entLiving instanceof EntityVillager) {
            return true;
        }
        if (ticks.getValue() && entLiving.ticksExisted < 100) {
            return false;
        }
        if (entLiving.getDisplayName() != null && entLiving instanceof EntityPlayer) {
            if (nameTags.getValue() && (entLiving.getDisplayName().getFormattedText().contains("§c") || entLiving.getDisplayName().getFormattedText().contains("§4"))) {
                return false;
            }
        }
        return entLiving instanceof EntityAnimal && animals.getValue();
    }

    private float[] getRotationsToEnt(Entity ent) {
        //target and player x distance
        final double diffX = ent.posX - mc.thePlayer.posX;
        //target and player y distance
        final double diffY = (ent.posY + ent.height) - (mc.thePlayer.posY + mc.thePlayer.height) - 0.5;
        //target and player z distance
        final double diffZ = ent.posZ - mc.thePlayer.posZ;
        // radian unit
        double radianUnit = 180.0D / Math.PI;
        /*
         * atan 2(x,y) need two parameters to compute angle
         * radian * radianUnit
         */
        // player head yaw rotation
        final float rotationYaw = (float) (Math.atan2(diffZ, diffX) * radianUnit) - 90.0f;
        // player head pitch rotation
        final float rotationPitch = (float) (Math.atan2(diffY, mc.thePlayer.getDistanceToEntity(ent)) * radianUnit);

        final float finishedYaw = mc.thePlayer.rotationYaw
                + MathHelper.wrapAngleTo180_float(rotationYaw - mc.thePlayer.rotationYaw);
        final float finishedPitch = mc.thePlayer.rotationPitch
                + MathHelper.wrapAngleTo180_float(rotationPitch - mc.thePlayer.rotationPitch);

        return new float[]{finishedYaw, -MathHelper.clamp_float(finishedPitch, -90, 90)};
    }

    private Rotation getTargetRotation(Entity entity) {
        AxisAlignedBB boundingBox = entity.getEntityBoundingBox();
        boundingBox = boundingBox.offset(
                (entity.posX - entity.prevPosX - (mc.thePlayer.posX - mc.thePlayer.prevPosX)) * RandomUtils.nextFloat(minRotateSpeed.getValue().floatValue(), maxRotateSpeed.getValue().floatValue()),
                (entity.posY - entity.prevPosY - (mc.thePlayer.posY - mc.thePlayer.prevPosY)) * RandomUtils.nextFloat(minRotateSpeed.getValue().floatValue(), maxRotateSpeed.getValue().floatValue()),
                (entity.posZ - entity.prevPosZ - (mc.thePlayer.posZ - mc.thePlayer.prevPosZ)) * RandomUtils.nextFloat(minRotateSpeed.getValue().floatValue(), maxRotateSpeed.getValue().floatValue())
        );
        Vec3 lastHitVec = new Vec3(0.0f, 0.0f, 0.0f);
        AxisAlignedBB bb = entity.getEntityBoundingBox();
        if (RotationUtils.targetRotation == null || (random.nextBoolean() && !timer.hasTimeElapsed((1000 / MathUtil.getRandomInRange(minCps.getValue().intValue(), maxCps.getValue().intValue()))))) {
            lastHitVec = new Vec3(
                    MathHelper.clamp_double(mc.thePlayer.posX, bb.minX, bb.maxX) + RandomUtils.nextDouble(0, 0.2),
                    MathHelper.clamp_double(mc.thePlayer.posY + 1.62F, bb.minY, bb.maxY) + RandomUtils.nextDouble(0, 0.2),
                    MathHelper.clamp_double(mc.thePlayer.posZ, bb.minZ, bb.maxZ) + RandomUtils.nextDouble(0, 0.2)
            );
        }
        return RotationUtils.limitAngleChange(
                RotationUtils.serverRotation,
                Objects.requireNonNull(RotationUtils.OtherRotation(
                        boundingBox,
                        lastHitVec,
                        true,
                        false,
                        reach.getValue().floatValue()
                )),
                (float) (Math.random() * (maxRotateSpeed.getValue().floatValue() - minRotateSpeed.getValue().floatValue()) + minRotateSpeed.getValue().floatValue()),
                (float) (Math.random() * (maxRotateSpeed.getValue().floatValue() - minRotateSpeed.getValue().floatValue()) + minRotateSpeed.getValue().floatValue())
        );
    }
    public void vulcanBlock(boolean state){
       /* KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(),state);*/
    }
    //copy from liquid bounce
    private void startBlocking(Entity interactEntity, boolean interact) {
        if (mc.thePlayer.getDistanceToEntity(interactEntity) >= reach.getValue().floatValue()) {
            return;
        }

        if (blocking) {
            return;
        }

        if (interact) {
            final Vec3 positionEye = mc.getRenderViewEntity().getPositionEyes(1F);

            float expandSize = interactEntity.getCollisionBorderSize();
            AxisAlignedBB boundingBox = interactEntity.getEntityBoundingBox();
            float yaw,pitch;
            if(RotationUtils.targetRotation != null){
                yaw = RotationUtils.targetRotation.getYaw();
                pitch = RotationUtils.targetRotation.getPitch();
            }else{
                Rotation rotation = new Rotation(mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch);
                yaw = rotation.getYaw();
                pitch = rotation.getPitch();
            }

            double yawCos = Math.cos(-yaw * 0.017453292F - Math.PI);
            double yawSin = Math.sin(-yaw * 0.017453292F - Math.PI);
            double pitchCos = -Math.cos(-pitch * 0.017453292F);
            double pitchSin = Math.sin(-pitch * 0.017453292F);
            double range = Math.min(reach.getValue().doubleValue(), mc.thePlayer.getDistanceToEntity(interactEntity)) + 1;
            Vec3 lookAt = positionEye.addVector(yawSin * pitchCos * range, pitchSin * range, yawCos * pitchCos * range);
            if(boundingBox.calculateIntercept(positionEye, lookAt) == null){
                return;
            }

            MovingObjectPosition movingObject = boundingBox.calculateIntercept(positionEye, lookAt);
            Vec3 hitVec = movingObject.hitVec;

            mc.getNetHandler().addToSendQueue(new C02PacketUseEntity(interactEntity, new Vec3(
                    hitVec.xCoord - interactEntity.posX,
                    hitVec.yCoord - interactEntity.posY,
                    hitVec.zCoord - interactEntity.posZ)
            ));
        }

        mc.getNetHandler().addToSendQueue(new C08PacketPlayerBlockPlacement(mc.thePlayer.inventory.getCurrentItem()));
        blocking = true;
    }
    @EventTarget
    public void respawn(RespawnPlayerEvent event) {
        if (autoDisable.getValue()) {
            this.setToggle(false);
        }
    }
    @Override
    public void onEnable() {
        SafeScaffold scaffold = Maple.getInstance().getModuleManager().getModuleByClass(SafeScaffold.class);
        if(scaffold.isToggle()){
            scaffold.setToggle(false);
        }
    }
}
