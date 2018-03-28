package com.sstyle.server.utils;

import com.github.bingoohuang.utils.crypto.Aes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ss on 18/3/17.
 */
public class StaffTokenUtils {
    private static final String SECRET_KEY = "bUcDxzE5u0i0SmvAkkRMRw";
    private static final String CRYPTO_SALT = "AFUOS_SALT";
    private static final String AF_SALT = "AF_SALT";

    public static String createStaffToken(String staffCode,String staffId) {
        StringBuilder builder = new StringBuilder(staffCode);
        builder.append(CRYPTO_SALT).append(staffId).append(AF_SALT).
                append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        return Aes.encrypt(builder.toString(), Aes.getKey(SECRET_KEY));
    }

    public static String decryptStaffToken(String staffToken) {
        String decrypt = Aes.decrypt(staffToken, Aes.getKey(SECRET_KEY));
        return decrypt.substring(0, decrypt.indexOf(CRYPTO_SALT));
    }

    public static Date getTokenTime(String staffToken) {
        String decrypt = Aes.decrypt(staffToken, Aes.getKey(SECRET_KEY));
        String timeStr = decrypt.substring(decrypt.indexOf(AF_SALT)+AF_SALT.length());
        try {
            return  new SimpleDateFormat("yyyyMMddHHmmss").parse(timeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String decryptStaffIdToken(String staffToken) {
        String decrypt = Aes.decrypt(staffToken, Aes.getKey(SECRET_KEY));
        return decrypt.substring(decrypt.indexOf(CRYPTO_SALT)+CRYPTO_SALT.length(), decrypt.indexOf(AF_SALT));
    }

}
