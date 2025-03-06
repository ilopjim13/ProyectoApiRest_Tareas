## Nombre del proyecto

- ApiRest_Tareas

## Descripción de los documentos

- Para este proyecto tendremos los siguientes documentos:
  - Usuarios: Que contendrá los datos de los usuarios como su username, password, roles y dirección.
  - Tareas: Con los datos de las tareas, nombre, descripción, fecha, estado.
  - Direccion: Con los datos de las direcciones de los usuarios.

## Endpoints a desarrollar

- Para el usuario vamos a utilizar el Endpoint de "/usuarios". Dentro de este endpoint vamos a generar otros diferentes endpoints que ejecutarán las funciones:
  - POST "/login" que realizará el logeo de un usuario, o dará fallo si se equivoca.
  - POST "/register" que permite registrar un usuario que no exista ya y que tenga sus campos correctamente.

 prueba
- Para las tareas utilizaremos el endpoint "/tareas". Y dentro tendremos estos endopoints:
  - GET "/mostrar/{username}" que muestra las tareas de un usuario.
  - GET "/mostrarTodas" que muestra todas las tareas pero debes ser admin para poder verlas.
  - POST "/agregarTarea/{username}" que agrega una tarea a un usuario.
  - POST "/actualizarEstado/{username}" que actualiza el estado de la tarea.
  - DELETE "/eliminarTarea{username}" que permite eliminar una tarea de un usuario.

## Lógica de negocio

- Para el documento usuario:
  - Para registrarse el usuario deberá tener todos sus campos correctamente y no tener campos únicos iguales a alguno en la base de datos.
  - Para que se inserte correctamente deberá tener en la dirección una dirección que exista.
  - Para logearse el usuario deberá existir en la base de datos y que los datos coincidan, tanto username como password.

- Para las tareas se diferenciará en dos tipos de usuarios según sus roles:
  - USER: Estos usuarios solo tendrán acceso a sus tareas y podrán borrar, actualizar o insertar tareas propias, no la de los demás.
  - ADMIN: Estos usuarios con poder administrativo podrán ver todas las tareas, eliminar cualquier tarea de cualquier usuario, dar de altar tareas a cualquier usuario y marcar cualquier tarea como realizada.

## Excepciones

- Tendremos varias excepciones en nuestro programa:
  - NotFoundException que dará un error 404 al no encontrar datos en la base de datos.
  - BadRequestException que dará un error 400 cuando algo haya salido como no se esperaba.
  - UnauthorizedException que dará un error 401 cuando no se pueda autorizar el usuario en el login.

## Restricciones 

- Las restricciones de nuestro proyecto se dividirán en públicos:
  - Los endpoints para login y register que podrá acceder cualquier persona.

- Y privados, de los cuales se divirán en dos a su vez:
  - Los privados que podrán entrar todos los usuarios en donde pueden entrar en los endpoints referentes a sus propias tareas.
  - Y los privados solo para los ADMIN: que solo pueden acceder los usuarios con rol ADMIN, como por ejemplo al endpoint de mostrarTodas.

## Videos de prueba

- Aqui tenemos un video de prueba del login: https://drive.google.com/file/d/1hrbgAJx3_XmbZ7LFET6CEthBx-Gcv7RB/view?usp=drive_link

- Aquí tenemos un vide de prueba del register: https://drive.google.com/file/d/1HpMDthfmTVLv4zpzO8Xvu_AHsMNeNxAH/view?usp=drive_link

- Elace del video final: https://drive.google.com/file/d/1658WAZvW71drLllGn_G511dh9hXWvzYk/view?usp=sharing
