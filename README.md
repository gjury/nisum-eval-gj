<a name="readme-top"></a>


[![LinkedIn][linkedin-shield]][linkedin-url]


<br />
<div align="center">
  <h3 align="center">Ejercicio de Evaluación para NISUM</h3>

  <p align="center">
    Guillermo Jury

  </p>
</div>

<br />

### Ejercicio Java Spring.

### Herramientas usadas


* ![JAVA][JAVA]
* ![SPRING][SPRING]
* ![MAVEN][MAVEN]



### Prerequisitos

Para poder ejecutar este proyecto, se requiere un entorno con JAVA 8 o superior y maven instalados

* ![JAVA] ![MAVEN]

### Instalación

_Ejecutar los siguientes pasos para probar la aplicación._

1. Ingresar a la consola
2. Clonar el repositorio:
   ```sh
   $ git clone git@github.com:gjury/nisum-eval-gj.git
   ```
3. cambiamos al directorio creado
   ```sh
   $ cd nisum-eval-gj
   ```
4. Ya podemos correr los tests para asegurarnos de que esta todo correcto:
   ```sh
   $ mvn test
   ```
5. Se ejecutan todas las pruebas y obtenemos un resumen que confirma el éxito de todas las ejecuciones:
![tests-screenshot]



## Uso

A continuacion ejecutamos la aplicacion para probar su funcionamiento.
   ```sh
   $ mvn spring-boot:run
   ```
   ![start-screenshot]

Al levantar la aplicacion nos va a dejar disponible los siguientes recursos:


1. La consola de la Base de Datos H2. 

Podemos ingresar a la misma meidante la url 
```js
http://localhost:8080/h2-console
```
La misma nos presenta una pantalla de ingreso como la siguiente:

   ![ssh2-screenshot]


Notese que los parametros para el ingreso estan especificados en el archivo de configuracion 
```sh
src/main/resources/application.properties
```

Especificamente podremos encontrar el JDBC URL, el usuario y password para poder explorar los datos que se vayan generando en la aplicacion. Al iniciar la aplicacion no existiran datos aun, pero cuando empecemos a crear usuarios, podremos monitorear simultaneamente los datos almacenados.
```sh
spring.datasource.url=jdbc:h2:mem:ejercicio
spring.datasource.username=sa
spring.datasource.password=nisum_pwd
```

<br />

2. Por otra parte contamos con la interface de usuario de Swagger que documenta los endpoints disponibles en nuestra aplicacion. En este caso podemos accederla mediante la url:
```js
http://localhost:8080/doc/swagger-ui/index.html
```
![sswagger-screenshot]







## Prueba

Esta API RESTful se puede probar con diferentes herramientas, Postman, la misma UI Swagger que se ofrece desde la aplicacion, y otras herramientas que permitan enviar un Request al endpoint. Para una mejor visualizacion, en este documento utilizare `curl` para mostrar las distintas interacciones request - response. 
En primer lugar crearemos un nuevo registro de usuario:
```sh
> curl -X 'POST' \
      'http://localhost:8080/auth/register' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
      -d '{
    "name": "Juan Pruebas",
    "email": "juan@alguien.com",
    "password": "12344231",
    "role": "USER",
    "phones": [
      {
        "number": "1234567",
        "citycode": "1",
        "countrycode": "57"
      },
      {
        "number": "8914522",
        "citycode": "1",
        "countrycode": "57"
      }
    ]
  }' -vvv
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /auth/register HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.74.0
> accept: */*
> Content-Type: application/json
> Content-Length: 299
>
* upload completely sent off: 299 out of 299 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: SAMEORIGIN
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Fri, 24 Nov 2023 16:38:58 GMT
<
* Connection #0 to host localhost left intact
{"token":"eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuQGFsZ3VpZW4uY29tIiwiaWF0IjoxNzAwODQzOTM4LCJleHAiOjE3MDA4NDUzNzh9.jnD_ArOzBvZitEGqnmriNGwJoxvupi8y7mbnBlmH0rg7IoGgOZPmQ0ExVRxMkQRU"}
```

Puede observarse como el registro es exitoso al recibir un codigo de status 200 y el token devuelto al usuario.

