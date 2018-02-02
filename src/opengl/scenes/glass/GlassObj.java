/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.scenes.glass;

import javax.media.opengl.GL4;
import opengl.engine.CubeMapTexture;
import opengl.engine.GLSLProgramObject;
import opengl.engine.SceneObject;

/**
 *
 * @author Andrey
 */
public class GlassObj extends SceneObject{
    
    protected int textureId;
    public GlassObj(GL4 gl) {
        super(gl);
        this.optLoadTexture = false;
        this.setShaderProgName("glass");
    }
    
    
  
    
}
