package top.youm.rocchi.utils.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import top.youm.rocchi.utils.math.MathUtil;
import top.youm.rocchi.utils.player.rotations.VecRotation;

import java.util.Random;


public class RotationUtil {
    private static final Random random = new Random();
    /*
     * Sets the player's head rotations to the given yaw and pitch (visual-only).
     */
    private static final Minecraft mc = Minecraft.getMinecraft();
    public static float[] serverRotation = new float[]{0,0};
    public static void setRotations(float yaw, float pitch) {
        mc.thePlayer.rotationYawHead = mc.thePlayer.renderYawOffset = yaw;
        mc.thePlayer.rotationPitchHead = pitch;
    }

    public static float clampRotation() {
        float rotationYaw = Minecraft.getMinecraft().thePlayer.rotationYaw;
        float n = 1.0f;
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward < 0.0f) {
            rotationYaw += 180.0f;
            n = -0.5f;
        }
        else if (Minecraft.getMinecraft().thePlayer.movementInput.moveForward > 0.0f) {
            n = 0.5f;
        }
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe > 0.0f) {
            rotationYaw -= 90.0f * n;
        }
        if (Minecraft.getMinecraft().thePlayer.movementInput.moveStrafe < 0.0f) {
            rotationYaw += 90.0f * n;
        }
        return rotationYaw * 0.017453292f;
    }

    public static float getSensitivityMultiplier() {
        float SENSITIVITY = Minecraft.getMinecraft().gameSettings.mouseSensitivity * 0.6F + 0.2F;
        return (SENSITIVITY * SENSITIVITY * SENSITIVITY * 8.0F) * 0.15F;
    }

    public static float smoothRotation(float from, float to, float speed) {
        float f = MathHelper.wrapAngleTo180_float(to - from);

        if (f > speed) {
            f = speed;
        }

        if (f < -speed) {
            f = -speed;
        }

        return from + f;
    }

    /*
     * Sets the player's head rotations to the given yaw and pitch (visual-only).
     */
    public static void setRotations(float[] rotations) {
        setRotations(rotations[0], rotations[1]);
    }

    public static float[] getRotationsNeeded(final Entity entity) {
        if (entity == null) {
            return null;
        }
        Minecraft mc = Minecraft.getMinecraft();
        final double xSize = entity.posX - mc.thePlayer.posX;
        final double ySize = entity.posY + entity.getEyeHeight() / 2 - (mc.thePlayer.posY + mc.thePlayer.getEyeHeight());
        final double zSize = entity.posZ - mc.thePlayer.posZ;
        final double theta = MathHelper.sqrt_double(xSize * xSize + zSize * zSize);
        final float yaw = (float) (Math.atan2(zSize, xSize) * 180 / Math.PI) - 90;
        final float pitch = (float) (-(Math.atan2(ySize, theta) * 180 / Math.PI));
        return new float[]{(mc.thePlayer.rotationYaw + MathHelper.wrapAngleTo180_float(yaw - mc.thePlayer.rotationYaw)) % 360, (mc.thePlayer.rotationPitch + MathHelper.wrapAngleTo180_float(pitch - mc.thePlayer.rotationPitch)) % 360.0f};
    }

    public static float[] getFacingRotations2(final int paramInt1, final double d, final int paramInt3) {
        final EntitySnowball localEntityPig = new EntitySnowball(Minecraft.getMinecraft().theWorld);
        localEntityPig.posX = paramInt1 + 0.5;
        localEntityPig.posY = d + 0.5;
        localEntityPig.posZ = paramInt3 + 0.5;
        return getRotationsNeeded(localEntityPig);
    }

    public static float getYaw(Vec3 to) {
        float x = (float) (to.xCoord - mc.thePlayer.posX);
        float z = (float) (to.zCoord - mc.thePlayer.posZ);
        float var1 = (float) (StrictMath.atan2(z, x) * 180.0D / StrictMath.PI) - 90.0F;
        float rotationYaw = mc.thePlayer.rotationYaw;
        return rotationYaw + MathHelper.wrapAngleTo180_float(var1 - rotationYaw);
    }

    public static Vec3 getVecRotations(float yaw, float pitch) {
        double d = Math.cos(Math.toRadians(-yaw) - Math.PI);
        double d1 = Math.sin(Math.toRadians(-yaw) - Math.PI);
        double d2 = -Math.cos(Math.toRadians(-pitch));
        double d3 = Math.sin(Math.toRadians(-pitch));
        return new Vec3(d1 * d2, d3, d * d2);
    }

    public static float[] getRotations(double posX, double posY, double posZ) {
        double x = posX - mc.thePlayer.posX, z = posZ - mc.thePlayer.posZ, y = posY + mc.thePlayer.getEyeHeight() - mc.thePlayer.posY;
        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (MathHelper.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
        float pitch = (float) (-(MathHelper.atan2(y, d3) * 180.0D / Math.PI));
        return new float[]{yaw, pitch};
    }
    public static float[] getSmoothRotations(EntityLivingBase entity) {
        float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
        float fac = f1 * f1 * f1 * 256.0F;

        double x = entity.posX - mc.thePlayer.posX;
        double z = entity.posZ - mc.thePlayer.posZ;
        double y = entity.posY + entity.getEyeHeight()
                - (mc.thePlayer.getEntityBoundingBox().minY
                + (mc.thePlayer.getEntityBoundingBox().maxY
                - mc.thePlayer.getEntityBoundingBox().minY));

        double d3 = MathHelper.sqrt_double(x * x + z * z);
        float yaw = (float) (MathHelper.atan2(z, x) * 180.0 / Math.PI) - 90.0F;
        float pitch = (float) (-(MathHelper.atan2(y, d3) * 180.0 / Math.PI));
        yaw = smoothRotation(mc.thePlayer.prevRotationYawHead, yaw, fac * MathUtil.getRandomFloat(0.7F, 1));
        pitch = smoothRotation(mc.thePlayer.prevRotationPitchHead, pitch, fac * MathUtil.getRandomFloat(0.6F, 1));

        return new float[]{yaw, pitch};
    }
    public static float[] limitAngleChange(final float[] currentRotation, final float[] targetRotation, final float turnSpeed) {
        final float yawDifference = getAngleDifference(targetRotation[0], currentRotation[0]);
        final float pitchDifference = getAngleDifference(targetRotation[1], currentRotation[1]);
        return new float[]{
                currentRotation[0] + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)),
                currentRotation[1] + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed))
        };
    }
    public static float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }
    public static VecRotation calculateCenter(final String calMode, final String randMode, final double randomRange, final AxisAlignedBB bb, final boolean predict, final boolean throughWalls) {

        //final Rotation randomRotation = toRotation(randomVec, predict);

        VecRotation vecRotation = null;

        double xMin;
        double yMin;
        double zMin;
        double xMax;
        double yMax;
        double zMax;
        double xDist;
        double yDist;
        double zDist;

        xMin = 0.15D; xMax = 0.85D; xDist = 0.1D;
        yMin = 0.15D; yMax = 1.00D; yDist = 0.1D;
        zMin = 0.15D; zMax = 0.85D; zDist = 0.1D;

        Vec3 curVec3 = null;

        switch(calMode) {
            case "LiquidBounce":
                break;
            case "Full":
                xMin = 0.00D; xMax = 1.00D;
                yMin = 0.00D;
                zMin = 0.00D; zMax = 1.00D;
                break;
            case "HalfUp":
                xMin = 0.10D; xMax = 0.90D;
                yMin = 0.50D; yMax = 0.90D;
                zMin = 0.10D; zMax = 0.90D;
                break;
            case "HalfDown":
                xMin = 0.10D; xMax = 0.90D;
                yMin = 0.10D; yMax = 0.50D;
                zMin = 0.10D; zMax = 0.90D;
                break;
            case "CenterSimple":
                xMin = 0.45D; xMax = 0.55D; xDist = 0.0125D;
                yMin = 0.65D; yMax = 0.75D; yDist = 0.0125D;
                zMin = 0.45D; zMax = 0.55D; zDist = 0.0125D;
                break;
            case "CenterLine":
                xMin = 0.45D; xMax = 0.55D; xDist = 0.0125D;
                yMin = 0.10D; yMax = 0.90D;
                zMin = 0.45D; zMax = 0.55D; zDist = 0.0125D;
                break;
        }

        for(double xSearch = xMin; xSearch < xMax; xSearch += xDist) {
            for (double ySearch = yMin; ySearch < yMax; ySearch += yDist) {
                for (double zSearch = zMin; zSearch < zMax; zSearch += zDist) {
                    final Vec3 vec3 = new Vec3(bb.minX + (bb.maxX - bb.minX) * xSearch, bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final float[] rotation = toRotation(vec3, predict);

                    if(throughWalls || isVisible(vec3)) {
                        final VecRotation currentVec = new VecRotation(vec3, rotation);

                        if (vecRotation == null || (getRotationDifference(currentVec.getRotation()) < getRotationDifference(vecRotation.getRotation()))) {
                            vecRotation = currentVec;
                            curVec3 = vec3;
                        }
                    }
                }
            }
        }

        if(vecRotation == null || randMode.equals("Off"))
            return vecRotation;

        double rand1 = random.nextDouble();
        double rand2 = random.nextDouble();
        double rand3 = random.nextDouble();

        final double xRange = bb.maxX - bb.minX;
        final double yRange = bb.maxY - bb.minY;
        final double zRange = bb.maxZ - bb.minZ;
        double minRange = 999999.0D;

        if(xRange<=minRange) minRange = xRange;
        if(yRange<=minRange) minRange = yRange;
        if(zRange<=minRange) minRange = zRange;

        rand1 = rand1 * minRange * randomRange;
        rand2 = rand2 * minRange * randomRange;
        rand3 = rand3 * minRange * randomRange;

        final double xPrecent = minRange * randomRange / xRange;
        final double yPrecent = minRange * randomRange / yRange;
        final double zPrecent = minRange * randomRange / zRange;

        Vec3 randomVec3 = new Vec3(
                curVec3.xCoord - xPrecent * (curVec3.xCoord - bb.minX) + rand1,
                curVec3.yCoord - yPrecent * (curVec3.yCoord - bb.minY) + rand2,
                curVec3.zCoord - zPrecent * (curVec3.zCoord - bb.minZ) + rand3
        );
        switch(randMode) {
            case "Horizonal":
                randomVec3 = new Vec3(
                        curVec3.xCoord - xPrecent * (curVec3.xCoord - bb.minX) + rand1,
                        curVec3.yCoord,
                        curVec3.zCoord - zPrecent * (curVec3.zCoord - bb.minZ) + rand3
                );
                break;
            case "Vertical":
                randomVec3 = new Vec3(
                        curVec3.xCoord,
                        curVec3.yCoord - yPrecent * (curVec3.yCoord - bb.minY) + rand2,
                        curVec3.zCoord
                );
                break;
        }

        final float[] randomRotation = toRotation(randomVec3, predict);

        vecRotation =  new VecRotation(randomVec3, randomRotation);

        return vecRotation;
    }
    public static float[] toRotation(final Vec3 vec, final boolean predict) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY +
                mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        if(predict) {
            if(mc.thePlayer.onGround) {
                eyesPos.addVector(mc.thePlayer.motionX, 0.0, mc.thePlayer.motionZ);
            }else eyesPos.addVector(mc.thePlayer.motionX, mc.thePlayer.motionY, mc.thePlayer.motionZ);
        }

        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;

        return new float[]{MathHelper.wrapAngleTo180_float(
                (float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F
        ), MathHelper.wrapAngleTo180_float(
                (float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))
        )};
    }
    public static boolean isVisible(final Vec3 vec3) {
        final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.getEntityBoundingBox().minY + mc.thePlayer.getEyeHeight(), mc.thePlayer.posZ);

        return mc.theWorld.rayTraceBlocks(eyesPos, vec3) == null;
    }
    public static double getRotationDifference(final float[] a, final float[] b) {
        return Math.hypot(getAngleDifference(a[0], b[0]), a[1] - b[1]);
    }
    public static double getRotationDifference(final float[] rotation) {
        return serverRotation == null ? 0D : getRotationDifference(rotation, serverRotation);
    }
    public static Vec3 getCenter(final AxisAlignedBB bb) {
        return new Vec3(bb.minX + (bb.maxX - bb.minX) * 0.5, bb.minY + (bb.maxY - bb.minY) * 0.5, bb.minZ + (bb.maxZ - bb.minZ) * 0.5);
    }
    public static float[] toPlayer(EntityPlayerSP player, float[] rotation) {
        if ((rotation[0] == 0 || rotation[1] == 0)) {
            return null;
        }

        float[] floats = fixedSensitivity(Minecraft.getMinecraft().gameSettings.mouseSensitivity, rotation);

        player.rotationYaw = floats[0];
        player.rotationPitch = floats[1];
        return floats;
    }
    public static float[] fixedSensitivity(float sensitivity, float[] rotation) {
        float f = sensitivity * 0.6F + 0.2F;
        float gcd = f * f * f * 1.2F;

        // get previous rotation
        float[] rotations = RotationUtil.serverRotation;

        // fix rotation[0]
        float deltaYaw = rotation[0] - rotations[0];
        deltaYaw -= deltaYaw % gcd;
        rotation[0] = rotations[0] + deltaYaw;

        // fix rotation[1]
        float deltaPitch = rotation[1] - rotations[1];
        deltaPitch -= deltaPitch % gcd;
        rotation[1] = rotations[1] + deltaPitch;
        return rotation;
    }
    public static float[] setTargetRotationReverse(final float[] rotation, int kl, int rt) {
        if(Double.isNaN(rotation[0]) || Double.isNaN(rotation[1])
                || rotation[1] > 90 || rotation[1] < -90)
            return null;

        return fixedSensitivity(mc.gameSettings.mouseSensitivity, rotation);
    }
}
