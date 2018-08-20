package com.heboot.common;

import java.util.List;

public class ConfigValue {

    private static List<String> kf_uids;

    public static List<String> getKf_uids() {
        return kf_uids;
    }

    public static void setKf_uids(List<String> kf_uids) {
        ConfigValue.kf_uids = kf_uids;
    }
}
