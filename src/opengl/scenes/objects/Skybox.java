/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.objects;

import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import engine.exception.LoadResourseException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_CUBE_MAP;
import static javax.media.opengl.GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_X;
import static javax.media.opengl.GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y;
import static javax.media.opengl.GL.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z;
import static javax.media.opengl.GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X;
import static javax.media.opengl.GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Y;
import static javax.media.opengl.GL.GL_TEXTURE_CUBE_MAP_POSITIVE_Z;
import javax.media.opengl.GL3;
import javax.media.opengl.GL4;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLException;
import opengl.engine.GLSLProgramObject;
import opengl.engine.SceneObject;
import opengl.scenes.utils.CubeMapTexture;

/**
 *
 * @author Andrey
 */
public class Skybox extends Cube{
    protected int textureId ;
private static final String[] suffixes = { "px", "nx", "py", "ny", "pz", "nz" };
  private static final int[] targets = { GL_TEXTURE_CUBE_MAP_POSITIVE_X,
                                         GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
                                         GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
                                         GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
                                         GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
                                         GL_TEXTURE_CUBE_MAP_NEGATIVE_Z };
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
          texture =  loadFromStreamsGl(gl, Skybox.class.getClassLoader() , modelsFilePath + "cubemap/Bridge2/", "jpg", true);
       }
       catch(IOException exception){
           System.out.println("Error while loading cubemap!");
       }
       
    }
    
      protected void setProps(GL4 gl){
        gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
        gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR );
        gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_WRAP_S, GL4.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_WRAP_T, GL4.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_WRAP_R, GL4.GL_CLAMP_TO_EDGE);
    }
    
    protected void setFaceProp(int faceId, Buffer data){
         gl.glTexImage2D(faceId, 0, GL.GL_RGB, 1024, 1024, 0, GL.GL_RGB, GL.GL_UNSIGNED_BYTE, data);
    }
    
    protected int createTexture(){
        IntBuffer textBuff = IntBuffer.allocate(1);
        gl.glGenTextures(1, textBuff);
        return textBuff.get(0);
    }
    
    public  Texture loadFromStreamsGl(GL4 gl,
                                        ClassLoader scope,
                                        String basename,
                                        String suffix, boolean mipmapped) throws IOException, GLException {
    Texture cubemap = TextureIO.newTexture(GL_TEXTURE_CUBE_MAP);
 
    textureId = createTexture();
  gl.glBindTexture( GL.GL_TEXTURE_CUBE_MAP, textureId);
  setProps(gl);
    for (int i = 0; i < suffixes.length; i++) {
      int target = targets[i];  
      String resourceName = basename + suffixes[i] + "." + suffix;
      String fileSuffix = IOUtil.getFileSuffix(resourceName);
      InputStream stream = scope.getResourceAsStream(resourceName.substring(1));
      TextureData data = TextureIO.newTextureData(GLContext.getCurrentGL().getGLProfile(), stream,
                                                  mipmapped,
                                                  null);
      
      setFaceProp(target, data.getBuffer());
      if (data == null) {
        throw new IOException("Unable to load texture " + resourceName);
      }
     // cubemap.updateImage(gl, data, targets[i]);
    
     
     
    }

    return cubemap;
  }

    @Override
    protected void setTextureToShader(GLSLProgramObject shaderProgram) {
         int texLoc = gl.glGetUniformLocation(shaderProgram.getProgramId(), "myTexture");

        gl.glUniform1i(texLoc, GL4.GL_TEXTURE0);
        gl.glEnable(GL_TEXTURE_CUBE_MAP);
        gl.glActiveTexture(GL.GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_CUBE_MAP, textureId);
        gl.glActiveTexture(0);
    }

}
