/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.scenes.objects;

import engine.Element;
import engine.Vector;
import engine.mesh.Mesh;
import java.util.ArrayList;
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
public class GraphAnimated3d extends SceneObject{
    protected float[] X; 
    protected int N = 10;
    protected long ms;
    protected long T0;
    protected double[][]sol;
    protected double frameT = 0.1;
    protected int tn = 0;
    protected final int BUFFER_VERTEX_U_VALUE = 7; 
    protected engine.Mesh meshGrid;
    public GraphAnimated3d(GL4 gl, engine.Mesh meshGrid, double[][]sol) {
        super(gl);
       
        this.optHasColors = false;
        this.optVertexNormals = false;
        this.optVeretexTextures = false;
        this.optLoadTexture = false;
        this.setShaderProgName("graph_3d");
        this.optLoadGeomShader = false;
        optLoadModelFile = false;
        this.vertexCoordsNum = 2;
       
        this.meshGrid = meshGrid;
        T0 = System.nanoTime();
        this.sol = sol;
        this.buffersCount = 8;
    }
    
    protected double[] getPointCoords(int pointInd){
        ArrayList<Vector> points =   meshGrid.getPoints();
        return points.get(pointInd).getCoordinates();
    }
    
    @Override
    protected void createMesh(){
        //float[] Y = getValues(X, 0.0f);
        int N = meshGrid.getElements().size() * 3 * 2;
        float[]C = new float[N];
        
        int i = 0;
        for(Element elm: meshGrid.getElements()){
            for(Integer pointInd: elm.getNodesList()){
                double[] coords = getPointCoords(pointInd);
                int k = i*2;
                C[k] = (float)coords[0];
                C[k + 1] = (float)coords[1];
                
                i++;
            }
        }
       
        
        mesh = new Mesh();
        mesh.setVertexesCoords(C);
        mesh.setVertexCoordsNum(2);
        mesh.setVertexesCount(N/2);
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
    
    protected float[] getUValues(double[]Y){
         int N = meshGrid.getElements().size() * 3;
         float[] R = new float[N];
         int i = 0;
         for(Element elm: meshGrid.getElements()){
            for(Integer pointInd: elm.getNodesList()){
                R[i] = (float)Y[pointInd];
                i++;
            }
         }
         
         return R;
    }
    
    @Override
    protected void render(GL4 gl, GLSLProgramObject shaderProgram) {
        float t = (float)((System.nanoTime() - T0)/1000000000.0);
        double deltaT = (System.nanoTime() - ms)/1000000000.0;
        if(deltaT >= frameT){
            ms = System.nanoTime();
            tn++;
            if(tn > sol.length - 1) tn = 0;
            //float[] Y = convToFloat (sol[tn]);//getValues(X, t);
            initBuffer(getUValues(sol[tn]), BUFFER_VERTEX_U_VALUE);
            //setToBuffer(X, Y);
        }
       
        super.render(gl, shaderProgram);
    }
}
