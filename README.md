# ⏱️ TimeWise — Gestión de Tiempo Inteligente
 
**Organiza tu tiempo. Maximiza tu potencia.**
 
[![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=flat-square&logo=android&logoColor=white)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![API Level](https://img.shields.io/badge/Min_API-26%20(Android_8.0)-brightgreen?style=flat-square)](https://developer.android.com/about/versions/oreo)
[![License](https://img.shields.io/badge/License-Proprietary-red?style=flat-square)](#licencia)
[![Idiomas](https://img.shields.io/badge/Idiomas-ES_%7C_EN-blue?style=flat-square)](#internacionalización)
 
</div>
---
 
## 📖 Descripción
 
**TimeWise** es una aplicación Android de gestión de tiempo diseñada para hispanohablantes, con soporte completo en inglés. Combina agendas visuales, bloqueo de tiempo (time-blocking) y estadísticas de productividad en una sola herramienta, con un modelo freemium que permite acceso básico gratuito y funcionalidades avanzadas en la versión premium.
 
Desarrollada en **Kotlin** con arquitectura moderna (MVVM + Jetpack), TimeWise se distingue por estar **diseñada en español desde el primer día**, sin ser una traducción de una app anglosajona.
 
---
 
## ✨ Funcionalidades
 
### 🆓 Versión Gratuita (con anuncios)
| Función | Descripción |
|---|---|
| 📅 Agenda diaria y semanal | Vista completa de tus compromisos |
| ✅ Gestión de tareas básica | Crea, edita y completa tareas |
| ⏰ Recordatorios | Hasta 5 recordatorios activos simultáneos |
| 🌐 Multilenguaje | Español (por defecto) e inglés |
| 📱 Widget de pantalla de inicio | Acceso rápido a tus tareas del día |
| 📢 Anuncios AdMob | Banners e intersticiales (no invasivos) |
 
### 👑 Versión Premium
| Función | Descripción |
|---|---|
| 🔲 Time-blocking avanzado | Bloqueo visual de horas por actividad |
| 📊 Estadísticas de uso del tiempo | Gráficas semanales y mensuales |
| 🔔 Recordatorios ilimitados | Sin restricciones |
| 🎨 Temas personalizados | Paletas de color a elegir |
| ☁️ Sincronización en la nube | Backup y sincronización entre dispositivos |
| 🚫 Sin anuncios | Experiencia completamente limpia |
| 📤 Exportación de datos | PDF y CSV de tus estadísticas |
 
---
 
## 🌐 Internacionalización
 
La aplicación soporta dos idiomas. El **castellano es el idioma predeterminado**, pensado como idioma nativo de diseño (no como traducción).
 
```
app/src/main/res/
├── values/           → Español (por defecto)
│   └── strings.xml
└── values-en/        → English
    └── strings.xml
```
 
El usuario puede cambiar el idioma desde `Ajustes → Idioma de la aplicación`. La configuración persiste con `SharedPreferences` y no requiere reiniciar la app en Android 13+.
## 🛠️ Stack Tecnológico
 
| Categoría | Librería / Tecnología |
|---|---|
| **Lenguaje** | Kotlin 2.x |
| **UI** | Jetpack Compose + Material Design 3 |
| **Arquitectura** | MVVM + Clean Architecture |
| **Base de datos local** | Room (SQLite) |
| **Sincronización** | Firebase Firestore (premium) |
| **Inyección de dependencias** | Hilt (Dagger) |
| **Coroutines** | Kotlin Coroutines + Flow |
| **Navegación** | Navigation Component |
| **Anuncios** | Google Mobile Ads SDK (AdMob) |
| **Pagos** | Google Play Billing Library 6+ |
| **Gráficas** | MPAndroidChart / Vico |
| **Testing** | JUnit 5, Mockk, Espresso |
 
---
 
## 🚀 Instalación y Configuración
 
### Prerrequisitos
- Android Studio Hedgehog (2023.1.1) o superior
- JDK 17+
- Cuenta de Firebase (para features premium)
- Cuenta de AdMob (para anuncios)
### Pasos
 
```bash
# 1. Clona el repositorio
git clone https://github.com/tu-usuario/timewise-android.git
cd timewise-android
 
# 2. Añade el archivo de configuración de Firebase
# Descarga google-services.json desde Firebase Console
# y colócalo en app/
 
# 3. Configura las claves de AdMob en local.properties
echo "ADMOB_APP_ID=ca-app-pub-XXXXXXXXXX~XXXXXXXXXX" >> local.properties
echo "ADMOB_BANNER_ID=ca-app-pub-XXXXXXXXXX/XXXXXXXXXX" >> local.properties
 
# 4. Abre el proyecto en Android Studio y sincroniza Gradle
# 5. Ejecuta en emulador o dispositivo (API 26+)
```
 
### Variables de entorno necesarias
 
```properties
# local.properties (NO subir a git — está en .gitignore)
ADMOB_APP_ID=ca-app-pub-XXXXXXXXXXXXXXXX~XXXXXXXXXX
ADMOB_BANNER_ID=ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX
ADMOB_INTERSTITIAL_ID=ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX
ADMOB_REWARDED_ID=ca-app-pub-XXXXXXXXXXXXXXXX/XXXXXXXXXX
```
## 🤝 Contribuciones
 
Este es un proyecto privado en desarrollo activo. Actualmente no se aceptan contribuciones externas. Consulta [CONTRIBUTING.md](CONTRIBUTING.md) cuando se abra a colaboración pública.
 
---
 
## 📄 Licencia
 
Código propietario — todos los derechos reservados. No se permite la copia, distribución ni modificación sin autorización expresa del autor.
 
