package com.example.login;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.*;

import static spark.Spark.*;

public class LoginService {

    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException, CertificateException {
        staticFiles.location("/public");
        port(getPort());
        secure("certificados/ecikeystore.p12", "123456", null, null);
        configureTrustedSSLContext();
        get("/login", (req, res) -> {
            res.type("application/json");
            return readURL("name=" + req.queryParams("name") + "&password=" + req.queryParams("password"));
        });
    }

    public static int getPort() {
        return Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt).orElse(8080);
    }

    private static void configureTrustedSSLContext() throws KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException {
        File trustStoreFile = new File("certificados/myTrustStore.p12");
        char[] trustStorePassword = "123456".toCharArray();
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        try (FileInputStream fis = new FileInputStream(trustStoreFile)) {
            trustStore.load(fis, trustStorePassword);
        }
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        for (TrustManager t : tmf.getTrustManagers()) System.out.println(t);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        SSLContext.setDefault(sslContext);
    }

    public static String readURL(String query) throws IOException {
        URL siteURL = new URL("https://localhost:8088/user?" + query);
        URLConnection urlConnection = siteURL.openConnection();
        Map<String, List<String>> headers = urlConnection.getHeaderFields();
        headers.forEach((headerName, headerValues) -> {
            if (headerName != null) System.out.print(headerName + ":");
            headerValues.forEach(System.out::print);
            System.out.println("");
        });
        System.out.println("------body------");
        StringBuilder response = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
            String inputLine;
            while ((inputLine = reader.readLine()) != null) response.append(inputLine);
        }
        System.out.println(response);
        System.out.println("GET DONE");
        return response.toString();
    }
}
