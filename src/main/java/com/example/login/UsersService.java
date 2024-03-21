package com.example.login;

import java.security.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Optional;

import static spark.Spark.*;

public class UsersService {

    private static final HashMap<String, byte[]> users = new HashMap<>();

    public static void main(String[] args) throws NoSuchAlgorithmException {
        addUser("Sergio", "123456");
        addUser("Lopez", "654321");
        port(getPort());
        secure("certificados/ecikeystore.p12", "123456", null, null);
        get("/user", (req, res) -> {
            res.type("application/json");
            boolean result = verifyPassword(req.queryParams("name"), req.queryParams("password"));
            return "{\"result\":" + result + "}";
        });
    }

    public static byte[] hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes();
        return md.digest(bytes);
    }

    public static boolean verifyPassword(String userName, String password) throws NoSuchAlgorithmException {
        byte[] hash = users.get(userName);
        byte[] attemptedHash = hashPassword(password);
        return Arrays.equals(hash, attemptedHash);
    }

    public static int getPort() {
        return Optional.ofNullable(System.getenv("PORT")).map(Integer::parseInt).orElse(8088);
    }

    public static void addUser(String name, String password) throws NoSuchAlgorithmException {
        users.put(name, hashPassword(password));
    }
}
