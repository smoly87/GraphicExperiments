/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package engine.softRenderer;

import math.linearAlgebra.Vector;

/**
 *
 * @author Andrey
 */
public class Vertex {
    protected Vector coords;
    protected Vector screenCoords;
    protected Vector textureCoords;       

    /**
     * @return the coords
     */
    public Vector getCoords() {
        return coords;
    }

    /**
     * @param coords the coords to set
     */
    public void setCoords(Vector coords) {
        this.coords = coords;
    }

    /**
     * @return the textureCoords
     */
    public Vector getTextureCoords() {
        return textureCoords;
    }

    /**
     * @param textureCoords the textureCoords to set
     */
    public void setTextureCoords(Vector textureCoords) {
        this.textureCoords = textureCoords;
    }

    /**
     * @return the screenCoords
     */
    public Vector getScreenCoords() {
        return screenCoords;
    }

    /**
     * @param screenCoords the screenCoords to set
     */
    public void setScreenCoords(Vector screenCoords) {
        this.screenCoords = screenCoords;
    }
}
