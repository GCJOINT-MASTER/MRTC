package cn.sh.qianyi.coinspider.util;

import java.net.*;
import java.io.*;
import java.util.regex.*;

public class FileUtils
{
    public static void write(final String filePath, final String content) {
        File file = null;
        FileWriter fw = null;
        try {
            file = new File(filePath);
            fw = new FileWriter(file, true);
            fw.write(String.valueOf(content) + "\r\n");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (fw != null) {
                try {
                    fw.close();
                }
                catch (IOException ex) {}
            }
        }
        if (fw != null) {
            try {
                fw.close();
            }
            catch (IOException ex2) {}
        }
    }
    
    public static void downloadFile(final String urlString, final String filename, final String filePath) throws Exception {
        final URL url = new URL(urlString);
        final URLConnection con = url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/57.0.2987.98 Safari/537.36");
        final InputStream is = con.getInputStream();
        final File dir = new File(filePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        final FileOutputStream fos = new FileOutputStream(String.valueOf(filePath) + filename);
        final byte[] bt = new byte[1024];
        int b = 0;
        while ((b = is.read(bt)) != -1) {
            fos.write(bt, 0, b);
        }
        fos.flush();
        fos.close();
        is.close();
    }
    
    public static String Html2Text(final String inputString) {
        String textStr = "";
        try {
            final String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";
            final String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";
            final String regEx_html = "<[^>]+>";
            final Pattern p_script = Pattern.compile(regEx_script, 2);
            final Matcher m_script = p_script.matcher(inputString);
            String htmlStr = m_script.replaceAll("");
            final Pattern p_style = Pattern.compile(regEx_style, 2);
            final Matcher m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll("");
            final Pattern p_html = Pattern.compile(regEx_html, 2);
            final Matcher m_html = p_html.matcher(htmlStr);
            htmlStr = (textStr = m_html.replaceAll(""));
        }
        catch (Exception ex) {}
        return textStr;
    }
}
