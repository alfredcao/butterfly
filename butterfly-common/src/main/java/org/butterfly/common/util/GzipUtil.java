package org.butterfly.common.util;

import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * gzip工具
 */
public class GzipUtil {
    /**
     * 解码
     */
    public static byte[] ungzip(InputStream inputStream) throws IOException {
        try(GZIPInputStream gin = new GZIPInputStream(inputStream);
            ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buf = new byte[1024];
            int len;
            while ((len = gin.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            return out.toByteArray();
        }
    }

    /**
     * 编码
     */
    public static String gzip(String source) throws IOException {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(source.getBytes());
            return out.toString("ISO-8859-1");
        }
    }

    /**
     * 编码
     */
    public static byte[] gzipBytes(String source) throws IOException {
        if (StringUtils.isBlank(source)) {
            return null;
        }
        try(ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out)) {
            gzip.write(source.getBytes());
            return out.toByteArray();
        }
    }
}
