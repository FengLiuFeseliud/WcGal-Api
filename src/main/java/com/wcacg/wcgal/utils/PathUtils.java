package com.wcacg.wcgal.utils;

import org.springframework.util.ClassUtils;
import org.springframework.util.DigestUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.Objects;

public class PathUtils {


    public static File ImgPathFile(String fileName) {
        return new File(Objects.requireNonNull(Objects.requireNonNull(
                ClassUtils.getDefaultClassLoader()).getResource("")).getPath() + "images" + "/" + fileName);
    }


    public static File ImgPathFile(InputStream stream) throws IOException {
        String type = URLConnection.guessContentTypeFromStream(new BufferedInputStream(stream)).split("/")[1];
        String path = Objects.requireNonNull(Objects.requireNonNull(ClassUtils.getDefaultClassLoader()).getResource("")).getPath() + "images";

        File file = new File(path + "/" + DigestUtils.md5DigestAsHex(stream) + "." + type);
        if (!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        return file;
    }
}
