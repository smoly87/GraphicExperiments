/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.scenes.objects;

import engine.mesh.Mesh;
import java.util.LinkedList;
import javax.media.opengl.GL4;
import math.linearAlgebra.Vector;
import math.linearAlgebra.Vector3;
import opengl.engine.SceneObject;
import utils.ArrayUtils;

/**
 *
 * @author Andrey
 */
public class Polygon2d extends SceneObject{
    protected LinkedList<Vector> vertexes;
    
    
    public Polygon2d(GL4 gl) {
        super(gl);
        this.drawMode = GL4.GL_LINE_LOOP;
        this.vertexCoordsNum = 3;
        this.optHasColors = false;
        this.optVertexNormals = false;
        this.optVeretexTextures = false;
        this.optLoadTexture = false;
        this.setShaderProgName("polygon_2d");
        
        vertexes = new LinkedList<>();
    }
    
    public void addVector(Vector3 point){
        vertexes.add(point);
    }
    
     protected void createMesh(){
         mesh = new Mesh();

         mesh.setVertexCoordsNum(3);
         mesh.setVertexesCoords(ArrayUtils.vectorListToArray(vertexes));
         mesh.setVertexesCount(vertexes.size() );
     }
}
