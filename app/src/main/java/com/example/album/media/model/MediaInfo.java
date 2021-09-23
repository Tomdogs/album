package com.example.album.media.model;

import android.net.Uri;

/**
 * @version : v1.0.0
 * @author: Tomdog
 * @since: 2021/9/23
 */
public class MediaInfo {
    public Uri contentUri;

    public MediaInfo() {
    }

    public Uri getContentUri() {
        return this.contentUri;
    }

    public void setContentUri(Uri contentUri) {
        this.contentUri = contentUri;
    }
}
