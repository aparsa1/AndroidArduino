//Arduino PINS
int forward = 10; 	// Pin 12 - Forward
int reverse = 11;	// Pin 11 - Reverse
int left = 7;		// Pin 10 - Left
int right = 6;		// Pin 9  - Right
int turbo = 4;		// Pin 8  - Turbo
int short_lights = 12;	// Pin 7  - Short Lights
int long_lights = 13;	// Pin 6  - Long Lights
int back_lights = 8;	// Pin 5  - Back Lights
int reverse_lights = 9;	// Pin 4  - Reverse Lights
int temperatureSensor=A5;
int lightSensor=A4;
#define ir A1   //Infrared
unsigned long time;
char val;  // Variable to receive data from the serial port
unsigned int distance=100;//distance of object in front. 100 is just a default value

//Class required for Infrared sensor for object detection===============================================================================

#define model 1080//20150
class SharpIR
{
  public:
    SharpIR (int irPin, int avg, int tolerance, int sensorModel);
    int distance();  
  private:
  int cm();  
    int _irPin;
    int _model;
    int _avg;
    int _p;
    int _sum;
    int _previousDistance;
    int _tol;
    
};

SharpIR sharp(ir, 25, 93, model);


//Loop and setup==========================================================================================================================

void setup() {

  // initialize the digital pins as output and Input
  pinMode(forward, OUTPUT);
  pinMode(reverse, OUTPUT);
  pinMode(left, OUTPUT);
  pinMode(right, OUTPUT);
  pinMode(turbo, OUTPUT);
  pinMode(short_lights, OUTPUT);
  pinMode(long_lights, OUTPUT);
  pinMode(back_lights, OUTPUT);
  pinMode(reverse_lights, OUTPUT);
  pinMode(temperatureSensor,INPUT);
  pinMode(lightSensor,INPUT);
  pinMode(ir,INPUT);
  time=0;
  Serial.begin(9600); 	// Start serial communication at 9600bps
}
void loop() {
  performCommand();
  if(millis()-time >1000)//Read sensors once per second
  {
    time=millis();
   ReadSensors();
  } 
}  



//Reading the Sensors=============================================================================================================================================
  void ReadSensors()
  {
  int LightValue=analogRead(lightSensor);
  
    int tempValue=analogRead(temperatureSensor);
    int B=3975;                  //B value of the thermistor
    float resistance=(float)(1023-tempValue)*10000/tempValue; //get the resistance of the sensor;
    float temperatureC=1/(log(resistance/10000)/B+1/298.15)-273.15;//convert to temperature via datasheet ;
    // float temperatureC = TempValue * 0.48828125;
    int temperatureF = (temperatureC * 9.0 / 5.0) + 32.0;
  
    //int dis=sharp.distance();  // this returns the distance 

   Serial.print('t');
   Serial.println(temperatureF); 
   Serial.print('l');
   Serial.println( LightValue); 
   Serial.print('d');
   Serial.println(distance);  
}



//Implementation of methods required for moving the car============================================================================================================================================
// Fordward action
void go_forward() {
  digitalWrite(forward, HIGH);
  digitalWrite(turbo, LOW);
  digitalWrite(reverse, LOW);
}

// Stop Forward action
void stop_go_forward() {
  digitalWrite(forward, LOW);
}

// Reverse action
void go_reverse() {
  digitalWrite(reverse, HIGH);
  digitalWrite(forward, LOW);
  digitalWrite(turbo, LOW);
  digitalWrite(reverse_lights, HIGH);
}

// Stop Reverse action
void stop_go_reverse() {
  digitalWrite(reverse, LOW);
  digitalWrite(reverse_lights, LOW);
}

// Turbo action
void go_turbo() {
  digitalWrite(turbo, HIGH);
  digitalWrite(forward, LOW);
  digitalWrite(reverse, LOW);
}

// Stop Turbo action
void stop_go_turbo() {
  digitalWrite(turbo, LOW);
}