Si se quisiera volver a registrar ese mismo email, obtenemos un error:
```sh
❯ curl -X 'POST' \
      'http://localhost:8080/auth/register' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
      -d '{
    "name": "Pedro Pruebas",
    "email": "juan@alguien.com",
    "password": "12344231",
    "role": "USER",
    }' -vvv
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /auth/register HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.74.0
> accept: */*
> Content-Type: application/json
> Content-Length: 107
>
* upload completely sent off: 107 out of 107 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 400
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: SAMEORIGIN
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Fri, 24 Nov 2023 16:45:21 GMT
< Connection: close
<
* Closing connection 0
{"mensaje":"Ya existe un usuario con este email"}⏎
```

_Notese que en el archivo de propiedades esta la configuracion de los formatos permitidos tanto para el email como para el password. Si se intenta registrar un usuario con un password o email que no satisfacen la expresion regular configurada en el application properties, el registro fallará_

```sh
gj-nisum.email.formato:^(.+)@(.+)$
gj-nisum.password.formato: ^\\d{8}$
```
Por ejemplo este email es invalido silvia#alguien.com, y el mensaje de error asi lo indica.

```sh
❯ curl -X 'POST' \
      'http://localhost:8080/auth/register' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
      -d '{
    "name": "Silvia Pruebas",
    "email": "silvia#alguien.com",
    "password": "12344231",
    "role": "USER"}' -vvv
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /auth/register HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.74.0
> accept: */*
> Content-Type: application/json
> Content-Length: 106
>
* upload completely sent off: 106 out of 106 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 400
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: SAMEORIGIN
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Fri, 24 Nov 2023 17:41:54 GMT
< Connection: close
<
* Closing connection 0
{"mensaje":"Error en formato de email"}⏎
```



<br />
Existiendo entonces el usuario en sistema, ya podemos realizar el login:


```sh
❯ curl -X 'POST' \
      'http://localhost:8080/auth/login' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
      -d '{
    "email": "juan@alguien.com",
    "password": "12344231"
  }' -vvv
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /auth/login HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.74.0
> accept: */*
> Content-Type: application/json
> Content-Length: 59
>
* upload completely sent off: 59 out of 59 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: SAMEORIGIN
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Fri, 24 Nov 2023 16:51:31 GMT
<
* Connection #0 to host localhost left intact
{"token":"eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuQGFsZ3VpZW4uY29tIiwiaWF0IjoxNzAwODQ0NjkxLCJleHAiOjE3MDA4NDYxMzF9.3UNTkTD9Ih01C0lXPpE5Ly-pdWhlkx9SCMv4tKuZA-bRNDd2Jhe2WMqTJTzD9FBJ"}⏎
```

Si no ponemos el password correcto, el sistema no dejara que ingresemos:
```sh
❯ curl -X 'POST' \
      'http://localhost:8080/auth/login' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
      -d '{
    "email": "juan@alguien.com",
    "password": "PASS_INCORRECTO"
  }' -vvv
Note: Unnecessary use of -X or --request, POST is already inferred.
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> POST /auth/login HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.74.0
> accept: */*
> Content-Type: application/json
> Content-Length: 66
>
* upload completely sent off: 66 out of 66 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 400
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: SAMEORIGIN
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Fri, 24 Nov 2023 16:55:15 GMT
< Connection: close
<
* Closing connection 0
{"mensaje":"Bad credentials"}⏎
```


Una vez logineados, ya tenemos el token que nos permitira acceder a los recursos protegidos de la aplicacion.
En primer lugar intentemos acceder `SIN` el token, para confirmar que `NO` podemos acceder:

```sh
❯ curl -X 'GET' \
      'http://localhost:8080/api/v1/usuario/juana@alguien.com' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
  -vvv

Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /api/v1/usuario/juana@alguien.com HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.74.0
> accept: */*
> Content-Type: application/json
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 403
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: SAMEORIGIN
< Content-Length: 0
< Date: Fri, 24 Nov 2023 17:01:20 GMT
<
* Connection #0 to host localhost left intact
```

Efectivamente obtenemos un 403 que impide el acceso al recurso protegido. 

Por el contrario cuando accedemos con el JWT token obtenido en el login, si podremos acceder:



