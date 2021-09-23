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
        sortType = " DESC",
        sortBy = "date_added"
)
public class DefaultVideoInfo extends MediaInfo {
    @ColumnName("duration")
    public long duration;
    @ColumnName("_size")
    public long size;
    @ColumnName("width")
    public int width;
    @ColumnName("height")
    public int height;
    @ColumnName(MediaStore.Images.ImageColumns.DATA)
    public String path;
    @ColumnName(MediaStore.Video.VideoColumns.BUCKET_DISPLAY_NAME)
    public String relativePath;

    public DefaultVideoInfo() {
    }
}
