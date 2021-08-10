#include <ESP8266WiFi.h>
#include <DHT.h>
#include <ArduinoJson.h>

#define DHTPIN D3
#define DHTTYPE DHT11
#define sensorPon A0

const char *ssid = "GauravWifi2g";
const char *password = "abcd1234";
const char *host = "plantmonitoringsystem.000webhostapp.com";
DHT dht(DHTPIN, DHTTYPE);

void setup()
{
    Serial.begin(115200);
    delay(100);
    dht.begin();
    Serial.println();
    Serial.println();
    Serial.print("Connecting to ");
    Serial.println(ssid);
    pinMode(D0, OUTPUT);
    digitalWrite(D0, 0);

    WiFi.begin(ssid, password);
    while (WiFi.status() != WL_CONNECTED)
    {
        delay(500);
        Serial.print(".");
    }

    Serial.println("");
    Serial.println("WiFi connected");
    Serial.println("IP address: ");
    Serial.println(WiFi.localIP());
    Serial.print("Netmask: ");
    Serial.println(WiFi.subnetMask());
    Serial.print("Gateway: ");
    Serial.println(WiFi.gatewayIP());
}
void loop()
{
    float h = dht.readHumidity();
    // Read temperature as Celsius (the default)
    float t = dht.readTemperature();
    float s = analogRead(sensorPon);
    if (isnan(h) || isnan(t))
    {
        Serial.println("Failed to read from DHT sensor!");
        return;
    }

    Serial.print("connecting to ");
    Serial.println(host);

    WiFiClient client;
    const int httpPort = 80;
    if (!client.connect(host, httpPort))
    {
        Serial.println("connection failed");
        return;
    }

    String url = "/api/insert.php?temp=" + String(t) + "&hum=" + String(h) + "&moist=" + String(s);
    Serial.print("Requesting URL: ");
    Serial.println(url);

    client.print(String("GET ") + url + " HTTP/1.1\r\n" +
                 "Host: " + host + "\r\n" +
                 "Connection: close\r\n\r\n");
    delay(500);

    while (client.available())
    {
        String line = client.readStringUntil('\r');
        Serial.print(line);
    }

    // To On D0
    for (int j = 0; j < 30; j++)
    {
        Serial.print("connecting to ");
        Serial.println(host);

        if (!client.connect(host, httpPort))
        {
            Serial.println("connection failed");
            return;
        }
        url = "/api/read_all.php?id=1";
        Serial.println(url);
        client.print(String("GET ") + url + " HTTP/1.1\r\n" +
                     "Host: " + host + "\r\n" +
                     "Connection: close\r\n\r\n");
        delay(500);
        String section = "header";
        while (client.available())
        {
            String line = client.readStringUntil('\r');
            if (section == "header")
            { // headers..

                if (line == "\n")
                { // skips the empty space at the beginning
                    section = "json";
                }
            }
            else if (section == "json")
            { // print the good stuff
                section = "ignore";
                String result = line.substring(1);

                int size = result.length() + 1;
                char json[size];
                result.toCharArray(json, size);
                StaticJsonBuffer<200> jsonBuffer;
                JsonObject &json_parsed = jsonBuffer.parseObject(json);
                if (!json_parsed.success())
                {
                    Serial.println("parseObject() failed");
                    return;
                }
                String led = json_parsed["led"][0]["status"];

                if (led == "on")
                {
                    digitalWrite(D0, 1);
                    delay(100);
                    Serial.println("D0 is On..!");
                }
                else if (led == "off")
                {
                    digitalWrite(D0, 0);
                    delay(100);
                    Serial.println("D0 is Off..!");
                }

                Serial.println();
                Serial.println("closing connection");
                delay(5000);
            }
        }
    }
}