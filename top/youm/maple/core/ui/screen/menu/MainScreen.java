package top.youm.maple.core.ui.screen.menu;

import top.youm.maple.utils.render.gl.ShaderUtil;
import net.minecraft.client.gui.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;

/**
 * @author YouM
 */
public class MainScreen extends GuiScreen implements GuiYesNoCallback {
    private final ShaderUtil backgroundShader = new ShaderUtil("Maple/shader/background.frag");
    private static final Logger logger = LogManager.getLogger();
    private long initTime = System.currentTimeMillis();


    @Override
    public void initGui() {
        initTime = System.currentTimeMillis();
        /* 初始化按钮组件 */
        this.buttonList.add(new MenuButton(0,this.width / 2 ,this.height / 2 - 30,"Single Player","g"));
        this.buttonList.add(new MenuButton(1,this.width / 2 ,this.height / 2,"Multi Player","h"));
        this.buttonList.add(new MenuButton(2,this.width / 2 ,this.height / 2 + 30,"Game Option","m"));
        this.buttonList.add(new IconButton(3,this.width - 25 ,5,20,20,"l"));
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        switch (button.id){
            case 0:
                this.mc.displayGuiScreen(new GuiSelectWorld(this));
                break;
            case 1:
                this.mc.displayGuiScreen(new GuiMultiplayer(this));
                break;
            case 2:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 3:
                this.mc.shutdown();
                break;
            default:
                logger.error("button id error");
                System.exit(0);
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        this.drawBackground();
        super.drawScreen(mouseX,mouseY,partialTicks);
//        drawLoadingScreen();
    }
    public void drawBackground(){
        backgroundShader.init();
        backgroundShader.setUniformf("time",(System.currentTimeMillis() - initTime) / 1000f);
        backgroundShader.setUniformf("resolution",this.width / 2.0f,this.height / 2.0f);
        ShaderUtil.drawQuads();
        backgroundShader.unload();
    }
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }
    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
    }
    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
    }
    int anim = 255;
//    public void drawLoadingScreen(){
//        ScaledResolution sr = new ScaledResolution(this.mc);
//        new Thread(()->{
//            try {
//                Thread.sleep(4000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            atomic.set(true);
//        }).start();
//        if(atomic.get() && anim != 0) {
//            anim = AnimationUtils.animateI(0, anim, 0.07f);
//            if (anim == 32){
//                anim -= 1;
//            }
//            if(anim <= 3){
//                anim = 0;
//            }
//        }
//        if(anim != 0){
//            RenderUtil.drawRect(0,0,sr.getScaledWidth(),sr.getScaledHeight(),new Color(0,0,0, anim));
//            FontLoaders.robotoR34.drawCenteredStringWithShadow("Authenticating...",sr.getScaledWidth() / 2.0f, sr.getScaledHeight() / 2.0f - FontLoaders.robotoR34.getHeight() / 2.0f,new Color(255,255,255, anim).getRGB());
//        }
//    }
    public void drawCenteredStringWithShadow(String text, float x, float y, int color) {
        mc.fontRendererObj.drawStringWithShadow(text, x - (float) (mc.fontRendererObj.getStringWidth(text) / 2), y, color);
    }
}
