package org.niu.steam.tasks;

import okhttp3.*;

import java.io.IOException;

public class Login extends Task {

    public static void main(String[] args) {
//        图片地址：https://steamcommunity-a.akamaihd.net/economy/image/
//        清空cookie
//        Cookie_T.writeAllCookie(null);
//        new Getrsakey("https://store.steampowered.com/login/getrsakey").post();
//        new Login("https://store.steampowered.com/login").get();
        new ALL("https://steamcommunity.com/inventory/"+ Dologin.steamid+"/"+ Getrsakey.appid+"/2?l=schinese&count=75").get();
    }

    public Login(String url) {
        super(url);
    }

    @Override
    public void setHeaders(Request.Builder builder) {
        builder.addHeader("Host", "store.steampowered.com")
                .addHeader("Connection", "keep-alive")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Site", "none")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "zh-CN,zh;q=0.9");
    }

    @Override
    public RequestBody requestBody() {
        return null;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println("login fail: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        new Getrsakey("https://store.steampowered.com/login/getrsakey").post();
        System.out.println("login over");
    }
}
