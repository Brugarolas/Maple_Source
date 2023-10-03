package top.youm.maple.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import org.lwjgl.input.Keyboard;
import top.youm.maple.common.events.MotionEvent;
import top.youm.maple.common.events.PacketReceiveEvent;
import top.youm.maple.common.events.TickEvent;
import top.youm.maple.common.settings.impl.SelectButtonSetting;
import top.youm.maple.common.settings.impl.SliderSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.utils.player.MovementUtil;

import java.util.ArrayList;

/**
 * @author YouM
 */
public class Fly extends Module {
    private final SelectButtonSetting mode = new SelectButtonSetting("Mode", "WatchDog","WatchDog","Vanilla","AirWalk","Vulcan");
    private final SliderSetting speed = new SliderSetting("Speed", 2, 5, 0, 0.1);
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
        this.addSettings(mode, speed);
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
                    MovementUtil.setSpeed(0,mc.thePlayer.rotationYaw, mc.thePlayer.movementInput.moveStrafe, mc.thePlayer.movementInput.moveForward);
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
            default:
                mc.thePlayer.motionY = 0;
                mc.thePlayer.onGround = true;
                mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.15, mc.thePlayer.posZ, true));

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
    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes(mode.getValue());
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
