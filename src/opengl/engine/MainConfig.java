/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package opengl.engine;

import java.util.LinkedHashMap;
import javax.media.opengl.GL4;

/**
 *
 * @author Andrey
 */
public class MainConfig {
    private static MainConfig instance;
    protected GL4 gl;
 
    protected final String assetsFilepath = "/graphicsexperiment/assets/";
    protected final String shadersFilePath =  "shaders/";
    protected final String modelsFilePath = "models/";

    public String getModelsFilePath() {
        return assetsFilepath + modelsFilePath;
    }
    
    protected final int screenWidth = 1024;
    protected final  int screenHeight = 768;

    
    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
    
    public GL4 getGl() {
        return gl;
    }

    public void setGl(GL4 gl) {
        this.gl = gl;
    }
            
   
    public String getAssetsFilepath() {
        return assetsFilepath;
    }

    public String getShadersFilePath() {
        return assetsFilepath + shadersFilePath;
    }
    
    protected LinkedHashMap<String, GLSLProgramObject> shadersPrograms;
    
    private MainConfig(){
        
    }
    
    public static MainConfig getInstance(){
        if(instance == null){
            instance = new MainConfig();
        }
        return instance;
    }
     
     
}
