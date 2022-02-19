package org.zibble.inventoryframework;

import org.zibble.inventoryframework.protocol.item.ItemStack;

public class MenuItem<C> {

    private C content;
    private ClickAction clickAction;

    private MenuItem(C content) {
        this.content = content;
    }

    public static MenuItem<ItemStack> of(ItemStack itemStack) {
        return new MenuItem<>(itemStack);
    }

    public ClickAction getClickAction() {
        return clickAction;
    }

    public void setClickAction(ClickAction clickAction) {
        this.clickAction = clickAction;
    }

    public C getContent() {
        return this.content;
    }

    public void setContent(C content) {
        this.content = content;
    }
}
