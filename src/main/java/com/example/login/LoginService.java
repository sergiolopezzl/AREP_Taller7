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

/**
 * Clase que proporciona un servicio de autenticación mediante un servicio remoto.
 */
public class LoginService {

    /**
     * Método principal para iniciar el servicio de autenticación.
     *
     * @param args Argumentos de línea de comandos (no se utilizan).
     * @throws NoSuchAlgorithmException Si el algoritmo requerido no está disponible.
     * @throws KeyStoreException Si se produce un error en el almacén de claves.
     * @throws IOException Si se produce un error de entrada/salida.
     * @throws KeyManagementException Si se produce un error en la gestión de claves.
     * @throws CertificateException Si se produce un error en la certificación.
     */
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

    /**
     * Obtiene el puerto en el que se ejecutará el servicio. Utiliza el puerto 8080 si no se especifica uno.
     *
     * @return El puerto en el que se ejecutará el servicio.
     */
    public static int getPort() {
        return Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt).orElse(8080);
    }

    /**
     * Configura el contexto SSL confiable para establecer una conexión segura.
     *
     * @throws KeyStoreException Si se produce un error en el almacén de claves.
     * @throws IOException Si se produce un error de entrada/salida.
     * @throws NoSuchAlgorithmException Si el algoritmo requerido no está disponible.
     * @throws KeyManagementException Si se produce un error en la gestión de claves.
     * @throws CertificateException Si se produce un error en la certificación.
     */
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

    /**
     * Realiza una solicitud HTTP GET a una URL y devuelve la respuesta como una cadena de texto.
     *
     * @param query Los parámetros de la consulta a agregar a la URL.
     * @return La respuesta obtenida como una cadena de texto.
     * @throws IOException Si se produce un error de entrada/salida.
     */
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
