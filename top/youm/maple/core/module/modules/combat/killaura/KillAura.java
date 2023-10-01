package top.youm.maple.core.module.modules.combat.killaura;

import com.darkmagician6.eventapi.EventTarget;
import com.darkmagician6.eventapi.events.Event;
import de.florianmichael.viamcp.fixes.AttackOrder;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.C0APacketAnimation;
import net.minecraft.potion.Potion;
import org.lwjgl.input.Keyboard;
import top.youm.maple.Maple;
import top.youm.maple.common.events.*;
import top.youm.maple.common.settings.impl.CheckBoxSetting;
import top.youm.maple.common.settings.impl.SelectButtonSetting;
import top.youm.maple.common.settings.impl.SliderSetting;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.module.modules.combat.Targets;
import top.youm.maple.core.module.modules.world.SafeScaffold;
import top.youm.maple.utils.TimerUtil;
import top.youm.maple.utils.liquidbounce.RotationUtils;
import top.youm.maple.utils.liquidbounce.VecRotation;
import top.youm.maple.utils.math.MathUtil;
import top.youm.maple.utils.network.PacketUtil;
import top.youm.maple.utils.player.RotationUtil;


/**
 * @author YouM
 */
public class KillAura extends Module {

    private float yaw = 0;
    public static final SliderSetting reach = new SliderSetting("Reach", 3.3, 6, 1, 0.01);
    private final SliderSetting minCps = new SliderSetting("Min CPS", 6, 20, 1, 1);
    private final SliderSetting maxCps = new SliderSetting("Max CPS", 11, 20, 1, 1);
    private final SliderSetting maxRotateSpeed = new SliderSetting("Max Rotate Speed", 180, 180, 0, 1);
    private final SliderSetting minRotateSpeed = new SliderSetting("Min Rotate Speed", 180, 180, 0, 1);
    private final SelectButtonSetting autoBlockMode = new SelectButtonSetting("AutoBlock Mode", "Vulcan", "Vulcan", "Watchdog", "Interaction", "AAC", "Verus", "New NCP", "Old Intave");
    private final CheckBoxSetting autoBlock = new CheckBoxSetting("AutoBlock", false);
    public static final CheckBoxSetting footCircle = new CheckBoxSetting("foot circle", true);
    public static final CheckBoxSetting scanCircle = new CheckBoxSetting("scan circle", true);
    private final CheckBoxSetting movementFix = new CheckBoxSetting("Movement Fix", true);
    private final SelectButtonSetting rotateMode = new SelectButtonSetting("Rotate Mode", "Dynamic", "Dynamic", "Smooth", "LiquidBounce", "Vanilla");
    private final CheckBoxSetting followRotation = new CheckBoxSetting("Rotate Follow", false);
    private final SliderSetting randomValue = new SliderSetting("random value", 0.0, 2.0f, 0.0f, 0.1f);
    private final CheckBoxSetting random = new CheckBoxSetting("random", false);
    private final CheckBoxSetting targetUI = new CheckBoxSetting("TargetUI", false);
    public static final SelectButtonSetting targetUIStyle = new SelectButtonSetting("TargetUI Style", "Normal", "Normal", "Gradient");
    private final CheckBoxSetting autoDisable = new CheckBoxSetting("Auto Disable", true);
    private final TimerUtil timer = new TimerUtil();
    private final TimerUtil rotateTimer = new TimerUtil();


    public KillAura() {
        super("Kill Aura", ModuleCategory.COMBAT, Keyboard.KEY_NONE);
        autoBlockMode.addParent(autoBlock, CheckBoxSetting::getValue);
        maxRotateSpeed.addParent(rotateMode, rotateMode -> rotateMode.getValue().equals("LiquidBounce"));
        minRotateSpeed.addParent(rotateMode, rotateMode -> rotateMode.getValue().equals("LiquidBounce"));
        random.addParent(rotateMode, rotateMode -> rotateMode.getValue().equals("LiquidBounce") || rotateMode.getValue().equals("Smooth"));
        randomValue.addParent(random, CheckBoxSetting::getValue);
        targetUIStyle.addParent(targetUI, CheckBoxSetting::getValue);

        new RotationUtils().init();
        this.addSetting(reach, minCps, maxCps, targetUI, targetUIStyle, footCircle, scanCircle, autoDisable, autoBlock, autoBlockMode, movementFix, rotateMode, random, randomValue, followRotation, maxRotateSpeed, minRotateSpeed);
    }
    AuraRenderer auraRenderer = new AuraRenderer();
    @EventTarget
    public void onRender2D(Render2DEvent event) {
        if (!targetUI.getValue()) return;
        auraRenderer.render2D();
    }
    @EventTarget
    public void onTick(TickEvent event) {
        this.setSuffixes(this.minCps.getValue().intValue() + "-" + this.maxCps.getValue().intValue() + " " + reach.getValue().floatValue());

        if (mc.thePlayer != null) {
            if ((mc.thePlayer.getHealth() <= 0.0f || mc.thePlayer.isDead) && this.autoDisable.getValue()) {
                this.setToggle(false);
            }
            auraRenderer.update();
        }
    }
    @EventTarget
    public void onRender3D(Render3DEvent event) {
        auraRenderer.render3D(event);
    }
    private float[] randomRotation;
    @EventTarget
    public void onMotion(MotionEvent event) {
        Targets.INSTANCE.sort();
        Targets.INSTANCE.setTarget();
        if (Targets.INSTANCE.target == null) {
            return;
        }

        if (Maple.getInstance().getModuleManager().getModuleByClass(SafeScaffold.class).isToggle()) {
            Maple.getInstance().getModuleManager().getModuleByClass(SafeScaffold.class).setToggle(false);
        }
        if (Targets.INSTANCE.removeTarget()) return;

        if (event.getState() == Event.State.PRE) rotation(event);

        if (!RotationUtil.isMouseOver(event.getYaw(), event.getPitch(), Targets.INSTANCE.target, reach.getValue().floatValue()))
            return;

        if (autoBlock.getValue() && mc.thePlayer.getCurrentEquippedItem() != null && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword) {
            autoBlock(event);
        }

        if (event.getState() == Event.State.PRE) {
            attack();
        }
    }

