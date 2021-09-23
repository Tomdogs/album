package com.example.album.media.reflect;

import com.example.album.media.LogUtil;
import com.example.album.media.annotations.ColumnName;
import com.example.album.media.annotations.MediaQueryParam;
import com.example.album.media.model.MediaClassMetadata;
import com.example.album.media.model.MediaInfo;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @version : v1.0.0
 * @author: Tomdog
 * @since: 2021/9/23
 */
public class MediaAnnotationParser {
    public MediaAnnotationParser() {
    }

    public MediaClassMetadata getMetaData(Class<? extends MediaInfo> tClass) {
        if (tClass == null) {
            LogUtil.e(new Object[]{"tClass", tClass});
            return null;
        } else {
            MediaClassMetadata metadata = new MediaClassMetadata();
            MediaQueryParam mediaQueryParam = (MediaQueryParam)tClass.getAnnotation(MediaQueryParam.class);
            if (mediaQueryParam != null) {
                metadata.select = this.appendStr(mediaQueryParam.select());
                metadata.selectArgs = mediaQueryParam.selectArgs();
                metadata.sort = this.appendStr(mediaQueryParam.sortBy(), mediaQueryParam.sortType());
            }

            metadata.fieldDatas = this.collectFieldData(tClass);
            return metadata;
        }
    }

    private String appendStr(String... strings) {
        if (strings == null) {
            return null;
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            String[] var3 = strings;
            int var4 = strings.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String str = var3[var5];
                stringBuilder.append(str);
            }

            return stringBuilder.toString();
        }
    }

    private Map<String, Field> collectFieldData(Class<? extends MediaInfo> tClass) {
        Map<String, Field> fieldDataMap = new HashMap();
        fieldDataMap.put("_id", (Field) null);
        if (tClass == null) {
            LogUtil.e(new Object[]{"collectFieldData tclass", tClass});
            return fieldDataMap;
        } else {
            Field[] fields = tClass.getFields();
            if (fields != null && fields.length > 0) {
                Field[] var4 = fields;
                int var5 = fields.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    Field field = var4[var6];
                    ColumnName columnName = (ColumnName)field.getAnnotation(ColumnName.class);
                    if (columnName != null) {
                        fieldDataMap.put(columnName.value(), field);
                    }
                }

                return fieldDataMap;
            } else {
                LogUtil.e(new Object[]{"no field find, please check your info class"});
                return fieldDataMap;
            }
        }
    }
}
