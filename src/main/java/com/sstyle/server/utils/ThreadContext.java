package com.sstyle.server.utils;

/**
 * Created by ss on 2018/3/22.
 */
public class ThreadContext {
    private static ThreadLocal<String> LOCAL_STAFF_CODE = new InheritableThreadLocal();
    private static ThreadLocal<String> LOCAL_STAFF_ID = new InheritableThreadLocal();

    public static void setStaffCode(String staffCode) {
        LOCAL_STAFF_CODE.set(staffCode);
    }
    public static void setStaffId(String staffId) {
        LOCAL_STAFF_ID.set(staffId);
    }

    public static String getStaffCode() {
        return LOCAL_STAFF_CODE.get();
    }

    public static String getStaffId() {
        return LOCAL_STAFF_ID.get();
    }

    public static void clear() {
        LOCAL_STAFF_CODE.remove();
    }
}
