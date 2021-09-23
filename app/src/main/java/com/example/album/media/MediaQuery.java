package com.example.album.media;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.album.media.model.MediaClassMetadata;
import com.example.album.media.model.MediaInfo;
import com.example.album.media.reflect.ConstructorConstructor;
import com.example.album.media.reflect.MediaAnnotationParser;
import com.example.album.media.reflect.ObjectConstructor;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 查询给定的 URI，在结果集上返回一个Cursor
 * 可查询媒体类型的文件
 *
 * @version : v1.0.0
 * @author: Tomdog
 * @since: 2021/9/23
 */
public class MediaQuery {
    private Context context;
    private Uri uri;
    private String selection;
    private String[] selectionArgs;
    private String sortOrder;

    private MediaQuery() {
    }

    public static MediaQuery.MediaQueryBuilder with(@NonNull Context context, @NonNull Uri uri) {
        return new MediaQuery.MediaQueryBuilder(context, uri);
    }

    public <T extends MediaInfo> List<T> query(@NonNull Class<T> tClass) {
        return this.queryInternal(this.context, this.uri, this.selection, this.selectionArgs, this.sortOrder, tClass);
    }

    private <T extends MediaInfo> List<T> queryInternal(Context context,
                                                        @NonNull Uri uri,
                                                        @Nullable String selection,
                                                        @Nullable String[] selectionArgs,
                                                        @Nullable String sortOrder,
                                                        @NonNull Class<T> tClass) {
        if (context != null) {
            ContentResolver contentResolver = context.getContentResolver();
            List<T> resultList = new ArrayList();
            ObjectConstructor<T> objectConstructor = (new ConstructorConstructor()).get(tClass);
            if (objectConstructor == null) {
                LogUtil.e(new Object[]{"cannot get constructor, please check your mediainfo class", tClass});
                return resultList;
            } else {
                MediaClassMetadata metadata = (new MediaAnnotationParser()).getMetaData(tClass);
                if (metadata == null) {
                    LogUtil.e(new Object[]{"cannnot get metadata from ", tClass});
                    return resultList;
                } else {
                    if (TextUtils.isEmpty(selection)) {
                        selection = metadata.select;
                    }

                    if (selectionArgs == null || selectionArgs.length < 1) {
                        selectionArgs = metadata.selectArgs;
                    }

                    if (TextUtils.isEmpty(sortOrder)) {
                        sortOrder = metadata.sort;
                    }

                    Cursor cursor = null;

                    try {
                        cursor = contentResolver.query(uri, metadata.getProjection(), selection, selectionArgs, sortOrder);

                        while(cursor.moveToNext()) {
                            T instance = (T) objectConstructor.construct();
                            int idIndex = cursor.getColumnIndex("_id");
                            if (idIndex == -1) {
                                continue;
                            }

                            int mediaID = cursor.getInt(idIndex);
                            instance.setContentUri(ContentUris.withAppendedId(uri, (long)mediaID));
                            T temp = (T) this.parseValue(cursor, instance, metadata.fieldDatas);
                            resultList.add(temp);
                        }
                    } catch (Exception var18) {
                        LogUtil.d(new Object[]{"query failed, exception", var18});
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }

                    return resultList;
                }
            }
        } else {
            LogUtil.e(new Object[]{"context", context, "uri", uri, "tClass", tClass});
            return null;
        }
    }

    private <T> T parseValue(Cursor cursor, T instance, Map<String, Field> fieldMap) throws IllegalAccessException {
        if (cursor != null && instance != null && fieldMap != null) {
            Iterator var4 = fieldMap.keySet().iterator();

            while(var4.hasNext()) {
                String columnName = (String)var4.next();
                Field field = (Field)fieldMap.get(columnName);
                if (!"_id".equals(columnName) && field != null) {
                    int columnIndex = cursor.getColumnIndex(columnName);
                    if (columnIndex == -1) {
                        LogUtil.e(new Object[]{"columnName error, columnName", columnName});
                        return instance;
                    }

                    int columnType = cursor.getType(columnIndex);
                    Object value = null;
                    switch(columnType) {
                        case 0:
                            return instance;
                        case 1:
                            value = cursor.getInt(columnIndex);
                            break;
                        case 2:
                            value = cursor.getFloat(columnIndex);
                            break;
                        case 3:
                            value = cursor.getString(columnIndex);
                            break;
                        case 4:
                            value = cursor.getBlob(columnIndex);
                            break;
                        default:
                            value = cursor.getString(columnIndex);
                    }

                    field.setAccessible(true);
                    field.set(instance, value);
                }
            }

            return instance;
        } else {
            LogUtil.e(new Object[]{"cusor", cursor, "instance", instance, "fieldMap", fieldMap});
            return instance;
        }
    }

    public static final class MediaQueryBuilder {
        private Context context;
        private Uri uri;
        private String selection;
        private String[] selectionArgs;
        private String sortOrder;

        public MediaQueryBuilder(Context context, Uri uri) {
            this.context = context;
            this.uri = uri;
        }

        public MediaQuery.MediaQueryBuilder selection(String selection) {
            this.selection = selection;
            return this;
        }

        public MediaQuery.MediaQueryBuilder selectionArgs(String[] selectionArgs) {
            this.selectionArgs = selectionArgs;
            return this;
        }

        public MediaQuery.MediaQueryBuilder sortOrder(String sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public MediaQuery build() {
            MediaQuery mediaQuery = new MediaQuery();
            mediaQuery.context = this.context;
            mediaQuery.uri = this.uri;
            mediaQuery.selection = this.selection;
            mediaQuery.selectionArgs = this.selectionArgs;
            mediaQuery.sortOrder = this.sortOrder;
            return mediaQuery;
        }
    }
}
