#include <Servo.h>

const int ldrPin1 = A0; // LDR1 pin (Analog pin A0)
const int ldrPin2 = A1; // LDR2 pin (Analog pin A1)
const int servoPin = 9; // Servo motor pin (Digital pin 9)

Servo myServo;

void setup() {
  Serial.begin(9600);
  myServo.attach(servoPin);
}

void loop() {
  int ldrValue1 = analogRead(ldrPin1);
  int ldrValue2 = analogRead(ldrPin2);
  
  int difference = ldrValue1 - ldrValue2;
  
  int angle = myServo.read();

  // Adjust sensitivity as needed
  int sensitivity = 10;

  if (difference > sensitivity) {
    angle++;
  } else if (difference < -sensitivity) {
    angle--;
  }
  
  angle = constrain(angle, 0, 180);
  myServo.write(angle);

  Serial.print("LDR1: ");
  Serial.print(ldrValue1);
  Serial.print(" | LDR2: ");
  Serial.print(ldrValue2);
  Serial.print(" | Difference: ");
  Serial.print(difference);
  Serial.print(" | Servo Angle: ");
  Serial.println(angle);

  delay(100); // Adjust delay as needed
}
