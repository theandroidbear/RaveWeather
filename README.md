
# RaveWeather

This application is designed for weather data and forecasts. It has been developed using the Java programming language, along with XML for the views, and is built in Android Studio. The app retrieves its weather data and forecasts from the OpenWeatherMap API. Additionally, it utilizes the Forward and Reverse Geocoding API from rapidapi.com to obtain coordinates for any city worldwide.


## API Reference

#### OpenWeatherMap API url.

https://api.openweathermap.org/data/2.5

#### Get weather data

```http
  GET /weather
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `lat` | `string` | **Required**. Latitude.|
| `lon` | `string` | **Required**. Longitude.|
| `appid` | `string` | **Required**. Your apiKey. |

#### Get forecast data

```http
  GET /forecast
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `lat`      | `string` | **Required**. Latitude. |
| `lon`      | `string` | **Required**. Longitude. |
| `appid`      | `string` | **Required**. Your apiKey. |

#### Forward and Reverse Geocoding API url.

https://forward-reverse-geocoding.p.rapidapi.com/v1/forward

#### Forward Geocoding

```http
  GET /forward
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `city` | `string` | **Optional**. City name.|

| Header | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `x-rapidapi-key` | `string` | **Required**. Your apiKey.|
| `x-rapidapi-host` | `string` | **Required**. Api host.|


## Authors

- [@Moses583](https://www.github.com/Moses583)


## Screenshots

![Alt text](two.png?raw=true "Optional Title")


## Features

- Get weather data for current day and forecast data for the next five days in intervals of three hours for each day.
- Search any city to get coordinates and show weather for that city.
- Save data and display data of previous loaded city weather when the app is offline.
- Shows shimmer effect when loading data.
- Shows live animations when app is offline when installed for the first time.
- Carefully designed light theme and dark theme.
- Modern Material3 components that blend well with the app theme.


## Tech Stack

**User Interface:** Material3 components, Views and ViewGroups, XML layouts.

**Backend:** Java language, Retrofit, Android SQLite database


##  Installation and Deployment

To install the project make sure you have the following dependencies.

```bash
  implementation 'com.squareup.retrofit2:retrofit:{version}'
  implementation 'com.squareup.retrofit2:converter-gson:{version}'
  implementation 'com.squareup.okhttp3:okhttp:{version}'
  implementation 'com.squareup.okhttp3:logging-interceptor:{version}'
```
```bash
  implementation 'com.airbnb.android:lottie:{version}'
```
```bash
  implementation 'com.github.bumptech.glide:glide:{version}'
```
```bash
  implementation 'com.facebook.shimmer:shimmer:{version}'
```
```bash
  implementation 'androidx.swiperefreshlayout:swiperefreshlayout:{version}'
```
```bash
  implementation 'androidx.multidex:multidex:{version}'
```

Gradle version

```bash
  Android Gradle Plugin version: 8.7.3
  Gradle version: 8.9
```

To run the project.

```bash
  ./gradlew build
```
```bash
  ./gradlew installDebug
```

