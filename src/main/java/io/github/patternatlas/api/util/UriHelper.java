package io.github.patternatlas.api.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class UriHelper {

    public static String createIdFromUri(String uri) throws UnsupportedEncodingException {
        return URLEncoder.encode(URLEncoder.encode(uri, StandardCharsets.UTF_8.toString()), StandardCharsets.UTF_8.toString());
    }
}
