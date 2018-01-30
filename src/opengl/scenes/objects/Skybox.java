/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.objects;

import engine.exception.LoadResourseException;
import java.io.IOException;
import javax.media.opengl.GL3;
import javax.media.opengl.GL4;
import javax.media.opengl.GLException;
import opengl.engine.GLSLProgramObject;
import opengl.engine.SceneObject;
import opengl.scenes.utils.CubeMapTexture;

/**
 *
 * @author Andrey
 */
public class Skybox extends Cube{

    public Skybox(GL4 gl) {
        super(gl);
        this.shaderProgName = "cubemap";
        // this.optVeretexTextures = false;
    }
    
    /*protected void setTextureToShader( GLSLProgramObject shaderProgram){
        textureId = gl.glGetUniformLocation(shaderProgram.getProgramId(), "myTexture");
        gl.glActiveTexture(GL4.GL_TEXTURE0);
        texture.enable(gl);
        texture.bind(gl);
        gl.glUniform1i(textureId, 0);
    }*/
    protected void render(GL4 gl, GLSLProgramObject prog){
       int[] depthFunc = new int[]{0};
       gl.glGetIntegerv(GL3.GL_DEPTH_FUNC, depthFunc, 0);
       
       gl.glCullFace(GL4.GL_FRONT);
       gl.glDepthFunc(GL4.GL_LEQUAL);
       super.render(gl, prog);
       
       gl.glCullFace(GL4.GL_BACK);
       gl.glDepthFunc(depthFunc[0]);
    }
    
    @Override
    protected void loadTextures() throws LoadResourseException{
        
       try {
          texture =  CubeMapTexture.loadFromStreams(gl, Skybox.class.getClassLoader() , modelsFilePath + "cubemap/Bridge2/", "jpg", true);
       }
       catch(IOException exception){
           System.out.println("Error while loading cubemap!");
       }
       
    }
}
