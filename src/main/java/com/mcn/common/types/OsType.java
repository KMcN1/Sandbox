package com.mcn.common.types;

public enum OsType {
    ANDROID("Android"), IOS("iOS");

    private final String friendlyName;

    OsType(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    @Override
    public String toString() {
        return friendlyName;
    }
}
