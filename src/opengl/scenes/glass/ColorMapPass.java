/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.scenes.glass;

import opengl.scenes.testscene.*;
import engine.exception.LoadResourseException;
import java.util.HashMap;
import javax.media.opengl.GL;
import javax.media.opengl.GL4;
import opengl.engine.CubeMapTexture;
import opengl.engine.FrameBuffer;
import opengl.engine.RenderPass;
import opengl.engine.Scene;
import opengl.engine.SceneObject;

/**
 *
 * @author Andrey
 */
public class ColorMapPass extends RenderPass{
    protected FrameBuffer colorBuf;
    protected int faceId;
    protected boolean useGeomShader;

    
    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }
    @Override
    public void render() {
       // colorBuf.bindFBO();
        colorBuf.bindCubeTexture(colorBuf.getTextureId(), this.getFaceId());
         gl.glClear(GL.GL_DEPTH_BUFFER_BIT|gl.GL_COLOR_BUFFER_BIT);
        super.render(); 
        //colorBuf.unbind();
    }
   
    public void bindFbo(){
        colorBuf.bindFBO();
    }
    
    public void unBindFbo(){
        colorBuf.unbind();
    }
    public ColorMapPass(Scene scene) throws LoadResourseException {
        super(scene);
        this.colorBuf = createColorBuffer();
        scene.getFrameBuffersStorage().put("ColorBuffer", colorBuf);
    }
    
    
    protected int createCubeTexture(){
         CubeMapTexture cubeMapTex = new CubeMapTexture(gl);
        
        // ??????? What should it be?
        cubeMapTex.setInternalFormat(GL.GL_RGBA);
        cubeMapTex.setTextureFaceWidth(1024);
        cubeMapTex.setTextureFaceHeight(1024);
        cubeMapTex.setFillWithMock(false);
        
        cubeMapTex.init();
        return cubeMapTex.getTextureId();
    }
    
    protected FrameBuffer createColorBuffer() throws LoadResourseException{
        
       FrameBuffer colorBuffer = new FrameBuffer(scene.getGl(), 1024, 1024);
       
       int textureId = createCubeTexture();
       colorBuffer.setTextureId(textureId);
       
       colorBuffer.setClearFlag(GL.GL_DEPTH_BUFFER_BIT);//|GL.GL_COLOR_BUFFER_BI
       colorBuffer.setBufferTexturePurpose(GL.GL_COLOR_ATTACHMENT0);
       colorBuffer.setTexturePurpose1(GL.GL_RGB);
       colorBuffer.setTexturePurpose2(GL.GL_RGB);
       colorBuffer.setUseDepthRenderBuffer(true); 
       colorBuffer.setUseDrawBuffer(true);
       colorBuffer.setTextureValueType(GL.GL_UNSIGNED_BYTE);
        System.out.println("Prepare glass FBO");
       colorBuffer.init();
       System.out.println("glass FBO was finished");
       return colorBuffer;
    }
    
}
