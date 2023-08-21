package top.youm.maple.utils.liquidbounce;

import net.minecraft.util.Vec3;

/**
 * @author YouM
 * Created on 2023/8/12
 */
public class VecRotation {
    private Vec3 vec3;
    private Rotation rotation;

    public VecRotation(Vec3 vec3, Rotation rotation) {
        this.vec3 = vec3;
        this.rotation = rotation;
    }

    public Vec3 getVec3() {
        return vec3;
    }

    public void setVec3(Vec3 vec3) {
        this.vec3 = vec3;
    }

    public Rotation getRotation() {
        return rotation;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }
}
