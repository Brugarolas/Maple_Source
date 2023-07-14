package top.youm.rocchi.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;
import top.youm.rocchi.common.events.MotionEvent;
import top.youm.rocchi.common.events.PacketReceiveEvent;
import top.youm.rocchi.common.settings.impl.ModeSetting;
import top.youm.rocchi.common.settings.impl.NumberSetting;
import top.youm.rocchi.core.module.Module;
import top.youm.rocchi.core.module.ModuleCategory;

import java.util.ArrayList;

/**
 * @author YouM
 */
public class Fly extends Module {
    private final ModeSetting mode = new ModeSetting("Mode", "WatchDog","WatchDog","Vanilla","AirWalk");
    private final NumberSetting speed = new NumberSetting("Speed", 2, 5, 0, 0.1);
    private float stage;
    private int ticks;
    private boolean doFly;
    private double x, y, z;
    private ArrayList<Packet<?>> packets = new ArrayList<>();
    private boolean hasClipped;
    private double speedStage;
    public Minecraft minecraft = Minecraft.getMinecraft();
    public Fly() {
        super("Fly", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
        this.addSetting(mode, speed);
    }
    @EventTarget
    public void onMotion(MotionEvent event) {
        switch (mode.getValue()) {
            case "WatchDog":
                mc.thePlayer.cameraYaw = mc.thePlayer.cameraPitch = 0.05f;
                mc.thePlayer.posY = y;
                if (mc.thePlayer.onGround && stage == 0) {
                    mc.thePlayer.motionY = 0.09;
                }
                stage++;
                if (mc.thePlayer.onGround && stage > 2 && !hasClipped) {
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.15, mc.thePlayer.posZ, false));
                    mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.15, mc.thePlayer.posZ, true));
                    hasClipped = true;
                }
                if (doFly) {
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.onGround = true;
                    mc.timer.timerSpeed = 2;
                } else {
                    this.setSpeed(0,mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
                    mc.timer.timerSpeed = 5;
                }
                break;
            case "Vanilla":
                mc.thePlayer.motionY = mc.gameSettings.keyBindJump.isKeyDown() ? speed.getValue().floatValue() : mc.gameSettings.keyBindSneak.isKeyDown() ? -speed.getValue().floatValue() : 0;
                break;
            case "AirWalk":
                mc.thePlayer.motionY = 0;
                mc.thePlayer.onGround = true;
                break;
        }
    }

    @EventTarget
    public void onPacketReceive(PacketReceiveEvent event) {
        if (mode.getValue().equals("WatchDog")) {
            if (event.getPacket() instanceof S08PacketPlayerPosLook) {
                S08PacketPlayerPosLook s08 = (S08PacketPlayerPosLook) event.getPacket();
                y = s08.getY();
                doFly = true;
            }
        }
    }
    public void setSpeed(double moveSpeed, float yaw, double strafe, double forward) {
        if (forward != 0.0D) {
            if (strafe > 0.0D) {
                yaw += ((forward > 0.0D) ? -45 : 45);
            } else if (strafe < 0.0D) {
                yaw += ((forward > 0.0D) ? 45 : -45);
            }
            strafe = 0.0D;
            if (forward > 0.0D) {
                forward = 1.0D;
            } else if (forward < 0.0D) {
                forward = -1.0D;
            }
        }
        if (strafe > 0.0D) {
            strafe = 1.0D;
        } else if (strafe < 0.0D) {
            strafe = -1.0D;
        }
        double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
        mc.thePlayer.motionX = forward * moveSpeed * mx + strafe * moveSpeed * mz;
        mc.thePlayer.motionZ = forward * moveSpeed * mz - strafe * moveSpeed * mx;
    }
    @Override
    public void onEnable() {
        if(this.mc.thePlayer != null){
            doFly = false;
            ticks = 0;
            stage = 0;
            x = mc.thePlayer.posX;
            y = mc.thePlayer.posY;
            z = mc.thePlayer.posZ;
            hasClipped = false;
            packets.clear();
        }
    }

    @Override
    public void onDisable() {
        if(this.mc.thePlayer != null) {
            if (mode.getValue().equals("Vanilla")) {
                mc.thePlayer.motionX = mc.thePlayer.motionY = mc.thePlayer.motionZ = 0;
            }
            mc.timer.timerSpeed = 1;
        }
    }
}
