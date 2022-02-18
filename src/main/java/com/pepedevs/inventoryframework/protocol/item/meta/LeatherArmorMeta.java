package com.pepedevs.inventoryframework.protocol.item.meta;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTInt;

import java.awt.*;

public class LeatherArmorMeta extends ItemMeta {

    private static final String COLOR = "color";

    private Color color;

    protected LeatherArmorMeta() {
        super();
    }

    public Color getColor() {
        return new Color(this.color.getColorSpace(), this.color.getComponents(new float[0]), this.color.getAlpha());
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public void applyTo(NBTCompound compound) {
        super.applyTo(compound);
        if (this.color == null) return;
        NBTCompound display = compound.getCompoundTagOrNull(DISPLAY);
        if (display == null)
            compound.setTag(DISPLAY, display = new NBTCompound());
        display.setTag(COLOR, new NBTInt(this.color.getRGB()));
    }
}
