import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;

public class Hatch_GLEventListener implements GLEventListener {
  
  private static final boolean DISPLAY_SHADERS = false;
  private Camera camera;
  private float xPosition = 0;
  /* The constructor is not used to initialise anything */
  public Hatch_GLEventListener(Camera camera) {
    this.camera = camera;
    this.camera.setPosition(new Vec3(-0.1f,23.3f,25.7f));
  }
  
  // ***************************************************
  /*
   * METHODS DEFINED BY GLEventListener
   */

  /* Initialisation */
  public void init(GLAutoDrawable drawable) {   
    GL3 gl = drawable.getGL().getGL3();
    System.err.println("Chosen GLCapabilities: " + drawable.getChosenGLCapabilities());
    gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f); 
    gl.glClearDepth(1.0f);
    gl.glEnable(GL.GL_DEPTH_TEST);
    gl.glDepthFunc(GL.GL_LESS);
    gl.glFrontFace(GL.GL_CCW);    // default is 'CCW'
    gl.glEnable(GL.GL_CULL_FACE); // default is 'not enabled' so needs to be enabled
    gl.glCullFace(GL.GL_BACK);   // default is 'back', assuming CCW
    initialise(gl);
  }
  
  /* Called to indicate the drawing surface has been moved and/or resized  */
  public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
    GL3 gl = drawable.getGL().getGL3();
    gl.glViewport(x, y, width, height);
    float aspect = (float)width/(float)height;
    camera.setPerspectiveMatrix(Mat4Transform.perspective(45, aspect));
  }
  
  /* Draw */
  public void display(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    render(gl);
  }

  /* Clean up memory */
  public void dispose(GLAutoDrawable drawable) {
    GL3 gl = drawable.getGL().getGL3();
    room.dispose(gl);
    table.dispose(gl);
    light.dispose(gl);
    lamp1.dispose(gl);
    lamp2.dispose(gl);
  }
  // ***************************************************
  /* THE SCENE
   * Now define all the methods to handle the scene.
   * This will be added to in later examples.
   */
  private Room room;
  private Table table;
  private Light light, light2;
  private Lamp lamp1;
  private Lamp2 lamp2;

  private void initialise(GL3 gl) {
    createRandomNumbers();
    light = new Light(gl);
    light.setCamera(camera);
    light2 = new Light(gl);
    light2.setCamera(camera);
    room = new Room(gl, camera, light);
    table = new Table(gl, camera, light);
    lamp1 = new Lamp(gl, camera, light);
    lamp2 = new Lamp2(gl, camera, light);
  }

  private void render(GL3 gl) {
    gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
    light.setPosition(getLightPosition());
    light.render(gl);
    light2.setPosition(getLight2Position());
    light2.render(gl);
    room.render(gl);
    table.render(gl);
    lamp1.render(gl);
    lamp2.render(gl);
  }

  // ***************************************************
  /* An array of random numbers
   */ 
  private int NUM_RANDOMS = 1000;
  private float[] randoms;
  
  private void createRandomNumbers() {
    randoms = new float[NUM_RANDOMS];
    for (int i=0; i<NUM_RANDOMS; ++i) {
      randoms[i] = (float)Math.random();
    }
  }
  
  private Vec3 getLightPosition() {
    float x = -0.1f;
    float y = 24.0f;
    float z = 1.0f;
    return new Vec3(x,y,z);
  }

  private Vec3 getLight2Position() {
    float x = 0.5f;
    float y = 24.0f;
    float z = 1.0f;
    return new Vec3(x,y,z);
  }

  // ***************************************************
  /* INTERACTIONS
  */ 
  public void originalPose1(){
    lamp1.originalPose();
  }

  public void firstPose1() {
    lamp1.firstPose();
  }

  public void secondPose1(){
    lamp1.secondPose();
  }

  public void originalPose2(){
    lamp2.originalPose();
  }

  public void firstPose2() {
    lamp2.firstPose();
  }

  public void secondPose2(){
    lamp2.secondPose();
  }

  public void lightSwitch(){
    light.lightSwitch();
  }
}