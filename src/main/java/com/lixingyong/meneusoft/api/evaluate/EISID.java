package com.lixingyong.meneusoft.api.evaluate;

public enum EISID {
    EIS_PT("PT"), EIS_JC("JC"), EIS_KC("KC"), EIS_MD("MD"), EIS_SU("SU"), EIS_TJC("TJC"), EIS_TKC("TKC"), EIS_TXM("TXM"), EIS_GT("GT");
    private String type;
    EISID(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }

    /**
     * 根据类型的名称，返回类型的枚举实例。
     *
     * @param typeName 类型名称
     */
    public static EISID fromTypeName(String typeName) {
        for (EISID eisid : EISID.values()) {
            if (eisid.getType().equals(typeName)) {
                return eisid;
            }
        }
        return null;
    }
}