// Left action
void go_left() {
  digitalWrite(left, HIGH);
  digitalWrite(right, LOW);
}

// Right action
void go_right() {
  digitalWrite(right, HIGH);
  digitalWrite(left, LOW);
}

// Stop turn action
void stop_turn() {
  digitalWrite(right, LOW);
  digitalWrite(left, LOW);
}

// Stop car
void stop_car() {
  digitalWrite(forward, LOW);
  digitalWrite(reverse, LOW);
  digitalWrite(turbo, LOW);
  digitalWrite(right, LOW);
  digitalWrite(left, LOW);
  digitalWrite(reverse_lights, LOW);
  digitalWrite(short_lights,LOW);
}

// Short Lights ON
void lights_on() {
  digitalWrite(short_lights, HIGH);
  digitalWrite(back_lights, HIGH);
}

// Short Lights OFF
void lights_off() {
  digitalWrite(short_lights, LOW);
  digitalWrite(back_lights, LOW);
}

// Long Lights ON
void long_lights_on() {
  digitalWrite(long_lights, HIGH);
}

// Long Lights OFF
void long_lights_off() {
  digitalWrite(long_lights, LOW);
}

// Reverse Lights ON
void back_lights_on() {
  digitalWrite(reverse_lights, HIGH);
}

// Reverse Lights OFF
void back_lights_off() {
  digitalWrite(reverse_lights, LOW);
}
  
// Read serial port and perform command
void performCommand() {
     distance=sharp.distance();  // don't move if you are too close to an object infront of you
    if(distance<=20)         //stop forward moving if object infront of you is closer than 20 cm
       stop_go_forward();
       
  if (Serial.available()) {
    val = Serial.read();
  //  Serial.println(val);
  }
    if (val == 'f' && distance>20) { // Forward
      go_forward();
    } else if (val == 'z') { // Stop Forward
      stop_go_forward();
    } else if (val == 'b') { // Backward
      go_reverse();
    } else if (val == 'y') { // Stop Backward
      stop_go_reverse();
    } else if (val == 't') { // Turbo
      go_turbo();
    } else if (val == 'x') { // Stop Turbo
      stop_go_turbo();
    } else if (val == 'r') { // Right
      go_right();
    } else if (val == 'l') { // Left
      go_left();
    } else if (val == 'v') { // Stop Turn
      stop_turn();
    } else if (val == 's') { // Stop
      stop_car();
    } else if (val == 'a') { // Short Lights
      lights_on();
    } else if (val == 'c') { // Stop Short Lights
      lights_off();
    } else if (val == 'd') { // Long Lights
      long_lights_on();
    } else if (val == 'e') { // Stop Long Lights
      long_lights_off();
    }
  else if (val=='w')
  {
  delay(1000);
  }
}















//Implementation of class SharpIR for Infrared object detector=====================================================================================================================================


SharpIR::SharpIR(int irPin, int avg, int tolerance, int sensorModel) {
  
    _irPin=irPin;
    _avg=avg;
    _tol=tolerance/100;
    _model=sensorModel;
    
    analogReference(DEFAULT);
}

int SharpIR::cm() {
    
    int raw=analogRead(_irPin);
    float voltFromRaw=map(raw, 0, 1023, 0, 5000);
    
    int puntualDistance;
    
    if (_model==1080) {
        
        puntualDistance=27.728*pow(voltFromRaw/1000, -1.2045);
        
    }else if (_model==20150){
    
        puntualDistance=61.573*pow(voltFromRaw/1000, -1.1068);
        
    }
    return puntualDistance;
}



int SharpIR::distance() {
    _p=0;
    _sum=0;
    for (int i=0; i<_avg; i++){
        
        int foo=cm();
        
        if (foo>=(_tol*_previousDistance)){
        
            _previousDistance=foo;
            _sum=_sum+foo;
            _p++;
            
        }
        
        
    }

    
    int accurateDistance=_sum/_p;
    
    return accurateDistance;

}



