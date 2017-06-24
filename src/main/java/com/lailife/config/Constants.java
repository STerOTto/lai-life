package com.lailife.config;

import java.io.File;

/**
 * Application constants.
 */
public final class Constants {

    //Regex for acceptable logins
    public static final String LOGIN_REGEX = "^[_'.@A-Za-z0-9-]*$";
    public static final String PHONE_REGEX = "^1[34578]\\d{9}$";
    public static final String IDENTITY_CARD_REGEX = "^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$";


    public static final String SYSTEM_ACCOUNT = "system";
    public static final String ANONYMOUS_USER = "anonymoususer";

    public static final String PHONE_CODE_CACHE = "phone_code_cache";

    public static final String ROOT_LOCATION = File.separator + "laiLifeData"+ File.separator;
    public static final String ICON_LOCATION_FORMAT = ROOT_LOCATION + "%d" + File.separator + "icon" + File.separator;
    public static final String NEEDS_LOCATION_FORMAT = ROOT_LOCATION + "%d" + File.separator + "needs" + File.separator;

    private Constants() {
    }
}
