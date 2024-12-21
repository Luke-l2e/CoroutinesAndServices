# Coroutines and Services: Exercise and Assessment

In this exercise, you will learn the fundamental concepts of coroutines and services in Android
development. Using a simple weather app, you will explore how to perform asynchronous tasks and
efficiently fetch data without blocking the user interface. The goal is to gain practical experience
with coroutines and services while extending the app with these features.

## Steps

### Step 1: Clone & Configure the Project (0 Points)

1. Open Android Studio. To avoid potential issues, we recommend using **Android Studio Ladybug**.
    - Check your version by navigating to **Help > About**.

2. Click **File > New > Project from Version Control**.
    - Enter the following URL to clone the project:
      ```
      https://github.com/julianertle/CoroutinesAndServices
      ```
    - Click **Clone** and wait for the project to download.

3. Open the cloned project in Android Studio and run the app on an emulator or a real device.

4. Open the app menu, go to **Settings**, define a sample city, and save it.

5. Upon saving the city, you will see an error because the OpenWeather API token is not configured.

6. Configure the API token:
    - Register at [OpenWeather](https://home.openweathermap.org/api_keys) to get your personal API
      token.
    - Copy the token and paste it into the app settings to establish a connection to OpenWeather.

7. Test the app by using its various features. Verify that city information is displayed correctly
   and that the app functions properly after API token configuration.

---

### Step 2: Implement the `fetchForecast` Method (4 Points)

1. Open `WeatherApiService.kt`.
2. Implement the `fetchForecast` method to fetch forecast data and return it, similar to the
   `fetchWeather` method.
    - Use the `suspend` keyword to execute the method asynchronously.
    - Choose a dispatcher that is more suitable than `Dispatchers.Default`.

---

### Step 3: Implement the `fetchForecastData` Method (7 Points)

1. Open `WeatherViewModel.kt`.
2. Implement the `fetchForecastData` method with the following parameters:
    - `city`: Name of the city.
    - `apiKey`: API key for accessing OpenWeather.

3. Use a coroutine to perform the API call asynchronously, ensuring the UI is not blocked during the
   fetch.

4. On a successful data fetch:
    - Save the forecast data to the `_forecast` variable.
    - Set the error message to `null`.

5. On a failed fetch or exception:
    - Set an appropriate error message in `_errorMessage`.

Example:

```kotlin
// Save the received data:
_forecast.value = forecastResponse.list

// Handle exceptions:
_errorMessage.value = "An error occurred while fetching data."
```

---

### Step 4: Display the Weather Forecast (3 Points)

1. Open `ForecastWeatherView.kt`.
2. Add a list to display the fetched forecast data.
    - Use the `WeatherCard` component.
    - Note: Weather data is already passed as parameters to this class.

---

### Step 5: Implement a Foreground Service (4 Points)

1. Go to `MainActivity.kt` and activate the `PopupService` in the code.
2. Fully implement the `onCreate()` method in `PopupService.kt`.
    - The `PopupService` should start as a foreground service.
    - Use an additional method in the same class to achieve this.
3. In the app settings, select the **10s Timer** option. You should now receive a notification every
   10 seconds.

---

### Step 6: Answer the Questions (8 Points)

Write your answers to the following questions in the `Antworten.txt` file in your project:

1. What are the benefits of coroutines in Android development, and how do they make asynchronous
   tasks more efficient and user-friendly?
2. Explain why you chose a specific dispatcher in **Task 2A**.
3. List two practical use cases where services are useful. Briefly explain why.
4. What are the advantages of combining services and coroutines? Can you provide an example from the
   weather app where both are used together?

---

### General Criteria (4 Points)

1. **Code Quality**:
    - Ensure the code is clean, well-structured, and readable.
    - Use meaningful variable names, comments, and adhere to best practices.

2. **Documentation**:
    - Document the code, especially complex functions and logic.
    - Describe what functions do, their parameters, and their return values.

3. **Error Handling**:
    - Ensure errors and exceptions are handled correctly.
    - Use `try-catch` blocks, validate input values, and ensure the app doesn't crash unexpectedly.
      Provide helpful error messages or alternatives.

4. **Buildability**:
    - Ensure the app compiles without errors.
    - Verify that all dependencies are correctly configured and no missing or inconsistent code
      parts exist.

---

### Submission

Submit your work by **January 2, 2025, at 23:55**. Provide the link to your repository with the
commit hash in the designated submission box.

- Example commit URL:
  ```
  https://github.com/<owner>/<project>/commit/<hash>
  ```
- Example:
  ```
  https://github.com/julianertle/CoroutinesAndServices/commit/5e2ad317a9c3cae2bd2cf79bcc0530853a8a844b

# Credits

<a href="https://www.flaticon.com/free-icons/sun" title="sun icons">App logo created by iconixar -
Flaticon</a>