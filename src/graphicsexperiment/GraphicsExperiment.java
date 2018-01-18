/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package graphicsexperiment;
import com.jogamp.opengl.util.FPSAnimator;

import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import engine.exception.LoadResourseException;

import engine.meshLoader.MeshLoaderObjFormat;
import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.shape.Mesh;
import javax.media.opengl.GL3;
import javax.media.opengl.GL4;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import math.linearAlgebra.Matrix;
import math.linearAlgebra.Matrix44;
import math.linearAlgebra.Vector3;
import math.transformMatricies4.MatrixRotationY;
import math.transformMatricies4.MatrixUnit;
import opengl.scenes.TestScene;

import utils.IoReader;
/**
 *
 * @author Andrey
 */
public class GraphicsExperiment  implements GLEventListener {

    

    public static int imageWidth = 1024;
    public static int imageHeight = 768;
    private GLCanvas canvas;
       
    protected FPSAnimator animator;
    protected TestScene scene;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        GraphicsExperiment tut01 = new GraphicsExperiment();

        Frame frame = new Frame("Tutorial 01");

        frame.add(tut01.getCanvas());

        frame.setSize(tut01.getCanvas().getWidth(), tut01.getCanvas().getHeight());

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }

  
    
    public GraphicsExperiment() {
        initGL();
      
    }

    private void initGL() {
        GLProfile profile = GLProfile.getDefault();

        GLCapabilities capabilities = new GLCapabilities(profile);

        canvas = new GLCanvas(capabilities);

        canvas.setSize(imageWidth, imageHeight);
        canvas.addGLEventListener(this);
        
    }

    @Override
    public void init(GLAutoDrawable glad) {
        System.out.println("init");

        canvas.setAutoSwapBufferMode(false);

        GL4 gl = glad.getGL().getGL4();
        try {
            scene = new TestScene(gl);
            // scene.init();
        } catch (LoadResourseException ex) {
            Logger.getLogger(GraphicsExperiment.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        scene.setScreenWidth(imageWidth);
        scene.setScreenHeight(imageHeight);
        scene.setFieldOfView(30.0f);
        scene.init();
        
        canvas.addKeyListener(scene);
                
        animator = new FPSAnimator(canvas, 60);
        animator.start();
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        
    }

    @Override
    public void display(GLAutoDrawable glad) {
        scene.display(glad);
         //System.out.println("display");
    }

    @Override
    public void reshape(GLAutoDrawable glad, int x, int y, int w, int h) {
        //System.out.println("reshape() x: " + x + " y: " + y + " width: " + w + " height: " + h);

       // GL3 gl3 = glad.getGL().getGL3();

        //gl3.glViewport(x, y, w, h);
    }
    
    public GLCanvas getCanvas() {
        return canvas;
    }

    public void setCanvas(GLCanvas canvas) {
        this.canvas = canvas;
    }
}
