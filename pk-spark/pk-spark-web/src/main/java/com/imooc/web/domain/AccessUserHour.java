package com.imooc.web.domain;

public class AccessUserHour {

    private String hour;
    private String user;
    private Double time;

    @Override
    public String toString() {
        return "AccessUserHour{" +
                "hour='" + hour + '\'' +
                ", user='" + user + '\'' +
                ", time=" + time +
                '}';
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Double getTime() {
        return time;
    }

    public void setTime(Double time) {
        this.time = time;
    }
}