```sh
❯ curl -X 'GET' \
      'http://localhost:8080/api/v1/usuario/juana@alguien.com' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
  -H 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuQGFsZ3VpZW4uY29tIiwiaWF0IjoxNzAwODQ1NDExLCJleHAiOjE3MDA4NDY4NTF9.Upv59jifwOF7asTMkHu9lLWomWryOT9HmgF-a3OT9R5Yn6gpKRnq-LEA3GGNu5ot' \
   -vvv
Note: Unnecessary use of -X or --request, GET is already inferred.
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> GET /api/v1/usuario/juana@alguien.com HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.74.0
> accept: */*
> Content-Type: application/json
> Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuQGFsZ3VpZW4uY29tIiwiaWF0IjoxNzAwODQ1NDExLCJleHAiOjE3MDA4NDY4NTF9.Upv59jifwOF7asTMkHu9lLWomWryOT9HmgF-a3OT9R5Yn6gpKRnq-LEA3GGNu5ot
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Disposition: inline;filename=f.txt
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: SAMEORIGIN
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Fri, 24 Nov 2023 17:04:13 GMT
<
* Connection #0 to host localhost left intact
{
	"id": "9e040879-f391-4542-967b-750141c84ca4",
	"name": "Juana Pruebas",
	"email": "juana@alguien.com",
	"role": "USER",
	"created": "2023-11-24T13:57:59.277053",
	"modified": null,
	"last_login": "2023-11-24T13:57:59.277113",
	"token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuYUBhbGd1aWVuLmNvbSIsImlhdCI6MTcwMDg0NTA3OSwiZXhwIjoxNzAwODQ2NTE5fQ.Y0ZuUamGHBGi1jJDWKup5IAoJX6rDSLEFXB9xsvuKDcgCn-OK0-6fX9QQwjxzy9U",
	"phones": [
		{
			"id": 5,
			"number": "1234567",
			"citycode": "1",
			"countrycode": "57"
		},
		{
			"id": 6,
			"number": "8914522",
			"citycode": "1",
			"countrycode": "57"
		}
	],
	"active": true
}
```

Tambien podremos hacer modificacione con el metodo PUT. Solo enviamos el dato que se quiere cambiar y el metodo nos devolvera el detalle del usuario si la operacion fue exitosa:

```sh
❯ curl -X 'PUT' \
      'http://localhost:8080/api/v1/usuario/juana@alguien.com' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
      -H 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJndWlsbGVAYWxndWllbi5jb20iLCJpYXQiOjE3MDA4NDY1ODQsImV4cCI6MTcwMDg0ODAyNH0.gemr9dpKnI7jLpRkR-o1iB97bBb8qGolBlEOxlcoAZ9JEPpyf2I
lKmU7ZT_05y6U' \
  -d '{
  "name": "Juana Almagro"
  }' -vvv
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> PUT /api/v1/usuario/juana@alguien.com HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.74.0
> accept: */*
> Content-Type: application/json
> Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJndWlsbGVAYWxndWllbi5jb20iLCJpYXQiOjE3MDA4NDY1ODQsImV4cCI6MTcwMDg0ODAyNH0.gemr9dpKnI7jLpRkR-o1iB97bBb8qGolBlEOxlcoAZ9JEPpyf2IlKmU7ZT_05y6U
> Content-Length: 27
>
* upload completely sent off: 27 out of 27 bytes
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Disposition: inline;filename=f.txt
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: SAMEORIGIN
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Fri, 24 Nov 2023 17:28:46 GMT
<
* Connection #0 to host localhost left intact
{
	"id": "9e040879-f391-4542-967b-750141c84ca4",
	"name": "Juana Almagro",
	"email": "juana@alguien.com",
	"role": "USER",
	"created": "2023-11-24T13:57:59.277053",
	"modified": "2023-11-24T14:28:46.311963489",
	"last_login": "2023-11-24T13:57:59.277113",
	"token": "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJqdWFuYUBhbGd1aWVuLmNvbSIsImlhdCI6MTcwMDg0NTA3OSwiZXhwIjoxNzAwODQ2NTE5fQ.Y0ZuUamGHBGi1jJDWKup5IAoJX6rDSLEFXB9xsvuKDcgCn-OK0-6fX9QQwjxzy9U",
	"phones": [
		{
			"id": 5,
			"number": "1234567",
			"citycode": "1",
			"countrycode": "57"
		},
		{
			"id": 6,
			"number": "8914522",
			"citycode": "1",
			"countrycode": "57"
		}
	],
	"active": true
}
```
Finalmente la eliminacion del usuario la realizamos con el metodo DELETE:

