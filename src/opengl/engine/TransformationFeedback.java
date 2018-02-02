/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.engine;

import com.jogamp.common.nio.Buffers;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GL2ES3;
import javax.media.opengl.GL4;
import javax.media.opengl.GL4ES3;

/**
 *
 * @author Andrey
 */
public class TransformationFeedback {

    protected GL4 gl;
    protected IntBuffer buffers;
    protected IntBuffer feedBacksBuffers;
    
    protected int buffersCompCount = 1;
    protected  int feedbackBuffId;
    protected ByteBuffer bufRead;
    
    protected int dataSize = 32*2;
    
    public TransformationFeedback(GL4 gl) {
        this.gl = gl;
        this.createTransFeedbackObj(dataSize);
    }
    
    public void addToProg( GLSLProgramObject prog, String[] feedbackVaryings){
        gl.glTransformFeedbackVaryings(prog.getProgramId(), 1, feedbackVaryings, GL2ES3.GL_SEPARATE_ATTRIBS);
    }
    
    protected void createReadBuffer(){
       
        buffers = IntBuffer.allocate(buffersCompCount);
        gl.glGenBuffers(buffersCompCount, buffers);
        feedbackBuffId = buffers.get(0);
        
    }
    
    public void createTransFeedbackObj(int bufferLength){
        createReadBuffer();
        feedBacksBuffers = IntBuffer.allocate(1);
        gl.glGenTransformFeedbacks(1, feedBacksBuffers);
        gl.glBindTransformFeedback(GL4ES3.GL_TRANSFORM_FEEDBACK, feedBacksBuffers.get(0));
        gl.glBindBufferBase(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, 0, feedbackBuffId); 
        gl.glBindTransformFeedback(GL4ES3.GL_TRANSFORM_FEEDBACK, 0);
        
        bindBuffer(bufferLength);
    }
    
    protected void bindBuffer(int bufferLength){
        gl.glBindBuffer(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, feedbackBuffId);
       
        gl.glBufferData(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, bufferLength, null, GL2ES3.GL_DYNAMIC_COPY );
         gl.glBindBuffer(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, 0);
        
        /*gl.glBindBuffer(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, feedbackBuffId);
        bufRead = gl.glMapBuffer(gl.GL_TRANSFORM_FEEDBACK_BUFFER, gl.GL_READ_ONLY );
        gl.glBindBuffer(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, 0);*/
    }
    
    public void  bind(int drawMode){
       // gl.glBindBuffer(GL2ES3.GL_ARRAY_BUFFER, feedbackBuffId);
       
        //gl.glEnable(GL2ES3.GL_RASTERIZER_DISCARD);
       
        gl.glBindTransformFeedback(GL4ES3.GL_TRANSFORM_FEEDBACK, feedBacksBuffers.get(0)); 
       //  gl.glBindBufferBase(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, 0, feedbackBuffId);
        gl.glBeginTransformFeedback(GL.GL_LINE_STRIP );
        
    }
    
    public void unbind(){
        gl.glEndTransformFeedback();
         gl.glBindTransformFeedback(GL4ES3.GL_TRANSFORM_FEEDBACK, 0);
    }
    
    public void readData(){
      /*  ByteBuffer b = Byte.allocate(12);
          
       gl.glGetBufferSubData(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, 0, 12, fb);*/
     /* gl.glBindBuffer(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, feedbackBuffId);
        bufRead = gl.glMapBuffer(gl.GL_TRANSFORM_FEEDBACK_BUFFER, gl.GL_STATIC_READ);
        gl.glBindBuffer(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, 0);
        //   */
        // bufRead = ByteBuffer.allocateDirect(dataSize); 
         FloatBuffer fb ;
          fb = Buffers.newDirectFloatBuffer(dataSize);
         gl.glBindBuffer(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, feedbackBuffId);
        // gl.glBindBufferBase(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, 0, feedbackBuffId);
         gl.glGetBufferSubData(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, 0, dataSize, fb);
        //gl.glFinish();
       //  System.out.println(bufRead.getInt(0));
         //FloatBuffer fb =  bufRead.asFloatBuffer();
         System.out.println(fb.get(4)); 
         System.out.println(fb.get(1)); 
        //  gl.glBindBuffer(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, 0);
        
         //   gl.glBindBuffer(GL2ES3.GL_TRANSFORM_FEEDBACK_BUFFER, 0); 
       //  gl.glUnmapBuffer( gl.GL_TRANSFORM_FEEDBACK_BUFFER );
         bufRead = null;
         
    }
    
}
