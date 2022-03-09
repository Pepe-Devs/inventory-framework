package org.zibble.inventoryframework.protocol.item.meta;

import com.github.retrooper.packetevents.protocol.nbt.*;
import com.github.retrooper.packetevents.util.AdventureSerializer;
import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.*;
import org.zibble.inventoryframework.protocol.Material;
import org.zibble.inventoryframework.protocol.item.ItemStack;
import org.zibble.inventoryframework.protocol.item.objects.enums.Enchantment;

import java.util.*;

public class ItemMeta {

    private static final String NAME = "Name";
    private static final String LORE = "Lore";
    private static final String HIDE_FLAGS = "HideFlags";
    private static final String UNBREAKABLE = "Unbreakable";
    private static final String REPAIR_COST = "RepairCost";

    private @Nullable Component displayName;
    private @NotNull List<@Nullable Component> lore;
    private final Map<@NotNull Enchantment, @Range(from = 1, to = Integer.MAX_VALUE) Integer> enchantments;
    private @Range(from = 0, to = Integer.MAX_VALUE) int repairCost;
    private final @NotNull EnumSet<Flag> flags;
    private boolean unBreakable;

    protected ItemMeta() {
        this.lore = new ArrayList<>();
        this.enchantments = new EnumMap<>(Enchantment.class);
        this.flags = EnumSet.noneOf(Flag.class);
        this.unBreakable = false;
    }

    /**
     * @return new {@link Builder}
     */
    @SuppressWarnings("unchecked")
    @Contract(value = "_ -> new", pure = true)
    public static <T extends ItemMeta> Builder<T> builder(@NotNull Material material) {
        return new Builder<>((T) MetaUtil.getMeta(material));
    }

    @Nullable
    public Component getDisplayName() {
        return displayName;
    }

    public void setDisplayName(@Nullable Component displayName) {
        this.displayName = displayName;
    }

    @NotNull
    public List<@Nullable Component> getLore() {
        return Collections.unmodifiableList(this.lore);
    }

    public void setLore(@NotNull List<@Nullable Component> lore) {
        this.lore = lore;
    }

    @NotNull
    public Map<@NotNull Enchantment, @Range(from = 1, to = Integer.MAX_VALUE) Integer> getEnchantments() {
        return enchantments;
    }

    public void addEnchant(@NotNull Enchantment enchantment, @Range(from = 0, to = Integer.MAX_VALUE) int level) {
        this.enchantments.put(enchantment, level);
    }

    public void removeEnchantment(@NotNull Enchantment enchantment) {
        this.enchantments.remove(enchantment);
    }

    @Range(from = 0, to = Integer.MAX_VALUE)
    public int getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(@Range(from = 0, to = Integer.MAX_VALUE) int repairCost) {
        this.repairCost = repairCost;
    }

    @NotNull
    public Set<Flag> getFlags() {
        return Collections.unmodifiableSet(flags);
    }

    public void addFlag(@NotNull Flag flag) {
        this.flags.add(flag);
    }

    public void removeFlag(@NotNull Flag flag) {
        this.flags.remove(flag);
    }

    public boolean isUnbreakable() {
        return unBreakable;
    }

    public void setUnbreakable(boolean unBreakable) {
        this.unBreakable = unBreakable;
    }

    @ApiStatus.Internal
    public void applyTo(@NotNull NBTCompound compound) {
        if (this.displayName != null || !this.lore.isEmpty()) {
            if (this.displayName != null)
                MetaUtil.applyDisplayTag(NAME, new NBTString(AdventureSerializer.asVanilla(this.displayName)), compound);
            if (!this.lore.isEmpty()) {
                NBTList<NBTString> strings = new NBTList<>(NBTType.STRING);
                for (Component component : this.lore) {
                    if (component == null) strings.addTag(new NBTString(""));
                    else strings.addTag(new NBTString(AdventureSerializer.asVanilla(component)));
                }
                MetaUtil.applyDisplayTag(LORE, strings, compound);
            }
        }
        byte flags = 0x00;
        for (Flag flag : this.flags) {
            flags |= 1 << flag.ordinal();
        }
        if (flags != 0x00) compound.setTag(HIDE_FLAGS, new NBTInt(flags));
        if (this.unBreakable)
            compound.setTag(UNBREAKABLE, new NBTByte((byte) 1));
        if (this.repairCost > 0)
            compound.setTag(REPAIR_COST, new NBTInt(this.repairCost));
    }

    public enum Flag {
        HIDE_ENCHANTS,
        HIDE_ATTRIBUTES,
        HIDE_UNBREAKABLE,
        HIDE_DESTROYS,
        HIDE_PLACED_ON,
        HIDE_POTION_EFFECTS;
    }

    public static class Builder<T extends ItemMeta> {

        private Component displayName;
        private List<@Nullable Component> lore;
        private final Map<@NotNull Enchantment, @Range(from = 1, to = Integer.MAX_VALUE) Integer> enchantments;
        private final Set<Flag> flags;
        private int repairCost;
        private boolean unBreakable;

        private final T meta;

        public Builder(T meta) {
            this.displayName = meta.getDisplayName();
            this.lore = meta.getLore();
            this.enchantments = new EnumMap<>(meta.getEnchantments());
            this.flags = EnumSet.copyOf(meta.getFlags());
            this.repairCost = meta.getRepairCost();
            this.unBreakable = meta.isUnbreakable();

            this.meta = meta;
        }

        public T getHandle() {
            return this.meta;
        }

        public Builder<T> withDisplayName(@Nullable Component displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder<T> withLore(@NotNull List<@Nullable Component> lore) {
            this.lore = lore;
            return this;
        }

        public Builder<T> withLore(@NotNull Component... lore) {
            this.lore = Arrays.asList(lore);
            return this;
        }

        public Builder<T> withEnchantment(@NotNull Enchantment enchantment, @Range(from = 1, to = Integer.MAX_VALUE) int level) {
            this.enchantments.put(enchantment, level);
            return this;
        }

        public Builder<T> withFlags(@NotNull Set<Flag> flags) {
            this.flags.addAll(flags);
            return this;
        }

        public Builder<T> withFlags(@NotNull Flag... flags) {
            this.flags.addAll(Arrays.asList(flags));
            return this;
        }

        public int withRepairCost(@Range(from = 0, to = Integer.MAX_VALUE) int repairCost) {
            this.repairCost = repairCost;
            return repairCost;
        }

        public Builder<T> withUnbreakability(boolean unBreakable) {
            this.unBreakable = unBreakable;
            return this;
        }

        public T build() {
            this.meta.setDisplayName(this.displayName);
            this.meta.setLore(this.lore);
            this.enchantments.forEach(this.meta::addEnchant);
            this.flags.forEach(this.meta::addFlag);
            this.meta.setRepairCost(this.repairCost);
            this.meta.setUnbreakable(this.unBreakable);
            return meta;
        }

        public void applyTo(ItemStack item) {
            if (item == null || item.getItemMeta().getClass().equals(this.meta.getClass())) return;
            item.setItemMeta(this.build());
        }

    }

}
