package org.niu.steam;

import okhttp3.Cookie;

import java.io.*;
import java.util.*;

public class Cookie_T {
    private static String fileName = "./src/cookies";
    private static Map<String, Cream> _SerObj_ = new HashMap<>();
    private static Map<String, Cookie> _TEMP_ = new HashMap<>();

    private static Cookie Cream2Cookie(Cream cream) {
        return new Cookie.Builder()
                .domain(cream.domain)
                .expiresAt(cream.expiresAt)
                .name(cream.name)
                .path(cream.path)
                .value(cream.value)
                .build();
    }

    private static Cream Cookie2Cream(Cookie cookie) {
        return new Cream(cookie.name(),
                cookie.value(),
                cookie.expiresAt(),
                cookie.domain(),
                cookie.path(),
                cookie.secure(),
                cookie.httpOnly(),
                cookie.persistent(),
                cookie.hostOnly());
    }

    public static Cookie getCookie(String name) {
        if (_TEMP_.containsKey(name)) {
            return _TEMP_.get(name);
        }
        return null;
    }

    public static void readAllCookie() {
        try {
            ObjectInputStream oin = new ObjectInputStream(new FileInputStream(new File(fileName)));
            _SerObj_ = (Map<String, Cream>) oin.readObject();
            Set<String> set = _SerObj_.keySet();
            for (String k : set) {
//                System.out.println(_SerObj_.get(k));
                _TEMP_.put(k, Cream2Cookie(_SerObj_.get(k)));
            }
            oin.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    public static void writeAllCookie(List<Cookie> cookies) {
        if (cookies == null){
            cookies = new ArrayList<>();
            _SerObj_.clear();
        }
        for (Cookie c : cookies) {
            _SerObj_.put(c.name(), Cookie2Cream(c));
        }
        try {
            ObjectOutputStream oout = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
            oout.writeObject(_SerObj_);
            oout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static class Cream implements Serializable {
        private static final long serialVersionUID = 6374381828722046732L;
        private String name;
        private String value;
        private long expiresAt = 253402300799999L;
        private String domain;
        private String path = "/";
        private boolean secure;
        private boolean httpOnly;
        private boolean persistent;
        private boolean hostOnly;

        public Cream(String name, String value, long expiresAt, String domain, String path, boolean secure, boolean httpOnly, boolean persistent, boolean hostOnly) {
            this.name = name;
            this.value = value;
            this.expiresAt = expiresAt;
            this.domain = domain;
            this.path = path;
            this.secure = secure;
            this.httpOnly = httpOnly;
            this.persistent = persistent;
            this.hostOnly = hostOnly;
        }

        @Override
        public String toString() {
            return "Cream{" +
                    "name='" + name + '\'' +
                    ", value='" + value + '\'' +
                    ", expiresAt=" + expiresAt +
                    ", domain='" + domain + '\'' +
                    ", path='" + path + '\'' +
                    ", secure=" + secure +
                    ", httpOnly=" + httpOnly +
                    ", persistent=" + persistent +
                    ", hostOnly=" + hostOnly +
                    '}';
        }
    }

}
