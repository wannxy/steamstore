package org.niu.steam.tasks;

import okhttp3.*;

import java.io.IOException;

public class GetNotificationCounts extends Task {

    public GetNotificationCounts(String url) {
        super(url);
    }

    public static String[] needCookie(String path){
        String[] cks = new String[]{"steamCountry","steamLoginSecure","steamMachineAuth"+ Dologin.steamid};
        if ("market".equals(path)){
            cks = new String[]{"steamCountry","steamLoginSecure","steamMachineAuth"+ Dologin.steamid,"sessionid","browserid"};
        }
        return cks;
    }

    @Override
    public void setHeaders(Request.Builder builder) {
        builder.addHeader("Host","steamcommunity.com")
                .addHeader("Connection","keep-alive")
                .addHeader("Accept","*/*")
                .addHeader("Sec-Fetch-Dest","empty")
                .addHeader("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                .addHeader("Origin","https://store.steampowered.com")
                .addHeader("Sec-Fetch-Site","cross-site")
                .addHeader("Sec-Fetch-Mode","cors")
                .addHeader("Referer","https://store.steampowered.com/")
                .addHeader("Accept-Encoding","gzip, deflate, br")
                .addHeader("Accept-Language","zh-CN,zh;q=0.9");
    }

    @Override
    public RequestBody requestBody() {
        return null;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println("GetNotificationCounts fail: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
//        byte[] raw = response.body().bytes();
//        System.out.println(new String(raw));
        new Inventory("https://steamcommunity.com/profiles/"+ Dologin.steamid+"/inventory",false).get();
    }
}
