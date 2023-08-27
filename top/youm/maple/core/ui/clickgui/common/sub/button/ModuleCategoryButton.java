package top.youm.maple.core.ui.clickgui.common.sub.button;

import net.minecraft.client.renderer.GlStateManager;
import top.youm.maple.core.module.ModuleCategory;
import top.youm.maple.core.ui.clickgui.common.MapleClickGUI;
import top.youm.maple.core.ui.font.CFontRenderer;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.render.CircleManager;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.RoundedUtil;
import top.youm.maple.utils.render.Stencil;
import top.youm.maple.utils.tools.StringUtils;

import java.awt.*;


/**
 * @author YouM
 * Created on 2023/8/22
 */
public class ModuleCategoryButton extends ButtonComponent {

    public final ModuleCategory category;
    private final CFontRenderer commonIcon = FontLoaders.commonIcon;
    private CircleManager clickCircle = new CircleManager();
    private int animationX,animationAlpha;
    public float offsetY;

    public ModuleCategoryButton(ModuleCategory category) {
        super(StringUtils.upperCaseLowerOther(category.name()), 40, 40);
        this.category = category;
    }

    @Override
    public void drawComponent() {
        if (onComponentHover()) {
            animationX = animator.animate(20,animationX,0.4f);
            animationAlpha = animator.animate(255,animationAlpha,0.1f);
        }else{
            animationX = animator.animate(0,animationX,0.2f);
            animationAlpha = animator.animate(0,animationAlpha,0.1f);
        }
        RoundedUtil.drawRound(x + 12 + animationX, y - 2, FontLoaders.aovel22.getStringWidth(getName()) + 10,20,3, new Color(85,85,85,animationAlpha));
        if(onComponentHover()){
            FontLoaders.aovel22.drawCenteredStringWithShadow(getName(),x + 12 + animationX + ((FontLoaders.aovel22.getStringWidth(getName()) + 10) / 2.0f) ,y + 3, new Color(210, 210, 210,animationAlpha).getRGB());
        }
        GlStateManager.pushMatrix();
        Stencil.write(false);
        RenderUtil.drawBorderedCircle(x ,y + commonIcon.getHeight()/2.0f,20, new Color(0,0,0,0).getRGB() ,new Color(0,0,0,0).getRGB());
        Stencil.erase(true);
        GlStateManager.enableBlend();
        clickCircle.drawCircles();
        Stencil.dispose();
        GlStateManager.popMatrix();

        commonIcon.drawCenteredStringWithShadow(getICON(), x, y, new Color(192, 192, 192).getRGB());
    }

    @Override
    public void onMouseClick(int mouseButton) {
        if(onComponentHover() && mouseButton == 0){
            MapleClickGUI.category = this.category;
            clickCircle.addCircle(x + 0.5f,y + commonIcon.getHeight()/2.0f - 0.5f,20,40,mc.gameSettings.keyBindAttack.getKeyCode());
        }
    }

    @Override
    public void onMouseRelease(int mouseButton) {

    }

    @Override
    protected boolean onComponentHover() {
        return isHovered(x - 17,y - 10, 34, 34, mouseX, mouseY);
    }

    @Override
    public void update(float x, float y, int mouseX, int mouseY) {
        super.update(x, y, mouseX, mouseY);
        clickCircle.runCircles();
    }

    public String getICON() {
        String icon = "";
        switch (category) {
            case COMBAT:
                icon = "E";
                break;
            case MOVEMENT:
                icon = "C";
                break;
            case VISUAL:
                icon = "B";
                break;
            case PLAYER:
                icon = "D";
                break;
            case WORLD:
                icon = "A";
        }
        return icon;
    }
}
