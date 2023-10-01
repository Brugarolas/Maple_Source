package top.youm.maple.core.module.modules.combat;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import top.youm.maple.Maple;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.events.MoveEvent;
import top.youm.maple.common.settings.impl.CheckBoxSetting;
import top.youm.maple.common.settings.impl.SliderSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.module.modules.combat.killaura.KillAura;
import top.youm.maple.core.module.modules.movement.Speed;
import top.youm.maple.utils.player.MovementUtil;
import top.youm.maple.utils.player.RotationUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public final class TargetStrafe extends Module {


    public static CheckBoxSetting edges = new CheckBoxSetting("Edges", false);
    public static CheckBoxSetting behind = new CheckBoxSetting("Behind", false);
    public CheckBoxSetting liquids = new CheckBoxSetting("Liquids", false);
    public CheckBoxSetting controllable = new CheckBoxSetting("Controllable", true);
    
    public static final SliderSetting radius = new SliderSetting("Radius", 2, 8, 0.5, 0.5);
    private static final SliderSetting points = new SliderSetting("Points", 12, 16, 3, 1);
    public static final CheckBoxSetting space = new CheckBoxSetting("Require space key", true);
    public static final CheckBoxSetting auto3rdPerson = new CheckBoxSetting("Auto 3rd Person", false);
    private final CheckBoxSetting render = new CheckBoxSetting("Render", true);

    private static int strafe = 1;
    private static int position;
    private boolean returnState;

    public TargetStrafe() {
        super("Target Strafe", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        addSetting(edges,behind,liquids,controllable, radius, points, space, auto3rdPerson, render);
    }

    @EventTarget
    public void onMotionEvent(MotionEvent event) {
        if (canStrafe()) {
            if (auto3rdPerson.getValue() && mc.gameSettings.thirdPersonView == 0) {
                mc.gameSettings.thirdPersonView = 1;
                returnState = true;
            }
            boolean updatePosition = false, positive = true;
            if (mc.thePlayer.isCollidedHorizontally) {
                strafe = -strafe;
                updatePosition = true;
                positive = strafe == 1;
            } else {
                if (controllable.getValue()) {
                    if (mc.gameSettings.keyBindLeft.isPressed()) {
                        strafe = 1;
                        updatePosition = true;
                    }
                    if (mc.gameSettings.keyBindRight.isPressed()) {
                        strafe = -1;
                        updatePosition = true;
                        positive = false;
                    }
                }
                if (edges.getValue() && isInVoid()) {
                    strafe = -strafe;
                    updatePosition = true;
                    positive = false;
                }
                if (liquids.getValue() && isInLiquid()) {
                    strafe = -strafe;
                    updatePosition = true;
                    positive = false;
                }
            }
            if (updatePosition) {
                position = (position + (positive ? 1 : -1)) % points.getValue().intValue();
            }
        } else if (auto3rdPerson.getValue() && mc.gameSettings.thirdPersonView != 0 && returnState) {
            mc.gameSettings.thirdPersonView = 0;
            returnState = false;
        }
    }

    public static boolean strafe(MoveEvent e) {
        return strafe(e, MovementUtil.getSpeed());
    }

    public static boolean strafe(MoveEvent e, double moveSpeed) {
        if (canStrafe()) {
            setSpeed(e, moveSpeed, RotationUtil.getYaw(Targets.INSTANCE.target.getPositionVector()), strafe,
                    mc.thePlayer.getDistanceToEntity(Targets.INSTANCE.target) <= radius.getValue().doubleValue() ? 0 : 1);
            return true;
        }
        return false;
    }

    public static boolean canStrafe() {

        KillAura killAura = Maple.getInstance().getModuleManager().getModuleByClass(KillAura.class);
        if (!Maple.getInstance().getModuleManager().getModuleByClass(TargetStrafe.class).isToggle() || !killAura.isToggle()
                || !MovementUtil.isMoving() || (space.getValue() && !Keyboard.isKeyDown(Keyboard.KEY_SPACE))) {
            return false;
        }
        if (!(Maple.getInstance().getModuleManager().getModuleByClass(Speed.class).isToggle())) {
            return false;
        }
        return Targets.INSTANCE.target != null && Targets.INSTANCE.targetCanAttack(Targets.INSTANCE.target);
    }

    public static void setSpeed(MoveEvent moveEvent, double speed, float yaw, double strafe, double forward) {
        EntityLivingBase target = Targets.INSTANCE.target;
        double rad = radius.getValue().doubleValue();
        int count = points.getValue().intValue();

        double a = (Math.PI * 2.0) / (double) count;
        double posX = StrictMath.sin(a * position) * rad * strafe, posY = StrictMath.cos(a * position) * rad;

        if (forward == 0 && strafe == 0) {
            moveEvent.setX(0);
            moveEvent.setZ(0);
        } else {

            boolean skip = false;
            if (edges.getValue()) {
                Vec3 pos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);
                Vec3 vec = RotationUtil.getVecRotations(0, 90);
                if (mc.theWorld.rayTraceBlocks(pos, pos.addVector(vec.xCoord * 5, vec.yCoord * 5, vec.zCoord * 5), false, false, false) == null) {
                    moveEvent.setX(0);
                    moveEvent.setZ(0);
                    skip = true;
                }
            }

            if (!skip) {
                double d;
                if (behind.getValue()) {
                    double x = target.posX + -StrictMath.sin(StrictMath.toRadians(target.rotationYaw)) * -2,
                            z = target.posZ + StrictMath.cos(StrictMath.toRadians(target.rotationYaw)) * -2;
                    d = StrictMath.toRadians(RotationUtil.getRotations(x, target.posY, z)[0]);
                } else {
                    d = StrictMath.toRadians(RotationUtil.getRotations(target.posX + posX, target.posY, target.posZ + posY)[0]);
                }
                moveEvent.setX(speed * -StrictMath.sin(d));
                moveEvent.setZ(speed * StrictMath.cos(d));
            }
        }

        double x = Math.abs(target.posX + posX - mc.thePlayer.posX), z = Math.abs(target.posZ + posY - mc.thePlayer.posZ);
        double dist = StrictMath.sqrt(x * x + z * z);
        if (dist <= 0.7) {
            position = (position + TargetStrafe.strafe) % count;
        } else if (dist > 3) {
            position = getClosestPoint(target);
        }
    }

    private boolean isInVoid() {
        double yaw = Math.toRadians(RotationUtil.getYaw(Targets.INSTANCE.target.getPositionVector()));
        double xValue = -Math.sin(yaw) * 2;
        double zValue = Math.cos(yaw) * 2;
        for (int i = 0; i <= 256; i++) {
            BlockPos b = new BlockPos(mc.thePlayer.posX + xValue, mc.thePlayer.posY - i, mc.thePlayer.posZ + zValue);
            if (mc.theWorld.getBlockState(b).getBlock() instanceof BlockAir) {
                if (b.getY() == 0) {
                    return true;
                }
            } else {
                return false;
            }
        }
        return !mc.thePlayer.isCollidedVertically && !mc.thePlayer.onGround && mc.thePlayer.fallDistance != 0 && mc.thePlayer.motionY != 0 && mc.thePlayer.isAirBorne && !mc.thePlayer.capabilities.isFlying && !mc.thePlayer.isInWater() && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isPotionActive(Potion.invisibility.id);
    }

    private boolean isInLiquid() {
        double yaw = Math.toRadians(RotationUtil.getYaw(Targets.INSTANCE.target.getPositionVector()));
        double xValue = -Math.sin(yaw) * 2;
        double zValue = Math.cos(yaw) * 2;
        BlockPos b = new BlockPos(mc.thePlayer.posX + xValue, mc.thePlayer.posY, mc.thePlayer.posZ + zValue);
        return mc.theWorld.getBlockState(b).getBlock() instanceof BlockLiquid;
    }

    private static int getClosestPoint(Entity target) {
        double playerX = mc.thePlayer.posX, playerZ = mc.thePlayer.posZ;
        return getPoints(target).stream().min(Comparator.comparingDouble(p -> p.getDistance(playerX, playerZ))).get().iteration;
    }

    private static List<Point> getPoints(Entity target) {
        double radius = TargetStrafe.radius.getValue().doubleValue();
        List<Point> pointList = new ArrayList<>();
        int count = points.getValue().intValue();
        double posX = target.posX, posZ = target.posZ;
        double d = (Math.PI * 2.0) / count;
        for (int i = 0; i <= count; i++) {
            double x = radius * StrictMath.cos(i * d);
            double z = radius * StrictMath.sin(i * d);
            pointList.add(new Point(posX + x, posZ + z, i));
        }
        return pointList;
    }

    private static class Point {
        private final double x, z;
        private final int iteration;

        private double getDistance(double posX, double posZ) {
            double x2 = Math.abs(posX - x), z2 = Math.abs(posZ - z);
            return StrictMath.sqrt(x2 * x2 + z2 * z2);
        }

        public Point(double x, double z, int iteration) {
            this.x = x;
            this.z = z;
            this.iteration = iteration;
        }

        public double getX() {
            return x;
        }

        public double getZ() {
            return z;
        }

        public int getIteration() {
            return iteration;
        }
    }

}
