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
    protected LinkedList<Vector> values;
    protected LinkedList<Vector> colors;
    
    protected float[] vertexesData;
    
    protected float[] tmpVertexes;
    protected float[] tmpVertexesData;
    protected boolean autoFillData = false;
    
    public VectorField(GL4 gl) {
        super(gl);
        this.drawMode = GL4.GL_POINTS;
        this.optHasColors = true;
        this.optVertexNormals = true;
        this.optVeretexTextures = false;
        this.optLoadTexture = false;
        this.setShaderProgName("vector_field");
        
        this.optLoadGeomShader = true;
        
        this.optUseTransformFeedback = false;
        this.setFeedbackVayrings(new String[]{"fragColorG"});
        
        this.vertexCoordsNum = 3;
        this.normalCoordsNum = 3;
        
        this.vertexes = new LinkedList<>();
        this.values = new LinkedList<>();
        this.colors = new LinkedList<>();
    }
    @Override
    protected void createMesh(){
        mesh = new Mesh();
        
        mesh.setVertexCoordsNum(3);
        mesh.setNormalCoordsNum(3);
        mesh.setColorCoordsNum(3);
        
        if(vertexes.size() > 0){
            vertexesData =  ArrayUtils.vectorListToArray(vertexes) ;
        }
        
        if(autoFillData){
            vertexesData = fillDataFromVertexes();
           
        }
        mesh.setVertexesCoords(vertexesData);
        mesh.setNormalsCoords(ArrayUtils.vectorListToArray(values));
        mesh.setVertexesColors(ArrayUtils.vectorListToArray(colors));
        mesh.setVertexesCount(vertexesData.length / vertexCoordsNum);//Check this
       
    }
    
    protected float[] fillDataFromVertexes(){
        
        float[] res = new float[tmpVertexes.length * 2];
        int N = tmpVertexes.length / vertexCoordsNum;
        
        for(int k = 0; k < N; k++){
            int s1 = k * vertexCoordsNum;
            int s2 = k * vertexCoordsNum ;
            
            for(int i = 0; i < vertexCoordsNum; i++){
                res[s2 + i] = tmpVertexes[s1 + i];
                res[s2 + 3 + i] =tmpVertexes[s1 + i] +  tmpVertexesData[s1 + i]; // Из точки откладываем значение, отсюда сумма
            }
        }
        
        return res;
    }
    
    public void addVector(Vector3 point, Vector3 value, Vector3 color){
        vertexes.add(point);
        values.add(value);
        colors.add(color);

    }
    /* public void addVector(Vector3 point, Vector3 value, Vector3 color){
     }*/
    public void setVertexes(float[] vertexesCoords){
        this.tmpVertexes =  vertexesCoords;
        autoFillData = true;
    }
    
    public void setVertexData(float[] vertexesDataCoords){
        this.tmpVertexesData =  vertexesDataCoords;
    }
}
