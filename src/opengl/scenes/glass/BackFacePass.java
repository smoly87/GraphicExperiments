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
import opengl.engine.GLSLProgramObject;
import opengl.engine.RenderPass;
import opengl.engine.Scene;
import opengl.engine.SceneObject;
import opengl.engine.ShaderProgOptions;
import opengl.engine.ShadersStore;

/**
 *
 * @author Andrey
 */
public class BackFacePass extends RenderPass{
    protected FrameBuffer fbo;
    protected int faceId;
    protected boolean useGeomShader;
    protected GLSLProgramObject buffProg;

    
    public int getFaceId() {
        return faceId;
    }

    public void setFaceId(int faceId) {
        this.faceId = faceId;
    }
    @Override
    public void render() {
       // colorBuf.bindFBO();
        fbo.bindCubeTexture(fbo.getTextureId(), this.getFaceId());
         gl.glClear(GL.GL_DEPTH_BUFFER_BIT|gl.GL_COLOR_BUFFER_BIT);
        gl.glCullFace(GL4.GL_FRONT);
        super.render(); 
          gl.glCullFace(GL4.GL_BACK);
        //colorBuf.unbind();
    }
   
    public void bindFbo(){
        fbo.bindFBO();
    }
    
    public void unBindFbo(){
        fbo.unbind();
    }
    public BackFacePass(Scene scene) throws LoadResourseException {
        super(scene);
        fbo = createBuffer();
        scene.getFrameBuffersStorage().put("BackFaceBuffer", fbo);
        buffProg = ShadersStore.getInstance()
                                 .getShaderProgram("BackFace", new ShaderProgOptions(false));
    
    
          
    }

    @Override
    protected void renderObject(GL4 gl, SceneObject sceneObj, String objName) {
   
           GLSLProgramObject programObject = buffProg;
           programObject.bind(gl);
           execObjShaderProg(gl, sceneObj, "Main", programObject);
           programObject.unbind(gl);
    
    }
   
    protected FrameBuffer createBuffer() throws LoadResourseException{
        
       FrameBuffer fbo = new FrameBuffer(scene.getGl(), 1024, 1024);
       
       
       fbo.setClearFlag(GL.GL_DEPTH_BUFFER_BIT|gl.GL_COLOR_BUFFER_BIT);//|GL.GL_COLOR_BUFFER_BI
       fbo.setBufferTexturePurpose(GL.GL_COLOR_ATTACHMENT0);
       fbo.setTexturePurpose1(gl.GL_RGBA16F );
       fbo.setTexturePurpose2(GL.GL_RGB);
       fbo.setUseDepthRenderBuffer(true); 
       fbo.setUseDrawBuffer(true);
       fbo.setTextureValueType(GL.GL_UNSIGNED_BYTE);
        System.out.println("Prepare glass FBO");
       fbo.init();
       System.out.println("glass FBO was finished");
       return fbo;
    }
    
}
