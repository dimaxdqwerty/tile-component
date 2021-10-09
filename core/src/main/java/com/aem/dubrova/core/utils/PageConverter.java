package com.aem.dubrova.core.utils;

import org.apache.commons.lang.StringUtils;

public class PageConverter {

    public static String convertPath(String path) {
        StringUtils.substring(path, 0, path.length() - 1);
        String pathToPage = path + ".html";
        return pathToPage;
    }

}
