/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.objects;

import math.linearAlgebra.Vector3;

/**
 *
 * @author Andrey
 */
public class VertexInfo {

    public Vector3 getVertexCoords() {
        return vertexCoords;
    }

    public void setVertexCoords(Vector3 vertexCoords) {
        this.vertexCoords = vertexCoords;
    }

    public Vector3 getTextureCoords() {
        return textureCoords;
    }

    public void setTextureCoords(Vector3 textureCoords) {
        this.textureCoords = textureCoords;
    }

    public Vector3 getNormalCoords() {
        return normalCoords;
    }

    public void setNormalCoords(Vector3 normalCoords) {
        this.normalCoords = normalCoords;
    }
    protected Vector3 vertexCoords;
    protected Vector3 textureCoords;
    protected Vector3 normalCoords;
}
