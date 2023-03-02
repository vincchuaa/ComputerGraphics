import gmaths.*;

import java.nio.*;

import javax.naming.NameNotFoundException;

import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

public class Lamp {
  
  private Mat4 m;
  private Model limb, joint, cube;
  private Camera camera;
  private Light light;
  private Texture texture_other, texture_arm;

  private SGNode lampRoot;

  private float xPosition = 0.5f;
  private TransformNode lampMoveTranslate, rotateLowerZ, rotateLowerY, rotateUpper, rotateHead, rotateEars;
  private float rotateLowerAngleZ = -30, rotateLowerAngleY = 0 ,rotateUpperAngle = 60, rotateHeadAngle = -30, rotateEarAngle = 0;
  private float currentLowerAngleZ = rotateLowerAngleZ, currentLowerAngleY = rotateLowerAngleY, currentUpperAngle = rotateUpperAngle, currentHeadAngle = rotateHeadAngle;
  private float goalLowerAngleZ = currentLowerAngleZ, goalLowerAngleY = currentLowerAngleY, goalUpperAngle = currentUpperAngle, goalHeadAngle = currentHeadAngle;
  
  public Lamp(GL3 gl, Camera c, Light l){
    loadTextures(gl);
    camera = c;
    light = l;
    limb = makeArms(gl);
    joint = makeJoint(gl);
    cube = makeSquare(gl);

    //Scene Graph
    //Lamp parameters
    float baseWidth = 1.5f;
    float baseHeight = 0.5f;
    float baseDepth = 1.0f;
    float armScale = 0.3f;
    float armHeight = 2.72f;
    lampRoot = new NameNode("lamp1");
    lampMoveTranslate = new TransformNode("lamp1 transform",Mat4Transform.translate(xPosition + 4.5f,0,0));

    TransformNode lampTranslate = new TransformNode("lamp1 transform",Mat4Transform.translate(0, 0,0));

    //Lamp Base
    NameNode lampBase = new NameNode("lampbase");
    m = new Mat4(1);
    m = Mat4Transform.scale(baseWidth, baseHeight, baseDepth);
    m = Mat4.multiply(m, Mat4Transform.translate(0,baseHeight,0));
      TransformNode lampBaseTransform = new TransformNode("lampbase transform", m);
        ModelNode lampBaseShape = new ModelNode("lampbase", cube);
    
    //Lower Limb
    rotateLowerY = new TransformNode("rotateAroundY("+rotateLowerAngleY+")",Mat4Transform.rotateAroundY(rotateLowerAngleY));
    rotateLowerZ = new TransformNode("rotateAroundZ("+rotateLowerAngleZ+")",Mat4Transform.rotateAroundZ(rotateLowerAngleZ));
    NameNode lowerLimb = new NameNode("lamplowerarm");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(0,baseHeight,0));
    m = Mat4.multiply(m, Mat4Transform.scale(armScale, armHeight, armScale));
    m = Mat4.multiply(m, Mat4Transform.translate(0,baseHeight,0));
      TransformNode lowerLimbTransform = new TransformNode("lamplowerarm transform", m);
        ModelNode lowerLimbShape = new ModelNode("Sphere(lamplowerarm)", limb);

