package com.pepedevs.inventoryframework.protocol;

import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindow;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientClickWindowButton;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientCloseWindow;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSetSlot;
import com.pepedevs.inventoryframework.ClickType;
import com.pepedevs.inventoryframework.InventoryFramework;
import com.pepedevs.inventoryframework.Menu;
import com.pepedevs.inventoryframework.openinventory.AbstractOpenInventory;

public class InventoryPacketListener extends PacketListenerAbstract {

    public InventoryPacketListener() {
        super(PacketListenerPriority.MONITOR);
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW) {
            WrapperPlayClientClickWindow packet = new WrapperPlayClientClickWindow(event);
            for (AbstractOpenInventory inv : Menu.OPEN_INVENTORIES) {
                if (inv.getWindowId() == packet.getWindowId() && event.getUser().equals(inv.getUser())) {
                    if (inv.getInventoryListener().onClick(packet.getSlot(), ClickType.fromPacketType(packet.getWindowClickType()))) {
                        WrapperPlayServerSetSlot setSlot = new WrapperPlayServerSetSlot(packet.getWindowId(), 0, packet.getSlot(), packet.getClickedItemStack());
                        PacketUtils.sendPacket(event.getUser(), setSlot);
                        WrapperPlayServerSetSlot setSlot2 = new WrapperPlayServerSetSlot(-1, 0, -1, InventoryFramework.framework().platformAdaptor().getItemOnCursor(event.getUser()));
                        PacketUtils.sendPacket(event.getUser(), setSlot2);
                    }
                }
            }
        } else if (event.getPacketType() == PacketType.Play.Client.CLOSE_WINDOW) {
            WrapperPlayClientCloseWindow packet = new WrapperPlayClientCloseWindow(event);
            for (AbstractOpenInventory inv : Menu.OPEN_INVENTORIES) {
                if (inv.getWindowId() == packet.getWindowId() && event.getUser().equals(inv.getUser())) {
                    inv.getInventoryListener().onClose();
                }
            }
        } else if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW_BUTTON) {
            WrapperPlayClientClickWindowButton packet = new WrapperPlayClientClickWindowButton(event);
            for (AbstractOpenInventory inv : Menu.OPEN_INVENTORIES) {
                if (inv.getWindowId() == packet.getWindowId() && event.getUser().equals(inv.getUser())) {
                    inv.getInventoryListener().onButtonClick(packet.getWindowId());
                }
            }
        }
    }

}
