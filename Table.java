import gmaths.*;

import java.nio.*;
import com.jogamp.common.nio.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.awt.*;
import com.jogamp.opengl.util.glsl.*;
import com.jogamp.opengl.util.texture.*;

public class Table{

  private Model tableTop, tableLeg, holder, sphere;
  private Camera camera;
  private Light light;
  private SGNode tableRoot;

  private Texture texture_table, texture_table_leg , texture_egg_spec, texture_egg_diff, texture_holder,texture_holder_spec; 

  private float xPosition = 0;
  private TransformNode tableMoveTranslate, eggFloatingTranslate, eggRotatingTranslate;
  private float rotateAllAngleStart = 25, rotateAllAngle = rotateAllAngleStart;

  public Table(GL3 gl, Camera c, Light l){
    loadTextures(gl);
    camera = c;
    light = l;
    tableTop = makeTableTop(gl);
    tableLeg = makeTableLeg(gl);
    holder = makeHolder(gl);
    sphere = makeSphere(gl);

    //Table parameters 
    float legHeight = 2f;
    float legWidth = 0.5f;
    float legDepth = 0.5f;
    float tableHeight = 0.5f;
    float tableWidth = 4.5f;
    float tableDepth = 4.5f;

    tableRoot = new NameNode("tableRoot");
    tableMoveTranslate = new TransformNode("table transform",Mat4Transform.translate(xPosition,0,0));
    
    TransformNode tableTranslate = new TransformNode("table transform",Mat4Transform.translate(0, legHeight,0));

    NameNode tabletop = new NameNode("tableTop");
      Mat4 m = Mat4Transform.scale(tableWidth, tableHeight, tableDepth);
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode tabletopTransform = new TransformNode("tableTop transform", m);
        ModelNode tabletopShape = new ModelNode("Cube(tableTop)", tableTop);

    NameNode backRightLeg = new NameNode("backrightleg");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate((tableWidth*0.5f)-(legWidth*0.5f),0,(tableDepth*-0.5f)+(legWidth*0.5f)));
      m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
      m = Mat4.multiply(m, Mat4Transform.scale(legWidth,legHeight,legDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode backRightLegTransform = new TransformNode("backrightleg transform", m);
        ModelNode backRightLegShape = new ModelNode("Cube(backrightLeg)", tableLeg);

    NameNode frontRightLeg = new NameNode("frontrightleg");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate((tableWidth*0.5f)-(legWidth*0.5f),0,(tableDepth*0.5f)-(legWidth*0.5f)));
      m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
      m = Mat4.multiply(m, Mat4Transform.scale(legWidth,legHeight,legDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode frontRightLegTransform = new TransformNode("frontrightleg transform", m);
        ModelNode frontRightLegShape = new ModelNode("Cube(frontrightleg)", tableLeg);

    NameNode backLeftLeg = new NameNode("backleftleg");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate((tableWidth*-0.5f)+(legWidth*0.5f),0,(tableDepth*-0.5f)+(legWidth*0.5f)));
      m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
      m = Mat4.multiply(m, Mat4Transform.scale(legWidth,legHeight,legDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode backLeftLegTransform = new TransformNode("backleftleg transform", m);
        ModelNode backLeftLegShape = new ModelNode("Cube(backleftLeg)", tableLeg);
  
    NameNode frontLeftLeg = new NameNode("frontleftleg");
      m = new Mat4(1);
      m = Mat4.multiply(m, Mat4Transform.translate((tableWidth*-0.5f)+(legWidth*0.5f),0,(tableDepth*0.5f)-(legWidth*0.5f)));
      m = Mat4.multiply(m, Mat4Transform.rotateAroundX(180));
      m = Mat4.multiply(m, Mat4Transform.scale(legWidth,legHeight,legDepth));
      m = Mat4.multiply(m, Mat4Transform.translate(0,0.5f,0));
      TransformNode frontLeftLegTransform = new TransformNode("frontleftleg transform", m);
        ModelNode frontLeftLegShape = new ModelNode("Cube(frontleftLeg)", tableLeg);

    NameNode tableHolder = new NameNode("tableholder");
      m = Mat4.multiply(m, Mat4Transform.translate(tableWidth -0.5f , -(tableHeight + tableHeight * 0.5f), tableDepth - 0.1f));
      m = Mat4.multiply(m, Mat4Transform.scale(tableWidth * 0.75f, tableHeight * 0.5f, tableDepth * 0.75f));
      TransformNode tableHolderTransform = new TransformNode("tableholder transform", m);
        ModelNode tableHolderShape = new ModelNode("Cube(tableholder)", holder);

    TransformNode moveEgg = new TransformNode("Move egg to tableholder", Mat4Transform.translate(0, tableHeight*2 + tableHeight * 0.5f, 0));

    eggFloatingTranslate = new TransformNode("translateY("+1.0f+")", Mat4Transform.translate(0, 1.0f, 0));
    eggRotatingTranslate = new TransformNode("rotateAroundY("+75+")", Mat4Transform.rotateAroundY(75));
    
    NameNode egg = new NameNode("egg");
      m = Mat4.multiply(m, Mat4Transform.scale(1f, 5f, 1f));
      m = Mat4.multiply(m, Mat4Transform.translate(0,-0.4f,0));
      TransformNode eggTransform = new TransformNode("egg transform", m);
        ModelNode eggShape = new ModelNode("Sphere(egg)", sphere);

    tableRoot.addChild(tableMoveTranslate);
    tableMoveTranslate.addChild(tableTranslate);
      tableTranslate.addChild(tabletop);
        tabletop.addChild(tabletopTransform);
          tabletopTransform.addChild(tabletopShape);
        tabletop.addChild(backLeftLeg);
          backLeftLeg.addChild(backLeftLegTransform);
          backLeftLegTransform.addChild(backLeftLegShape);
        tabletop.addChild(frontLeftLeg);
          frontLeftLeg.addChild(frontLeftLegTransform);
          frontLeftLegTransform.addChild(frontLeftLegShape);
        tabletop.addChild(backRightLeg);
          backRightLeg.addChild(backRightLegTransform);
          backRightLegTransform.addChild(backRightLegShape);
        tabletop.addChild(frontRightLeg);
          frontRightLeg.addChild(frontRightLegTransform);
          frontRightLegTransform.addChild(frontRightLegShape);
        tabletop.addChild(tableHolder);
          tableHolder.addChild(tableHolderTransform);
            tableHolderTransform.addChild(tableHolderShape);
          tableHolder.addChild(moveEgg);
            moveEgg.addChild(egg);
              egg.addChild(eggFloatingTranslate);
              eggFloatingTranslate.addChild(eggRotatingTranslate);
                eggRotatingTranslate.addChild(eggTransform);
                eggTransform.addChild(eggShape);

    tableRoot.update();
  }
  
  private Model makeTableTop(GL3 gl){
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.7f, 0.7f, 0.7f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    tableTop = new Model(gl, camera, light, shader, material, new Mat4(1), mesh, texture_table);
    return tableTop;
  }

  private Model makeHolder(GL3 gl){
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.5f, 0.5f, 0.5f), 32.0f);
    holder = new Model(gl, camera, light, shader, material, new Mat4(1), mesh, texture_holder);
    return holder;
  }

  private Model makeTableLeg(GL3 gl){
    Mesh mesh = new Mesh(gl, Cube.vertices.clone(), Cube.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.5f, 0.5f, 0.5f), 12.0f);
    tableLeg = new Model(gl, camera, light, shader, material, new Mat4(1), mesh, texture_table_leg);
    return tableLeg;
  }

