/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.objects;

import engine.mesh.Mesh;
import java.util.LinkedList;
import javax.media.opengl.GL4;
import math.linearAlgebra.Vector3;
import math.linearAlgebra.Vector;
import opengl.engine.SceneObject;
import utils.ArrayUtils;

/**
 *
 * @author Andrey
 */
public class VectorField extends SceneObject{

    protected LinkedList<Vector> vertexes;
    protected float[] vertexesData;
    
    protected float[] tmpVertexes;
    protected float[] tmpVertexesData;
    protected boolean autoFillData = false;
    
    public VectorField(GL4 gl) {
        super(gl);
        this.drawMode = GL4.GL_LINES;
        this.optHasColors = false;
        this.optVeretexTextures = false;
        this.optVertexNormals = false;
        this.optLoadTexture = false;
        this.setShaderProgName("vector_field");
        this.vertexes = new LinkedList<>();
        
    }
    @Override
    protected void createMesh(){
        mesh = new Mesh();
        
        if(vertexes.size() > 0){
            vertexesData =  ArrayUtils.vectorListToArray(vertexes) ;
        }
        
        if(autoFillData){
            vertexesData = fillDataFromVertexes();
        }
        mesh.setVertexesCount(vertexesData.length / 3);
        mesh.setVertexesCoords(vertexesData);
    }
    
    protected float[] fillDataFromVertexes(){
        
        float[] res = new float[tmpVertexes.length * 2];
        int N = tmpVertexes.length / 3;
        
        for(int k = 0; k < N; k++){
            int s1 = k * 3;
            int s2 = k * 6;
            
            for(int i = 0; i < 3; i++){
                res[s2 + i] = tmpVertexes[s1 + i];
                res[s2 + 3 + i] =tmpVertexes[s1 + i] +  tmpVertexesData[s1 + i]; // Из точки откладываем значение, отсюда сумма
            }
        }
        
        return res;
    }
    
    public void addVector(Vector3 point, Vector3 value, Vector3 color){
        vertexes.add(point);
        vertexes.add(point.plus(value));
    }
    
    public void setVertexes(float[] vertexesCoords){
        this.tmpVertexes =  vertexesCoords;
        autoFillData = true;
    }
    
    public void setVertexData(float[] vertexesDataCoords){
        this.tmpVertexesData =  vertexesDataCoords;
    }
}
