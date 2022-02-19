package org.zibble.inventoryframework.protocol.item.meta;

import com.github.retrooper.packetevents.protocol.nbt.*;
import org.zibble.inventoryframework.protocol.item.objects.PotionEffect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PotionMeta extends ItemMeta {

    private static final String AMPLIFIER = "Amplifier";
    private static final String AMBIENT = "Ambient";
    private static final String DURATION = "Duration";
    private static final String SHOW_PARTICLES = "ShowParticles";
    private static final String POTION_EFFECTS = "CustomPotionEffects";
    private static final String ID = "Id";

    private List<PotionEffect> potionEffects;

    protected PotionMeta() {
        super();
        this.potionEffects = new ArrayList<>();
    }

    public List<PotionEffect> getPotionEffects() {
        return Collections.unmodifiableList(potionEffects);
    }

    public void setPotionEffects(List<PotionEffect> potionEffects) {
        this.potionEffects = potionEffects;
    }

    @Override
    public void applyTo(NBTCompound compound) {
        super.applyTo(compound);
        if (this.potionEffects.isEmpty()) return;
        NBTList<NBTCompound> list = new NBTList<>(NBTType.COMPOUND);
        for (PotionEffect potionEffect : this.potionEffects) {
            NBTCompound potion = new NBTCompound();
            potion.setTag(ID, new NBTByte((byte) potionEffect.getEffectType().getID()));
            potion.setTag(AMPLIFIER, new NBTByte((byte) potionEffect.getAmplifier()));
            potion.setTag(DURATION, new NBTInt((int) (potionEffect.getDuration().toMillis() / 50)));
            potion.setTag(AMBIENT, new NBTByte((byte) (potionEffect.isAmbient() ? 1 : 0)));
            potion.setTag(SHOW_PARTICLES, new NBTByte((byte) (potionEffect.isParticles() ? 1 : 0)));
            list.addTag(potion);
        }
        compound.setTag(POTION_EFFECTS, list);
    }
}