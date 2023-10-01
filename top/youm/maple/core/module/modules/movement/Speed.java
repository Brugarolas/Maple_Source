package top.youm.maple.core.module.modules.movement;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.BlockPos;
import top.youm.maple.common.events.*;
import top.youm.maple.common.settings.impl.CheckBoxSetting;
import top.youm.maple.common.settings.impl.SelectButtonSetting;
import top.youm.maple.common.settings.impl.SliderSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import org.lwjgl.input.Keyboard;
import top.youm.maple.core.module.modules.combat.TargetStrafe;
import top.youm.maple.utils.TimerUtil;
import top.youm.maple.utils.network.PacketUtil;
import top.youm.maple.utils.player.MovementUtil;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author YouM
 */
public final class Speed extends Module {
    private final SelectButtonSetting mode = new SelectButtonSetting("Mode", "Watchdog",
            "Watchdog", "Strafe", "Matrix", "HurtTime", "Vanilla", "BHop", "Verus", "Viper", "Vulcan", "Zonecraft", "Heatseeker", "Mineland");
    private final SelectButtonSetting watchdogMode = new SelectButtonSetting("Watchdog Mode", "Hop", "Hop", "Dev", "Low Hop", "Ground");
    private final SelectButtonSetting verusMode = new SelectButtonSetting("Verus Mode", "Normal", "Low", "Normal");
    private final SelectButtonSetting viperMode = new SelectButtonSetting("Viper Mode", "Normal", "High", "Normal");
    private final CheckBoxSetting autoDisable = new CheckBoxSetting("Auto Disable", false);
    private final SliderSetting groundSpeed = new SliderSetting("Ground Speed", 2, 5, 1, 0.1);
    private final SliderSetting timer = new SliderSetting("Timer", 1, 5, 1, 0.1);
    private final SliderSetting vanillaSpeed = new SliderSetting("Speed", 1, 10, 1, 0.1);

    private final TimerUtil timerUtil = new TimerUtil();
    private final float r = ThreadLocalRandom.current().nextFloat();
    private double speed, lastDist;
    private float speedChangingDirection;
    private int stage;
    private boolean strafe, wasOnGround;
    private boolean setTimer = true;
    private double moveSpeed;
    private int inAirTicks;

