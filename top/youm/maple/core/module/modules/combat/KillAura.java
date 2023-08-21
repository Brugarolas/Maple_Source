package top.youm.maple.core.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.network.play.client.C0APacketAnimation;
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
import top.youm.maple.core.module.modules.movement.SafeScaffold;
import top.youm.maple.core.module.modules.visual.HUD;
import top.youm.maple.core.module.modules.world.Teams;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.AnimationUtils;
import top.youm.maple.utils.TimerUtil;
import top.youm.maple.utils.liquidbounce.Rotation;
import top.youm.maple.utils.liquidbounce.RotationUtils;
import top.youm.maple.utils.math.MathUtil;
import top.youm.maple.utils.network.PacketUtil;
import top.youm.maple.utils.player.RotationUtil;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.RoundedUtil;

import java.awt.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import static net.optifine.CustomColors.random;

/**
 * @author YouM
 */
public class KillAura extends Module {

    private float yaw = 0;
    private final NumberSetting reach = new NumberSetting("Reach", 3.3, 6, 1, 0.01);
    private final NumberSetting minCps = new NumberSetting("Min CPS", 6, 20, 1, 1);
    private final NumberSetting maxCps = new NumberSetting("Max CPS", 11, 20, 1, 1);
    private final NumberSetting minSpeed = new NumberSetting("Min Speed", 0.5, 2.0, 0, 0.1);
    private final NumberSetting maxSpeed = new NumberSetting("Max Speed", 0.6, 2.0, 0, 0.1);
    private final NumberSetting maxRotateSpeed = new NumberSetting("Max Rotate Speed", 180, 180, 0, 1);
    private final NumberSetting minRotateSpeed = new NumberSetting("Min Rotate Speed", 180, 180, 0, 1);
    private final ModeSetting autoBlockMode = new ModeSetting("AutoBlock Mode", "Legit", "Legit", "Interaction", "AAC", "Vulcan");
    private final BoolSetting autoBlock = new BoolSetting("AutoBlock", false);
    private final BoolSetting footCircle = new BoolSetting("foot circle", true);
    private final NumberSetting footWidth = new NumberSetting("foot width", 2, 5, 1, 1);

    private final BoolSetting scanCircle = new BoolSetting("scan circle", true);
    private final NumberSetting scanWidth = new NumberSetting("scan width", 2, 5, 1, 1);

    BoolSetting players = new BoolSetting("Players", true);
    BoolSetting mobs = new BoolSetting("Mobs", false);
    BoolSetting animals = new BoolSetting("Animals", false);
    BoolSetting invisible = new BoolSetting("Invisible", false);
    BoolSetting ticks = new BoolSetting("Ticks", true);
    BoolSetting nameTags = new BoolSetting("NameTags", true);
    BoolSetting packet = new BoolSetting("Packet", true);
    ModeSetting rotateMode = new ModeSetting("RotateMode", "Dynamic", "Dynamic", "Smooth", "Grim", "Vanilla");
    BoolSetting safeRotation = new BoolSetting("Safe Rotation", false);
    BoolSetting followRotation = new BoolSetting("Follow", false);
    public static List<EntityLivingBase> targets = new ArrayList<>();
    public static EntityLivingBase target;
    public static boolean blocking;
    public static boolean attacking;
    private final TimerUtil timer = new TimerUtil();

    private int hitTicks = 0;
    private final BoolSetting autoDisable = new BoolSetting("auto c", true);

