package top.youm.maple.core.ui.hud.components;

import top.youm.maple.core.module.modules.visual.HUD;
import top.youm.maple.core.module.modules.visual.Statistics;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.ShadowUtils;

import java.awt.*;

/**
 * @author YouM
 * Created on 2023/8/23
 */
public class StatisticsUI implements HUDComponent<Statistics>{
    private float width = 170,height = 75;
    @Override
    public void draw(Statistics statistics) {
        if(statistics.shadow.getValue()){
            ShadowUtils.shadow(
                    13.0f
                    , ()-> RenderUtil.drawRect(5,50,width,height,new Color(0,0,0,130))
                    , ()-> RenderUtil.drawRect(5,50,width,height,new Color(0,0,0,130))
            );
        }else{
            RenderUtil.drawRect(5,50,width,height,new Color(0,0,0,130));
        }

        RenderUtil.drawRect(15,50 + FontLoaders.robotoB26.getHeight() + 4,150,1, HUD.getHUDThemeColor());
        FontLoaders.robotoB26.drawCenteredStringWithShadow("Session Info",5 + (width / 2.0f),50 + 4,-1);
        FontLoaders.aovel22.drawString("Kills:",8,60 + FontLoaders.robotoB26.getHeight(),-1);
        FontLoaders.aovel22.drawString("Wins:",8,64 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight(),-1);
        FontLoaders.aovel22.drawString("Deaths:",8,68 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight() * 2,-1);
        FontLoaders.aovel22.drawString("Server IP:",8,72 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight() * 3,-1);

        FontLoaders.aovel22.drawString(String.valueOf(statistics.kills),width - 2 - FontLoaders.aovel22.getStringWidth(String.valueOf(statistics.kills)),60 + FontLoaders.robotoB26.getHeight(),-1);
        FontLoaders.aovel22.drawString(String.valueOf(statistics.wins),width - 2 - FontLoaders.aovel22.getStringWidth(String.valueOf(statistics.wins)),64 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight(),-1);
        FontLoaders.aovel22.drawString(String.valueOf(statistics.death),width - 2 - FontLoaders.aovel22.getStringWidth(String.valueOf(statistics.death)),68 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight() * 2,-1);
        if(mc.isSingleplayer()){
            FontLoaders.aovel22.drawString("Single Player",width - 2 - FontLoaders.aovel22.getStringWidth("Single Player"),72 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight() * 3,-1);
        }else{
            FontLoaders.aovel22.drawString(mc.getCurrentServerData().serverIP,width - 2 - FontLoaders.aovel22.getStringWidth(mc.getCurrentServerData().serverIP.toLowerCase()),74 + FontLoaders.robotoB26.getHeight() + FontLoaders.aovel22.getHeight() * 3,-1);
        }
    }
}
