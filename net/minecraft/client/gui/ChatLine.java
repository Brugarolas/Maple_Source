package net.minecraft.client.gui;

import net.minecraft.util.IChatComponent;
import top.youm.maple.utils.AnimationUtils;

public class ChatLine
{
    /** GUI Update Counter value this Line was created at */
    private final int updateCounterCreated;
    private final IChatComponent lineString;

    public AnimationUtils animator = new AnimationUtils();

    public float font;
    public float x;
    public void update(boolean in,float fontWidth,float chatWidth){
        if (in) {
            font = animator.animate(fontWidth, font, 0.05f);
            x = animator.animate(chatWidth, x, 0.05f);
        } else {
            font = animator.animate(-5, font, 0.05f);
            x = animator.animate(0, x, 0.05f);
        }
    }
    /**
     * int value to refer to existing Chat Lines, can be 0 which means unreferrable
     */
    private final int chatLineID;

    public ChatLine(int p_i45000_1_, IChatComponent p_i45000_2_, int p_i45000_3_)
    {
        this.lineString = p_i45000_2_;
        this.updateCounterCreated = p_i45000_1_;
        this.chatLineID = p_i45000_3_;
    }

    public IChatComponent getChatComponent()
    {
        return this.lineString;
    }

    public int getUpdatedCounter()
    {
        return this.updateCounterCreated;
    }

    public int getChatLineID()
    {
        return this.chatLineID;
    }
}