```sh
❯ curl -X 'DELETE' \
      'http://localhost:8080/api/v1/usuario/juana@alguien.com' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
      -H 'Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJndWlsbGVAYWxndWllbi5jb20iLCJpYXQiOjE3MDA4NDY1ODQsImV4cCI6MTcwMDg0ODAyNH0.gemr9dpKnI7jLpRkR-o1iB97bBb8qGolBlEOxlcoAZ9JEPpyf2IlKmU7ZT_05y6U' \
   -vvv
*   Trying 127.0.0.1:8080...
* Connected to localhost (127.0.0.1) port 8080 (#0)
> DELETE /api/v1/usuario/juana@alguien.com HTTP/1.1
> Host: localhost:8080
> User-Agent: curl/7.74.0
> accept: */*
> Content-Type: application/json
> Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJndWlsbGVAYWxndWllbi5jb20iLCJpYXQiOjE3MDA4NDY1ODQsImV4cCI6MTcwMDg0ODAyNH0.gemr9dpKnI7jLpRkR-o1iB97bBb8qGolBlEOxlcoAZ9JEPpyf2IlKmU7ZT_05y6U
>
* Mark bundle as not supporting multiuse
< HTTP/1.1 200
< Content-Disposition: inline;filename=f.txt
< X-Content-Type-Options: nosniff
< X-XSS-Protection: 0
< Cache-Control: no-cache, no-store, max-age=0, must-revalidate
< Pragma: no-cache
< Expires: 0
< X-Frame-Options: SAMEORIGIN
< Content-Type: application/json
< Transfer-Encoding: chunked
< Date: Fri, 24 Nov 2023 17:33:45 GMT
<
* Connection #0 to host localhost left intact
{"mensaje":"Usuario Eliminado juana@alguien.com"}

```

Obteniendose la confirmacion correspondiente.


<br /><br /><br />

Las mismas pruebas pueden realizarse desde la UI de swagger que ofrece la misma aplicacion.
Primero es necesario loginearse, para obtener el token.
![SStoken]

 Ese token debemos agregarlo en la UI mediante el boton de Authorize y pegarlo en ese campo.
 ![SSAuthorize]

 A partir de ese momento ya podemos acceder a los endpoints protegidos
 ![SSAll]



## Diagrama Solución

La solucion se basa en Spring Boot con Spring Security que incorpora los filtros a todo request que reciba el Servlet. Mediante la configuracion de dichos filtros, crearemos un token segun la especificacion para convertirlo en JWT, JSON Web Token que es un string firmado por el servidor y que contiene informacion pertinente. Este token lo genera el server como respuesta a un pedido de autenticacion por parte del cliente, el que lo recibe y lo usara para cada requerimiento subdiguiente. Cuando el servidor recibe una nueva peticion, sera capaz de determinar si ese JWT fue efectivamente generado por el servidor, y validar su autenticidad y ademas datos adicionales que viajan dentro del token.

![diagrama]

Existe un controlador que es accesible siempre y permite entregar los tokens si se satisfacen los mecanismos de autenticacion. Luego los controladores protegidos "autorizaran" o no el acceso en funcion del token que provea el cliente.




<!-- CONTACT -->
## Contacto

Guillermo Jury - guille[at]cgj.com.ar

Project Link: [https://github.com/gjury/nisum-eval-gj](https://github.com/gjury/nisum-eval-gj)



[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://linkedin.com/in/guillermojury

[tests-screenshot]: doc/SSTests.png
[start-screenshot]: doc/SSStart.png
[ssh2-screenshot]: doc/SSH2.png
[sswagger-screenshot]: doc/SSSwagger.png


[SStoken]:doc/SSLoginSW.png
[SSAuthorize]:doc/SSpastetoken.png
[SSAll]:doc/SSSwaggersecured.png

[diagrama]: doc/Diagrama.png

[JAVA]: https://img.shields.io/badge/Java-17+-blue?style=for-the-badge&logo=coffeescript&logoColor=white
[MAVEN]: https://img.shields.io/badge/Maven-3.6-blue?style=for-the-badge&logo=apachemaven&logoColor=white
[SPRING]: https://img.shields.io/badge/Spring-3.1-blue?style=for-the-badge&logo=spring&logoColor=white
