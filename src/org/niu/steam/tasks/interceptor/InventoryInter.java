package org.niu.steam.tasks.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class InventoryInter implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        if (response.code() == 302){
            String location = response.header("Location");
            if (location.contains("market")){
                response = chain.proceed(doRequest(location));
            }
        }
        return response;
    }

    private Request doRequest(String url) {
//        System.out.println("market: " + url);
        return new Request.Builder()
                .url(url)
                .addHeader("Host","steamcommunity.com")
                .addHeader("Connection","keep-alive")
                .addHeader("Upgrade-Insecure-Requests","1")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                .addHeader("Sec-Fetch-Dest","document")
                .addHeader("Accept","text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Site","cross-site")
                .addHeader("Sec-Fetch-Mode","navigate")
                .addHeader("Sec-Fetch-User","?1")
                .addHeader("Referer","https://store.steampowered.com/")
                .addHeader("Accept-Encoding","gzip, deflate, br")
                .addHeader("Accept-Language","zh-CN,zh;q=0.9")
                .build();
    }


}
