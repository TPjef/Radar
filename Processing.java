import processing.serial.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

Serial myPort;

String angle = "";
String distance = "";
String data = "";
String noObject;

float pixsDistance;
int iAngle, iDistance;
int index1 = 0;
int index2 = 0;

PFont orcFont;

ArrayList<PVector> objectPoints = new ArrayList<PVector>();
ArrayList<Integer> objectAges = new ArrayList<Integer>();

void setup() {
  size(1200, 700);
  smooth();
  myPort = new Serial(this, "COM4", 9600);
  myPort.bufferUntil('.');
}

void draw() {
  fill(98, 245, 31);
  noStroke();
  fill(0, 10);
  rect(0, 0, width, height - height * 0.065);
  fill(98, 245, 31);

  drawRadar(); 
  drawLine();
  updateObjects();
  drawObjects();
  drawText();
}

void serialEvent(Serial myPort) {
  data = myPort.readStringUntil('.');
  if (data == null) return;
  data = data.substring(0, data.length() - 1);
  index1 = data.indexOf(",");
  if (index1 < 0) return;

  angle = data.substring(0, index1);
  distance = data.substring(index1 + 1, data.length());
  iAngle = int(angle);
  iDistance = int(distance);

  if (iDistance <= 60) {
    float px = iDistance * ((height - height * 0.1666) * 0.0167) * cos(radians(iAngle));
    float py = -iDistance * ((height - height * 0.1666) * 0.0167) * sin(radians(iAngle));
    objectPoints.add(new PVector(px, py));
    objectAges.add(255);
  }
}

void drawRadar() {
  pushMatrix();
  translate(width/2, height - height*0.074);
  noFill();
  strokeWeight(2);
  stroke(98, 245, 31);

  arc(0,0,(width-width*0.0625),(width-width*0.0625),PI,TWO_PI);
  arc(0,0,(width-width*0.27),(width-width*0.27),PI,TWO_PI);
  arc(0,0,(width-width*0.479),(width-width*0.479),PI,TWO_PI);
  arc(0,0,(width-width*0.687),(width-width*0.687),PI,TWO_PI);
  arc(0,0,(width-width*0.895),(width-width*0.895),PI,TWO_PI); // 60cm

  line(-width/2,0,width/2,0);
  for (int a = 30; a <= 150; a += 30) {
    line(0,0,(-width/2)*cos(radians(a)),-(width/2)*sin(radians(a)));
  }
  popMatrix();
}

void drawLine() {
  pushMatrix();
  strokeWeight(2);
  stroke(30, 250, 60);
  translate(width/2, height - height*0.074);
  line(0, 0, (height - height*0.12) * cos(radians(iAngle)), -(height - height*0.12) * sin(radians(iAngle)));
  popMatrix();
}

void updateObjects() {
  for (int i = objectAges.size() - 1; i >= 0; i--) {
    int age = objectAges.get(i);
    age -= 15; 
    if (age <= 0) {
      objectAges.remove(i);
      objectPoints.remove(i);
    } else {
      objectAges.set(i, age);
    }
  }
}

void drawObjects() {
  pushMatrix();
  translate(width/2, height - height*0.074);
  noStroke();

  for (int i = 0; i < objectPoints.size(); i++) {
    PVector p = objectPoints.get(i);
    int age = objectAges.get(i);
    fill(255, 10, 10, age);
    ellipse(p.x, p.y, 10, 10);
  }
  popMatrix();
}

void drawText() {
  pushMatrix();
  if (iDistance > 60) {
    noObject = "Out of Range";
  } else {
    noObject = "In Range";
  }

  fill(0, 0, 0);
  noStroke();
  rect(0, height - height * 0.0648, width, height);
  fill(98,245,31);
  textSize(25);

  text("10cm", width - width*0.42, height - height*0.0833);
  text("20cm", width - width*0.33, height - height*0.0833);
  text("30cm", width - width*0.25, height - height*0.0833);
  text("40cm", width - width*0.17, height - height*0.0833);
  text("50cm", width - width*0.09, height - height*0.0833);
  text("60cm", width - width*0.03, height - height*0.0833);

  textSize(40);
  text("Simple Circuits", width - width*0.875, height - height*0.0277);
  text("Angle: " + iAngle + " °", width - width*0.48, height - height*0.0277);
  text("Dist.: ", width - width*0.26, height - height*0.0277);
  if (iDistance <= 60) {
    text("        " + iDistance + " cm", width - width*0.225, height - height*0.0277);
  }
  textSize(25);
  fill(98,245,60);

  translate((width-width*0.4994)+width/2*cos(radians(30)), (height-height*0.0907)-width/2*sin(radians(30)));
  rotate(-radians(-60));
  text("30°",0,0);
  resetMatrix();

  translate((width-width*0.503)+width/2*cos(radians(60)), (height-height*0.0888)-width/2*sin(radians(60)));
  rotate(-radians(-30));
  text("60°",0,0);
  resetMatrix();

  translate((width-width*0.507)+width/2*cos(radians(90)), (height-height*0.0833)-width/2*sin(radians(90)));
  text("90°",0,0);
  resetMatrix();

  translate(width-width*0.513+width/2*cos(radians(120)), (height-height*0.07129)-width/2*sin(radians(120)));
  rotate(radians(-30));
  text("120°",0,0);
  resetMatrix();

  translate((width-width*0.5104)+width/2*cos(radians(150)), (height-height*0.0574)-width/2*sin(radians(150)));
  rotate(radians(-60));
  text("150°",0,0);
  popMatrix(); 
}
