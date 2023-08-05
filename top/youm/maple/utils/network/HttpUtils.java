package top.youm.maple.utils.network;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * @author YouM
 * Created on 2023/7/17
 */
public class HttpUtils {
    public static String POSTRequest(String urlParam, Map<String,String> headers, String body) throws IOException {
        return sendRequest(urlParam,"POST",headers,body);
    }
    public static String GETRequest(String urlParam, Map<String,String> headers) throws IOException {
        return sendRequest(urlParam,"GET",headers,null);
    }
    public static String sendRequest(String urlParam, String requestType, Map<String,String> headers, String body) throws IOException {
        URL url = new URL(urlParam);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestType);
        headers.forEach(connection::setRequestProperty);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        connection.connect();
        if(requestType.equals("POST")){
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
            writer.write(body);
            writer.flush();
        }
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            StringBuilder resultBuffer = new StringBuilder();
            String line;
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((line = buffer.readLine()) != null) {
                resultBuffer.append(line);
            }
            buffer.close();
            connection.disconnect();
            return resultBuffer.toString();
        }
        return "ERROR!!!!!!!";
    }
    public static String htmlRequest(String urlParam,String requestType,String host,String cookie) throws IOException {
        URL url = new URL(urlParam);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod(requestType);
        connection.setRequestProperty("Host", host);
        connection.setRequestProperty("Cookie", cookie);
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:109.0) Gecko/20100101 Firefox/115.0");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setUseCaches(false);
        int responseCode = connection.getResponseCode();
        if(responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = connection.getInputStream();
            StringBuilder resultBuffer = new StringBuilder();
            String line;
            BufferedReader buffer = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while ((line = buffer.readLine()) != null) {
                resultBuffer.append(line);
            }
            return resultBuffer.toString();
        }
        return "";
    }
    public static String getRedirectUrl(String url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setInstanceFollowRedirects(false);
        conn.setConnectTimeout(5000);
        return conn.getHeaderField("Location");
    }
}
