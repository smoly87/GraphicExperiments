/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package engine.meshLoader;

import engine.exception.LoadResourseException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import math.linearAlgebra.Vector;
import utils.ArrayUtils;
import utils.IoReader;
import static utils.IoReader.readFileFromStream;
import engine.mesh.Mesh;
/**
 *
 * @author Andrey
 */
public abstract class BaseMeshLoader {
   
   
    
    protected int indexOffeset = 1;
    protected int vertexesCount = 0;
    protected int indexesCount = 0;
    protected boolean optLoadTextures;
    protected boolean optLoadNormals;

    protected int vertexCoordsNum = 4;
    protected int vertexPerFace = 3;
    
    protected int textureCoordsNum = 2;
    protected int normalCoordsNum = 3;
    
    protected boolean fileHasTextureCoords = true;
    protected boolean fileHasNormalsCoords = true;
    
    public BaseMeshLoader() {
         
    }
    
    public  float[] getVertexesCoords(){
        return new float[1];
    }
    
    public  int[] getVertexesIndexes(){
        return new int[1];
    }
    
    public  float[] getTextureCoords(){
        return new float[1];
    }  
    
    public  float[] getNormalsCoords(){
        return new float[1];
    }
    
    
        
    public abstract  Mesh load(String fileName) throws LoadResourseException;

    /**
     * @return the optLoadTextures
     */
    public boolean isOptLoadTextures() {
        return optLoadTextures;
    }

    /**
     * @param optLoadTextures the optLoadTextures to set
     */
    public void setOptLoadTextures(boolean optLoadTextures) {
        this.optLoadTextures = optLoadTextures;
    }

    /**
     * @return the optLoadNormals
     */
    public boolean isOptLoadNormals() {
        return optLoadNormals;
    }

    /**
     * @param optLoadNormals the optLoadNormals to set
     */
    public void setOptLoadNormals(boolean optLoadNormals) {
        this.optLoadNormals = optLoadNormals;
    }

    /**
     * @return the vertexesCount
     */
    public int getVertexesCount() {
        return vertexesCount;
    }

    /**
     * @return the indexesCount
     */
    public int getIndexesCount() {
        return indexesCount;
    }

    /**
     * @return the indexOffeset
     */
    public int getIndexOffeset() {
        return indexOffeset;
    }

    /**
     * @param indexOffeset the indexOffeset to set
     */
    public void setIndexOffeset(int indexOffeset) {
        this.indexOffeset = indexOffeset;
    }

   
    /**
     * @return the vertexCoordsNum
     */
    public int getVertexCoordsNum() {
        return vertexCoordsNum;
    }

    /**
     * @param vertexCoordsNum the vertexCoordsNum to set
     */
    public void setVertexCoordsNum(int vertexCoordsNum) {
        this.vertexCoordsNum = vertexCoordsNum;
    }

    /**
     * @return the vertexPerFace
     */
    public int getVertexPerFace() {
        return vertexPerFace;
    }

    /**
     * @param vertexPerFace the vertexPerFace to set
     */
    public void setVertexPerFace(int vertexPerFace) {
        this.vertexPerFace = vertexPerFace;
    }

    /**
     * @return the textureCoordsNum
     */
    public int getTextureCoordsNum() {
        return textureCoordsNum;
    }

    /**
     * @param textureCoordsNum the textureCoordsNum to set
     */
    public void setTextureCoordsNum(int textureCoordsNum) {
        this.textureCoordsNum = textureCoordsNum;
    }

    /**
     * @return the normalCoordsNum
     */
    public int getNormalCoordsNum() {
        return normalCoordsNum;
    }

    /**
     * @param normalCoordsNum the normalCoordsNum to set
     */
    public void setNormalCoordsNum(int normalCoordsNum) {
        this.normalCoordsNum = normalCoordsNum;
    }

    /**
     * @return the fileHasTextureCoords
     */
    public boolean isFileHasTextureCoords() {
        return fileHasTextureCoords;
    }

    /**
     * @return the fileHasNormalsCoords
     */
    public boolean isFileHasNormalsCoords() {
        return fileHasNormalsCoords;
    }
}
