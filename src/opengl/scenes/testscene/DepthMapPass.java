/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.scenes.testscene;

import engine.exception.LoadResourseException;
import java.util.HashMap;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GL4;
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
public class DepthMapPass extends RenderPass{
    protected FrameBuffer depthBuff;
    protected GLSLProgramObject shadowProg;

    @Override
    public void render() {
        depthBuff.bindFBO();
         
        super.render(); 
        depthBuff.unbind();
    }
   
    public DepthMapPass(Scene scene) throws LoadResourseException {
        super(scene);
        this.depthBuff = createDepthBuffer();
        scene.getFrameBuffersStorage().put("DepthBuffer", depthBuff);
        shadowProg = ShadersStore.getInstance()
                                 .getShaderProgram("ShadowMap", new ShaderProgOptions(false));
    }

    @Override
    protected void renderObject(GL4 gl, SceneObject sceneObj, String objName) {
   
           GLSLProgramObject programObject = shadowProg;
           programObject.bind(gl);
           execObjShaderProg(gl, sceneObj, "Main", programObject);
           programObject.unbind(gl);
    
    }
    
    protected FrameBuffer createDepthBuffer() throws LoadResourseException{
        System.out.println("Prepare depth buffer");
       FrameBuffer buff = new FrameBuffer(scene.getGl(), 1024, 768);
       buff.setBufferTexturePurpose(GL.GL_DEPTH_ATTACHMENT);
       buff.setTexturePurpose1(GL.GL_DEPTH_COMPONENT24);
       buff.setTexturePurpose2(GL2ES2.GL_DEPTH_COMPONENT);
       buff.setUseDepthRenderBuffer(false); 
       buff.setUseDrawBuffer(true);
       buff.setUseColorRenderBuffer(true);
       buff.setTextureValueType(GL.GL_FLOAT);
       buff.init();
       System.out.println("Prepare depth buffer finished");
       return buff;
    }
    
}
