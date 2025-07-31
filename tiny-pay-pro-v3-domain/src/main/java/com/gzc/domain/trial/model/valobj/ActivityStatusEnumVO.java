package com.gzc.domain.trial.model.valobj;

public enum ActivityStatusEnumVO {

    CREATE(0,"创建"),
    EFFECTIVE(1,"生效"),
    EXPIRE(2,"过期"),
    DELETE(3,"废弃"),
    ;

    private Integer code;
    private String info;

    ActivityStatusEnumVO(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
