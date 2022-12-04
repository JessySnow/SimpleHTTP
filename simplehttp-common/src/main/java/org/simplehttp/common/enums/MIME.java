package org.simplehttp.common.enums;

/**
 * 支持的 MIME 类型
 */
public enum MIME {
    TEXT_PLAIN("text/plain"),
    IMAGE_JPEG("image/jpeg"),
    IMAGE_PNG("image/png"),
    AUDIO_MPEG("audio/mpeg"),
    VIDEO_MP4("video/mp4"),
    TEXT_HTML("text/html"),
    BINARY("application/octet-stream");

    public String value;
    MIME(String value){
        this.value = value;
    }
}
