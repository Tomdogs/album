package com.example.album.media.model;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;

/**
 * @version : v1.0.0
 * @author: Tomdog
 * @since: 2021/9/23
 */
public class MediaClassMetadata {
    public String select;
    public String[] selectArgs;
    public String sort;
    public Map<String, Field> fieldDatas;

    public MediaClassMetadata() {
    }

    public String[] getProjection() {
        return this.fieldDatas == null ? null : (String[])this.fieldDatas.keySet().toArray(new String[0]);
    }

    public String toString() {
        return "MediaClassMetadata{select='" + this.select + '\''
                + ", selectArgs=" + Arrays.toString(this.selectArgs)
                + ", sort='" + this.sort + '\'' + ", fieldDatas=" + this.fieldDatas + '}';
    }
}
