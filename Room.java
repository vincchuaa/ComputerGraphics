import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

public class Room {

  private Model[] wall;
  private Camera camera;
  private Light light;

  private Texture texture_floor,texture_window_frame,texture_wall,texture_window;
 
  private Vec3 wall_ambient = new Vec3(0.1f, 0.5f, 0.31f);
  private Vec3 wall_diffuse = new Vec3(0.1f, 0.5f, 0.31f);
  private Vec3 wall_specular = new Vec3(0.3f,0.3f,0.3f); 
  private Vec3 default_material = new Vec3(1.0f,1.0f,1.0f);
  
  private Vec3 frame_material = new Vec3(0.7f,0.7f,0.7f);

  private float size = 16f;
  private float frameWidth = size;
  private float frameHeight = 2f;
  private float frameDepth = 0.5f;

  public Room(GL3 gl, Camera c, Light l){
    loadTextures(gl);
    camera = c;
    light = l;
    wall = new Model[9];
    wall[0] = makeFloor(gl);
    wall[1] = makeLeftWall(gl);
    wall[2] = makeRightWall(gl);
    wall[3] = makeBackWall(gl);
    wall[4] = makeUpperWindowFrame(gl);
    wall[5] = makeLowerWindowFrame(gl);
    wall[6] = makeLeftWindowFrame(gl);
    wall[7] = makeRightWindowFrame(gl);
    wall[8] = makeWindow(gl);
  }

  private Model makeFloor(GL3 gl){
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(default_material, default_material, new Vec3(0.3f,0.3f,0.3f), 32.0f);
    Mat4 modelMatrix = Mat4Transform.scale(16,1f,16);
    Model floor = new Model(gl, camera, light, shader, material, modelMatrix, mesh, texture_floor);
    return floor;
  }

  private Model makeLeftWall(GL3 gl){
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(default_material, default_material, wall_specular, 12.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(-size*0.5f,size*0.5f,0), modelMatrix);
    Model wall_l = new Model(gl, camera, light, shader, material, modelMatrix, mesh, texture_wall);
    return wall_l;
  }

  private Model makeRightWall(GL3 gl){
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(default_material, default_material, wall_specular, 12.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundY(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundZ(-270), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(size*0.5f,size*0.5f,0), modelMatrix);
    Model wall_r = new Model(gl, camera, light, shader, material, modelMatrix, mesh, texture_wall);
    return wall_r;
  }

  private Model makeBackWall(GL3 gl){
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(default_material, default_material, wall_specular, 12.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size,1f,size), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(270), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,size*0.5f), modelMatrix);
    Model wall_b = new Model(gl, camera, light, shader, material, modelMatrix, mesh, texture_wall);
    return wall_b;
  }

  private Model makeWindow(GL3 gl){
    Mesh mesh = new Mesh(gl, TwoTriangles.vertices.clone(), TwoTriangles.indices.clone());
    Shader shader = new Shader(gl, "vs_window.txt", "fs_window.txt");
    Material material = new Material(default_material, default_material, new Vec3(0.3f,0.3f,0.3f), 32.0f);
    Mat4 modelMatrix = new Mat4(1);
    modelMatrix = Mat4.multiply(Mat4Transform.scale(size*3,0,size*3), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.rotateAroundX(90), modelMatrix);
    modelMatrix = Mat4.multiply(Mat4Transform.translate(0,size*0.5f,-size*1.5f), modelMatrix);
    Model window = new Model(gl, camera, light, shader, material, modelMatrix, mesh, texture_window);
    return window;
  }

  private Model makeLowerWindowFrame(GL3 gl){
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(frame_material, frame_material, new Vec3(0.3f,0.3f,0.3f), 32.0f);
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(frameWidth,frameHeight,frameDepth), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m);
    m = Mat4.multiply(Mat4Transform.translate(0,0.3f,-size*0.5f), m);
    Model frame_below = new Model(gl, camera, light, shader, material, m, mesh, texture_window_frame);
    return frame_below;
  }

  private Model makeUpperWindowFrame(GL3 gl){
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(frame_material, frame_material, new Vec3(0.3f,0.3f,0.3f), 32.0f);
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(frameWidth,frameHeight,frameDepth), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m);
    m = Mat4.multiply(Mat4Transform.translate(0,size-0.2f,-size*0.5f), m);
    Model frame_upper = new Model(gl, camera, light, shader, material, m, mesh, texture_window_frame);
    return frame_upper;
  }

  private Model makeLeftWindowFrame(GL3 gl){
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(frame_material, frame_material, new Vec3(0.3f,0.3f,0.3f), 32.0f);
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(frameWidth,frameHeight,frameDepth), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(90), m);
    m = Mat4.multiply(Mat4Transform.translate(-7.75f,size/2,-8f), m);
    Model frame_left = new Model(gl, camera, light, shader, material, m, mesh, texture_window_frame);
    return frame_left;
  }

  private Model makeRightWindowFrame(GL3 gl){
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(frame_material, frame_material, new Vec3(0.3f,0.3f,0.3f), 32.0f);
    Mat4 m = new Mat4(1);
    m = Mat4.multiply(Mat4Transform.scale(frameWidth,frameHeight,frameDepth), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundX(90), m);
    m = Mat4.multiply(Mat4Transform.rotateAroundZ(90), m);
    m = Mat4.multiply(Mat4Transform.translate(7.75f,size/2, -8f), m);
    Model frame_right = new Model(gl, camera, light, shader, material, m, mesh, texture_window_frame);
    return frame_right;
  }

  private void loadTextures(GL3 gl) {
    texture_window = TextureLibrary.loadTexture(gl, "textures/cloud_sky.jpg");
    texture_floor = TextureLibrary.loadTexture(gl, "textures/marble.jpg");
    texture_wall = TextureLibrary.loadTexture(gl, "textures/dark_grey.jpg");
    texture_window_frame = TextureLibrary.loadTexture(gl, "textures/metal.jpg");
  }

  public void render(GL3 gl) {
    for (int i=0; i<9; i++) {
      wall[i].render(gl);
    }
  }

  public void dispose(GL3 gl) {
    for (int i=0; i<9; i++) {
      wall[i].dispose(gl);
    }
  }

}
