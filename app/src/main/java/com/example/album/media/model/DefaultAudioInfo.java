package com.example.album.media.model;

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
public class DefaultAudioInfo extends MediaInfo {
    @ColumnName("duration")
    public long duration;
    @ColumnName("_size")
    public long size;

    public DefaultAudioInfo() {
    }
}