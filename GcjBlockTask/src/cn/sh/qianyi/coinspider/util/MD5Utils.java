package cn.sh.qianyi.coinspider.util;

import java.security.*;

public class MD5Utils
{
    public static void main(final String[] args) {
        final String str = "javascript:window.open(&quot;../ZuiGaoXJ/ReadAttachFile.aspx";
        System.out.println(str.replaceAll("\\.\\.", "http://www.sohu.com"));
        System.out.println(getMd5("http://www.sohu.com"));
    }
    
    public static String getMd5(final String plainText) {
        try {
            final MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            final byte[] b = md.digest();
            final StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }
                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
