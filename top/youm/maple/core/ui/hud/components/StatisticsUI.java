package top.youm.maple.core.ui.hud.components;

import top.youm.maple.core.module.modules.visual.HUD;
import top.youm.maple.core.module.modules.visual.Statistics;
import top.youm.maple.core.ui.font.FontLoaders;
import top.youm.maple.utils.render.RenderUtil;
import top.youm.maple.utils.render.RoundedUtil;
import top.youm.maple.utils.render.ShadowUtils;

import java.awt.*;

/**
 * @author YouM
 * Created on 2023/8/23
 */
public class StatisticsUI implements HUDComponent<Statistics> {
    private final float width = 100;
    private final float height = 60;

    @Override
    public void draw(Statistics statistics) {
        float yPosition;
        if (HUD.mode.getValue().equals("Tenacity")) {
            yPosition = 60;
        } else {
            yPosition = 50;
        }

        if (statistics.shadow.getValue()) {
            ShadowUtils.shadow(
                    13.0f
                    , () -> RenderUtil.drawRect(5, yPosition - 30, width, height, HUD.getHUDThemeColor())
                    , () -> RenderUtil.drawRect(5, yPosition - 30, width, height, HUD.getHUDThemeColor())
            );
        }
        RoundedUtil.drawRoundOutline(5, yPosition - 30, width, height, 1, 0.2f, new Color(0, 0, 0, 130), HUD.getHUDThemeColor());


        RenderUtil.drawRect(5, yPosition - 30 + FontLoaders.robotoB26.getHeight() + 4, 100, 1, HUD.getHUDThemeColor());
        FontLoaders.statistics.drawCenteredStringWithShadow("Statistics", 5 + (width / 2.0f), yPosition - 30 + 4, -1);
        FontLoaders.robotoR22.drawString("Kills:", 8, yPosition - 20 + FontLoaders.robotoB26.getHeight(), -1);
        FontLoaders.robotoR22.drawString("Wins:", 8, yPosition - 16 + FontLoaders.robotoB26.getHeight() + FontLoaders.robotoR22.getHeight(), -1);
        FontLoaders.robotoR22.drawString("Deaths:", 8, yPosition - 12 + FontLoaders.robotoB26.getHeight() + FontLoaders.robotoR22.getHeight() * 2, -1);
        FontLoaders.robotoR22.drawString(String.valueOf(statistics.kills), width - 2 - FontLoaders.robotoR22.getStringWidth(String.valueOf(statistics.kills)), yPosition - 20 + FontLoaders.robotoB26.getHeight(), -1);
        FontLoaders.robotoR22.drawString(String.valueOf(statistics.wins), width - 2 - FontLoaders.robotoR22.getStringWidth(String.valueOf(statistics.wins)), yPosition - 18 + (FontLoaders.robotoB26.getHeight() * 2), -1);
        FontLoaders.robotoR22.drawString(String.valueOf(statistics.death), width - 2 - FontLoaders.robotoR22.getStringWidth(String.valueOf(statistics.death)), yPosition - 16 + (FontLoaders.robotoB26.getHeight() * 3), -1);
    }
}
