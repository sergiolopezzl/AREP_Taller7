package com.example.login;

import java.security.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static spark.Spark.*;

/**
 * Clase que gestiona la autenticación de usuarios.
 */
public class UsersService {

    /** Mapa que almacena los nombres de usuario y sus contraseñas cifradas. */
    private static final HashMap<String, byte[]> users = new HashMap<>();

    /**
     * Método principal para iniciar el servicio de autenticación de usuarios.
     *
     * @param args Argumentos de línea de comandos (no se utilizan).
     * @throws NoSuchAlgorithmException Si el algoritmo requerido no está disponible.
     */
    public static void main(String[] args) throws NoSuchAlgorithmException {
        // Agregar usuarios y sus contraseñas
        addUser("Sergio", "123456");
        addUser("Lopez", "654321");

        // Configurar el puerto en el que se ejecutará el servicio
        port(getPort());

        // Configurar la seguridad utilizando un almacén de claves
        secure("certificados/ecikeystore.p12", "123456", null, null);

        // Establecer la ruta para manejar las solicitudes de autenticación
        get("/user", (req, res) -> {
            res.type("application/json");
            boolean result = verifyPassword(req.queryParams("name"), req.queryParams("password"));
            return "{\"result\":" + result + "}";
        });
    }

    /**
     * Calcula el hash SHA-256 de una contraseña.
     *
     * @param password La contraseña a cifrar.
     * @return El hash de la contraseña.
     * @throws NoSuchAlgorithmException Si el algoritmo de hash no está disponible.
     */
    public static byte[] hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes();
        return md.digest(bytes);
    }

    /**
     * Verifica si una contraseña coincide con la almacenada para un usuario.
     *
     * @param userName El nombre de usuario.
     * @param password La contraseña a verificar.
     * @return true si la contraseña coincide, false en caso contrario.
     * @throws NoSuchAlgorithmException Si el algoritmo de hash no está disponible.
     */
    public static boolean verifyPassword(String userName, String password) throws NoSuchAlgorithmException {
        byte[] hash = users.get(userName);
        byte[] attemptedHash = hashPassword(password);
        return Arrays.equals(hash, attemptedHash);
    }

    /**
     * Obtiene el puerto en el que se ejecutará el servicio. Utiliza el puerto 8088 si no se especifica uno.
     *
     * @return El puerto en el que se ejecutará el servicio.
     */
    public static int getPort() {
        return Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt).orElse(8088);
    }

    /**
     * Agrega un nuevo usuario con su contraseña cifrada al mapa de usuarios.
     *
     * @param name El nombre de usuario.
     * @param password La contraseña del usuario.
     * @throws NoSuchAlgorithmException Si el algoritmo de hash no está disponible.
     */
    public static void addUser(String name, String password) throws NoSuchAlgorithmException {
        users.put(name, hashPassword(password));
    }
}
