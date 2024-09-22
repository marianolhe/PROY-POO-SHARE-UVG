Proceso de Creación de base de datos: Marian Olivares

1) intenté realizando la creación de una base con la versión 9.00 "Innovative", pero luego la contraseña no funcionaba para ingresar a las configuraciones.
2) desintalé la versión 9.00 e instalé la versión 8.4 LTC, pude ingresar con la contraseña y comencé a investigar que necesitaba para conectar usuarios de manera remota.
3) Vi que necesitaba usar un servicio de SSH entonces lo instalé en mi computadora y configuré algunos de los puertos para permitir las conexiones
4) Instalé putty para probar unas de las conexiones, configuré los puertos 3306 para el servidor y 22 para putty

5) No funcionaba la conexión en la red de la universidad, por lo que probamos al llegar a nuestras casas pero no pudimos hacer que funcionara la conexión de nuevo pero no tuvimos éxito.
6) Buscamos más información en diferentes tutoriales y vimos que en muchos proyectos como el que queremos comenzar utilizaban Xamp, por lo que comencé realizando la instalación y editando la configuración
   de la base que nos da acceso Xamp porque el puerto predeterminado estaba ocupado (3306), lo cambié a 3308 y cambié el documento my.ini para permitir conexiones desde cualquier IP, de nuevo realizamos pruebas pero
   como nos encontrabamos en la red de la universidad no pudimos comprobar que funcionara correctamente.

7) Decidimos volver a comenzar todo el proceso para utilizar copias locales.
