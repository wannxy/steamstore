package org.niu.steam;

import java.math.BigInteger;

public class RAS {
    private static BigInteger modulus;
    private static BigInteger encryptionExponent;

    private static String hex = "0123456789abcdef";
    private static String base64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=";

    public static void setRSAPublicKey(String modulus_hex, String encryptionExponent_hex) {
        modulus = new BigInteger(modulus_hex,16);
        encryptionExponent = new BigInteger(encryptionExponent_hex,16);
    }

    public static String Hex_encode(String input) {
        if (input == null) return "";
        StringBuilder output = new StringBuilder();
        int k;
        int i = 0;
        do {
            k = input.codePointAt(i++);
            output.append(hex.charAt((k >> 4) & 0xf));
            output.append(hex.charAt(k & 0xf));
        } while (i < input.length());
        return output.toString();
    }

    public static String Hex_decode(String input) {
        if (input == null) return "";
        input = input.replaceAll("[^0-9abcdef]", "");
        StringBuilder output = new StringBuilder();
        int i = 0;
        do {
            output.append((char) (((hex.indexOf(input.charAt(i++)) << 4) & 0xf0) | (hex.indexOf(input.charAt(i++)) & 0xf)));
        } while (i < input.length());
        return output.toString();
    }

    public static String Base64_encode(String input) {
        if (input == null) {
            return "";
        }
        StringBuilder output = new StringBuilder();
        int chr1, chr2, chr3;
        int enc1 = 0, enc2 = 0, enc3 = 0, enc4 = 0;
        int i = 0;
        do {
            chr1 = input.codePointAt(i++);
            enc1 = chr1 >> 2;
            if (i < input.length()) {
                chr2 = input.codePointAt(i++);
                enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                if (i < input.length()) {
                    chr3 = input.codePointAt(i++);
                    enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                    enc4 = chr3 & 63;
                } else {
                    enc3 = ((chr2 & 15) << 2);
                    enc4 = 64;
                }
            } else {
                enc2 = ((chr1 & 3) << 4);
                enc3 = enc4 = 64;
            }
            output.append(base64.charAt(enc1)).append(base64.charAt(enc2)).append(base64.charAt(enc3)).append(base64.charAt(enc4));
        } while (i < input.length());
        return output.toString();
    }

    public static String Base64_decode(String input) {
        if (input == null) return null;
        input = input.replaceAll("[^A-Za-z0-9/+////=]", "");
        StringBuilder output = new StringBuilder();
        int enc1, enc2, enc3, enc4;
        int i = 0;
        do {
            enc1 = base64.indexOf(input.charAt(i++));
            enc2 = base64.indexOf(input.charAt(i++));
            enc3 = base64.indexOf(input.charAt(i++));
            enc4 = base64.indexOf(input.charAt(i++));
            output.append((char) ((enc1 << 2) | (enc2 >> 4)));
            if (enc3 != 64) output.append((char) (((enc2 & 15) << 4) | (enc3 >> 2)));
            if (enc4 != 64) output.append((char) (((enc3 & 3) << 6) | enc4));
        } while (i < input.length());
        return output.toString();
    }

    public static String RSA_encrypt(String data) {
        BigInteger temp = null;
        temp = RSA_pkcs1pad2(data, (modulus.bitLength() + 7) >> 3);
        if (temp == null) return "";
        temp = temp.modPow(encryptionExponent,modulus);
        if (temp == null) return "";
        data = temp.toString(16);
        if ((data.length() & 1) == 1)
            data = "0" + data;
        return Base64_encode(Hex_decode(data));
    }

    public static BigInteger RSA_pkcs1pad2(String data, int keysize) {
        if (keysize < data.length() + 11)
            return null;
        byte[] buffer = new byte[keysize];
        int i = data.length() - 1;
        while (i >= 0 && keysize > 0){
            buffer[--keysize] = (byte)data.codePointAt(i--);
        }
        buffer[--keysize] = 0;
        while (keysize > 2){
            buffer[--keysize] = (byte)(Math.floor(Math.random() * 254) + 1);
        }
        buffer[--keysize] = 2;
        buffer[--keysize] = 0;
        return new BigInteger(buffer);
    }

    public static void main(String[] args) {
        String key = "db2c0e44273867c5ab16bd771eb5446999abe87f9e467d688b86ab57843639404fcb4988ab7b29b6c110cbf29d5bd19d7e534efbf29d8700a1eaa68cb2790c7977ebb2b664278a625730322cc1674035fe8e898c20553ae2db094be6b98c19589799fc72352b12dbbaaab524aa5d8b5bdab24ece3a678f386ddd6d308626bd5511dde75ca75aa53377e2c327698ba009c91270afc49bc9ae4a3fc0ba180d986762de3f2a512b0decfff1a38a6b617a037b08c703356e14cc5289e97d6fd65246685dfb2fe215be0341644575130f6a11e83aaee0d7ddf95b992bfad5fb8f1bddb818818341349651310fad0107ffd41c0b86b68a26c08eff12906efdafb74405";
        String e = "010001";
        BigInteger key_m = new BigInteger(key,16);
//        String r = Hex_encode(key);
//        System.out.println(r);
//        String s = Hex_decode(r);
//        System.out.println(s);
        String i = Base64_encode(key);
        System.out.println(i);
        String s = Base64_decode(i);
        System.out.println(s);

        setRSAPublicKey(key,e);
        for (int j = 0; j < 5; j++) {
            String r = RSA_encrypt("123456789");
            System.out.println(r);
        }

    }
}
