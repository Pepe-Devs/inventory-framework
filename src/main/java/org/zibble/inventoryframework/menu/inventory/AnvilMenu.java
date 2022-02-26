package org.zibble.inventoryframework.menu.inventory;

import org.jetbrains.annotations.NotNull;
import org.zibble.inventoryframework.InventoryType;
import org.zibble.inventoryframework.menu.Menu;
import org.zibble.inventoryframework.menu.openinventory.AbstractOpenInventory;
import org.zibble.inventoryframework.menu.openinventory.AnvilOpenInventory;
import org.zibble.inventoryframework.protocol.ProtocolPlayer;

import java.util.function.Consumer;

public class AnvilMenu extends Menu {

    private Consumer<String> outputClick;

    public AnvilMenu() {
        super(1, 2);
    }

    public Consumer<String> getOutputClick() {
        return outputClick;
    }

    public void setOutputClick(Consumer<String> outputClick) {
        this.outputClick = outputClick;
    }

    @Override
    public @NotNull InventoryType type() {
        return InventoryType.ANVIL;
    }

    @Override
    public boolean isSupported() {
        return true;
    }

    @Override
    public void open(@NotNull ProtocolPlayer<?> user) {
        AnvilOpenInventory openInventory = new AnvilOpenInventory(user, this);
        Menu.OPEN_INVENTORIES.put(user, openInventory);
        openInventory.show();
        this.update(openInventory);
    }

    @Override
    protected void update(@NotNull AbstractOpenInventory openInventory) {
        openInventory.sendItems(this.items());
    }

}