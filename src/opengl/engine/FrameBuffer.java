/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.engine;

import java.nio.IntBuffer;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_CLAMP_TO_EDGE;
import static javax.media.opengl.GL.GL_FLOAT;
import static javax.media.opengl.GL.GL_FRAMEBUFFER;
import static javax.media.opengl.GL.GL_NEAREST;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL.GL_TEXTURE_MAG_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_MIN_FILTER;
import static javax.media.opengl.GL.GL_TEXTURE_WRAP_S;
import static javax.media.opengl.GL.GL_TEXTURE_WRAP_T;
import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import static javax.media.opengl.GL2ES2.GL_DEPTH_COMPONENT;
import javax.media.opengl.GL4;


/**
 *
 * @author Andrey
 */
public class FrameBuffer {

    protected GL4 gl;
    protected IntBuffer FBoBuffers;
    protected IntBuffer textBuffers; 
    protected IntBuffer RBoBuffers;
    
    protected int depthTextureId; 
    protected  GLSLProgramObject shadowProg;
    
    protected int width;
    protected int height;
    protected int texturePurpose1;
    protected int texturePurpose2;
    protected boolean useDepthRenderBuffer;
    protected boolean useDrawBuffer;
    protected int textureValueType ;

    protected boolean useStencilBuffer;

    public boolean isUseStencilBuffer() {
        return useStencilBuffer;
    }

    public void setUseStencilBuffer(boolean useStencilBuffer) {
        this.useStencilBuffer = useStencilBuffer;
    }
    
    public int getTextureValueType() {
        return textureValueType;
    }

    public void setTextureValueType(int textureValueType) {
        this.textureValueType = textureValueType;
    }

    public boolean isUseDrawBuffer() {
        return useDrawBuffer;
    }

    public void setUseDrawBuffer(boolean useDrawBuffer) {
        this.useDrawBuffer = useDrawBuffer;
    }

    public boolean isUseDepthRenderBuffer() {
        return useDepthRenderBuffer;
    }

    public void setUseDepthRenderBuffer(boolean useDepthRenderBuffer) {
        this.useDepthRenderBuffer = useDepthRenderBuffer;
    }

    
    public int getTexturePurpose2() {
        return texturePurpose2;
    }

    public void setTexturePurpose2(int texturePurpose2) {
        this.texturePurpose2 = texturePurpose2;
    }
    protected int bufferTexturePurpose;

    protected int bufferType =  GL.GL_UNSIGNED_BYTE;

    public int getBufferType() {
        return bufferType;
    }

    public void setBufferType(int bufferType) {
        this.bufferType = bufferType;
    }
    
    public int getTexturePurpose1() {
        return texturePurpose1;
    }

    public void setTexturePurpose1(int texturePurpose) {
        this.texturePurpose1 = texturePurpose;
    }

    public int getBufferTexturePurpose() {
        return bufferTexturePurpose;
    }

    public void setBufferTexturePurpose(int bufferPurpose) {
        this.bufferTexturePurpose = bufferPurpose;
    }
    
    public FrameBuffer(GL4 gl,  int width, int height ) {
        this.gl = gl;
        this.width = width;
        this.height = height;
    }
     
    protected void createFBO(){
       
        createFrameBuffer();
       
        /*GLSLProgramObject shadowProg = shadersExtPrograms.get("ShadowMap");
        shadowProg.setUniform(gl, "lightMVP", lightMVP);*/
    }
    
