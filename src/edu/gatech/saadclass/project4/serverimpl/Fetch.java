package edu.gatech.saadclass.project4.serverimpl;

import com.google.gson.Gson;

import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import javax.lang.model.element.Name;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Copyright (c) 2015 Sangwhan Moon <sangwhan@iki.fi>
 */
public class Fetch {
    private static void initProxy() {
        if (proxy == null)
            proxy = new HttpHost("127.0.0.1", 8888, "http");
        if (config == null)
            config = RequestConfig.custom().setProxy(proxy).build();
    }

    public static String get(String url) {
        try (CloseableHttpClient hc = HttpClientBuilder.create().build()) {
            HttpGet getMethod = new HttpGet(url);
            if (useProxy) {
                initProxy();
                getMethod.setConfig(config);
            }
            CloseableHttpResponse response = hc.execute(getMethod);

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String buffer = null;

            while ((buffer = br.readLine()) != null) {
                sb.append(buffer);
            }

            return sb.length() == 0 ? "" : sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static String post(String url, HashMap<String, String> values) {
        try (CloseableHttpClient hc = HttpClientBuilder.create().build()) {
            ArrayList<NameValuePair> nvpl = new ArrayList<>();

            values.keySet().stream().forEach((key) -> {
                nvpl.add(new BasicNameValuePair(key, values.get(key)));
            });

            HttpPost postMethod = new HttpPost(url);
            if (useProxy) {
                initProxy();
                postMethod.setConfig(config);
            }
            postMethod.setEntity(new UrlEncodedFormEntity(nvpl));
            CloseableHttpResponse response = hc.execute(postMethod);

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String buffer = null;

            while ((buffer = br.readLine()) != null) {
                sb.append(buffer);
            }

            return sb.length() == 0 ? "" : sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static String post(String url, String jsonValue) {
        try (CloseableHttpClient hc = HttpClientBuilder.create().build()) {
            StringEntity se = new StringEntity(jsonValue);
            se.setContentType("application/json");

            HttpPost postMethod = new HttpPost(url);
            if (useProxy) {
                initProxy();
                postMethod.setConfig(config);
            }
            postMethod.setEntity(se);
            CloseableHttpResponse response = hc.execute(postMethod);

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String buffer = null;

            while ((buffer = br.readLine()) != null) {
                sb.append(buffer);
            }

            return sb.length() == 0 ? "" : sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static String put(String url, HashMap<String, String> values) {
        try (CloseableHttpClient hc = HttpClientBuilder.create().build()) {
            ArrayList<NameValuePair> nvpl = new ArrayList<>();

            values.keySet().stream().forEach((key) -> {
                nvpl.add(new BasicNameValuePair(key, values.get(key)));
            });

            HttpPut putMethod = new HttpPut(url);
            if (useProxy) {
                initProxy();
                putMethod.setConfig(config);
            }
            putMethod.setEntity(new UrlEncodedFormEntity(nvpl));
            CloseableHttpResponse response = hc.execute(putMethod);

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String buffer = null;

            while ((buffer = br.readLine()) != null) {
                sb.append(buffer);
            }

            return sb.length() == 0 ? "" : sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public static String put(String url, String jsonValue) {
        try (CloseableHttpClient hc = HttpClientBuilder.create().build()) {
            StringEntity se = new StringEntity(jsonValue);
            se.setContentType("application/json");

            HttpPut putMethod = new HttpPut(url);
            if (useProxy) {
                initProxy();
                putMethod.setConfig(config);
            }
            putMethod.setEntity(se);
            CloseableHttpResponse response = hc.execute(putMethod);

            BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String buffer = null;

            while ((buffer = br.readLine()) != null) {
                sb.append(buffer);
            }

            return sb.length() == 0 ? "" : sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

    private static final boolean useProxy = false;
    private static HttpHost proxy = null;
    private static RequestConfig config = null;

    // TODO: We don't use DELETE at all in the solver, so that's been skipped for now.
}