    public KillAura() {
        super("Kill Aura", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        footWidth.addParent(footCircle, BoolSetting::getValue);
        scanWidth.addParent(scanCircle, BoolSetting::getValue);
        autoBlockMode.addParent(autoBlock, BoolSetting::getValue);
        maxSpeed.addParent(rotateMode, rotateMode -> rotateMode.getValue().equals("Smooth"));
        minSpeed.addParent(rotateMode, rotateMode -> rotateMode.getValue().equals("Smooth"));
        maxRotateSpeed.addParent(rotateMode, rotateMode -> rotateMode.getValue().equals("Grim"));
        minRotateSpeed.addParent(rotateMode, rotateMode -> rotateMode.getValue().equals("Grim"));
        this.addSetting(reach, minCps, maxCps, footCircle, footWidth, scanCircle, scanWidth, autoDisable, autoBlock, autoBlockMode, players, invisible, mobs, animals, rotateMode, safeRotation, followRotation, maxSpeed, minSpeed, maxRotateSpeed, minRotateSpeed, ticks, packet, nameTags);
    }

    boolean direction;
    float offsetY = 0;
    private AnimationUtils animator = new AnimationUtils();
    private float size;
    private float health = 120;
    @EventTarget
    public void onRender2D(Render2DEvent event){
        if(!targets.isEmpty()){
            size = animator.animate(100,size,0.15f);
            health = animator.animate(target.getHealth() / target.getMaxHealth() * 120,health,0.1f);
        }else{
            health = animator.animate(0,health,0.1f);
            size = animator.animate(0,size,0.15f);
        }

        ScaledResolution sr = new ScaledResolution(mc);
        int width = sr.getScaledWidth();
        int height = sr.getScaledHeight();
        int x = width - (width / 3);
        int y = height - (height / 3);
        RenderUtil.scale(x + (200 / 2.0f),y + (50 / 2.0f),size / 100,()->{
            RoundedUtil.drawRound(x,y,200,50,2,new Color(0,0,0,120));
            ResourceLocation locationSkin;
            if(mc.getNetHandler().getPlayerInfo(target.getUniqueID()) != null){
                locationSkin = mc.getNetHandler().getPlayerInfo(target.getUniqueID()).getLocationSkin();
            }else {
                locationSkin = DefaultPlayerSkin.getDefaultSkinLegacy();
            }
            RenderUtil.drawHead(locationSkin,x + 8,y + 8,34,34);
            FontLoaders.robotoB32.drawStringWithShadow(target.getName(),x + 50,y + 8,-1);
            FontLoaders.comfortaaB24.drawStringWithShadow(" " + ((float)Math.round(target.getHealth() *10)) / 10,x + 174,y + 30,HUD.getHUDThemeColor().getRGB());
            RoundedUtil.drawRound(x + 50,y + 30,health,10,5,HUD.getHUDThemeColor());
        });

    }

    @EventTarget
    public void onTick(TickEvent event) {
        this.setSuffixes(this.minCps.getValue().intValue() + "-" + this.maxCps.getValue().intValue() + " " + reach.getValue().floatValue());
        if (mc.thePlayer != null) {
            if ((mc.thePlayer.getHealth() <= 0.0f || mc.thePlayer.isDead) && this.autoDisable.getValue()) {
                this.setToggle(false);
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
            GL11.glPushMatrix();
            GL11.glTranslated(
                    mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ
            );
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glLineWidth(footWidth.getValue().floatValue());
            GL11.glRotatef(90F, 1F, 0F, 0F);
            RenderUtil.color(HUD.getHUDThemeColor().getRGB());
            GL11.glBegin(GL11.GL_LINE_STRIP);

            for (int i = 0; i <= 360; i += 2) {
                GL11.glVertex2f(
                        (float) Math.cos(i * Math.PI / 180.0) * reach.getValue().floatValue(),
                        (float) Math.sin(i * Math.PI / 180.0) * reach.getValue().floatValue()
                );
            }
            GL11.glEnd();

            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
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

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(7425);
        GL11.glLineWidth(scanWidth.getValue().floatValue());
        double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * (double) event.getTicks() - mc.getRenderManager().viewerPosX;
        double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * (double) event.getTicks() - mc.getRenderManager().viewerPosY + offsetY;
        double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * (double) event.getTicks() - mc.getRenderManager().viewerPosZ;
        GL11.glBegin(GL11.GL_LINE_STRIP);
        for (int i = 0; i <= 360; i++) {
            double c1 = i * Math.PI * 2 / 360;
            RenderUtil.color(HUD.getHUDThemeColor().getRGB());
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y, z + 0.5 * Math.sin(c1) * 1.2);
        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(7424);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    public void drawShadow(Render3DEvent event) {
        GL11.glPushMatrix();
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
            GL11.glColor4f(HUD.red.getValue().floatValue() / 255f, HUD.green.getValue().floatValue() / 255f, HUD.blue.getValue().floatValue() / 255f, 0.4f);
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y, z + 0.5 * Math.sin(c1) * 1.2);
            GL11.glVertex3d(x + 0.5 * Math.cos(c2) * 1.2, y, z + 0.5 * Math.sin(c2) * 1.2);
            GL11.glColor4f(HUD.red.getValue().floatValue() / 255f, HUD.green.getValue().floatValue() / 255f, HUD.blue.getValue().floatValue() / 255f, 0f);
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y + (direction ? -0.3 : 0.3), z + 0.5 * Math.sin(c1) * 1.2);
            GL11.glVertex3d(x + 0.5 * Math.cos(c2) * 1.2, y + (direction ? -0.3 : 0.3), z + 0.5 * Math.sin(c2) * 1.2);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel(7424);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }


    public void legitBlock() {
        KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), true);
    }

