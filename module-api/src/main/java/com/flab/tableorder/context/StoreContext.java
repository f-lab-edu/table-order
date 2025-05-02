package com.flab.tableorder.context;

public class StoreContext {

    // ThreadLocal을 사용하여 각 요청에 대한 storeId를 저장
    private static final ThreadLocal<Long> storeIdThreadLocal = new ThreadLocal<>();

    // storeId를 설정하는 메서드
    public static void setStoreId(Long storeId) {
        storeIdThreadLocal.set(storeId);
    }

    // storeId를 가져오는 메서드
    public static Long getStoreId() {
        return storeIdThreadLocal.get();
    }

    // storeId를 정리하는 메서드 (요청 처리 후)
    public static void clear() {
        storeIdThreadLocal.remove();
    }
}