
# Air Quality App

**Por**: Juan Guillermo Escobar Baez

*El objetivo principal de la aplicación Air Quality App es brindar al deportista la información tanto presente como futura de calidad del aire, por medio de IA y demás elementos (descritos posteriormente)*

# Un poco de Air Quality App

Air Quality App una aplicación desarrollada por medio de Jetpack Compose (framework de desarrollo moderno), con la cual se puede consultar el índice de calidad del aire tanto actualmente como en el futuro; se realiza por medio de parámetros como la hora y la concentración de PM10, que predicen EL AQI DE PM2.5, teniendo los valores deseados, para la toma de decisiones de calidad del aire. El proyecto fue realizado principalmente para deportistas, aunque cualquier persona que desee conocer la calidad aérea la puede consultar; cuenta con un mapa, una pantalla de predicción, y el AQI actual en la ciudad de Bucaramanga (por el momento, únicamente en esta); además, cuenta con un formulario para reconocer el tipo de persona que es.


# Funciones detalladas de la aplicación

1. **Inicio** : Es posible la visualización de la calidad aérea en el momento actual, en la ciudad de Bucaramanga, Colombia; esto se realizó por medio de una API que proporciona los datos de calidad del aire en tiempo real (es open-source).

![image](https://github.com/user-attachments/assets/03f0b46f-1989-4453-8eed-383e4db05eaf)


2. **Mapa** : Es un mapa (implementado por medio de OpenStreetMap, open-source) que sombrea un círculo según el AQI (índice de calidad aérea) encima de la ciudad de Bucaramanga. Además, cuenta con una leyenda sobre qué significa el color sombreado (valor promedio, dado que es una versión simplificada de la aplicación).

![image](https://github.com/user-attachments/assets/179a9ef4-a26a-4043-a246-63bf60865208)


3. **Predicción del AQI PM2.5** : Esta pantalla posee deslizadores que permiten al usuario elegir la hora,la concentración de PM10 y PM2.5, para que la aplicación prediga el AQI PM2.5, futuro. Ademas, da consejos sobre lo que debe hacer si existe esa calidad aérea, a parte de su categoría.

 ![image](https://github.com/user-attachments/assets/13edd5df-2fa3-4d3f-ab03-1c4dce3e2600)
  

4. **Detección de tipo de persona** : Esta pantalla permite a la persona, según algunas de sus características, detectar si es una persona muy sensible, medianamente sensible o muy poco sensible; esto, para que el resultado de las predicciones sea más entendible y se pueda interpretar mejor.

![image](https://github.com/user-attachments/assets/5fd4b195-5853-44f7-ba05-ab4f70dac336)


**Observación**: El servidor puede demorar en responder, dado que es inactivo después de un tiempo.


# Indicaciones para utilizar la aplicación (usuario)

Para poder ejecutar la aplicación como usuario, es necesario:

1. Descargar el archivo APK desde GitHub (recomendado: desde Android)
2. Instala la aplicación desde el gestor de archivos.
3. Abre la aplicación.


# (Para desarrolladores) Herramientas con las cuales se construyó la aplicación

*La aplicación fue realizada con las siguientes herramientas*:

1. Jetpack Compose: Un framework moderno de desarrollo Android, que permite la creación de interfaces de usuario de alta calidad y eficiencia, sin necesidad de utilizar XML, por medio del lenguaje Kotlin.

2. OpenStreetMap: Un mapa de código abierto, que permite la visualización de mapas gratuitamente, con la conexión a internet.

3. Retrofit: Una biblioteca de código abierto que permite la creación de interfaces de API, para la comunicación con el servidor.

4. Render : Servidor donde se encuentra montado el código en Python

5. Uvicorn: Para inicializar el servidor.

6. FastAPI: Creación de la API, de manera sencilla, en el backend, con Python.


# Algunas limitaciones de la aplicación

Por el momento, la aplicación tiene las siguientes limitaciones:

* Se encuentra limitada únicamente a la ciudad de Bucaramanga.
* Por el momento, la funcionalidad del mapa se encuentra únicamente en la ciudad de Bucaramanga, mostrando el AQI de una zona única (no en toda la ciudad).
* No cuenta con recomendaciones personalizadas para el usuario (no tiene inicio de sesión ni gestión de usuarios).

# Posibles mejores
Algunas mejoras a la aplicación podrían ser las siguientes, basadas en las funciones actuales:

1. Implementar más funcionalidades en el mapa (más zonas, más colores, etc.)
2. Hacer la aplicación de uso global, obteniendo la ubicación del usuario para obtener el AQI actual.
3. Incluir gráficos de tendencias de AQI (gráfico de barras, circular, etc).
4. Incluir recomendaciones personalizadas por medio de notificaciones.

*Nota: ¿Cómo funciona la predicción del AQI?*
La aplicación utiliza un modelo de inteligencia artificial (Random Forest) que, al recibir ciertos datos como entrada, predice el AQI (Índice de Calidad del Aire). En pocas palabras, se ingresan los parámetros y el modelo se encarga de calcular cómo está la calidad del aire.

**EL CÓDIGO DE ANDROID STUDIO SE ENCUENTRA EN LA RAMA MASTER**
