/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.scenes.testscene;

import engine.exception.LoadResourseException;
import java.util.HashMap;
import javax.media.opengl.GL;
import javax.media.opengl.GL4;
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

    @Override
    public void render() {
        colorBuf.bindFBO();
         
        super.render(); 
        colorBuf.unbind();
    }
   
    public ColorMapPass(Scene scene) throws LoadResourseException {
        super(scene);
        this.colorBuf = createColorBuffer();
        scene.getFrameBuffersStorage().put("ColorBuffer", colorBuf);
    }
    
    protected FrameBuffer createColorBuffer() throws LoadResourseException{
          System.out.println("Prepare color buffer");
       FrameBuffer colorBuffer = new FrameBuffer(scene.getGl(), 1024, 768);
       colorBuffer.setBufferTexturePurpose(GL.GL_COLOR_ATTACHMENT0);
       colorBuffer.setTexturePurpose1(GL.GL_RGB);
       colorBuffer.setTexturePurpose2(GL.GL_RGB);
       colorBuffer.setUseDepthRenderBuffer(true); 
       colorBuffer.setUseDrawBuffer(true);
       colorBuffer.setTextureValueType(GL.GL_UNSIGNED_BYTE);
       colorBuffer.init();
       return colorBuffer;
    }
    
}
