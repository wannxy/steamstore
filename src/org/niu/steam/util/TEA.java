package org.niu.steam.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;

public class TEA {

    public static byte[] encode(byte[] body, byte[] key) {
        byte[] ret = null;
        if (body.length != 0) {
            ret = TEA.a_encodebyte(TEA.a_encodeint(TEA.a_getencodeint(body, true), TEA.a_getencodeint(key, false)), false);
        }
        return ret;
    }

    public static byte[] decode(byte[] arg2, byte[] arg3) {
        if (arg2.length != 0) {
            arg2 = TEA.a_encodebyte(TEA.b_decode(TEA.a_getencodeint(arg2, false), TEA.a_getencodeint(arg3, false)), true);
        }
        return arg2;
    }

    public static int[] a_getencodeint(byte[] arg7, boolean arg8) {
        int[] ints;
        int length1 = (arg7.length & 3) == 0 ? arg7.length >>> 2 : (arg7.length >>> 2) + 1;
        if (arg8) {
            ints = new int[length1 + 1];
            ints[length1] = arg7.length;
        } else {
            ints = new int[length1];
        }

        int length2 = arg7.length;
        for (length1 = 0; length1 < length2; ++length1) {
            int v3 = length1 >>> 2;
            ints[v3] |= (arg7[length1] & 0xFF) << ((length1 & 3) << 3);
        }
//        String str = "[" ;
//        for (int i : ints){
//            str = str+i+"] [";
//        }
//        System.out.println("byte to ints = "+str);
        return ints;
    }

    public static int[] a_encodeint(int[] bodyint, int[] keyint) {
        int bytesize = 4;
        int bodylength = bodyint.length - 1;
        if (bodylength >= 1) {
            if (keyint.length < bytesize) {
                int[] v0 = new int[bytesize];
                System.arraycopy(keyint, 0, v0, 0, keyint.length);
                keyint = v0;
            }

            bytesize = bodyint[bodylength];
            int v6 = -1640531527;
            int len01 = 52 / (bodylength + 1) + 6;
            int v4 = 0;
            while (true) {
                int v3 = len01 - 1;
                if (len01 <= 0) {
//                    String str = "[" ;
//                    for (int i : bodyint){
//                        str = str+i+"] {";
//                    }
//                    System.out.println("encrypt ints = "+str);
                    return bodyint;
                }

                v4 += v6;
                int v7 = v4 >>> 2 & 3;
                for (len01 = 0; len01 < bodylength; ++len01) {
                    int d1 = (bytesize ^ keyint[len01 & 3 ^ v7]);
                    int d2 = (bodyint[len01 + 1] ^ v4);
                    int d3 = bytesize >>> 5;
                    int d4 = bodyint[len01 + 1] << 2;
                    int d5 = bodyint[len01 + 1] >>> 3 ^ bytesize << 4;
                    int d6 = bodyint[len01];
                    int d7 = d1 + d2;
                    int d8 = d3 ^ d4;
                    int d9 = d8 + d5;
                    int d10 = d7 ^ d9;
                    int d11 = d10 + d6;
                    bytesize = ((bytesize ^ keyint[len01 & 3 ^ v7])
                            + (bodyint[len01 + 1] ^ v4) ^ (bytesize >>> 5 ^ bodyint[len01 + 1] << 2)
                            + (bodyint[len01 + 1] >>> 3 ^ bytesize << 4))
                            + bodyint[len01];
                    bodyint[len01] = bytesize;
                }
                int d1 = bodyint[len01 & 3 ^ v7] ^ bytesize;
                int d2 = (bodyint[0] ^ v4);
                int d3 = bytesize >>> 5;
                int d4 = d3 ^ bodyint[0] << 2;
                int d5 = bodyint[0] >>> 3 ^ bytesize << 4;
                int d6 = bodyint[bodylength];
                int d7 = d1 + d2;
                int d8 = d4 + d5;
                int d10 = d7 ^ d8;
                int d11 = d10 + d6;
                bytesize = bodyint[bodylength]
                        + ((keyint[len01 & 3 ^ v7] ^ bytesize)
                        + (bodyint[0] ^ v4) ^ (bytesize >>> 5 ^ bodyint[0] << 2)
                        + (bodyint[0] >>> 3 ^ bytesize << 4));
                bodyint[bodylength] = bytesize;
                len01 = v3;
            }
        }

        return bodyint;
    }

