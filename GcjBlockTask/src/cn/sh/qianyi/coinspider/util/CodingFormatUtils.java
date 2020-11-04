package cn.sh.qianyi.coinspider.util;

import org.apache.http.impl.client.*;
import java.io.*;

public class CodingFormatUtils
{
    static CloseableHttpClient httpClient;
    private static final String CHARSET = "UTF-8";
    
    static {
        CodingFormatUtils.httpClient = null;
    }
    
    public static String convertStreamToString(final InputStream is, final String charet) {
        final StringBuilder sb1 = new StringBuilder();
        final byte[] bytes = new byte[4096];
        int size = 0;
        try {
            while ((size = is.read(bytes)) > 0) {
                final String str = new String(bytes, 0, size, "utf-8");
                sb1.append(str);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            try {
                is.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
            return sb1.toString();
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e2) {
                e2.printStackTrace();
            }
        }
        try {
            is.close();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return sb1.toString();
    }
    
    public static String toUtf8(final String html) {
        BufferedReader br = null;
        StringBuffer sbf = null;
        InputStream is = null;
        try {
            is = new ByteArrayInputStream(html.getBytes());
            br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            sbf = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sbf.append(line);
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        return sbf.toString();
    }
}
