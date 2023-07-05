package top.youm.rocchi.utils.player.rotations;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.util.Vec3;
import top.youm.rocchi.utils.player.RotationUtil;

public class VecRotation {
    private Vec3 vec3;
    private float[] rotation;

    public VecRotation() {
    }

    public VecRotation(Vec3 vec3, float[] rotation) {
        this.vec3 = vec3;
        this.rotation = rotation;
    }

    public Vec3 getVec3() {
        return vec3;
    }

    public void setVec3(Vec3 vec3) {
        this.vec3 = vec3;
    }

    public float[] getRotation() {
        return rotation;
    }

    public void setRotation(float[] rotation) {
        this.rotation = rotation;
    }

}