    public static byte[] a_encodebyte(int[] body, boolean flag) {
        byte[] encodebytes;
        int bodylength;
        int bodylen = body.length << 2;
        if (flag) {
            bodylength = body[body.length - 1];
            if (bodylength > bodylen) {
                encodebytes = null;
            } else {
                byte[] bytes = new byte[bodylength];
                int v2;
                for (v2 = 0; v2 < bodylength; ++v2) {
                    bytes[v2] = ((byte) (body[v2 >>> 2] >>> ((v2 & 3) << 3) & 0xFF));
                }

                encodebytes = bytes;
            }
        } else {
            bodylength = bodylen;
            byte[] bytes = new byte[bodylength];
            int v2;
            for (v2 = 0; v2 < bodylength; ++v2) {
                bytes[v2] = ((byte) (body[v2 >>> 2] >>> ((v2 & 3) << 3) & 0xFF));
            }

            encodebytes = bytes;
        }
//        System.out.println("encrypt bytes = "+ ByteHex.bytesToHex3(encodebytes));
        return encodebytes;
    }

    public static int[] b_decode(int[] bodybytes, int[] key) {
        int ints = 4;
        int len0 = bodybytes.length - 1;
        if (len0 >= 1) {
            if (key.length < ints) {
                int[] v0 = new int[ints];
                System.arraycopy(key, 0, v0, 0, key.length);
                key = v0;
            }

            ints = bodybytes[0];
            int v4 = -1640531527;
            int v3;
            for (v3 = (52 / (len0 + 1) + 6) * v4; v3 != 0; v3 -= v4) {
                int v5 = v3 >>> 2 & 3;
                int v0_1;
                for (v0_1 = len0; v0_1 > 0; --v0_1) {
                    ints = bodybytes[v0_1] - ((ints ^ v3) + (bodybytes[v0_1 - 1] ^ key[v0_1 & 3 ^ v5]) ^ (bodybytes[v0_1 - 1] >>> 5 ^ ints << 2) + (ints >>> 3 ^ bodybytes[v0_1 - 1] << 4));
                    bodybytes[v0_1] = ints;
                }

                ints = bodybytes[0] - ((key[v0_1 & 3 ^ v5] ^ bodybytes[len0]) + (ints ^ v3) ^ (bodybytes[len0] >>> 5 ^ ints << 2) + (ints >>> 3 ^ bodybytes[len0] << 4));
                bodybytes[0] = ints;
            }
        }

        return bodybytes;
    }

    public static byte[] gzipa(byte[] arg6) throws IOException {
        byte[] v0_2 = null;
        int v0_1 = 0;
        GZIPInputStream gzipinputStream = null;
        ByteArrayOutputStream output1 = null;
        ByteArrayInputStream bytearrayInputStream = null;
        ByteArrayOutputStream output2 = null;
        try {
            bytearrayInputStream = new ByteArrayInputStream(arg6);
        } catch (Throwable v0) {

        }

        try {
            gzipinputStream = new GZIPInputStream(((InputStream) bytearrayInputStream));
            v0_1 = 0x400;
        } catch (Throwable v0) {

        }

        try {
            v0_2 = new byte[v0_1];
            output1 = new ByteArrayOutputStream();
        } catch (Throwable v0) {

        }

        try {
            while (true) {
                int v2_1 = gzipinputStream.read(v0_2, 0, v0_2.length);
                if (v2_1 == -1) {
                    break;
                }

                output1.write(v0_2, 0, v2_1);
            }

            v0_2 = output1.toByteArray();
            output1.flush();
            if (output1 == null) {
                if (gzipinputStream != null) {
                    gzipinputStream.close();
                }

                if (bytearrayInputStream == null) {
                    return v0_2;
                }

                bytearrayInputStream.close();
                return v0_2;
            }
        } catch (Throwable v0) {

        }

        try {
            output1.close();
            if (gzipinputStream != null) {
                gzipinputStream.close();
            }

            if (bytearrayInputStream == null) {
                return v0_2;
            }

            bytearrayInputStream.close();
        } catch (Exception v1_1) {
        }
        return v0_2;

    }

}
