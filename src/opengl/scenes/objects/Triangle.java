/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.objects;

import com.jogamp.opengl.util.GLBuffers;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import javax.media.opengl.GL3;
import javax.media.opengl.GL4;

/**
 *
 * @author Andrey
 */
public class Triangle {
    private int[] vertexBufferObject = new int[1];
    private int[] vertexArrayObject = new int[1];
    private int[] positionBufferObject = new int[1];
       private float[] vertexPositions = new float[]{
        0.75f, 0.75f, 0.0f, 1.0f,
        0.75f, -0.75f, 0.0f, 1.0f,
        -0.75f, -0.75f, 0.0f, 1.0f,};
    private void initializeVertexBuffer(GL3 gl) {
        gl.glGenBuffers(1, IntBuffer.wrap(positionBufferObject));

        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, positionBufferObject[0]);
        {
            FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(vertexPositions);

            gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertexPositions.length * 4, buffer, GL3.GL_STATIC_DRAW);
        }
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, 0);
    }
    
    public void init(){
    }

    public void render(GL4 gl){
   
        /*gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT);*/

       // programObject.bind(gl3);
        {
            gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, positionBufferObject[0]);

            gl.glEnableVertexAttribArray(0);
            {
                gl.glVertexAttribPointer(0, 4, GL4.GL_FLOAT, false, 0, 0);
                //Последний аттрибут - кол-во вершин
                gl.glDrawArrays(GL3.GL_TRIANGLES, 0, 3);
            }
            gl.glDisableVertexAttribArray(0);
        }}
}
