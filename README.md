### Sergio Daniel Lopez Vargas

# AREP_Taller7


## Introducción

El sistema de autenticación desarrollado consta de dos clases principales: `LoginService` y `UsersService`. Estas 
clases forman parte de un servicio que permite a los usuarios iniciar sesión mediante un servidor remoto.

## Características y Funcionalidades:

- **Autenticación de Usuarios**: Permite a los usuarios iniciar sesión utilizando un nombre de usuario y una contraseña.
- **Cifrado de Contraseñas**: Las contraseñas de los usuarios se almacenan cifradas utilizando el algoritmo SHA-256.
- **Seguridad SSL/TLS**: Se utiliza un contexto SSL seguro para establecer conexiones seguras entre el cliente y el servidor.
- **Configuración de Puerto**: Permite configurar el puerto en el que se ejecutará el servicio, con un valor predeterminado de 8080 para `LoginService` y 8088 para `UsersService`.
- **Interfaz RESTful**: Utiliza Spark para proporcionar una interfaz RESTful que maneja las solicitudes de autenticación.

## Arquitectura

El sistema sigue una arquitectura cliente-servidor, donde el cliente realiza solicitudes de autenticación al servidor 
a través de una conexión segura. El servidor responde a estas solicitudes verificando las credenciales proporcionadas 
por el cliente y devolviendo un resultado de autenticación en formato JSON. El cifrado de las contraseñas garantiza la 
seguridad de las credenciales almacenadas en el servidor. Además, la configuración de SSL/TLS asegura la integridad y 
la confidencialidad de las comunicaciones entre el cliente y el servidor.

## Instrucciones de Ejecución
* Clone el repositorio desde GitHub:

```
git clone https://github.com/sergiolopezzl/AREP_Taller7.git
```

* Navegue al directorio del proyecto: 

```
cd AREP_Taller7
```

* Compile el proyecto y descargue las dependencias con Maven: 

```
mvn clean package
```

* Ejecuta el 1 servicio con el siguiente comando: 

```
mvn exec:java '-Dexec.mainClass=com.example.login.LoginService'
```

* Ejecuta el 2 servicio con el siguiente comando: :

```
mvn exec:java '-Dexec.mainClass=com.example.login.UsersService'
```

* Entre a la pagina mediante este link si es Localmente:

```
https://localhost:8080/index.html
```

* Entre a la pagina mediante este link si esta la instancia EC2:

```
https://ec2-3-93-43-143.compute-1.amazonaws.com:8080/index.html
```


## Ejemplo de desarrollo

### Pruebas

# Video

[![Video](https://img.youtube.com/vi/haHc01DqGTo/sddefault.jpg)](https://www.youtube.com/watch?v=haHc01DqGTo)

# Imagenes
### Login 1
![prueba1.png](src/main/resources/public/img/prueba1.png)
### Login 2
![prueba2.png](src/main/resources/public/img/prueba2.png)
### Login Incorrecto
![prueba3.png](src/main/resources/public/img/prueba3.png)
### Pruebas unitarias
![prueba4.png](src/main/resources/public/img/prueba4.png)







