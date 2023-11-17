# MyContacts

---

MyContacts es una aplicación intuitiva y fácil de usar que ofrece una solución para almacenar y gestionar contactos. Está construida con el patrón de diseño MVVM (Model–View–ViewModel) para garantizar una arquitectura de código limpia y mantenible.

## Módulos

La aplicación consta de los siguientes módulos:

1. **Lista de Contactos**: Aquí se listan todos los contactos almacenados en la base de datos de Firebase Firestore. Puedes ver todos tus contactos en un solo lugar en un formato ordenado y atractivo.
2. **Guardar Contacto**: Este módulo permite a los usuarios agregar nuevos contactos al almacenamiento de Firestore.
3. **Capturar Imagen**: Este módulo ofrece la posibilidad de tomar una foto utilizando la cámara del dispositivo, permitiendo una experiencia de usuario personalizada.
4. **Acceso a la Galería**: Te permite seleccionar y utilizar cualquier imagen de la galería del dispositivo como foto de perfil del contacto.
5. **Acceso a la Cámara**: Este módulo te permite acceder directamente a la cámara del dispositivo para tomar una foto y usarla como foto de perfil del contacto.

## Tecnologías

Las tecnologías utilizadas en el desarrollo de esta aplicación incluyen:

* **Firebase Firestore**: Para el almacenamiento y recuperación en tiempo real de los datos de los contactos.
* **Storage Firebase**: Para el almacenamiento de las fotos de los contactos.
* **Glide**: Para la carga eficiente de imágenes y la administración de la caché.
* **Material You**: Para el estilo y el tema de la interfaz de usuario, siguiendo las mejores prácticas de diseño.
* **Firebase Authentication**: Para autenticar a los usuarios de la aplicación.

## Características

Una vez que un contacto está almacenado en Firebase Firestore, los detalles de ese contacto se pueden ver simplemente haciendo clic sobre él en la lista. Además, tienes la opción de modificar los detalles del contacto que estás visualizando.

Además, se ha implementado la persistencia de datos fuera de línea. Esto significa que aunque no tengas conexión a internet, seguirás siendo capaz de visualizar tus contactos. Esta característica le proporciona una experiencia de usuario continua y sin interrupciones.
