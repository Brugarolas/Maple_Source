package top.youm.rocchi.core.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.network.play.server.S18PacketEntityTeleport;
import net.minecraft.util.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import top.youm.rocchi.Rocchi;
import top.youm.rocchi.common.events.*;
import top.youm.rocchi.common.settings.impl.BoolSetting;
import top.youm.rocchi.common.settings.impl.ModeSetting;
import top.youm.rocchi.common.settings.impl.NumberSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;
import top.youm.rocchi.core.module.modules.movement.Scaffold;
import top.youm.rocchi.core.module.modules.world.Teams;
import top.youm.rocchi.core.ui.theme.Theme;
import top.youm.rocchi.utils.AnimationUtils;
import top.youm.rocchi.utils.TimerUtil;
import top.youm.rocchi.utils.math.MathUtil;
import top.youm.rocchi.utils.network.PacketUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import top.youm.rocchi.utils.player.MovementUtil;
import top.youm.rocchi.utils.player.RotationUtil;
import top.youm.rocchi.utils.render.RenderUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author YouM
 */
public class KillAura extends Module {
    private final NumberSetting reach = new NumberSetting("Reach", 3.77, 6, 1, 0.1);
    private final NumberSetting minCps = new NumberSetting("Min CPS", 11, 20, 1, 1);
    private final NumberSetting maxCps = new NumberSetting("Max CPS", 14, 20, 1, 1);
    private final NumberSetting minSpeed = new NumberSetting("Min Speed", 0.5, 1.0, 0, 0.1);
    private final NumberSetting maxSpeed = new NumberSetting("Max Speed", 0.6, 1.0, 0, 0.1);
    private final ModeSetting autoBlockMode = new ModeSetting("AutoBlock Mode", "WatchDog","WatchDog","Interaction","AAC" );
    private final BoolSetting autoblock = new BoolSetting("Autoblock", false);

    private final BoolSetting footCircle = new BoolSetting("foot circle",true);
    private final NumberSetting footWidth = new NumberSetting("foot width", 2, 5, 1, 1);

    private final BoolSetting scanCircle = new BoolSetting("scan circle",true);
    private final NumberSetting scanWidth = new NumberSetting("scan width", 2, 5, 1, 1);

    BoolSetting players = new BoolSetting("Players", true);
    BoolSetting mobs = new BoolSetting("Mobs", false);
    BoolSetting animals = new BoolSetting("Animals", false);
    BoolSetting invisibles = new BoolSetting("Invisibles",false);
    BoolSetting ticks = new BoolSetting("Ticks", true);
    BoolSetting invisible = new BoolSetting("Invisible", false);
    BoolSetting nameTags = new BoolSetting("NameTags", true);
    BoolSetting packet = new BoolSetting("Packet", true);
    ModeSetting rotateMode = new ModeSetting("RotateMode","Dynamic","Dynamic","Smooth","Resolver");
    BoolSetting safeRotation = new BoolSetting("Safe Rotation",false);
    BoolSetting followRotation = new BoolSetting("Follow",false);
    public List<EntityLivingBase> targets = new ArrayList<>();
    public static EntityLivingBase target;
    public static boolean blocking;
    public static boolean attacking;
    private final TimerUtil timer = new TimerUtil();

