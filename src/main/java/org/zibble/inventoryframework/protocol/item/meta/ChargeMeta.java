package org.zibble.inventoryframework.protocol.item.meta;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import org.zibble.inventoryframework.protocol.item.objects.FireworkEffect;

public class ChargeMeta extends ItemMeta {

    private static final String EXPLOSION = "Explosion";

    private FireworkEffect fireworkEffect;

    protected ChargeMeta() {
        super();
    }

    public FireworkEffect getFireworkEffect() {
        return fireworkEffect;
    }

    public void setFireworkEffect(FireworkEffect fireworkEffect) {
        this.fireworkEffect = fireworkEffect;
    }

    @Override
    public void applyTo(NBTCompound compound) {
        super.applyTo(compound);
        if (this.fireworkEffect != null)
            compound.setTag(EXPLOSION, MetaUtil.asMeta(this.fireworkEffect));
    }
}