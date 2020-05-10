package org.niu.steam.tasks;

import okhttp3.*;

import java.io.IOException;

public class ALL extends Task {

    public static Boolean isOk = false;

    public ALL(String url) {
        super(url);
    }

    public static String[] needCookie(String s) {
        String[] cks = new String[]{"steamCountry","steamLoginSecure","sessionid","browserid","webTradeEligibility","steamMachineAuth"+Dologin.steamid};
        return cks;
    }

    @Override
    public void setHeaders(Request.Builder builder) {
        builder.addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Referer", "https://steamcommunity.com/profiles/"+Dologin.steamid+"/inventory/")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9");
    }


    @Override
    public RequestBody requestBody() {
        return null;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println("查询失败： " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        byte[] raw = response.body().bytes();
        System.out.println(new String(raw));
    }
}