    public KillAura() {
        super("KillAura", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        footWidth.addParent(footCircle,BoolSetting::getValue);
        scanWidth.addParent(scanCircle,BoolSetting::getValue);
        this.addSetting(reach, minCps, maxCps, autoBlockMode,footCircle,footWidth,scanCircle,scanWidth,autoblock,players,invisibles,mobs,animals,rotateMode,safeRotation,followRotation,maxSpeed,minSpeed,ticks,packet,nameTags,invisible);
    }
    boolean direction;
    float offsetY = 0;
    @EventTarget
    public void onTick(TickEvent event){
        if(direction){
            offsetY += 0.04;
            if (2 - offsetY < 0.02) {
                direction = false;
            }
        }else {
            offsetY -= 0.04;
            if (offsetY < 0.02) {
                direction = true;
            }
        }
    }

    @EventTarget
    public void onRender3D(Render3DEvent event){

        if(footCircle.getValue()){
            GL11.glPushMatrix();
            GL11.glTranslated(
                    mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX,
                    mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY,
                    mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ
            );
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA,GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glLineWidth(footWidth.getValue().floatValue());
            GL11.glRotatef(90F, 1F, 0F, 0F);
            RenderUtil.color(Theme.theme.getRGB());
            GL11.glBegin(GL11.GL_LINE_STRIP);

            for(int i = 0; i <= 360; i += 2 ){
                GL11.glVertex2f(
                        (float) Math.cos(i * Math.PI / 180.0) * reach.getValue().floatValue(),
                        (float) Math.sin(i * Math.PI / 180.0) * reach.getValue().floatValue()
                );
            }
            GL11.glEnd();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
        }
        if (targets.get(0).isDead || targets.get(0).getHealth() <= 0|| Rocchi.getInstance().getModuleManager().getModuleByClass(Scaffold.class).isToggle() || mc.thePlayer.isDead || mc.thePlayer.isSpectator())
            return;
        if(target != null && this.scanCircle.getValue()){
            drawCircle(event);
            drawShadow(event);
        }
    }
    public void drawCircle(Render3DEvent event){

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
            GL11.glColor4f(Theme.theme.getRed() / 255f, (float) Theme.theme.getGreen() / 255f, (float) Theme.theme.getBlue() / 255f, 1);
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y, z + 0.5 * Math.sin(c1) * 1.2);
        }

        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel( 7424);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
    public void drawShadow(Render3DEvent event){
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel((int) 7425);
        double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * (double) event.getTicks() - mc.getRenderManager().viewerPosX;
        double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * (double) event.getTicks() - mc.getRenderManager().viewerPosY + offsetY;
        double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * (double) event.getTicks() - mc.getRenderManager().viewerPosZ;
        GL11.glBegin(GL11.GL_QUAD_STRIP);
        for (int i = 0; i <= 360; i++) {
            double c1 = i * Math.PI * 2 / 360;
            double c2 = (i + 1) * Math.PI * 2 / 360;
            GL11.glColor4f(Theme.theme.getRed() / 255f, (float) Theme.theme.getGreen() / 255f, (float) Theme.theme.getBlue() / 255f, 0.4f);
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y, z + 0.5 * Math.sin(c1) * 1.2);
            GL11.glVertex3d(x + 0.5 * Math.cos(c2) * 1.2, y, z + 0.5 * Math.sin(c2) * 1.2);
            GL11.glColor4f(Theme.theme.getRed() / 255f, (float) Theme.theme.getGreen() / 255f, (float) Theme.theme.getBlue() / 255f, 0f);
            GL11.glVertex3d(x + 0.5 * Math.cos(c1) * 1.2, y + (direction ? -0.3 : 0.3), z + 0.5 * Math.sin(c1) * 1.2);
            GL11.glVertex3d(x + 0.5 * Math.cos(c2) * 1.2, y + (direction ? -0.3 : 0.3), z + 0.5 * Math.sin(c2) * 1.2);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glShadeModel((int) 7424);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }
    private float partialTicks;
    @EventTarget
    public void onRender(Render2DEvent event){
        this.partialTicks = event.getPartialTicks();
    }
    AnimationUtils animator= new AnimationUtils();
    @EventTarget
    public void onMotion(MotionEvent event){
        sortTargets();
        this.setSuffixes(autoBlockMode.getValue());
        if(targets.get(0).getHealth() <= 0){
            targets.remove(0);
        }
        if (targets.get(0).isDead || Rocchi.getInstance().getModuleManager().getModuleByClass(Scaffold.class).isToggle() || mc.thePlayer.isDead || mc.thePlayer.isSpectator())
            return;
        if (!targets.isEmpty()) {
            target = targets.get(0);
            if (event.getState() == Event.State.PRE) {

                float[] smoothRotations = getRotationsToEnt(target);
                switch (rotateMode.getValue()){
                    case "Dynamic":
                        smoothRotations[0] += MathUtil.getRandomInRange(-5, 5);
                        smoothRotations[1] += MathUtil.getRandomInRange(-5, 5);
                        break;
                    case "Resolver":
                        if (target.posY < 0) {
                            smoothRotations[1] = 1;
                        } else if (target.posY > 255) {
                            smoothRotations[1] = 90;
                        }
                        break;
                    default:
                        smoothRotations = RotationUtil.getSmoothRotations(target,maxSpeed.getValue().floatValue(),minSpeed.getValue().floatValue());
                        if(MovementUtil.isMoving()){
                            if (this.mc.thePlayer.movementInput.moveStrafe == -1.0) {
                                smoothRotations[0] -= 5;
                            }else if (this.mc.thePlayer.movementInput.moveStrafe == 1.0f) {
                                smoothRotations[0] += 3;
                            }
                        }
                        if(target.posY >= mc.thePlayer.posY + 2f){
                            smoothRotations[1] += 5;
                        }
                }
                event.setYaw(smoothRotations[0]);
                event.setPitch(smoothRotations[1]);
                if(followRotation.getValue()){
                    mc.thePlayer.rotationYaw = smoothRotations[0];
                    mc.thePlayer.rotationPitch = smoothRotations[1];
                }
                RotationUtil.setRotations(smoothRotations);
            }

            if (autoblock.getValue() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
                mc.playerController.syncCurrentPlayItem();
                blocking = true;
                switch (autoBlockMode.getValue()) {
                    case "WatchDog":
                        if (event.getState() == Event.State.POST) {
                            if (mc.thePlayer.swingProgressInt == -1) {
                                PacketUtil.sendPacket(new C07PacketPlayerDigging(
                                        C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(-1, -1, -1), EnumFacing.DOWN));

                            } else if (mc.thePlayer.swingProgressInt == 0) {
                                PacketUtil.sendPacket(new C08PacketPlayerBlockPlacement(
                                        new BlockPos(-1, -1, -1), 255, mc.thePlayer.getHeldItem(), 0, 0, 0));
                                mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem());
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
                        break;
                }
                PacketUtil.sendPacket(new C0APacketAnimation());
            }

            if (event.getState() == Event.State.PRE) {
                attacking = true;
                if (timer.hasTimeElapsed((1000 / MathUtil.getRandomInRange(minCps.getValue().intValue(), maxCps.getValue().intValue())), true)) {
                    if(safeRotation.getValue()){
                        if(event.getYaw() + 4.0f >= getRotationsToEnt(target)[0] && event.getYaw() - 4.0f <= getRotationsToEnt(target)[0] && event.getPitch() + 8.0f >= getRotationsToEnt(target)[1] && event.getPitch() - 8.0f <= getRotationsToEnt(target)[1]){
                            mc.thePlayer.swingItem();
                            mc.playerController.attackEntity(mc.thePlayer,target);
                        }
                    }else {
                        mc.thePlayer.swingItem();
                        mc.playerController.attackEntity(mc.thePlayer,target);
                    }
                }
            }
        }
        if (targets.isEmpty()) {
            attacking = false;
            blocking = false;
        }
    }
    public boolean isInAABB(AxisAlignedBB aabb,EntityPlayer player){

        return false;
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
        AntiBot antiBot = Rocchi.getInstance().getModuleManager().getModuleByClass(AntiBot.class);
        if (entLiving == null) {
            return false;
        }
        if (Teams.isInTeam(entLiving)) return false;
        if (entLiving instanceof EntityPlayer && players.getValue() && !entLiving.isInvisible()) {
            return true;
        }

        if (entLiving instanceof EntityPlayer && invisibles.getValue() && entLiving.isInvisible() && !antiBot.isServerBot(target)) {
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
        if (!entLiving.canEntityBeSeen(mc.thePlayer) && invisible.getValue()) {
            return false;
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
         * atan2(x,y) need two parameters to compute angle
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
}
