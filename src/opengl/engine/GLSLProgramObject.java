package opengl.engine;

// Translated from C++ Version see below:
//
// GLSLProgramObject.h - Wrapper for GLSL program objects
//
// Author: Louis Bavoil
// Email: sdkfeedback@nvidia.com
//
// Copyright (c) NVIDIA Corporation. All rights reserved.
////////////////////////////////////////////////////////////////////////////////
import engine.exception.LoadResourseException;
import engine.exception.ShaderException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.util.ArrayList;
import javax.media.opengl.GL3;
import javax.media.opengl.GL4;
import math.linearAlgebra.Matrix;
import utils.IoReader;

public class GLSLProgramObject {

//    protected Vector<Integer> _vertexShaders = new Vector<>();
//    protected Vector<Integer> _fragmentShaders = new Vector<>();
    protected ArrayList<Integer> _vertexShaders = new ArrayList<>();
    protected ArrayList<Integer> _fragmentShaders = new ArrayList<>();
    private Integer _progId;
//    private String shadersPath = "/shaders/";
  
    protected boolean useTransformFeedback;
    protected String[] feedbackVaryings;
    
    public boolean isUseTransformFeedback() {
        return useTransformFeedback;
    }

    public void setUseTransformFeedback(boolean useTransformFeedback) {
        this.useTransformFeedback = useTransformFeedback;
    }

    public String[] getFeedbackVaryings() {
        return feedbackVaryings;
    }

    public void setFeedbackVaryings(String[] feedbackVaryings) {
        this.feedbackVaryings = feedbackVaryings;
    }

  
    protected TransformationFeedback transformFeedback;

    public TransformationFeedback getTransformFeedback() {
        return transformFeedback;
    }
    
    public GLSLProgramObject(GL3 gl3) {
        _progId = 0;
    }


    public void destroy(GL3 gl3) {
        for (int i = 0; i < _vertexShaders.size(); i++) {
            gl3.glDeleteShader(_vertexShaders.get(i));
        }
        for (int i = 0; i < _fragmentShaders.size(); i++) {
            gl3.glDeleteShader(_fragmentShaders.get(i));
        }
        if (_progId != 0) {
            gl3.glDeleteProgram(_progId);
        }
    }

    public void bind(GL3 gl3) {
        gl3.glUseProgram(_progId);
    }

    public void unbind(GL3 gl3) {
        gl3.glUseProgram(0);
    }

    public void setUniform(GL3 gl3, String name, float[] val, int count) {
        int id = gl3.glGetUniformLocation(_progId, name);
        if (id == -1) {
            //System.err.println("Warning: Invalid uniform parameter " + name);
            return;
        }
        switch (count) {
            case 1:
                gl3.glUniform1fv(id, 1, val, 0);
                break;
            case 2:
                gl3.glUniform2fv(id, 1, val, 0);
                break;
            case 3:
                gl3.glUniform3fv(id, 1, val, 0);
                break;
            case 4:
                gl3.glUniform4fv(id, 1, val, 0);
                break;
        }
    }
    public void setUniform(GL3 gl3, String name, int val ){
        int id = gl3.glGetUniformLocation(_progId, name);
        gl3.glUniform1i(id, val);
    }
    
    public void setUniform(GL3 gl3, String matrixName, Matrix matrix){
        float[] mBuffer = matrix.valuesToBuffer();
        int matrixId = gl3.glGetUniformLocation(_progId, matrixName);
        if (matrixId == -1) {
            //System.err.println("Warning: Invalid texture " + matrixName);
            return;
        }
        gl3.glUniformMatrix4fv(matrixId, 1, false, mBuffer , 0);
    }
    
    public void setTextureUnit(GL3 gl3, String texname, int texunit) {
        int[] params = new int[]{0};
        gl3.glGetProgramiv(_progId, GL3.GL_LINK_STATUS, params, 0);
        if (params[0] != 1) {
            System.err.println("Error: setTextureUnit needs program to be linked.");
        }
        int id = gl3.glGetUniformLocation(_progId, texname);
        if (id == -1) {
            System.err.println("Warning: Invalid texture " + texname);
            return;
        }
        gl3.glUniform1i(id, texunit);
    }

    public void bindTexture(GL3 gl3, int target, String texname, int texid, int texunit) {
        gl3.glActiveTexture(GL3.GL_TEXTURE0 + texunit);
        gl3.glBindTexture(target, texid);
        setTextureUnit(gl3, texname, texunit);
        gl3.glActiveTexture(GL3.GL_TEXTURE0);
    }

