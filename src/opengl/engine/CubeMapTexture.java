/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.engine;

import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import javax.media.opengl.GL4;
import javax.media.opengl.GLContext;
import opengl.scenes.testscene.TestScene;
import utils.IoReader;

/**
 *
 * @author Andrey
 */
public class CubeMapTexture {
    protected GL4 gl;
    protected IntBuffer textBuff;
    protected int textureId;

    public int getTextureId() {
        return textureId;
    }
    
    protected int textureFaceWidth;
    protected int textureFaceHeight;

    protected Buffer bufferMockData;
    protected boolean fillWithMock;

    public boolean isFillWithMock() {
        return fillWithMock;
    }

    public void setFillWithMock(boolean fillWithMock) {
        this.fillWithMock = fillWithMock;
    }
    
    public int getTextureFaceWidth() {
        return textureFaceWidth;
    }

    public void setTextureFaceWidth(int textureFaceWidth) {
        this.textureFaceWidth = textureFaceWidth;
    }

    public int getTextureFaceHeight() {
        return textureFaceHeight;
    }

    public void setTextureFaceHeight(int textureFaceHeight) {
        this.textureFaceHeight = textureFaceHeight;
    }

    public int getInternalFormat() {
        return internalFormat;
    }

    public void setInternalFormat(int internalFormat) {
        this.internalFormat = internalFormat;
    }
    protected int internalFormat;
    
    public CubeMapTexture(GL4 gl) {
        this.gl = gl;
    }
    
    protected int createTexture(){
        textBuff = IntBuffer.allocate(1);
        gl.glGenTextures(1, textBuff);
        return textBuff.get(0);
    }
    
    protected void setProps(){
        gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_NEAREST );
        gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_NEAREST );
        gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_WRAP_S, GL4.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_WRAP_T, GL4.GL_CLAMP_TO_EDGE);
        gl.glTexParameteri(GL.GL_TEXTURE_CUBE_MAP, GL4.GL_TEXTURE_WRAP_R, GL4.GL_CLAMP_TO_EDGE);
    }
    
    protected void setFaceProp(int faceId, Buffer data){
         gl.glTexImage2D(faceId, 0, internalFormat, textureFaceWidth, textureFaceHeight, 0, GL.GL_RGBA, GL.GL_UNSIGNED_BYTE, data);
    }

    public void init(){
        if(fillWithMock){
            try {
                bufferMockData = getMockData();
            } catch (IOException ex) {
                System.err.println("Cubemap mock data can not load!");
            }
        }
         gl.glEnable(GL.GL_TEXTURE_CUBE_MAP);
        textureId = createTexture();
        gl.glBindTexture( GL.GL_TEXTURE_CUBE_MAP, textureId);
        this.setProps();
        
        for(int i = 0; i<6; i++){
            setFaceProp(GL.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i, bufferMockData); 
        }
        gl.glBindTexture( GL.GL_TEXTURE_CUBE_MAP, 0);
    }
    
    
    protected Buffer getMockData() throws IOException{
        String fn = MainConfig.getInstance().getModelsFilePath() + "african_head_diffuse.jpg";
        InputStream stream = IoReader.class.getResourceAsStream(fn);
        boolean mipmapped = true;
        TextureData data = TextureIO.newTextureData(GLContext.getCurrentGL().getGLProfile(), stream,
                                                  mipmapped,
                                                  TextureIO.JPG);
        return data.getBuffer();
    }
    
}
