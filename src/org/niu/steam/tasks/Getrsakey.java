package org.niu.steam.tasks;

import okhttp3.*;
import org.json.JSONObject;
import org.niu.steam.RAS;
import org.niu.steam.util.TEA;


import java.io.IOException;


public class Getrsakey extends Task {

    // steam 账号
    public static String username = "";
    // steam 密码
    public static String password = "";


    public static String enpass = "";
    public static String timestamp = "";
    public static String appid = "440"; // 游戏id

    public Getrsakey(String url) {
        super(url);
    }

    @Override
    public void onFailure(Call call, IOException e) {
        System.out.println("Getrsakey fail: " + e.getMessage());
    }

    @Override
    public void onResponse(Call call, Response response) throws IOException {
        byte[] raw = response.body().bytes();
        JSONObject result = new JSONObject(new String(TEA.gzipa(raw)));
        String publickey_mod = result.getString("publickey_mod");
        String publickey_exp = result.getString("publickey_exp");
        timestamp = result.getString("timestamp");
        RAS.setRSAPublicKey(publickey_mod, publickey_exp);
        enpass = RAS.RSA_encrypt(password);
        new Dologin("https://store.steampowered.com/login/dologin/").post();
        System.out.println("Getrsakey over");
    }

    @Override
    public void setHeaders(Request.Builder builder) {
        builder.addHeader("Host", "store.steampowered.com")
                .addHeader("Connection", "keep-alive")
                .addHeader("Accept", "*/*")
                .addHeader("Sec-Fetch-Dest", "empty")
                .addHeader("X-Requested-With", "XMLHttpRequest")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0 Win64 x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.149 Safari/537.36")
                .addHeader("Content-Type", "application/x-www-form-urlencoded charset=UTF-8")
                .addHeader("Origin", "https://store.steampowered.com")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "cors")
                .addHeader("Referer", "https://store.steampowered.com/login/")
                .addHeader("Accept-Encoding", "gzip, deflate, br")
                .addHeader("Accept-Language", "zh-CN,zhq=0.9");
    }

    public static String[] needCookie(String path) {
        String[] cks = new String[]{"browserid", "steamCountry", "sessionid", "steamLoginSecure"};
        return cks;
    }

    @Override
    public RequestBody requestBody() {
        return RequestBody.
                create(MediaType.parse(" application/x-www-form-urlencoded; charset=UTF-8"),
                        "donotcache=" + System.currentTimeMillis() + "&username=" + username);
    }

}
