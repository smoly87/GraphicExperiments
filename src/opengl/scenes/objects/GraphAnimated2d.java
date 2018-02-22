/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.scenes.objects;

import engine.mesh.Mesh;
import java.util.Arrays;
import javax.media.opengl.GL4;
import opengl.engine.GLSLProgramObject;
import opengl.engine.SceneObject;
import tasks.membrane.waveEquation2d;
import tasks.waveequation.waveEquation1d;

/**
 *
 * @author Andrey
 */
public class GraphAnimated2d extends SceneObject{
    protected float[] X; 
    protected int N = 10;
    protected long ms;
    protected long T0;
    protected double[][]sol;
    protected double frameT = 0.1;
    protected int tn = 0;
    protected final int BUFFER_VERTEX_U_VALUE = 7; 
    
    public GraphAnimated2d(GL4 gl, float[]X, double[][]sol) {
        super(gl);
        this.drawMode = GL4.GL_LINE_STRIP;
        this.optHasColors = false;
        this.optVertexNormals = false;
        this.optVeretexTextures = false;
        this.optLoadTexture = false;
        this.setShaderProgName("graph_2d");
        this.optLoadGeomShader = false;
        optLoadModelFile = false;
        this.vertexCoordsNum = 1;
        this.X = X;
        T0 = System.nanoTime();
        this.sol = sol;
        this.buffersCount = 8;
    }
    
    @Override
    protected void createMesh(){
        float[] Y = getValues(X, 0.0f);
        
        mesh = new Mesh();
        mesh.setVertexesCoords(X);
        mesh.setVertexCoordsNum(1);
        mesh.setVertexesCount(Y.length);
    }
    
    protected float[] getValues(float[]X, float t){
        int N = X.length;
        float[] Y =  new float[N];
        float PI = (float)Math.PI;
        for(int i =0; i < N; i++){
            Y[i] = (float)(Math.sin(X[i] * PI) * Math.cos(t * PI));
        }
        return Y;
    }
    
    protected float[] getX(int N){
        float[] X = new float[N];
        float h = 1/(float)(N - 1);
        for(int i = 0; i < N; i++){
            X[i] = h* i;
        }
        return X;
    }
    
    protected float[] convert(float[]X, float[] Y){
        int N = X.length;
        float[] coords = new float[N * 2];
        for(int i =0; i < N; i++){
            int k = 2 * i;
            coords[k] = X[i];
            coords[k + 1] = Y[i];
            //coords[k + 2] = 1.0f;
        }
        return coords;
    }
    
    protected void setToBuffer(float[]X, float[] Y){     
        initBuffer(convert(X, Y), BUFFER_VERTEX);
    }

    /*@Override
    protected void initVertexes() {
        
    }*/
    @Override
    protected void initCustomBuffers(){
        
    }
    
    @Override
    protected void setupVBaCustomBuffers(){
        bindBufferToAttr(1, BUFFER_VERTEX_U_VALUE, 1);
    }
    
    protected float[] convToFloat(double[]arr){
        int N = arr.length;
        float[] res = new float[N];
        for(int i = 0; i < N; i++){
            res[i] = (float)arr[i];
        }
        return res; 
    }
    @Override
    protected void render(GL4 gl, GLSLProgramObject shaderProgram) {
        float t = (float)((System.nanoTime() - T0)/1000000000.0);
        double deltaT = (System.nanoTime() - ms)/1000000000.0;
        if(deltaT >= frameT){
            ms = System.nanoTime();
            tn++;
            if(tn > sol.length - 1) tn = 0;
            float[] Y = convToFloat (sol[tn]);//getValues(X, t);
            initBuffer(Y, BUFFER_VERTEX_U_VALUE);
            //setToBuffer(X, Y);
        }
       
        super.render(gl, shaderProgram);
    }
}
