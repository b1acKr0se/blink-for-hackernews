package nt.hai.blinkforhackernews.utility;


import java.net.MalformedURLException;
import java.net.URL;

public class UrlUtils {
    public static String getHostName(String url) {
        if (url == null)
            return "";
        url = url.toLowerCase();
        String hostName = url;
        if (!url.equals("")) {
            if (url.startsWith("http") || url.startsWith("https")) {
                try {
                    URL netUrl = new URL(url);
                    String host = netUrl.getHost();
                    if (host.startsWith("www")) {
                        hostName = host.substring("www".length() + 1);
                    } else {
                        hostName = host;
                    }
                } catch (MalformedURLException e) {
                    hostName = url;
                }
            } else if (url.startsWith("www")) {
                hostName = url.substring("www".length() + 1);
            }
            return hostName;
        } else {
            return "";
        }
    }
}
