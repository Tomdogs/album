package com.example.album.media.model;

/**
 * @version : v1.0.0
 * @author: Tomdog
 * @since: 2021/9/23
 */
public class MimeType {
    public MimeType() {
    }

    public static final class Video {
        public static final String MP4 = "video/mp4";
        public static final String MPEG = "video/mpeg";
        public static final String GP3 = "video/3gpp";
        public static final String AVI = "video/avi";
        public static final String MKV = "video/x-matroska";

        public Video() {
        }
    }

    public static final class Audio {
        public static final String AMR = "audio/amr";
        public static final String OGG = "audio/ogg";
        public static final String M4A = "audio/mp4";
        public static final String WAV = "audio/x-wav";

        public Audio() {
        }
    }

    public static final class Image {
        public static final String JPG = "image/jpg";
        public static final String JPEG = "image/jpeg";
        public static final String GIF = "image/gif";
        public static final String PNG = "image/png";
        public static final String WEBP = "image/webp";
        public static final String HEIF = "image/heif";
        public static final String HEIC = "image/heic";

        public Image() {
        }
    }
}
