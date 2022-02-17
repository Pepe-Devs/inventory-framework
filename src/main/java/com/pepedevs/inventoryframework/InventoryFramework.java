package com.pepedevs.inventoryframework;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerCommon;
import com.github.retrooper.packetevents.protocol.item.ItemStack;
import com.github.retrooper.packetevents.protocol.player.User;
import com.pepedevs.inventoryframework.protocol.InventoryPacketListener;
import com.pepedevs.inventoryframework.protocol.PlayerListener;
import com.pepedevs.inventoryframework.protocol.ProtocolPlayer;

public class InventoryFramework {

    private static InventoryFramework instance;

    private PlatformAdaptor platformAdaptor;
    private final PacketListenerCommon listener;
    private PacketListenerCommon playerListener;

    public static InventoryFramework init() {
        return init(null);
    }

    public static InventoryFramework init(PlatformAdaptor platformAdaptor) {
        return new InventoryFramework(platformAdaptor);
    }

    private InventoryFramework(PlatformAdaptor platformAdaptor) {
        if (instance != null) {
            throw new IllegalStateException("InventoryFramework is already initialized");
        }

        instance = this;
        this.platformAdaptor = platformAdaptor;
        this.listener = PacketEvents.getAPI().getEventManager().registerListener(new InventoryPacketListener());
        if (platformAdaptor == null) {
            this.platformAdaptor = new DefaultPlatformAdaptor();
            this.playerListener = PacketEvents.getAPI().getEventManager().registerListener(new PlayerListener());
        }
    }

    public PlatformAdaptor platformAdaptor() {
        return this.platformAdaptor;
    }

    public void platformAdaptor(PlatformAdaptor platformAdaptor) {
        this.platformAdaptor = platformAdaptor;
    }

    public void terminate() {
        PacketEvents.getAPI().getEventManager().unregisterListener(listener);
        if (playerListener != null) {
            PacketEvents.getAPI().getEventManager().unregisterListener(playerListener);
        }
        instance = null;
    }

    public static InventoryFramework framework() {
        return instance;
    }

    private static class DefaultPlatformAdaptor implements PlatformAdaptor {

        @Override
        public ItemStack getItemOnCursor(User user) {
            return ProtocolPlayer.player(user).getCarried();
        }

    }

}