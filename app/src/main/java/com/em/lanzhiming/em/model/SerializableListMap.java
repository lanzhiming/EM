package com.em.lanzhiming.em.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 作者：lanzhm on 2016/7/21 21:40
 * 邮箱：18770915807@163.com
 */
public class SerializableListMap implements Serializable{
    private List<Map<String,String>> listMap;

    private String startTime;
    private String endTime;

    public List<Map<String, String>> getListMap() {
        return listMap;
    }

    public void setListMap(List<Map<String, String>> listMap) {
        this.listMap = listMap;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
