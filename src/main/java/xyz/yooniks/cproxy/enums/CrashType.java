package xyz.yooniks.cproxy.enums;

public enum CrashType {

    WINDOW(true),
    PLACE(true),
    SET_CREATIVE_SLOT(true),
    CHANGE_HELD_ITEM(false),
    SWINGARM(false);

    private Boolean hasItem;

    private CrashType(Boolean hasItem) {
        this.hasItem = hasItem;
    }

    public Boolean hasItem() {
        return hasItem;
    }
}
