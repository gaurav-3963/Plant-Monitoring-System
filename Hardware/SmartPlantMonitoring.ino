#include <ESP8266WiFi.h>
#include <BlynkSimpleEsp8266.h>
#include <SimpleTimer.h>
#include <DHT.h>

#define DHTPIN D3    
#define DHTTYPE DHT11 
#define sensorPon A0 
#define BLYNK_PRINT Serial   

char auth[] ="GCvPje291hynanihKGh293_zWDJJfn2T";               //Authentication code sent by Blynk
char ssid[] = "GauravWifi2g";                       //WiFi SSID
char pass[] = "abcd1234";                       //WiFi Password

DHT dht(DHTPIN, DHTTYPE);
SimpleTimer timer;

void sendSensor()
{
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  float sensorValue = analogRead(sensorPon);

  if (isnan(h) || isnan(t)) {
    Serial.println("Failed to read from DHT sensor!");
    return;
  }

  if (isnan(sensorValue)){
    Serial.println("Failed to read from Moisture Sensor");
    return;
  }
 
  Blynk.virtualWrite(V5, h);  
  Blynk.virtualWrite(V6, t);  
  Blynk.virtualWrite(V2,sensorValue);
}

void setup()
{
  Serial.begin(9600);
  Blynk.begin(auth, ssid, pass);
  pinMode(DHTPIN, INPUT);
  dht.begin();
  timer.setInterval(10, sendSensor);
}

void loop()
{
  Blynk.run(); 
  timer.run(); 
  delay(100);
}
