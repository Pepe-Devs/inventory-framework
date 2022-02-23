package org.zibble.inventoryframework.menu.inventory.buttonmenus;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;
import org.zibble.inventoryframework.InventoryType;
import org.zibble.inventoryframework.menu.Menu;
import org.zibble.inventoryframework.menu.nameable.NamedMenu;
import org.zibble.inventoryframework.menu.openinventory.AbstractOpenInventory;
import org.zibble.inventoryframework.menu.openinventory.BeaconOpenInventory;
import org.zibble.inventoryframework.menu.property.DataPropertyHolder;
import org.zibble.inventoryframework.menu.property.PropertyPair;
import org.zibble.inventoryframework.protocol.ProtocolPlayer;
import org.zibble.inventoryframework.protocol.item.objects.enums.EffectType;

import java.util.function.Consumer;

public class BeaconMenu extends NamedMenu implements DataPropertyHolder {

    private @Range(from = 0, to = 4) int level;
    private @Nullable EffectType primaryPower;
    private @Nullable EffectType secondaryPower;

    private @Nullable Consumer<Integer> buttonClickHandler;

    public BeaconMenu(@Nullable Component title) {
        super(1, 1, title);
    }

    @Override
    @NotNull
    public InventoryType type() {
        return InventoryType.BEACON;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Nullable
    public EffectType primaryPower() {
        return primaryPower;
    }

    @Nullable
    public EffectType secondaryPower() {
        return secondaryPower;
    }

    public void primaryPower(@Nullable EffectType primaryPower) {
        this.primaryPower = primaryPower;
    }

    public void secondaryPower(@Nullable EffectType secondaryPower) {
        this.secondaryPower = secondaryPower;
    }

    public @Range(from = 0, to = 4) int level() {
        return level;
    }

    public void level(@Range(from = 0, to = 4) int level) {
        this.level = level;
    }

    @Override
    protected void update(@NotNull AbstractOpenInventory openInventory) {
        openInventory.sendItems(this.items());
        openInventory.updateWindowData(this.properties());
    }

    @Override
    public void open(@NotNull ProtocolPlayer<?> user) {
        BeaconOpenInventory openInventory = new BeaconOpenInventory(user, this);
        Menu.OPEN_INVENTORIES.put(user, openInventory);
        openInventory.show();
        this.update(openInventory);
    }

    public Consumer<Integer> buttonClickHandler() {
        return buttonClickHandler;
    }

    public void buttonClickHandler(@Nullable Consumer<Integer> clickHandler) {
        this.buttonClickHandler = clickHandler;
    }

    @Override
    @NotNull
    public PropertyPair[] properties() {
        return new PropertyPair[]{
                PropertyPair.of(0, this.level),
                PropertyPair.of(1, (this.primaryPower == null ? 0 : this.primaryPower.id())),
                PropertyPair.of(2, (this.secondaryPower == null ? 0 : this.secondaryPower.id()))
        };
    }
}
