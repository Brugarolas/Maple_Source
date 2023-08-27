package top.youm.maple.core.module.modules.visual;

import com.darkmagician6.eventapi.EventTarget;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import top.youm.maple.common.events.*;
import top.youm.maple.core.module.Module;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.math.MathUtil;
import top.youm.maple.utils.render.RenderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

/**
 * Created by John on 2017/06/24.
 */
public class DamageParticle extends Module {
	public DamageParticle() {
		super("DMGParticle", ModuleCategory.VISUAL, Keyboard.KEY_NONE);
	}

	private HashMap<EntityLivingBase, Float> healthMap = new HashMap<>();
	private List<Particle> particles = new ArrayList<>();

	@EventTarget
	public void onRespawn(RespawnPlayerEvent event) {
		this.particles.clear();
		this.healthMap.clear();
	}

	@EventTarget
	public void onTick(TickEvent event) {
		// move particles
		for (Particle particle : particles) {
			particle.ticks++;

			if (particle.ticks <= 10) {
				particle.location.setY(particle.location.getY() + particle.ticks * 0.005);
			}

			if (particle.ticks > 20) {
				particles.remove(particle);
			}
		}

	}

	@EventTarget
	public void onLivingUpdate(LivingUpdateEvent event) {
		final EntityLivingBase entity = event.getEntity();

		if (entity == mc.thePlayer)
			return;

		// detect

		if (!healthMap.containsKey(entity))
			healthMap.put(entity, entity.getHealth());

		final float before = healthMap.get(entity);
		final float after = entity.getHealth();

		if (before != after) {
			String text;

			if ((before - after) < 0) {
				text = EnumChatFormatting.GREEN + "" + MathUtil.roundToPlace((before - after) * -1, 1);
			} else {
				text = EnumChatFormatting.YELLOW + "" + MathUtil.roundToPlace((before - after), 1);
			}

			Location location = new Location(entity);

			location.setY(entity.getEntityBoundingBox().minY
					+ ((entity.getEntityBoundingBox().maxY - entity.getEntityBoundingBox().minY) / 2));

			location.setX((location.getX() - 0.5) + (new Random(System.currentTimeMillis()).nextInt(5) * 0.1));
			location.setZ((location.getZ() - 0.5) + (new Random(System.currentTimeMillis() + 1).nextInt(5) * 0.1));

			particles.add(new Particle(location, text));

			healthMap.remove(entity);
			healthMap.put(entity, entity.getHealth());
		}
	}

	@EventTarget
	public void onRender3D(Render3DEvent event) {
		for (Particle particle : this.particles) {
			final double x = particle.location.getX() - mc.getRenderManager().renderPosX;
			final double y = particle.location.getY() - mc.getRenderManager().renderPosY;
			final double z = particle.location.getZ() - mc.getRenderManager().renderPosZ;

			GlStateManager.pushMatrix();

			GlStateManager.enablePolygonOffset();
			GlStateManager.doPolygonOffset(1.0F, -1500000.0F);

			GlStateManager.translate((float) x, (float) y, (float) z);
			GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
			float var10001 = mc.gameSettings.thirdPersonView == 2 ? -1.0F : 1.0F;
			GlStateManager.rotate(mc.getRenderManager().playerViewX, var10001, 0.0F, 0.0F);
			double scale = 0.03;
			GlStateManager.scale(-scale, -scale, scale);

			RenderUtil.enableGL2D();
			RenderUtil.disableGL2D();

			GL11.glDepthMask(false);
			mc.fontRendererObj.drawStringWithShadow(particle.text,
					-(mc.fontRendererObj.getStringWidth(particle.text) / 2.0f),
					-(mc.fontRendererObj.FONT_HEIGHT - 1), 0);
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
			GL11.glDepthMask(true);

			GlStateManager.doPolygonOffset(1.0F, 1500000.0F);
			GlStateManager.disablePolygonOffset();

			GlStateManager.popMatrix();
		}
	}
}

class Particle {
	public Particle(Location location, String text) {
		this.location = location;
		this.text = text;
		this.ticks = 0;
	}

	public int ticks;
	public Location location;
	public String text;
}
class Location {
	private double x;
	private double y;
	private double z;
	private float yaw;
	private float pitch;

	public Location(final double x, final double y, final double z, final float yaw, final float pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}

	public Location(final double x, final double y, final double z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 0.0f;
		this.pitch = 0.0f;
	}

	public Location(BlockPos pos) {
		this.x = pos.getX();
		this.y = pos.getY();
		this.z = pos.getZ();
		this.yaw = 0.0f;
		this.pitch = 0.0f;
	}

	public Location(EntityLivingBase entity) {
		this.x = entity.posX;
		this.y = entity.posY;
		this.z = entity.posZ;
		this.yaw = 0.0f;
		this.pitch = 0.0f;
	}

	public Location(final int x, final int y, final int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = 0.0f;
		this.pitch = 0.0f;
	}

	public Location add(final int x, final int y, final int z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Location add(final double x, final double y, final double z) {
		this.x += x;
		this.y += y;
		this.z += z;
		return this;
	}

	public Location subtract(final int x, final int y, final int z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Location subtract(final double x, final double y, final double z) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		return this;
	}

	public Block getBlock() {
		return Minecraft.getMinecraft().theWorld.getBlockState(this.toBlockPos()).getBlock();
	}

	public double getX() {
		return this.x;
	}

	public Location setX(final double x) {
		this.x = x;
		return this;
	}

	public double getY() {
		return this.y;
	}

	public Location setY(final double y) {
		this.y = y;
		return this;
	}

	public double getZ() {
		return this.z;
	}

	public Location setZ(final double z) {
		this.z = z;
		return this;
	}

	public float getYaw() {
		return this.yaw;
	}

	public Location setYaw(final float yaw) {
		this.yaw = yaw;
		return this;
	}

	public float getPitch() {
		return this.pitch;
	}

	public Location setPitch(final float pitch) {
		this.pitch = pitch;
		return this;
	}

	public static Location fromBlockPos(final BlockPos blockPos) {
		return new Location(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}

	public BlockPos toBlockPos() {
		return new BlockPos(this.getX(), this.getY(), this.getZ());
	}

	public double distanceTo(Location loc) {
		double dx = loc.x - this.x;
		double dz = loc.z - this.z;
		double dy = loc.y - this.y;
		return Math.sqrt(dx * dx + dy * dy + dz * dz);
	}

	public double distanceToXZ(Location loc) {
		double dx = loc.x - this.x;
		double dz = loc.z - this.z;
		return Math.sqrt(dx * dx + dz * dz);
	}

	public double distanceToY(Location loc) {
		double dy = loc.y - this.y;
		return Math.sqrt(dy * dy);
	}

	public Vec3 toVector(){
		return new Vec3(this.x, this.y, this.z);
	}
}

