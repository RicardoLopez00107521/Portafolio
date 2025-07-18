#include <ESP32Servo.h>
#include <WiFi.h>
#include <WifiMulti.h>
#include <ESPmDNS.h>
#include <WebServer.h>
#include <ArduinoJson.h>  // Librería para manejo de JSON
#include "token.h"

Servo pluma;
Servo puerta;

int pinPluma = 2;
int pinPuerta = 4;

WiFiMulti wifiMulti;
WebServer server(puertoHTTP);

const uint32_t TiempoEsperaWifi = 5000;

// Variables para controlar el tiempo con millis()
unsigned long previousMillisPluma = 0;
unsigned long previousMillisPuerta = 0;
const long intervaloMovimiento = 15;  // Intervalo entre cada incremento o decremento de posición (en ms)
const long tiempoEsperaCerrar = 5000;  // Tiempo de espera antes de cerrar el servo (en ms)

int posPluma = 0;
int posPuerta = 0;
bool plumaAbriendo = false;
bool puertaAbriendo = false;
bool plumaCerrando = false;
bool puertaCerrando = false;

void moverPluma() {
    unsigned long currentMillis = millis();
    if (plumaAbriendo) {
        if (currentMillis - previousMillisPluma >= intervaloMovimiento) {
            previousMillisPluma = currentMillis;
            if (posPluma < 90) {
                posPluma++;
                pluma.write(posPluma);
            } else {
                plumaAbriendo = false;
            }
        }
    } else if (plumaCerrando) {
        if (currentMillis - previousMillisPluma >= intervaloMovimiento) {
            previousMillisPluma = currentMillis;
            if (posPluma > 0) {
                posPluma--;
                pluma.write(posPluma);
            } else {
                plumaCerrando = false;
            }
        }
    }
}

void moverPuerta() {
    unsigned long currentMillis = millis();
    if (puertaAbriendo) {
        if (currentMillis - previousMillisPuerta >= intervaloMovimiento) {
            previousMillisPuerta = currentMillis;
            if (posPuerta < 180) {
                posPuerta++;
                puerta.write(posPuerta);
            } else {
                puertaAbriendo = false;
            }
        }
    } else if (puertaCerrando) {
        if (currentMillis - previousMillisPuerta >= intervaloMovimiento) {
            previousMillisPuerta = currentMillis;
            if (posPuerta > 0) {
                posPuerta--;
                puerta.write(posPuerta);
            } else {
                puertaCerrando = false;
            }
        }
    }
}

bool plumaAbierta() {
    return (posPluma > 50);
}

bool puertaAbierta() {
    return (posPuerta > 50);
}

void setup() {
    Serial.begin(115200);

    pluma.attach(pinPluma, 500, 2400); // Attach del servo pluma al pin 2
    puerta.attach(pinPuerta, 500, 2400); // Attach del servo puerta al pin 4

    // Inicializamos la posición inicial de los servos (opcional)
    pluma.write(0);  // Pluma a posición inicial (cerrada)
    puerta.write(0);  // Puerta a posición inicial (cerrada)

    Serial.println("\nIniciando Server Web");

    wifiMulti.addAP(ssid, password);

    WiFi.mode(WIFI_STA);
    Serial.print("Conectando a Wifi ..");
    while (wifiMulti.run(TiempoEsperaWifi) != WL_CONNECTED) {
        Serial.print(".");
    }
    Serial.println(".. Conectado!");
    Serial.print("SSID: ");
    Serial.print(WiFi.SSID());
    Serial.print(" ID: ");
    Serial.println(WiFi.localIP());

    if(!MDNS.begin("elesp")) {
        Serial.println("Error configurando mDNS!");
        while(1) {
            delay(1000);
        }
    }

    MDNS.addService("http", "tcp", puertoHTTP);

    server.on("/accionArduino", HTTP_POST, [](){
        String cuerpoSolicitud = server.arg("plain");
        Serial.print("Cuerpo de la solicitud recibida: ");
        Serial.println(cuerpoSolicitud);

        // Procesar el cuerpo de la solicitud JSON
        StaticJsonDocument<200> doc;
        DeserializationError error = deserializeJson(doc, cuerpoSolicitud);
        if (error) {
            Serial.print("Error al deserializar JSON: ");
            Serial.println(error.c_str());
            server.send(400, "text/plain", "Error al deserializar JSON");
            return;
        }

        // Verificar el contenido del JSON
        const char* accion = doc["command"];
        const char* dispositivo = doc["deviceType"];
        if (strcmp(accion, "open") == 0) {
            if (strcmp(dispositivo, "pluma") == 0) {
                plumaAbriendo = true;
            } else if (strcmp(dispositivo, "puerta") == 0) {
                puertaAbriendo = true;
            } else {
                server.send(400, "text/plain", "Dispositivo no válido");
                return;
            }
            // Enviar respuesta al cliente HTTP (Spring Boot)
            server.send(200, "text/plain", "Acción ejecutada en Arduino");
        } else {
            server.send(400, "text/plain", "Acción no válida");
        }
    });

    server.begin();
    Serial.println("Servidor HTTP iniciado");
}

void loop() {
    server.handleClient();

    moverPluma();
    moverPuerta();

    // Verificar y cerrar si están abiertos después de tiempoEsperaCerrar
    unsigned long currentMillis = millis();

    if (plumaAbierta() && !plumaAbriendo) {
        if (currentMillis - previousMillisPluma >= tiempoEsperaCerrar) {
            plumaCerrando = true;
        }
    }

    if (puertaAbierta() && !puertaAbriendo) {
        if (currentMillis - previousMillisPuerta >= tiempoEsperaCerrar) {
            puertaCerrando = true;
        }
    }
}