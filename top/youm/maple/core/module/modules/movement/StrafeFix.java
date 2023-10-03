package top.youm.maple.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.util.MathHelper;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.StrafeEvent;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.CheckBoxSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;

/**
 * @author YouM
 * Created on 2023/10/2
 */
public class StrafeFix extends Module {
    public CheckBoxSetting silentFixSetting = new CheckBoxSetting("Silent",false);
    public boolean silentFix = false;
    public boolean doFix = false;
    public boolean isOverwrite = false;
    public StrafeFix() {
        super("StrafeFix", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
        this.addSettings(silentFixSetting);
    }

    @EventTarget
    public void onUpdate(TickEvent event) {
        if (!isOverwrite) {
            silentFix = silentFixSetting.getValue();
            doFix = true;
        }
    }
    @Override
    public void onDisable() {
        doFix = false;
    }

    public void applyForceStrafe(boolean isSilent, boolean runStrafeFix ) {
        silentFix = isSilent;
        doFix = runStrafeFix;
        isOverwrite = true;
    }

    public void updateOverwrite() {
        isOverwrite = false;
        doFix = isEnabled();
        silentFix = silentFixSetting.getValue();
    }

    public void runStrafeFixLoop(boolean isSilent , StrafeEvent event) {
        if (event.isCancelled()) {
            return;
        }
        float yaw = mc.thePlayer.rotationYaw;

        float strafe = event.getStrafe();
        float forward = event.getForward();
        float friction = event.getFriction();
        float factor = strafe * strafe + forward * forward;

        float angleDiff = (float) ((MathHelper.wrapAngleTo180_float(mc.thePlayer.rotationYaw - yaw - 22.5f - 135.0f) + 180.0) / (45.0));
        float calcYaw = isSilent ? yaw + 45.0f * angleDiff : yaw;

        float calcMoveDir = Math.max(Math.abs(strafe), Math.abs(forward));
        calcMoveDir = calcMoveDir * calcMoveDir;
        float calcMultiplier = MathHelper.sqrt_float(calcMoveDir / Math.min(1.0f, calcMoveDir * 2.0f));

        if (isSilent) {
            if(angleDiff == 1 || angleDiff == 3 || angleDiff == 5 || angleDiff == 7 || angleDiff == 9) {
                boolean value = Math.abs(forward) > 0.005 && Math.abs(strafe) > 0.005;
                if ((Math.abs(forward) > 0.005 || Math.abs(strafe) > 0.005) && !(value)) {
                    friction = friction / calcMultiplier;
                } else if (value) {
                    friction = friction * calcMultiplier;
                }
            }
        }
        if (factor >= 1.0E-4F) {
            factor = MathHelper.sqrt_float(factor);

            if (factor < 1.0F) {
                factor = 1.0F;
            }

            factor = friction / factor;
            strafe *= factor;
            forward *= factor;

            float yawSin = MathHelper.sin((float) (calcYaw * Math.PI / 180F));
            float yawCos = MathHelper.cos((float) (calcYaw * Math.PI / 180F));

            mc.thePlayer.motionX += strafe * yawCos - forward * yawSin;
            mc.thePlayer.motionZ += forward * yawCos + strafe * yawSin;
        }
        event.setCancelled(true);
    }
}
