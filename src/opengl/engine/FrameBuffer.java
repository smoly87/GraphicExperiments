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

    public int getTexturePurpose2() {
        return texturePurpose2;
    }

    public void setTexturePurpose2(int texturePurpose2) {
        this.texturePurpose2 = texturePurpose2;
    }
    protected int bufferPurpose;

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

    public int getBufferPurpose() {
        return bufferPurpose;
    }

    public void setBufferPurpose(int bufferPurpose) {
        this.bufferPurpose = bufferPurpose;
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
        //gl.glActiveTexture( GL4.GL_TEXTURE2);
        gl.glBindTexture(GL_TEXTURE_2D, depthTextureId);
        /*int texLoc = gl.glGetUniformLocation(shadowProg.getProgramId(), "fboTexture");
        gl.glUniform1i(texLoc, GL4.GL_TEXTURE2);*/
        //TODO: Screen size
        gl.glTexImage2D(GL_TEXTURE_2D, 0, texturePurpose1, 1024, 768, 0, texturePurpose2, GL.GL_UNSIGNED_BYTE, null);

        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
       // gl.glTexParameteri(GL_TEXTURE_2D, GL2ES2.GL_TEXTURE_COMPARE_MODE, GL2.GL_COMPARE_R_TO_TEXTURE);
        //gl.glFramebufferTexture(GL_FRAMEBUFFER, GL.GL_DEPTH_ATTACHMENT, depthTextureId, 0);
        return depthTextureId;
    }
    
    protected void createFrameBuffer(){
        
        depthTextureId = createFboTexture();
        gl.glBindFramebuffer(GL_FRAMEBUFFER, FBoBuffers.get(0));
        gl.glFramebufferTexture2D(GL_FRAMEBUFFER, bufferPurpose, GL_TEXTURE_2D, depthTextureId, 0);
        
        int rboId = addRenderBuffer();
        gl.glFramebufferRenderbuffer(GL_FRAMEBUFFER, gl.GL_DEPTH_ATTACHMENT, gl.GL_RENDERBUFFER, rboId);
        
        gl.glReadBuffer(GL.GL_NONE);
        gl.glDrawBuffer(FBoBuffers.get(0));
        
        int status =  gl.glCheckFramebufferStatus(GL_FRAMEBUFFER);
        if (status != gl.GL_FRAMEBUFFER_COMPLETE) {
            System.err.println("FrameBuffer Error");
        }
    }
    
    
    public int addRenderBuffer(){
        gl.glGenRenderbuffers(1, RBoBuffers);
        gl.glBindRenderbuffer(gl.GL_RENDERBUFFER, RBoBuffers.get(0));
        gl.glRenderbufferStorage(gl.GL_RENDERBUFFER, gl.GL_DEPTH_COMPONENT, width, height);
        gl.glBindRenderbuffer(gl.GL_RENDERBUFFER, 0);
        return RBoBuffers.get(0);
    }
    
    public void bindFBO(){
        gl.glBindFramebuffer(GL_FRAMEBUFFER, FBoBuffers.get(0));
        
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