    //Joint
    float jointHeight = armHeight+baseHeight/2;
    NameNode limbJoint = new NameNode("lampjoint");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(0,jointHeight,0));
    m = Mat4.multiply(m, Mat4Transform.scale(baseHeight,baseHeight,baseHeight));
    m = Mat4.multiply(m, Mat4Transform.translate(0,baseHeight,0));
      TransformNode jointTransform = new TransformNode("lamplowerarm transform", m);
        ModelNode jointShape = new ModelNode("Sphere(lamplowerarm)", joint);

    //Tail
    float tailHeight = jointHeight+0.2f;
    NameNode tail = new NameNode("lamptail");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(0.2f,tailHeight,0));
    m = Mat4.multiply(m, Mat4Transform.rotateAroundZ(-60));
    m = Mat4.multiply(m, Mat4Transform.scale((float)(baseHeight*1.5), (float)(baseHeight*1.5), (float)(baseHeight*1.5)));
    m = Mat4.multiply(m, Mat4Transform.translate(0,baseHeight,0));
      TransformNode tailTransform = new TransformNode("lamp1tail transform", m);
        ModelNode tailShape = new ModelNode("Sphere(lamp1tail)", limb);

    //Upper Limb
    rotateUpper = new TransformNode("rotateAroundZ("+rotateUpperAngle+")", Mat4Transform.rotateAroundZ(rotateUpperAngle));
    NameNode upperLimb = new NameNode("lampupperarm");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(0,armHeight+0.5f,0));
      TransformNode upperLimbTranslate = new TransformNode("lampupperarm translate", m);
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.scale(armScale, armHeight, armScale));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
        TransformNode upperLimbTransform = new TransformNode("lampupperarm transform", m);
          ModelNode upperLimbShape = new ModelNode("Sphere(lampupperarm)", limb);

    //Head
    NameNode head = new NameNode("lamphead");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(0,armHeight,0));
    TransformNode headTranslate = new TransformNode("lamp1head translate", m);
    rotateHead = new TransformNode("rotateAroundZ("+rotateHeadAngle+")", Mat4Transform.rotateAroundZ(rotateHeadAngle));
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.scale(baseWidth, baseHeight, baseDepth));
    m = Mat4.multiply(m, Mat4Transform.translate(0,baseHeight,0));
    TransformNode headTransform = new TransformNode("lamphead transform", m);
    ModelNode headShape = new ModelNode("Cube(lamphead)", cube);

    //Eyes
    NameNode eye1 = new NameNode("lampeye1");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(-0.7f,0.3f,baseDepth/2));
    m = Mat4.multiply(m, Mat4Transform.scale(0.3f, 0.3f, 0.3f));
    m = Mat4.multiply(m, Mat4Transform.translate(0,baseHeight,0));
    TransformNode eye1Transform = new TransformNode("lampeye1 transform", m);
    ModelNode eye1Shape = new ModelNode("Cube(lamp1eye1)", limb);

    NameNode eye2 = new NameNode("lampeye2");
    m = new Mat4(1);
    m = Mat4.multiply(m, Mat4Transform.translate(-0.7f,0.3f,-baseDepth/2));
    m = Mat4.multiply(m, Mat4Transform.scale(0.3f, 0.3f, 0.3f));
    m = Mat4.multiply(m, Mat4Transform.translate(0,baseHeight,0));
    TransformNode eye2Transform = new TransformNode("lampeye2 transform", m);
    ModelNode eye2Shape = new ModelNode("Cube(lampeye2)", limb);

    //Horns
    rotateEars = new TransformNode("rotateAroundZ("+rotateEarAngle+")", Mat4Transform.rotateAroundZ(rotateEarAngle));
    NameNode horn = new NameNode("lamphorn1");
    m = new Mat4(1);
    m = Mat4Transform.scale(0.2f, armHeight/2, 0.4f);
    m = Mat4.multiply(m, Mat4Transform.translate(0.0f,0.1f,0.2f));
    m = Mat4.multiply(m, Mat4Transform.translate(0,baseHeight,0.3f));
    TransformNode hornTransform = new TransformNode("lamphorn1 transform", m);
    ModelNode hornShape = new ModelNode("Cube(lamphorn1)", cube);

    NameNode horn2 = new NameNode("lamphorn2");
    m = new Mat4(1);
    m = Mat4Transform.scale(0.2f, armHeight/2, -0.4f);
    m = Mat4.multiply(m, Mat4Transform.translate(0.0f,0.1f,0.2f));
    m = Mat4.multiply(m, Mat4Transform.translate(0,baseHeight,0.3f));
    TransformNode horn2Transform = new TransformNode("lamphorn2 transform", m);
    ModelNode horn2Shape = new ModelNode("Cube(lamphorn2)", cube);

    //Scene graph
    lampRoot.addChild(lampMoveTranslate);
    lampMoveTranslate.addChild(lampTranslate);
      lampTranslate.addChild(lampBase);
        lampBase.addChild(lampBaseTransform);
          lampBaseTransform.addChild(lampBaseShape);
        lampBase.addChild(rotateLowerY);
          rotateLowerY.addChild(rotateLowerZ);
            rotateLowerZ.addChild(lowerLimb);
              lowerLimb.addChild(lowerLimbTransform);
                lowerLimbTransform.addChild(lowerLimbShape);
              lowerLimb.addChild(limbJoint);
                limbJoint.addChild(jointTransform);
                  jointTransform.addChild(jointShape);
                limbJoint.addChild(tail);
                    tail.addChild(tailTransform);
                      tailTransform.addChild(tailShape);
                limbJoint.addChild(upperLimbTranslate);
                  upperLimbTranslate.addChild(rotateUpper);
                    rotateUpper.addChild(upperLimb);
                      upperLimb.addChild(upperLimbTransform);
                        upperLimbTransform.addChild(upperLimbShape);
                      upperLimb.addChild(headTranslate);
                        headTranslate.addChild(rotateHead);
                          rotateHead.addChild(head);
                            head.addChild(headTransform);
                              headTransform.addChild(headShape);
                            head.addChild(eye1);
                              eye1.addChild(eye1Transform);
                                eye1Transform.addChild(eye1Shape);
                            head.addChild(eye2);
                              eye2.addChild(eye2Transform);
                                eye2Transform.addChild(eye2Shape);
                            head.addChild(rotateEars);
                              rotateEars.addChild(horn);
                                horn.addChild(hornTransform);
                                  hornTransform.addChild(hornShape);
                            head.addChild(rotateEars);
                              rotateEars.addChild(horn2);
                                horn2.addChild(horn2Transform);
                                  horn2Transform.addChild(horn2Shape);
      lampRoot.update();
  }

  private Model makeArms(GL3 gl){
    Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.7f, 0.7f, 0.7f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    limb = new Model(gl, camera, light, shader, material, new Mat4(1), mesh, texture_arm);
    return limb;
  }

  private Model makeJoint(GL3 gl){
    Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.7f, 0.7f, 0.7f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    joint = new Model(gl, camera, light, shader, material, new Mat4(1), mesh, texture_other);
    return joint;
  }

  private Model makeSquare(GL3 gl){
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.7f, 0.7f, 0.7f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    cube = new Model(gl, camera, light, shader, material, new Mat4(1), mesh, texture_other);
    return cube;
  }

  private void loadTextures(GL3 gl) {
    texture_arm = TextureLibrary.loadTexture(gl, "textures/marble.jpg");
    texture_other = TextureLibrary.loadTexture(gl, "textures/dark_grey.jpg");
  }

  /* Time */
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
  
  public void render(GL3 gl) {
    updateEars();
    updateHead();
    updateLowerLimb();
    updateUpperLimb();
    lampRoot.draw(gl);
  }

  public void dispose(GL3 gl) {
    limb.dispose(gl);
    joint.dispose(gl);
    cube.dispose(gl);
  }

  //  ANIMATIONS
  public void updateHead(){
    if (currentHeadAngle > goalHeadAngle){
      currentHeadAngle -= 1;
      rotateHead.setTransform(Mat4Transform.rotateAroundZ(currentHeadAngle));
      rotateHead.update();
    }

    if (currentHeadAngle < goalHeadAngle){
      currentHeadAngle += 1;
      rotateHead.setTransform(Mat4Transform.rotateAroundZ(currentHeadAngle));
      rotateHead.update();
    }
  }

  public void updateUpperLimb(){
    if (currentUpperAngle > goalUpperAngle){
      currentUpperAngle -= 1;
      rotateUpper.setTransform(Mat4Transform.rotateAroundZ(currentUpperAngle));
      rotateUpper.update();
    }
    if (currentUpperAngle < goalUpperAngle){
      currentUpperAngle += 1;
      rotateUpper.setTransform(Mat4Transform.rotateAroundZ(currentUpperAngle));
      rotateUpper.update();
    }
  }

  public void updateLowerLimb(){
    if (currentLowerAngleZ > goalLowerAngleZ){
      currentLowerAngleZ -= 1;
      rotateLowerZ.setTransform(Mat4Transform.rotateAroundZ(currentLowerAngleZ));
      rotateLowerZ.update();
    }
    if (currentLowerAngleZ < goalLowerAngleZ){
      currentLowerAngleZ += 1;
      rotateLowerZ.setTransform(Mat4Transform.rotateAroundZ(currentLowerAngleZ));
      rotateLowerZ.update();
    }
    if (currentLowerAngleY > goalLowerAngleY){
      currentLowerAngleY -= 1;
      rotateLowerY.setTransform(Mat4Transform.rotateAroundY(currentLowerAngleY));
      rotateLowerY.update();
    }
    if (currentLowerAngleY < goalLowerAngleY){
      currentLowerAngleY += 1;
      rotateLowerY.setTransform(Mat4Transform.rotateAroundY(currentLowerAngleY));
      rotateLowerY.update();
    }
  }

  public void updateEars(){
    double elapsedTime = getSeconds()-startTime;
    float rotateAngle = (float)Math.sin(elapsedTime*15);
    rotateEars.setTransform(Mat4Transform.rotateAroundX(rotateAngle));
    rotateEars.update();
  }

  // INTERATIONS
  public void originalPose(){
    goalLowerAngleZ = -30f;
    goalLowerAngleY = 0f;
    goalUpperAngle = 60f;
    goalHeadAngle = -30f;
  }

  public void firstPose(){
    goalLowerAngleZ = 0f;
    goalLowerAngleY = -60f;
    goalUpperAngle = 0f;
    goalHeadAngle = 0f;
  }

  public void secondPose(){
    goalLowerAngleZ = -40f;
    goalLowerAngleY = 60f;
    goalUpperAngle = 90f;
    goalHeadAngle = 20f;
  }
}
