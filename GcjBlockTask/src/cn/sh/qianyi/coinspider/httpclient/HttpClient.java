package cn.sh.qianyi.coinspider.httpclient;

import com.alibaba.fastjson.JSON;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.NoHttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpClient {
    static Logger log;
    private static String charset;
    public static String cookies;

    static {
        HttpClient.log = Logger.getLogger(HttpClient.class);
        HttpClient.charset = "utf-8";
        HttpClient.cookies = "";
    }

    public static String doPostJson(final Map<String, Object> map, final Map<String, Object> headerMap, final String url) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        CloseableHttpResponse response = null;
        boolean hasError = false;
        do {
            hasError = false;
            Label_0357:
            {
                try {
                    httpClient = HttpClients.custom().build();
                    httpPost = new HttpPost(url);
                    httpPost.setConfig(RequestConfig.custom().setConnectTimeout(300000).setConnectionRequestTimeout(600000).setSocketTimeout(600000).build());
                    final List<NameValuePair> list = new ArrayList<NameValuePair>();
                    final StringEntity entity = new StringEntity(JSON.toJSONString(map), HttpClient.charset);
                    entity.setContentEncoding("UTF-8");
                    entity.setContentType("application/json");
                    httpPost.setEntity(entity);
                    if (headerMap != null && !headerMap.isEmpty()) {
                        for (final Map.Entry<String, Object> m : headerMap.entrySet()) {
                            httpPost.setHeader(m.getKey(), m.getValue().toString());
                        }
                    }
                    response = httpClient.execute((HttpUriRequest) httpPost);
                    final int statusCode = response.getStatusLine().getStatusCode();
                    if (response == null) {
                        break Label_0357;
                    }
                    final HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity, HttpClient.charset);
                    }
                } catch (NoHttpResponseException ex) {
                    ex.printStackTrace();
                    hasError = true;
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                    hasError = true;
                } finally {
                    if (response != null) {
                        try {
                            response.close();
                        } catch (IOException ex3) {
                        }
                    }
                    if (httpClient != null) {
                        try {
                            httpClient.close();
                        } catch (IOException ex4) {
                        }
                    }
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex5) {
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ex6) {
                }
            }
        } while (hasError);
        return result;
    }

    public static String doPost(final Map<String, Object> map, final Map<String, Object> headerMap, final String url) {
        CloseableHttpClient httpClient = null;
        HttpPost httpPost = null;
        String result = null;
        CloseableHttpResponse response = null;
        boolean hasError = false;
        do {
            hasError = false;
            Label_0424:
            {
                try {
                    httpClient = HttpClients.custom().build();
                    httpPost = new HttpPost(url);


                    httpPost.setConfig(RequestConfig.custom().setConnectTimeout(300000).setConnectionRequestTimeout(600000).setSocketTimeout(600000).build());
                    final List<NameValuePair> list = new ArrayList<NameValuePair>();
                    for (final Map.Entry<String, Object> elem : map.entrySet()) {
                        list.add(new BasicNameValuePair(elem.getKey(), elem.getValue().toString()));
                    }
                    if (list.size() > 0) {
                        final UrlEncodedFormEntity entity = new UrlEncodedFormEntity(list, HttpClient.charset);
                        httpPost.setEntity(entity);
                    }
                    if (headerMap != null && !headerMap.isEmpty()) {
                        for (final Map.Entry<String, Object> m : headerMap.entrySet()) {
                            httpPost.setHeader(m.getKey(), m.getValue().toString());
                        }
                    }
                    response = httpClient.execute((HttpUriRequest) httpPost);
                    final int statusCode = response.getStatusLine().getStatusCode();
                    if (response == null) {
                        break Label_0424;
                    }
                    final HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity, HttpClient.charset);
                    }
                } catch (NoHttpResponseException ex) {
                    ex.printStackTrace();
                    hasError = true;
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                    hasError = true;
                } finally {
                    if (response != null) {
                        try {
                            response.close();
                        } catch (IOException ex3) {
                        }
                    }
                    if (httpClient != null) {
                        try {
                            httpClient.close();
                        } catch (IOException ex4) {
                        }
                    }
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex5) {
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ex6) {
                }
            }
        } while (hasError);
        return result;
    }

    public static String doGet(final Map<String, Object> headersMap, final String url) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;
        CloseableHttpResponse response = null;
        boolean hasError = false;
        hasError = false;
        try {
            httpClient = HttpClients.custom().build();
            httpGet = new HttpGet(url);
            httpGet.setConfig(RequestConfig.custom().setConnectTimeout(30000).setConnectionRequestTimeout(60000).setSocketTimeout(60000).build());
            for (final Map.Entry<String, Object> m : headersMap.entrySet()) {
                httpGet.setHeader(m.getKey(), m.getValue().toString());
            }
            response = httpClient.execute((HttpUriRequest) httpGet);
            if (response != null) {
                final HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, HttpClient.charset);
                }
                final Header[] header = response.getAllHeaders();
                Header[] array;
                for (int length = (array = header).length, i = 0; i < length; ++i) {
                    final Header h = array[i];
                    if (h.getName().equals("Set-Cookie")) {
                        HttpClient.cookies = h.getValue().replace("Path=/", "");
                        break;
                    }
                }
            }
        } catch (NoHttpResponseException ex) {
            HttpClient.log.error("http is error:" + url, ex);
            hasError = true;
        } catch (Exception ex2) {
            HttpClient.log.error("http is error:" + url, ex2);
            hasError = true;
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex3) {
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ex4) {
                }
            }
        }
        if (response != null) {
            try {
                response.close();
            } catch (IOException ex5) {
            }
        }
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException ex6) {
            }
        }
        return result;
    }

    public static String doGet(final String url) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;
        CloseableHttpResponse response = null;
        boolean hasError = false;
        hasError = false;
        Label_0258:
        {
            try {
                httpClient = HttpClients.custom().build();
                httpGet = new HttpGet(url);
                httpGet.setConfig(RequestConfig.custom().setConnectTimeout(50000).setConnectionRequestTimeout(10000).setSocketTimeout(50000).build());
                response = httpClient.execute((HttpUriRequest) httpGet);
                if (response == null) {
                    break Label_0258;
                }
                final HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    result = EntityUtils.toString(resEntity, "gbk");
                }
            } catch (NoHttpResponseException ex) {
                HttpClient.log.error("http is error:" + url, ex);
                hasError = true;
            } catch (Exception ex2) {
                HttpClient.log.error("http is error:" + url, ex2);
                hasError = true;
            } finally {
                if (response != null) {
                    try {
                        response.close();
                    } catch (IOException ex3) {
                    }
                }
                if (httpClient != null) {
                    try {
                        httpClient.close();
                    } catch (IOException ex4) {
                    }
                }
            }
        }
        if (response != null) {
            try {
                response.close();
            } catch (IOException ex5) {
            }
        }
        if (httpClient != null) {
            try {
                httpClient.close();
            } catch (IOException ex6) {
            }
        }
        return result;
    }

    public static String doGetGbk(final String url) throws Exception {
        CloseableHttpClient httpClient = null;
        HttpGet httpGet = null;
        String result = null;
        CloseableHttpResponse response = null;
        boolean hasError = false;
        do {
            hasError = false;
            Label_0218:
            {
                try {
                    httpClient = HttpClients.custom().build();
                    httpGet = new HttpGet(url);
                    httpGet.setConfig(RequestConfig.custom().setConnectTimeout(50000).setConnectionRequestTimeout(10000).setSocketTimeout(50000).build());
                    response = httpClient.execute((HttpUriRequest) httpGet);
                    if (response == null) {
                        break Label_0218;
                    }
                    final HttpEntity resEntity = response.getEntity();
                    if (resEntity != null) {
                        result = EntityUtils.toString(resEntity, "gbk");
                    }
                } catch (NoHttpResponseException ex) {
                    ex.printStackTrace();
                    hasError = true;
                } catch (Exception ex2) {
                    ex2.printStackTrace();
                    hasError = true;
                } finally {
                    if (response != null) {
                        try {
                            response.close();
                        } catch (IOException ex3) {
                        }
                    }
                    if (httpClient != null) {
                        try {
                            httpClient.close();
                        } catch (IOException ex4) {
                        }
                    }
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException ex5) {
                }
            }
            if (httpClient != null) {
                try {
                    httpClient.close();
                } catch (IOException ex6) {
                }
            }
        } while (hasError);
        return result;
    }

}
