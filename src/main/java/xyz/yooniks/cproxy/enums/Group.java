package xyz.yooniks.cproxy.enums;

public enum Group {

    GRACZ("&7Gracz", "&8", 0),
    ADMIN("&cAdmin", "&a", 1);

    private final String prefix, suffix;
    private final Integer permissionLevel;

    private Group(String prefix, String suffix, Integer permissionLevel) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.permissionLevel = permissionLevel;
    }

    public Integer getPermissionLevel() {
        return permissionLevel;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