    @EventTarget
    public void respawn(RespawnPlayerEvent event) {
        if (autoDisable.getValue()) {
            this.setToggle(false);
        }
    }

    private float[] randomRotation;

    @EventTarget
    public void onMotion(MotionEvent event) {
        sortTargets();
        if (targets.size() == 0) {
            return;
        }
        if (targets.get(0).getHealth() <= 0) {
            targets.remove(0);
        }
        if (targets.get(0).isDead || Maple.getInstance().getModuleManager().getModuleByClass(SafeScaffold.class).isToggle() || mc.thePlayer.isDead || mc.thePlayer.isSpectator()) {
            return;
        }
        if (!targets.isEmpty()) {
            target = targets.get(0);
            if (event.getState() == Event.State.PRE) {
                KeyBinding.setKeyBindState(mc.gameSettings.keyBindUseItem.getKeyCode(), false);
                float[] smoothRotations = getRotationsToEnt(target);
                switch (rotateMode.getValue()) {
                    case "Dynamic":
                        if (timer.hasTimeElapsed(100, true)) {
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
            if (!RotationUtil.isMouseOver(event.getYaw(), event.getPitch(), target, reach.getValue().floatValue()))
                return;

            if (autoBlock.getValue() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                mc.playerController.syncCurrentPlayItem();
                hitTicks++;
                blocking = true;
                switch (autoBlockMode.getValue()) {
                    case "Legit":
                        if (event.getState() == Event.State.POST) {
                            if (this.hitTicks == 1) {
                                legitBlock();
                                blocking = true;
                            }
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
                    default:
                        break;
                }
                PacketUtil.sendPacket(new C0APacketAnimation());
            }

            if (event.getState() == Event.State.PRE) {
                attacking = true;
                if (timer.hasTimeElapsed((1000 / MathUtil.getRandomInRange(minCps.getValue().intValue(), maxCps.getValue().intValue())), true)) {
                    mc.thePlayer.swingItem();
                    mc.playerController.attackEntity(mc.thePlayer, target);
                    if (mc.thePlayer.fallDistance > 0 && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null) {
                        mc.thePlayer.onCriticalHit(target);
                    }
                    this.hitTicks = 0;

                }
            }
        }
        if (targets.isEmpty()) {
            attacking = false;
            blocking = false;
            this.hitTicks = 0;
        }
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
    public static ItemStack getItemStack() {
        return (mc.thePlayer == null || mc.thePlayer.inventoryContainer == null ? null : mc.thePlayer.getHeldItem());
    }
    @Override
    public void onDisable() {
        targets.clear();
        blocking = false;
        attacking = false;
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
        if (entLiving instanceof EntityMob && mobs.getValue()) {
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
}
