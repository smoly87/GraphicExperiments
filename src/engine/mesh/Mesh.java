/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package engine.mesh;

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

/**
 *
 * @author Andrey
 */
public class Mesh {
     
    
    protected int vertexesCount = 0;
    protected int indexesCount = 0;
    protected boolean optLoadTextures;
    protected boolean optLoadNormals;
    
    protected int indexOffeset = 1;
    protected int vertexCoordsNum = 4;
    protected int vertexPerFace = 3;
    protected int textureCoordsNum = 2;
    protected int normalCoordsNum = 3;
    
    protected float[] vertexesCoords;
    protected int[] vertexesIndexes;
    protected float[] textureCoords;
    protected float[] normalsCoords;
     
    public Mesh() {
         
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
     * @param vertexesCount the vertexesCount to set
     */
    public void setVertexesCount(int vertexesCount) {
        this.vertexesCount = vertexesCount;
    }

    /**
     * @param indexesCount the indexesCount to set
     */
    public void setIndexesCount(int indexesCount) {
        this.indexesCount = indexesCount;
    }

    /**
     * @return the vertexesCoords
     */
    public float[] getVertexesCoords() {
        return vertexesCoords;
    }

    /**
     * @param vertexesCoords the vertexesCoords to set
     */
    public void setVertexesCoords(float[] vertexesCoords) {
        this.vertexesCoords = vertexesCoords;
    }

    /**
     * @return the vertexesIndexes
     */
    public int[] getVertexesIndexes() {
        return vertexesIndexes;
    }

    /**
     * @param vertexesIndexes the vertexesIndexes to set
     */
    public void setVertexesIndexes(int[] vertexesIndexes) {
        this.vertexesIndexes = vertexesIndexes;
    }

    /**
     * @return the textureCoords
     */
    public float[] getTextureCoords() {
        return textureCoords;
    }

    /**
     * @param textureCoords the textureCoords to set
     */
    public void setTextureCoords(float[] textureCoords) {
        this.textureCoords = textureCoords;
    }

    /**
     * @return the normalsCoords
     */
    public float[] getNormalsCoords() {
        return normalsCoords;
    }

    /**
     * @param normalsCoords the normalsCoords to set
     */
    public void setNormalsCoords(float[] normalsCoords) {
        this.normalsCoords = normalsCoords;
    }
}

