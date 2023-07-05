package top.youm.rocchi.core.ui.clickgui.music;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import top.youm.rocchi.core.ui.Component;
import top.youm.rocchi.core.ui.clickgui.music.sub.Button;
import top.youm.rocchi.core.ui.clickgui.music.layout.Layout;
import top.youm.rocchi.core.ui.clickgui.music.layout.container.Container;
import top.youm.rocchi.utils.render.RoundedUtil;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayerScreen extends GuiScreen {
    public static int x, y;
    public static int screenWidth = 450, screenHeight = 260;
    ArrayList<Button> components = new ArrayList<>();
    public MusicPlayerScreen() {
        components.add(new Button());
        components.add(new Button());
        components.add(new Button());
        components.add(new Button());
        components.add(new Button());
        components.add(new Button());
        components.add(new Button());
        components.add(new Button());
        components.add(new Button());
        components.add(new Button());
        components.add(new Button());
        components.add(new Button());
        components.add(new Button());
    }
    @Override
    public void initGui() {
        super.initGui();
        ScaledResolution sr = new ScaledResolution(mc);
        x = sr.getScaledWidth() / 2 - screenWidth / 2;
        y = sr.getScaledHeight() / 2 - screenHeight / 2;
    }
    Container container = new Container(Layout.Row,components,20);
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        RoundedUtil.drawRound(x,y,screenWidth,screenHeight,2,new Color(40,40,40));
        container.build(x+100,y,screenWidth - 100,height);
        List<Button> test = (List<Button>) container.getComponents();
        for (Button component : test) {
            component.draw(0,0,mouseX,mouseY);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
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
}