    public void init(){
        textBuffers = IntBuffer.allocate(1);
        FBoBuffers = IntBuffer.allocate(1); 
        RBoBuffers = IntBuffer.allocate(1);
        
        gl.glGenFramebuffers(1, FBoBuffers);
        gl.glBindFramebuffer(GL_FRAMEBUFFER, FBoBuffers.get(0));
        createFrameBuffer();
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0); 
    }
    
    protected int createFboTexture(){
        gl.glGenTextures(1, textBuffers);

        depthTextureId = textBuffers.get(0);
        gl.glBindTexture(GL_TEXTURE_2D, depthTextureId);

        gl.glTexImage2D(GL_TEXTURE_2D, 0, texturePurpose1, width, height, 0, texturePurpose2, textureValueType, null);
        

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        
        /*gl.glTexParameteri(GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_FUNC, gl.GL_GREATER);
	gl.glTexParameteri(GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_MODE, gl.GL_COMPARE_REF_TO_TEXTURE);
        */
       // gl.glTexParameteri(GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_MODE, GL2.GL_COMPARE_R_TO_TEXTURE);
        //gl.glFramebufferTexture(GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, depthTextureId, 0);
      /*  gl.glTexParameteri( GL_TEXTURE_2D, gl.GL_TEXTURE_COMPARE_MODE, gl.GL_NONE );
gl.glTexParameteri( GL_TEXTURE_2D, gl.GL_DEPTH_TEXTURE_MODE, gl.GL_LUMINANCE );*/
      gl.glBindTexture(GL_TEXTURE_2D,0);
        return depthTextureId;
    }
    
    protected void createFrameBuffer(){
        
        depthTextureId = createFboTexture();
        //gl.glBindFramebuffer(GL_FRAMEBUFFER, FBoBuffers.get(0));
        gl.glFramebufferTexture2D(GL_FRAMEBUFFER, bufferTexturePurpose, GL_TEXTURE_2D, depthTextureId, 0);
        ///gl.glFramebufferTexture2D(GL_FRAMEBUFFER, gl.GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, depthTextureId, 0);
        
       // gl.glRenderbufferStorage(gl.GL_RENDERBUFFER, gl.GL_DEPTH_COMPONENT, 1024, 768);
        //addRenderShaBuffer();
        if(useDepthRenderBuffer){
            int rboId = addRenderDepthBuffer();
            gl.glFramebufferRenderbuffer(GL_FRAMEBUFFER, gl.GL_DEPTH_ATTACHMENT, gl.GL_RENDERBUFFER, rboId);
        }
         if(useStencilBuffer){
            int rboId = addStencilBuffer();
            gl.glFramebufferRenderbuffer(GL_FRAMEBUFFER, gl.GL_DEPTH_STENCIL_ATTACHMENT, gl.GL_RENDERBUFFER, rboId);
        }
         
         //
      //  gl.glReadBuffer(GL.GL_NONE);
        //gl.glDrawBuffer(useDrawBuffer ?   FBoBuffers.get(0): GL.GL_NONE);//gl.GL_COLOR_ATTACHMENT0
        gl.glDrawBuffer(GL.GL_COLOR_ATTACHMENT0);//
        
        int status =  gl.glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (status != gl.GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("FrameBuffer Error:" + gl.glGetString(gl.glGetError()));
            
        }
    }
    
    
    public int addStencilBuffer(){
        gl.glGenRenderbuffers(1, RBoBuffers);
        gl.glBindRenderbuffer(gl.GL_RENDERBUFFER, RBoBuffers.get(0));
        gl.glRenderbufferStorage(gl.GL_RENDERBUFFER, gl.GL_DEPTH24_STENCIL8, width, height);
        gl.glBindRenderbuffer(gl.GL_RENDERBUFFER, 0);
        return RBoBuffers.get(0);
    }
    
    public int addRenderDepthBuffer(){
        gl.glGenRenderbuffers(1, RBoBuffers);
        gl.glBindRenderbuffer(gl.GL_RENDERBUFFER, RBoBuffers.get(0));
        gl.glRenderbufferStorage(gl.GL_RENDERBUFFER, gl.GL_DEPTH_COMPONENT, width, height);
        gl.glFramebufferRenderbuffer(GL_FRAMEBUFFER, gl.GL_DEPTH_ATTACHMENT, gl.GL_RENDERBUFFER, RBoBuffers.get(0));

        gl.glBindRenderbuffer(gl.GL_RENDERBUFFER, 0);
        return RBoBuffers.get(0);
    }
    
      public int addRenderShaBuffer(){
          gl.glGenRenderbuffers(1, RBoBuffers);
          gl.glBindRenderbuffer(gl.GL_RENDERBUFFER, RBoBuffers.get(0));
          gl.glRenderbufferStorage(gl.GL_RENDERBUFFER, gl.GL_RGBA, width, height);
// Attach the renderbuffer
          gl.glFramebufferRenderbuffer(gl.GL_DRAW_FRAMEBUFFER, gl.GL_COLOR_ATTACHMENT0, gl.GL_RENDERBUFFER, RBoBuffers.get(0));
          return RBoBuffers.get(0);
    }
    
    public void bindFBO(){
        gl.glBindFramebuffer(GL_FRAMEBUFFER, FBoBuffers.get(0));
         gl.glClear(GL4.GL_COLOR_BUFFER_BIT|gl.GL_DEPTH_BUFFER_BIT);
        gl.glViewport(0,0,1024,768); 
          gl.glDrawBuffer(GL.GL_COLOR_ATTACHMENT0);//
       
    }
    
    public void unbind(){
        gl.glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }
    

        public void setTexture(GLSLProgramObject prog){
       // GLSLProgramObject shadowProg = shadersExtPrograms.get("ShadowMap");
        int texLoc = gl.glGetUniformLocation(prog.getProgramId(), "fboTexture");
  
        gl.glUniform1i(texLoc, GL4.GL_TEXTURE0);
            gl.glEnable(GL_TEXTURE_2D);  
        gl.glActiveTexture(GL.GL_TEXTURE0);    
        gl.glBindTexture(GL_TEXTURE_2D, depthTextureId);
        gl.glActiveTexture(0);
    }
}
