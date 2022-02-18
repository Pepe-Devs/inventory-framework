package com.pepedevs.inventoryframework.protocol.item.meta;

import com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import com.github.retrooper.packetevents.protocol.nbt.NBTList;
import com.github.retrooper.packetevents.protocol.nbt.NBTString;
import com.github.retrooper.packetevents.protocol.nbt.NBTType;
import com.pepedevs.inventoryframework.protocol.item.objects.Profile;
import com.pepedevs.inventoryframework.protocol.item.objects.Property;

public class SkullMeta extends ItemMeta {

    public static final String SKULL_OWNER = "SkullOwner";

    private Profile profile;

    protected SkullMeta() {
        super();
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    @Override
    public void applyTo(NBTCompound compound) {
        super.applyTo(compound);
        if (profile != null) {
            NBTCompound owner = new NBTCompound();
            if (profile.getName() != null && !profile.getName().isEmpty()) {
                owner.setTag("Name", new NBTString(profile.getName()));
            }

            if (profile.getUuid() != null) {
                owner.setTag("Id", new NBTString(profile.getUuid().toString()));
            }

            if (!profile.getProperties().isEmpty()) {
                NBTCompound properties = new NBTCompound();
                NBTList<NBTCompound> list = new NBTList<>(NBTType.COMPOUND);
                for (Property property : profile.getProperties()) {
                    NBTCompound prop = new NBTCompound();
                    prop.setTag("Value", new NBTString(property.getValue()));
                    if (property.getSignature() != null) {
                        prop.setTag("Signature", new NBTString(property.getSignature()));
                    }
                }
                properties.setTag("textures", list);
                owner.setTag("Properties", properties);
            }

            compound.setTag(SKULL_OWNER, owner);
        }
    }

}
