/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.engine;

import engine.exception.LoadResourseException;
import java.util.LinkedHashMap;
import javax.media.opengl.GL4;

/**
 *
 * @author Andrey
 */
public class ShadersStore {
    protected static ShadersStore instance;
    protected GL4 gl;
            
    protected LinkedHashMap<String, GLSLProgramObject> shadersPrograms;
    
    private ShadersStore(){
        shadersPrograms = new LinkedHashMap<>();
        this.gl = MainConfig.getInstance().getGl();
    }
    
    public static ShadersStore getInstance(){
        if(instance == null){
            instance = new ShadersStore();
        }
        return instance;
    }
    
    public GLSLProgramObject getShaderProgram(String shaderProgName, ShaderProgOptions options) throws LoadResourseException{
        GLSLProgramObject shaderProg;
        String shaderPath = MainConfig.getInstance().getShadersFilePath();
        if(!shadersPrograms.containsKey(shaderProgName)) {
           shaderProg  = GLProgramBuilder.buildProgram(gl, shaderPath + shaderProgName + "/", options);
           this.shadersPrograms.put(shaderProgName, shaderProg);
        } else{
            shaderProg = shadersPrograms.get(shaderProgName);
        }
        
        return shaderProg;
         
    }
            
}