    public void attack() {
        if (timer.hasTimeElapsed((1000 / MathUtil.getRandomInRange(minCps.getValue().intValue(), maxCps.getValue().intValue())), true)) {
            AttackOrder.sendFixedAttack(mc.thePlayer, Targets.INSTANCE.target);
            if (mc.thePlayer.fallDistance > 0 && !mc.thePlayer.onGround && !mc.thePlayer.isOnLadder() && !mc.thePlayer.isInWater() && !mc.thePlayer.isPotionActive(Potion.blindness) && mc.thePlayer.ridingEntity == null) {
                mc.thePlayer.onCriticalHit(Targets.INSTANCE.target);
            }
        }
    }

    private void rotation(MotionEvent event) {
        float[] smoothRotations = RotationUtil.getRotationsToEnt(Targets.INSTANCE.target);
        switch (rotateMode.getValue()) {
            case "Dynamic":
                if (rotateTimer.hasTimeElapsed(80, true)) {
                    smoothRotations[0] += MathUtil.getRandomInRange(-7, 7);
                    smoothRotations[1] += MathUtil.getRandomInRange(-5, 10);
                    randomRotation = smoothRotations;
                } else {
                    smoothRotations = randomRotation;
                }
                break;
            case "LiquidBounce":
                VecRotation vecRotation = RotationUtils.searchCenter(Targets.INSTANCE.target.getEntityBoundingBox(), false, true, true, true, mc.thePlayer.getDistanceToEntity(Targets.INSTANCE.target), randomValue.getValue().floatValue(), true);
                smoothRotations = RotationUtil.limitAngleChange(new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch}, new float[]{vecRotation.getRotation().getYaw(), vecRotation.getRotation().getPitch()}, MathUtil.getRandomInRange(minRotateSpeed.getValue().floatValue(), maxRotateSpeed.getValue().floatValue()));
                break;
            case "Vanilla":
                smoothRotations = RotationUtil.getRotationsToEnt(Targets.INSTANCE.target);
                smoothRotations = RotationUtil.limitAngleChange(new float[]{mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch},smoothRotations,90);
                break;
            default:
                smoothRotations = RotationUtil.getSmoothRotations(Targets.INSTANCE.target, 1.2f, 0.9f);
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

    private void autoBlock(MotionEvent event) {

        switch (autoBlockMode.getValue()) {
            case "Watchdog":
                break;
            case "Interaction":
                break;
            case "AAC":
                break;
            case "New NCP":
                break;
            case "Old Intave":
                break;
            default:
        }
        PacketUtil.sendPacket(new C0APacketAnimation());

    }

    @EventTarget
    public void onJumpFixEvent(JumpFixEvent event) {
        if (Targets.INSTANCE.target != null && this.movementFix.getValue()) {
            event.setYaw(yaw);
        }
    }

    @EventTarget
    public void onPlayerMoveUpdateEvent(MoveInputEvent event) {
        if (Targets.INSTANCE.target != null && this.movementFix.getValue()) {
            event.setYaw(yaw);
        }
    }

    @Override
    public void onDisable() {
        Targets.INSTANCE.clear();
    }


    @EventTarget
    public void respawn(RespawnPlayerEvent event) {
        if (autoDisable.getValue()) {
            this.setToggle(false);
        }
    }

    @Override
    public void onEnable() {
        SafeScaffold scaffold = Maple.getInstance().getModuleManager().getModuleByClass(SafeScaffold.class);
        if (scaffold.isToggle()) {
            scaffold.setToggle(false);
        }
    }
}
