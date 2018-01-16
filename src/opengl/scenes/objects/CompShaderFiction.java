/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.objects;

import com.jogamp.opengl.util.GLBuffers;
import engine.exception.LoadResourseException;
import engine.mesh.Mesh;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL4;
import opengl.engine.GLSLProgramObject;
import opengl.engine.SceneObject;

/**
 *
 * @author Andrey
 */
public class CompShaderFiction extends Quad{
    protected static final int BUF_1 = 0;
    protected static final int BUF_2 = 1;
     protected static final int BUF_3= 2;
     
    protected final int buffersCompCount = 3;
    
    protected boolean optVertexNormals = false;
    //protected boolean optVeretexTextures = false;
    protected boolean optLoadTexture = true;
    protected boolean optLoadModelFile = false;
    protected boolean optHasVisual = true;

    protected IntBuffer  buffersComp;
    protected     GLSLProgramObject computeShaderProg ;
    //    protected int vertexCoordsNum = 1;
    public CompShaderFiction(GL4 gl) {
        super(gl);
        
    }

  
    
    @Override
    protected void buildShaders() throws LoadResourseException {
         GLSLProgramObject shaderProg = new GLSLProgramObject(gl);
         String shadersPath = assetsFilepath + shadersFilePath;
         shaderProg.attachShader(gl, shadersPath+"shader.comp", gl.GL_COMPUTE_SHADER);
         shaderProg.initializeProgram(gl, true);
         this.computeShaderProg = shaderProg;
         //this.shadersPrograms.put("ComputeShader", shaderProg) ;
         super.buildShaders(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void render(GL4 gl) {
         GLSLProgramObject shaderProg  = computeShaderProg;
         //  FloatBuffer buf = null;// GLBuffers.newDirectFloatBuffer(new float[3]);
         int WORK_GR =2;
          shaderProg.bind(gl);
         // initCompShaderBuffer();
         gl.glBindBufferBase(gl.GL_SHADER_STORAGE_BUFFER, 1,buffersComp.get(BUF_1));
         gl.glBindBufferBase(gl.GL_SHADER_STORAGE_BUFFER, 2,buffersComp.get(BUF_2));
         gl.glBindBufferBase(gl.GL_SHADER_STORAGE_BUFFER, 3,buffersComp.get(BUF_3));
          gl.glBindBufferBase(gl.GL_SHADER_STORAGE_BUFFER, 4,buffers.get(BUFFER_VERTEX));
          
         gl.glDispatchCompute(  WORK_GR, 1, 1); 
         gl.glMemoryBarrier( gl.GL_BUFFER_UPDATE_BARRIER_BIT  ); //GL_VERTEX_ATTRIB_ARRAY_BARRIER_BIT GL_BUFFER_UPDATE_BARRIER_BIT
         
         //gl.glBindBufferBase(gl.GL_SHADER_STORAGE_BUFFER, 1,buffersCompget(BUF_1));
         gl.glBindBuffer( gl.GL_SHADER_STORAGE_BUFFER,buffersComp.get(BUF_3) );
         
         ByteBuffer buf = gl.glMapBuffer(gl.GL_SHADER_STORAGE_BUFFER, gl.GL_READ_ONLY );
        /*ByteBuffer buf = null;
        gl.glBufferData(gl.GL_SHADER_STORAGE_BUFFER,  6*4, buf, gl.GL_READ_ONLY);*/
         FloatBuffer fb =  buf.asFloatBuffer();
         System.out.println(fb.get(1)); 
         gl.glUnmapBuffer( gl.GL_SHADER_STORAGE_BUFFER );
         //
        
        super.render(gl);
        
       
         
         
    }
    
    @Override
    
    
    protected void initBuffers(){
         
        buffersComp = IntBuffer.allocate(buffersCompCount);
        gl.glGenBuffers(buffersCompCount, buffersComp);
        initCompShaderBuffer();
        
        super.initBuffers();
       
         
    }
   
    
    protected void initCompShaderBuffer() {
        float[] values  = {1.0f, 2.0f, 3.0f, 1.0f, 2.0f, 3.0f };
        initBuffer(values, BUF_1, GL4.GL_SHADER_STORAGE_BUFFER, buffersComp );
        initBuffer(values, BUF_2, GL4.GL_SHADER_STORAGE_BUFFER, buffersComp );
        initBuffer(values, BUF_3, GL4.GL_SHADER_STORAGE_BUFFER, buffersComp );
    }
    
}
