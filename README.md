
# Air Quality App

**Por**: Juan Guillermo Escobar Baez

*El objetivo principal de la aplicación Air Quality App es brindar al deportista la información tanto presente como futura de calidad del aire, por medio de IA y demás elementos.*

# Un poco de Air Quality App

Air Quality App una aplicación desarrollada por medio de Jetpack Compose (framework de desarrollo moderno), con la cual se puede consultar el índice de calidad del aire tanto actualmente como en el futuro; se realiza por medio de parámetros como la hora y el AQI PM10, que predicen EL AQI DE PM2.5, teniendo los valores deseados, para la toma de decisiones de calidad del aire. El proyecto fue realizado principalmente para deportistas, aunque cualquier persona que desee conocer la calidad aérea la puede consultar; cuenta con un mapa, una pantalla de predicción, y el AQI actual en la ciudad de Bucaramanga (por el momento, únicamente en esta).


# Funciones detalladas de la aplicación

1. **Inicio** : Es posible la visualización de la calidad aérea en el momento actual, en la ciudad de Bucaramanga, Colombia; esto se realizó por medio de una API que proporciona los datos de calidad del aire en tiempo real (es open-source).

2. **Mapa** : Es un mapa (implementado por medio de OpenStreetMap, open-source) que sombrea un círculo según el AQI (índice de calidad aérea) encima de la ciudad. Además, cuenta con una leyenda sobre qué significa el color sombreado (valor promedio, dado que es una versión simplificada de la aplicación).

3. **Predicción del AQI PM2.5** : Esta pantalla posee deslizadores que permiten al usuario elegir la hora,el AQI PM10 y el AQI PM2.5, para que la aplicación prediga el AQI PM2.5, futuro. Ademas, da consejos sobre lo que debe hacer si existe esa calidad aérea, a parte de su categoría.

**Observación**: El servidor se demora en responder, dado que es inactivo después de un tiempo.


# Indicaciones para utilizar la aplicación (usuario)

Para poder ejecutar la aplicación como usuario, es necesario:

1. Descargar la aplicación APK desde GitHub (recomendado: desde Android)
2. Instala la aplicación desde el gestor de archivos.
3. Abre la aplicación.


# (Para desarrolladores) Con lo que se construyó la aplicación

*La aplicación fue realizada con las siguientes herramientas*:

1. Jetpack Compose: Un framework moderno de desarrollo Android, que permite la creación de interfaces de usuario de alta calidad y eficiencia, sin necesidad de utilizar XML, por medio del lenguaje Kotlin.

2. OpenStreetMap: Un mapa de código abierto, que permite la visualización de mapas gratuitamente, con la conexión a internet.

3. Retrofit: Una biblioteca de código abierto que permite la creación de interfaces de API, para la comunicación con el servidor.

4. Render : Servidor donde se encuentra montado el código en Python

5. Uvicorn: Para inicializar el servidor.

6. FastAPI: Creación de la API, de manera sencilla, en el backend, con Python.