  private Model makeSphere(GL3 gl){
    Mesh mesh = new Mesh(gl, Sphere.vertices.clone(), Sphere.indices.clone());
    Shader shader = new Shader(gl, "vs_tt_05.txt", "fs_tt_05.txt");
    Material material = new Material(new Vec3(1.0f, 1.0f, 1.0f), new Vec3(1.0f, 1.0f, 1.0f), new Vec3(0.5f, 0.5f, 0.5f), 22.0f);
    sphere = new Model(gl, camera, light, shader, material, new Mat4(1), mesh, texture_egg_diff, texture_egg_spec);
    return sphere;
  }

  private void loadTextures(GL3 gl) {
    texture_table = TextureLibrary.loadTexture(gl, "textures/marble.jpg");
    texture_holder = TextureLibrary.loadTexture(gl, "textures/dark_grey.jpg"); 
    texture_table_leg = TextureLibrary.loadTexture(gl, "textures/container2.jpg");
    texture_egg_diff = TextureLibrary.loadTexture(gl, "textures/ven0aaa2.jpg");
    texture_egg_spec = TextureLibrary.loadTexture(gl, "textures/ven0aaa2_specular.jpg");
  }

  private void updateEggPos() {
    double elapsedTime = getSeconds()-startTime;
    float floatingHeight = 0.5f * (float)Math.sin(elapsedTime);
    eggFloatingTranslate.setTransform(Mat4Transform.translate(0, floatingHeight, 0));
    
    rotateAllAngle = rotateAllAngleStart*(float)Math.sin(elapsedTime);
    eggRotatingTranslate.setTransform(Mat4Transform.rotateAroundY(rotateAllAngle));

    eggFloatingTranslate.update();
    eggRotatingTranslate.update();
  }
  
  /* Time */
  private double startTime;
  
  private double getSeconds() {
    return System.currentTimeMillis()/1000.0;
  }
  
  public void render(GL3 gl) {
    tableRoot.draw(gl);
    updateEggPos();
  }

  public void dispose(GL3 gl) {
    sphere.dispose(gl);
    tableLeg.dispose(gl);
  }
}
