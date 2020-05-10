package org.niu.steam.tasks;

import okhttp3.*;
import org.niu.steam.Cookie_T;
import org.niu.steam.tasks.interceptor.InventoryInter;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.List;

public abstract class Task implements Callback {

    private static OkHttpClient __client__;
    private String __url__ = "";

    private String url() {
        return __url__;
    }

    public abstract void setHeaders(Request.Builder builder);

    public abstract RequestBody requestBody();

    public void get() {
        Request.Builder builder = new Request.Builder();
        Request request = builder.url(url())
                .get().build();
        __client__.newCall(request).enqueue(this);
    }

    public void post() {
        Request.Builder builder = new Request.Builder();
        setHeaders(builder);
        Request request = builder.url(url())
                .post(requestBody())
                .build();
        __client__.newCall(request).enqueue(this);
    }

    public Task(String url) {
        this.__url__ = url;
        if (__client__ == null) {
            __client__ = new OkHttpClient().newBuilder()
                    .followRedirects(false)
                    .addInterceptor(new InventoryInter())
                    .proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 10809)))
                    .cookieJar(new CookieJar() {
                        @Override
                        public void saveFromResponse(HttpUrl httpUrl, List<Cookie> list) {
                            Cookie_T.writeAllCookie(list);
                            System.out.println("saved <" + list.size() + ">: " + httpUrl.url().getPath());
                        }

                        @Override
                        public List<Cookie> loadForRequest(HttpUrl httpUrl) {
                            String path = httpUrl.url().getPath();
                            Cookie_T.readAllCookie();
                            List<Cookie> cookies = new ArrayList<>();;
                            String[] cks = new String[]{};
                            switch (path){
                                case "/login" : break;
                                case "/login/getrsakey" : cks = Getrsakey.needCookie("/");break;
                                case "/login/dologin/" : cks = Dologin.needCookie("/");break;
                                case "/actions/GetNotificationCounts": cks = GetNotificationCounts.needCookie("/");break;
                                case "/market/eligibilitycheck/": cks = GetNotificationCounts.needCookie("market");break;
                            }
                            if (("/profiles/"+ Dologin.steamid+"/inventory").equals(path)){
                                cks = Inventory.needCookie("/");
                            }
                            if (("/inventory/"+ Dologin.steamid+"/"+ Getrsakey.appid+"/2").equals(path)){
                                cks = ALL.needCookie("/");
                            }
                            fillCooke(cookies,cks);
                            System.out.println("load <" + cookies.size() + ">: " + httpUrl.url().getPath());
                            return cookies;
                        }
                    }).build();
        }
    }

    public static void fillCooke(List list, String[] cks) {
        Cookie t = null;
        for (String k : cks) {
            t = Cookie_T.getCookie(k);
            if (t != null) {
                list.add(t);
            }
        }

    }
}
