package com.example.album.media.model;

import android.provider.MediaStore;

import com.example.album.media.annotations.ColumnName;
import com.example.album.media.annotations.MediaQueryParam;

/**
 * @version : v1.0.0
 * @author: Tomdog
 * @since: 2021/9/23
 */
@MediaQueryParam(
        select = {"mime_type", "=?", " or ", "mime_type", "=?"},
        selectArgs = {"image/jpeg", "image/png"},
        sortType = " DESC",
        sortBy = "date_added"
)
public class DefaultImageInfo extends MediaInfo {
    @ColumnName("bucket_id")
    public String bucketId;
    @ColumnName("bucket_display_name")
    public String bucketDisplayName;
    @ColumnName("date_modified")
    public long dateModified;
    @ColumnName("_size")
    public long size;
    /** 宽度 */
    @ColumnName(MediaStore.Images.ImageColumns.WIDTH)
    public int width;
    /** 高度 */
    @ColumnName(MediaStore.Images.ImageColumns.HEIGHT)
    public int height;
    /** 名称 */
//    @ColumnName(MediaStore.Images.ImageColumns.DISPLAY_NAME)
//    public int name;

    public DefaultImageInfo() {
    }

    public String toString() {
        return "DefaultImageInfo{bucketId=" + this.bucketId + ", bucketDisplayName='"
                + this.bucketDisplayName + '\'' + ", dateModified=" + this.dateModified + ", size=" + this.size + '}';
    }
}