    public Speed() {
        super("Speed", ModuleCategory.MOVEMENT, Keyboard.KEY_NONE);
        watchdogMode.addParent(mode, modeSetting -> modeSetting.getValue().equals("Watchdog"));
        verusMode.addParent(mode, modeSetting -> modeSetting.getValue().equals("Verus"));
        viperMode.addParent(mode, modeSetting -> modeSetting.getValue().equals("Viper"));
        groundSpeed.addParent(watchdogMode, modeSetting -> modeSetting.getValue().equals("Ground") && mode.getValue().equals("Watchdog"));
        vanillaSpeed.addParent(mode, modeSetting -> modeSetting.getValue().equals("Vanilla") || modeSetting.getValue().equals("BHop"));
        this.addSetting(mode, vanillaSpeed, watchdogMode, verusMode, viperMode, autoDisable, groundSpeed, timer);
    }
    @EventTarget
    public void onTick(TickEvent event){
        this.setSuffixes(mode.getValue());
    }
    @EventTarget
    public void onMotionEvent(MotionEvent e) {
        if (setTimer) {
            mc.timer.timerSpeed = timer.getValue().floatValue();
        }

        double distX = e.getPosX() - mc.thePlayer.prevPosX, distZ = e.getPosZ() - mc.thePlayer.prevPosZ;
        lastDist = Math.hypot(distX, distZ);

        switch (mode.getValue()) {
            case "Watchdog":
                switch (watchdogMode.getValue()) {
                    case "Hop":
                    case "Low Hop":
                    case "Dev":
                        if (e.getState() == Event.State.PRE) {
                            if (MovementUtil.isMoving() && mc.thePlayer.fallDistance < 1) {
                                if (mc.thePlayer.onGround) {
                                    mc.thePlayer.jump();
                                }
                            }
                        }
                        break;
                }
                break;
            case "Heatseeker":
                if (e.getState() == Event.State.PRE) {
                    if (mc.thePlayer.onGround) {
                        if (timerUtil.hasTimeElapsed(300, true)) {
                            strafe = !strafe;
                        }
                        if (strafe) {
                            MovementUtil.setSpeed(1.5);
                        }
                    }
                }
                break;
            case "Mineland":
                if (e.getState() == Event.State.PRE) {
                    stage++;
                    if (stage == 1)
                        mc.thePlayer.motionY = 0.2;

                    if (mc.thePlayer.onGround && stage > 1)
                        MovementUtil.setSpeed(0.5);

                    if (stage % 14 == 0)
                        stage = 0;
                }
                break;
            case "Vulcan":
                if (e.getState() == Event.State.PRE) {
                    if (mc.thePlayer.onGround) {
                        if (MovementUtil.isMoving()) {
                            mc.thePlayer.jump();
                            MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() * 1.6);
                            inAirTicks = 0;
                        }
                    } else {
                        inAirTicks++;
                        if (inAirTicks == 1)
                            MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() * 1.16);
                    }
                }
                break;
            case "Zonecraft":
                if (e.getState() == Event.State.PRE) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                        MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() * 1.8);
                        stage = 0;
                    } else {
                        if (stage == 0 && !mc.thePlayer.isCollidedHorizontally)
                            mc.thePlayer.motionY = -0.4;
                        stage++;
                    }
                }
                break;
            case "Matrix":
                if (MovementUtil.isMoving()) {
                    if (mc.thePlayer.onGround && mc.thePlayer.motionY < 0.003) {
                        mc.thePlayer.jump();
                        mc.timer.timerSpeed = 1.0f;
                    }
                    if (mc.thePlayer.motionY > 0.003) {
                        mc.thePlayer.motionX *= speed;
                        mc.thePlayer.motionZ *= speed;
                        mc.timer.timerSpeed = 1.05f;
                    }
                    speed = 1.0012f;
                }
                break;
            case "HurtTime":
                if (MovementUtil.isMoving()) {
                    if (mc.thePlayer.hurtTime <= 0) {
                        mc.thePlayer.motionX *= 1.001f;
                        mc.thePlayer.motionZ *= 1.001f;
                    } else {
                        mc.thePlayer.motionX *= 1.0294f;
                        mc.thePlayer.motionZ *= 1.0294f;
                    }
                    if (mc.thePlayer.onGround && mc.thePlayer.motionY < 0.003) {
                        mc.thePlayer.jump();
                    }
                }
                break;
            case "Vanilla":
                if (MovementUtil.isMoving()) {
                    MovementUtil.setSpeed(vanillaSpeed.getValue().doubleValue() / 4);
                }
                break;
            case "BHop":
                if (MovementUtil.isMoving()) {
                    MovementUtil.setSpeed(vanillaSpeed.getValue().doubleValue() / 4);
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    }
                }
                break;
            case "Verus":
                switch (verusMode.getValue()) {
                    case "Low":
                        if (e.getState() == Event.State.PRE) {
                            if (MovementUtil.isMoving()) {
                                if (mc.thePlayer.onGround) {
                                    mc.thePlayer.jump();
                                    wasOnGround = true;
                                } else if (wasOnGround) {
                                    if (!mc.thePlayer.isCollidedHorizontally) {
                                        mc.thePlayer.motionY = -0.0784000015258789;
                                    }
                                    wasOnGround = false;
                                }
                                MovementUtil.setSpeed(0.33);
                            } else {
                                mc.thePlayer.motionX = mc.thePlayer.motionZ = 0;
                            }
                        }
                        break;
                    case "Normal":
                        if (e.getState() == Event.State.PRE) {
                            if (MovementUtil.isMoving()) {
                                if (mc.thePlayer.onGround) {
                                    mc.thePlayer.jump();
                                    MovementUtil.setSpeed(0.48);
                                } else {
                                    MovementUtil.setSpeed(MovementUtil.getSpeed());
                                }
                            } else {
                                MovementUtil.setSpeed(0);
                            }
                        }
                        break;
                }
                break;
            case "Viper":
                switch (viperMode.getValue()) {
                    case "High":
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.motionY = 0.7;
                        }
                        break;
                    case "Normal":
                        if (mc.thePlayer.onGround) {
                            mc.thePlayer.motionY = 0.42;
                        }
                        break;
                }
                MovementUtil.setSpeed(MovementUtil.getBaseMoveSpeed() * 1.2);
                break;
            case "Strafe":
                if (e.getState() == Event.State.PRE && MovementUtil.isMoving()) {
                    if (mc.thePlayer.onGround) {
                        mc.thePlayer.jump();
                    } else {
                        MovementUtil.setSpeed(MovementUtil.getSpeed());
                    }
                }
                break;
        }

    }

    @EventTarget
    public void onMoveEvent(MoveEvent e) {
        if (mode.getValue().equals("Watchdog")) {
            switch (watchdogMode.getValue()) {
                case "Ground":
                    strafe = !strafe;
                    if (mc.thePlayer.onGround && MovementUtil.isMoving() && mc.theWorld.getBlockState(new BlockPos(mc.thePlayer.posX + e.getX(), mc.thePlayer.posY, mc.thePlayer.posZ + e.getZ())).getBlock() == Blocks.air && !mc.thePlayer.isCollidedHorizontally) {
                        if (strafe || groundSpeed.getValue().doubleValue() >= 1.6)
                            PacketUtil.sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX + e.getX(), mc.thePlayer.posY, mc.thePlayer.posZ + e.getZ(), true));
                        e.setSpeed(MovementUtil.getBaseMoveSpeed() * groundSpeed.getValue().doubleValue());
                        break;
                    }
                    break;
                case "Low Hop":
                    if (MovementUtil.isMoving()) {
                        if (mc.thePlayer.onGround)
                            inAirTicks = 0;
                        else
                            inAirTicks++;
                        if (inAirTicks == 5)
                            e.setY(mc.thePlayer.motionY = -0.19);
                    }
                    break;
            }
        }
        TargetStrafe.strafe(e);
    }

    @EventTarget
    public void onPlayerMoveUpdateEvent(MoveInputEvent e) {
        if (mode.getValue().equals("Watchdog") && (watchdogMode.getValue().equals("Hop") || watchdogMode.getValue().equals("Dev") || watchdogMode.getValue().equals("Low Hop")) && mc.thePlayer.fallDistance < 1 && !mc.thePlayer.isPotionActive(Potion.jump)) {
            if (MovementUtil.isMoving()) {
                switch (watchdogMode.getValue()) {
                    case "Low Hop":
                    case "Hop":
                        if (mc.thePlayer.onGround)
                            speed = 1.5f;
                        speed -= 0.025;
                        e.applyMotion(MovementUtil.getBaseMoveSpeed() * speed, 0.55f);
                        break;
                    case "Dev":
                        if (mc.thePlayer.onGround) {
                            moveSpeed = MovementUtil.getBaseMoveSpeed() * 2.1475 * 0.76;
                            wasOnGround = true;
                        } else if (wasOnGround) {
                            moveSpeed = lastDist - 0.81999 * (lastDist - MovementUtil.getBaseMoveSpeed());
                            moveSpeed *= 1 / 0.91;
                            wasOnGround = false;
                        } else {
                            moveSpeed -= TargetStrafe.canStrafe() ? lastDist / 100.0 : lastDist / 150.0;
                        }
                        if (mc.thePlayer.isInWater() || mc.thePlayer.isInLava()) {
                            speed = MovementUtil.getBaseMoveSpeed() * 0.25;
                        } else {
                            speed = Math.max(moveSpeed, MovementUtil.getBaseMoveSpeed());
                        }
                        e.applyMotion(speed, 0.6f);
                        break;
                }
            } else {
                e.applyMotion(0, 0);
            }
        }
    }

    @EventTarget
    public void onPacketReceiveEvent(PacketReceiveEvent e) {
        if (e.getPacket() instanceof S08PacketPlayerPosLook) {
            this.setToggle(!this.isToggle());
        }
    }

    @Override
    public void onEnable() {
        speed = 1.5f;
        timerUtil.reset();
        if (mc.thePlayer != null) {
            wasOnGround = mc.thePlayer.onGround;
        }
        inAirTicks = 0;
        moveSpeed = 0;
        stage = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        mc.timer.timerSpeed = 1;
        mc.thePlayer.motionX = 0;
        mc.thePlayer.motionZ = 0;
        super.onDisable();
    }

}