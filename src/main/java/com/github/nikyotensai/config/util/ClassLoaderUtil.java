package com.github.nikyotensai.config.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author nikyotensai
 * @since 2022/11/4
 */
public class ClassLoaderUtil {

    private static URLClassLoader classLoader = null;


    public static void init(List<String> urls) {
        List<URL> urlList = urls.stream().map(k -> {
            try {
                return new File(k).toURI().toURL();
            } catch (MalformedURLException e) {
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        URL[] urls1 = new URL[urlList.size()];
        classLoader = new URLClassLoader(urlList.toArray(urls1));
    }

    public static ClassLoader getClassLoader() {
        if (classLoader == null) {
            System.err.println("classLoader not inited");
        }
        return classLoader;
    }

}