    public void bindTexture2D(GL3 gl3, String texname, int texid, int texunit) {
        bindTexture(gl3, GL3.GL_TEXTURE_2D, texname, texid, texunit);
    }

    public void bindTexture3D(GL3 gl3, String texname, int texid, int texunit) {
        bindTexture(gl3, GL3.GL_TEXTURE_3D, texname, texid, texunit);
    }

    public void bindTextureRECT(GL3 gl3, String texname, int texid, int texunit) {
        bindTexture(gl3, GL3.GL_TEXTURE_RECTANGLE, texname, texid, texunit);
    }

    protected String processDirectives(String line){
        String res = line;
        return res;
    }
    
    public  void attachShader(GL3 gl3, String filename, int shaderType) throws LoadResourseException{
        String content = "";
        
        try {
            URL fileURL = getClass().getResource(filename);
            BufferedReader input = new BufferedReader(new InputStreamReader(fileURL.openStream()));
            String line = null;
            
            while ((line = input.readLine()) != null) {
                if (line.length() > 3 &&  line.substring(0, 2) == "//@") {
                    line = processDirectives(line.substring(3));
                }
                content += line + "\n";
            }
        } catch(Exception e){
            throw new LoadResourseException("Can't find file with shader " + filename);
        }
         
        int iID = gl3.glCreateShader(shaderType);
        String[] akProgramText = new String[1];
        akProgramText[0] = content;

        int[] params = new int[]{0};

        int[] aiLength = new int[1];
        aiLength[0] = akProgramText[0].length();
        int iCount = 1;

        gl3.glShaderSource(iID, iCount, akProgramText, null, 0);

        gl3.glCompileShader(iID);

        gl3.glGetShaderiv(iID, GL3.GL_COMPILE_STATUS, params, 0);

        if (params[0] != 1) {
            //System.err.println(filename);
            //System.err.println("compile status: " + params[0]);
            gl3.glGetShaderiv(iID, GL3.GL_INFO_LOG_LENGTH, params, 0);
           // System.err.println("log length: " + params[0]);
            byte[] abInfoLog = new byte[params[0]];
            gl3.glGetShaderInfoLog(iID, params[0], params, 0, abInfoLog, 0);
            throw new ShaderException("Error in shader: " + filename + "." + new String(abInfoLog));
            //System.exit(-1);
        }
        _vertexShaders.add(iID);
    }
    
    public final void attachVertexShader(GL3 gl3, String filename) throws LoadResourseException{
        attachShader(gl3, filename, GL3.GL_VERTEX_SHADER);
    }

    public final void attachFragmentShader(GL3 gl3, String filename) throws LoadResourseException{
        attachShader(gl3, filename, GL3.GL_FRAGMENT_SHADER);
    }

    public final void initializeProgram(GL4 gl3, boolean cleanUp) {
        _progId = gl3.glCreateProgram();

        for (int i = 0; i < _vertexShaders.size(); i++) {
            gl3.glAttachShader(_progId, _vertexShaders.get(i));
        }

        for (int i = 0; i < _fragmentShaders.size(); i++) {
            gl3.glAttachShader(_progId, _fragmentShaders.get(i));
        }

        if(useTransformFeedback){
            transformFeedback = new TransformationFeedback(gl3);
            transformFeedback.addToProg(this, feedbackVaryings);
        }
        
        gl3.glLinkProgram(_progId);

        int[] params = new int[]{0};
        gl3.glGetProgramiv(_progId, GL3.GL_LINK_STATUS, params, 0);

        if (params[0] != 1) {

            System.err.println("link status: " + params[0]);
            gl3.glGetProgramiv(_progId, GL3.GL_INFO_LOG_LENGTH, params, 0);
            System.err.println("log length: " + params[0]);

            byte[] abInfoLog = new byte[params[0]];
            gl3.glGetProgramInfoLog(_progId, params[0], params, 0, abInfoLog, 0);
            System.err.println(new String(abInfoLog));
        }

        gl3.glValidateProgram(_progId);

        if (cleanUp) {
            for (int i = 0; i < _vertexShaders.size(); i++) {
                gl3.glDetachShader(_progId, _vertexShaders.get(i));
                gl3.glDeleteShader(_vertexShaders.get(i));
            }

            for (int i = 0; i < _fragmentShaders.size(); i++) {
                gl3.glDetachShader(_progId, _fragmentShaders.get(i));
                gl3.glDeleteShader(_fragmentShaders.get(i));
            }
        }
    }

    public Integer getProgramId() {
        return _progId;
    }

    private void LoadResourseException(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
};
