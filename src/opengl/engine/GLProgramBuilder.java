/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.engine;

import engine.exception.LoadResourseException;
import javax.media.opengl.GL4;

/**
 *
 * @author Andrey
 */
public class GLProgramBuilder {
    public static GLSLProgramObject buildProgram(GL4 gl, String folder, 
            boolean vertexShader, 
            boolean fragmentShader,
            boolean geometryShader
    ) throws LoadResourseException{
        GLSLProgramObject shaderProg = new GLSLProgramObject(gl);
        shaderProg.attachShader(gl, folder + "shader.vert", GL4.GL_VERTEX_SHADER);
        shaderProg.attachShader(gl, folder + "shader.frag", GL4.GL_FRAGMENT_SHADER);
        if(geometryShader)shaderProg.attachShader(gl, folder + "shader.geom", GL4.GL_GEOMETRY_SHADER);
        shaderProg.initializeProgram(gl, true);
        return shaderProg;
    }
}
