package org.zibble.inventoryframework.menu.inventory;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.zibble.inventoryframework.InventoryType;
import org.zibble.inventoryframework.menu.Menu;
import org.zibble.inventoryframework.menu.nameable.NamedMenu;
import org.zibble.inventoryframework.menu.openinventory.OpenInventory;
import org.zibble.inventoryframework.protocol.ProtocolPlayer;

public class HopperMenu extends NamedMenu {

    public HopperMenu(@Nullable final Component title) {
        super(1, 5, title);
    }

    @Override
    @NotNull
    public InventoryType type() {
        return InventoryType.HOPPER;
    }

    @Override
    public void open(@NotNull final ProtocolPlayer<?> user) {
        OpenInventory openInventory = new OpenInventory(user, this);
        Menu.OPEN_INVENTORIES.put(user, openInventory);
        openInventory.show();
        openInventory.sendItems(this.items());
    }

}
