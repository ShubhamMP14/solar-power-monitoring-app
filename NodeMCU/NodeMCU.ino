#include <Arduino.h>
#if defined(ESP32)
  #include <WiFi.h>
#elif defined(ESP8266)
  #include <ESP8266WiFi.h>
#endif
#include <Firebase_ESP_Client.h>
#include <NTPClient.h>
#include <WiFiUdp.h>

// Provide the token generation process info.
#include "addons/TokenHelper.h"
// Provide the RTDB payload printing info and other helper functions.
#include "addons/RTDBHelper.h"

#include "DHT.h"

#define DHTPIN D5
#define DHTTYPE DHT11

#define VOLTAGE_SENSOR_PIN A0

#define WIFI_SSID "Shubham7"
#define WIFI_PASSWORD "Shubham@1411"

// Insert your network credentials
#define API_KEY "AIzaSyCF07cyI3MAk4rTEK_ItHVNLLbHXgWk3y0"

// Insert your Firebase project's Realtime Database URL
#define DATABASE_URL "solarapplication-2003-default-rtdb.firebaseio.com"

// Insert Authorized Email and Corresponding Password
#define USER_EMAIL "shubhamparshuram1411@gmail.com"
#define USER_PASSWORD "Shubham1234@"
DHT dht(DHTPIN, DHTTYPE);

FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

// Variable to save USER UID
String uid;

// Define NTP client
WiFiUDP ntpUDP;
NTPClient timeClient(ntpUDP, "pool.ntp.org");

// Function to get the number of days in a month
unsigned long daysInMonth(unsigned long month, unsigned long year) {
  switch (month) {
    case 2:
      return (year % 4 == 0) ? 29 : 28;
    case 4:
    case 6:
    case 9:
    case 11:
      return 30;
    default:
      return 31;
  }
}

// Initialize WiFi
void initWiFi() {
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to WiFi ..");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print('.');
    delay(1000);
  }
  Serial.println(WiFi.localIP());
  Serial.println();
}

void setup(){
  Serial.begin(115200);
  
  // Initialize WiFi
  initWiFi();

  // Assign the api key (required)
  config.api_key = API_KEY;

  // Assign the user sign in credentials
  auth.user.email = USER_EMAIL;
  auth.user.password = USER_PASSWORD;

  Firebase.reconnectWiFi(true);
  fbdo.setResponseSize(4096);

  // Assign the callback function for the long running token generation task
  config.token_status_callback = tokenStatusCallback; //see addons/TokenHelper.h

  // Assign the maximum retry of token generation
  config.max_token_generation_retry = 5;

  // Insert the Firebase project's Realtime Database URL
  config.database_url = DATABASE_URL;

  // Initialize the library with the Firebase authen and config
  Firebase.begin(&config, &auth);

  // Getting the user UID might take a few seconds
  Serial.println("Getting User UID");
  while ((auth.token.uid) == "") {
    Serial.print('.');
    delay(1000);
  }
  // Print user UID
  uid = auth.token.uid.c_str();
  Serial.print("User UID: ");
  Serial.print(uid);

  // Initialize DHT sensor
  dht.begin();

  // Initialize NTP client
  timeClient.begin();
  timeClient.setTimeOffset(19800); // Adjust time offset according to your timezone in seconds (19800 corresponds to UTC+5:30, Indian Standard Time)
}

void loop(){
  if (Firebase.isTokenExpired()){
    Firebase.refreshToken(&config);
    Serial.println("Refresh token");
  }

  delay(2000); // Send data every 2 seconds

  // Update NTP client
  timeClient.update();

  // Get current time as Unix timestamp
  unsigned long currentUnixTime = timeClient.getEpochTime();

  // Convert Unix timestamp to human-readable date
  unsigned long daysSince1970 = currentUnixTime / 86400; // 86400 seconds in a day
  unsigned long year = 1970;
  unsigned long month, day;
  unsigned long daysThisYear;

  while (1) {
    unsigned long days = (year % 4 == 0) ? 366 : 365;
    if (daysSince1970 < days)
      break;
    daysSince1970 -= days;
    year++;
  }

  daysThisYear = (year % 4 == 0) ? 366 : 365;
  for (month = 1; month <= 12; month++) {
    unsigned long days = daysInMonth(month, year);
    if (daysSince1970 < days)
      break;
    daysSince1970 -= days;
  }

  day = daysSince1970 + 1; // Days since start of month (1-indexed)

  // Create a string with the current date
  String currentDate = String(day) + "-" + String(month) + "-" + String(year);

  float h = dht.readHumidity();
  float t = dht.readTemperature();
  float voltage = analogRead(VOLTAGE_SENSOR_PIN) * (3.3 / 1023.0);
  // Store data in "Realtime" node under "User1"
  if (Firebase.ready()) {
    if (Firebase.RTDB.setFloat(&fbdo, "User1/Realtime/humidity", h) &&
        Firebase.RTDB.setFloat(&fbdo, "User1/Realtime/temperature", t) &&
        Firebase.RTDB.setFloat(&fbdo, "User1/Realtime/voltage", voltage)) {
      Serial.println("Realtime data stored successfully");
    } else {
      Serial.println("Failed to store realtime data");
      Serial.println("Reason: " + fbdo.errorReason());
    }

    // Store data with timestamp in a new node with current date as name under "User1"
if ((currentUnixTime % 300 == 0) && (year >= 1970)) { // Check every 5 minutes
  String nodeName = String(day) + "-" + String(month) + "-" + String(year) + "/" + timeClient.getFormattedTime(); // Combine date and time
  if (Firebase.RTDB.setFloat(&fbdo, "User1/Date/" + nodeName + "/humidity", h) &&
      Firebase.RTDB.setFloat(&fbdo, "User1/Date/" + nodeName + "/temperature", t) &&
      Firebase.RTDB.setFloat(&fbdo, "User1/Date/" + nodeName + "/voltage", voltage)) {
    Serial.println("Data with timestamp stored successfully");
  } else {
    Serial.println("Failed to store data with timestamp");
    Serial.println("Reason: " + fbdo.errorReason());
  }
}

  }

  Serial.println("______________________________");
}