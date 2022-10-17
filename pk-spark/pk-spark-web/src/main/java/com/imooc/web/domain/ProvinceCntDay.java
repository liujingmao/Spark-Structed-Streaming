package com.imooc.web.domain;

public class ProvinceCntDay {

    private String day;
    private String province;
    private Long cnt;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Long getCnt() {
        return cnt;
    }

    public void setCnt(Long cnt) {
        this.cnt = cnt;
    }

    @Override
    public String toString() {
        return "ProvinceCntDay{" +
                "day='" + day + '\'' +
                ", province='" + province + '\'' +
                ", cnt=" + cnt +
                '}';
    }
}
