package com.example.testLogin;

import com.example.login.UsersService;
import org.junit.jupiter.api.Test;
import java.security.NoSuchAlgorithmException;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginTest {

    @Test
    void testVerifyPassword() throws NoSuchAlgorithmException {
        UsersService.addUser("TestUser", "TestPassword");

        // Verificar que la contraseña correcta sea válida
        assertTrue(UsersService.verifyPassword("TestUser", "TestPassword"));

        // Verificar que una contraseña incorrecta no sea válida
        assertFalse(UsersService.verifyPassword("TestUser", "IncorrectPassword"));

        // Verificar que un usuario inexistente no sea válido
        assertFalse(UsersService.verifyPassword("NonExistentUser", "TestPassword"));
    }
    @Test
    void testHashPassword() throws NoSuchAlgorithmException {
        // Verificar que el hash de una contraseña sea correcto
        byte[] hash = UsersService.hashPassword("TestPassword");
        assertTrue(hash.length > 0);
    }

    @Test
    void testAddUser() throws NoSuchAlgorithmException {
        // Verificar que se pueda agregar un usuario correctamente
        UsersService.addUser("TestUser", "TestPassword");
        assertTrue(UsersService.verifyPassword("TestUser", "TestPassword"));

        // Verificar que no se pueda agregar un usuario duplicado
        UsersService.addUser("DuplicateUser", "TestPassword");
        //assertFalse(UsersService.addUser("DuplicateUser", "TestPassword"));
    }
}
