/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.engine;

import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import engine.exception.LoadResourseException;
import engine.mesh.Mesh;
import engine.meshLoader.BaseMeshLoader;
import engine.meshLoader.MeshLoaderObjFormat;
import opengl.engine.GLProgramBuilder;
import opengl.engine.GLSLProgramObject;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_TEXTURE_2D;
import static javax.media.opengl.GL2ES3.GL_RASTERIZER_DISCARD;
import javax.media.opengl.GL4;
import static javax.media.opengl.GL4.*;
import javax.media.opengl.GLException;
import math.linearAlgebra.Matrix;
import math.linearAlgebra.Matrix33;
import math.linearAlgebra.Matrix44;
import math.linearAlgebra.Vector;
import math.linearAlgebra.Vector3;
import math.sceneCalculations.MatrixChainOperations;
import math.transformMatricies4.MatrixRotationX;
import math.transformMatricies4.MatrixRotationY;
import math.transformMatricies4.MatrixRotationZ;
import math.transformMatricies4.MatrixScale;
import math.transformMatricies4.MatrixTranslation;
import math.transformMatricies4.MatrixUnit;
import opengl.scenes.utils.VertexAggregator;

import utils.ArrayUtils;

/**
 *
 * @author Andrey
 */
public class SceneObject {
    protected Mesh mesh;

    public Mesh getMesh() {
        return mesh;
    }
    protected GL4 gl;
    
    protected IntBuffer buffers;
    protected IntBuffer vertexArrayObject;
    
    
    protected HashMap<Integer, Integer> buffersAssociations;
    protected int curBufNum = 0;
    
    
    protected int buffersCount = 7;
    protected int vertexArrayObjectCount = 1 ;
    
    protected int vertexCoordsNum = 4;
    protected int vertexPerFace = 3;
    protected int textureCoordsNum = 2;
    protected int normalCoordsNum = 3;
    protected int vertexColorsNum = 4;
    
    protected final int BUFFER_VERTEX = 1;
    protected final int BUFFER_VERTEX_INDEXES = 2;
    protected final int BUFFER_TEXTURE_COORDS = 3;
    protected final int BUFFER_NORMALS_COORDS = 4;
    protected final int BUFFER_VETREX_COLORS = 5;
    protected final int BUFFER_TANGET_COORDS = 6;

    protected String modelsFilePath; 
    protected String shaderProgName = "Main";
    protected ShadersStore shadersStore;
    
    protected String textureFile; 
    protected String modelFile;
    
    protected int modelMatrixId;
    protected int textureId;
    protected Texture texture;
    protected Texture bumpMappingTexture;
    
    protected Matrix modelMatrix;
    
    protected Vector objScaleVec;
    protected Vector objRotVec;
    protected Vector objTranslateVec;
    
    protected boolean optVertexIndexes = false;
    protected boolean optVertexNormals = true;
    protected boolean optVeretexTextures = true;
    protected boolean optLoadTexture = true;
    protected boolean optLoadModelFile = true;
    protected boolean optHasVisual = true;
    protected boolean optIsShaderProgSpecial = true;
    protected boolean optHasColors = false;
    protected boolean optBumpMapping = false;
    protected boolean optShadowMapping = true;

    protected String bumpMappingTextureFile;
    
    protected boolean optRenderEnabled = true;
    
    protected boolean optUseTransformFeedback = false;
    protected String[] feedbackVayrings;

    public String[] getFeedbackVayrings() {
        return feedbackVayrings;
    }

    public void setFeedbackVayrings(String[] feedbackVayrings) {
        this.feedbackVayrings = feedbackVayrings;
    }
    
    public String getShaderProgName() {
        return shaderProgName;
    }

    public void setShaderProgName(String shaderProgName) {
        this.shaderProgName = shaderProgName;
    }

    public boolean isOptRenderEnabled() {
        return optRenderEnabled;
    }

    public void setOptRenderEnabled(boolean optRenderEnabled) {
        this.optRenderEnabled = optRenderEnabled;
    }


    

    public String getBumpMappingTextureFile() {
        return bumpMappingTextureFile;
    }

    public void setBumpMappingTextureFile(String bumpMappingTextureFile) {
        this.bumpMappingTextureFile = bumpMappingTextureFile;
    }
    public boolean isOptBumpMapping() {
        return optBumpMapping;
    }

    public void setOptBumpMapping(boolean optBumpMapping) {
        this.optBumpMapping = optBumpMapping;
    }
    
   /* protected Material material;*/
    protected Vector[] vertexesColors;
    
    protected boolean detectTextureNormalsOptByFile = true;
    protected boolean optLoadGeomShader = false;
    
    protected LinkedHashMap<String, GLSLProgramObject> shadersPrograms;
    
    
    protected int drawMode = GL4.GL_TRIANGLES;
    
    public SceneObject(GL4 gl){
        this.gl = gl;
        shadersPrograms = new LinkedHashMap<>();
        
        objRotVec = new Vector(4);
        objTranslateVec = new Vector(4);
        objScaleVec = new Vector(new float[]{1.0f, 1.0f, 1.0f, 1.0f});
        this.modelsFilePath = MainConfig.getInstance().getModelsFilePath();
    }
    
    protected void setTextureToShader( GLSLProgramObject shaderProgram, Texture tex, String samplerName, int ind){
        textureId = gl.glGetUniformLocation(shaderProgram.getProgramId(), samplerName);
        if(textureId < 0) return;
        
        gl.glActiveTexture(GL_TEXTURE0+ ind);
        gl.glUniform1i(textureId, ind);
        //gl.glEnable(GL_TEXTURE_2D);
        
        gl.glBindTexture(GL_TEXTURE_2D, tex.getTextureObject());
        gl.glActiveTexture(0);

        //tex.ge
    }
    
    protected void setTextureToShader(GLSLProgramObject shaderProgram){ 
        if(optLoadTexture){
           setTextureToShader(shaderProgram, texture, "myTexture", 0);
        }
    }
    
    public void display(GL4 gl, GLSLProgramObject shaderProgram, String shaderProgramName ){
        gl.glBindVertexArray(vertexArrayObject.get(0));
 
        setTextureToShader(shaderProgram);
        
        if(optBumpMapping){
            setTextureToShader(shaderProgram, bumpMappingTexture, "bumpTexture", 1);
        }
  
        render(gl, shaderProgram); 
        gl.glBindVertexArray(0);
    }
    
  
    
    public void setGlobalMatricies(GLSLProgramObject programObject, Matrix projectionMatrix, Matrix viewMatrix){


         programObject.setUniform(gl, "viewMatrix", viewMatrix);
         programObject.setUniform(gl, "projectionMatrix", projectionMatrix);
         programObject.setUniform(gl, "invProjectionMatrix", projectionMatrix.inverse());
         
         Matrix modelMatrix = getModelMatrix();
         Matrix viewModel = viewMatrix.multiply(modelMatrix);
         Matrix MVP = projectionMatrix.multiply(viewModel);
         programObject.setUniform(gl, "normalMatrix", ((modelMatrix.inverse()).transpose()));//.transpose()
         programObject.setUniform(gl, "MVP", MVP) ;  

    }
    
    protected void render(GL4 gl, GLSLProgramObject shaderProgram){
         gl.glBindVertexArray(vertexArrayObject.get(0));
         
         
         TransformationFeedback trFeedback = null;
         if(shaderProgram.isUseTransformFeedback()){
             trFeedback = shaderProgram.getTransformFeedback();
            // trFeedback.createTransFeedbackObj(16);
             trFeedback.bind(drawMode);
             gl.glEnable(GL_RASTERIZER_DISCARD);
         }
         
         if(optVertexIndexes){
            gl.glDrawElements(this.drawMode,  mesh.getIndexesCount(), gl.GL_UNSIGNED_INT, 0);
         } else{
             gl.glDrawArrays(this.drawMode, 0, mesh.getVertexesCount());
         }
         if(shaderProgram.isUseTransformFeedback()){ 
             trFeedback.unbind();
             gl.glDisable(GL_RASTERIZER_DISCARD);
             gl.glFlush();
             //shaderProgram.unbind(gl);
             // gl.glMemoryBarrier( gl.GL_BUFFER_UPDATE_BARRIER_BIT  );
             trFeedback.readData();
            
         }
         gl.glBindVertexArray(0);
         
    }
    
    public void setMaterialPropsToShader(GLSLProgramObject programObject){
       /* programObject.setUniform(gl, "material.ambient", material.getAmbient().values, 4);
        programObject.setUniform(gl, "material.diffuse", material.getAmbient().values, 4);*/
    }
    
    public void setBodyPropsToShader(GLSLProgramObject programObject){
        
        programObject.setUniform(gl, "modelMatrix", getModelMatrix());
        //setMaterialPropsToShader();
    }
   
    
    public void init(){
        try{
            initConfig();
            
            if(optHasVisual){
                buffers = IntBuffer.allocate(buffersCount);
                buffersAssociations = new HashMap<>();
                vertexArrayObject = IntBuffer.allocate(vertexArrayObjectCount);
                gl.glGenVertexArrays(vertexArrayObjectCount, vertexArrayObject);
                createMesh();
                initBuffers();
               if(optLoadTexture)loadTextures();
            }

            buildShaders();
        } catch(LoadResourseException e){
            System.err.println(e.getMessage());
        }
        
        initMatricies();
        afterInit();
    }
    
    protected void initMatricies(){
        modelMatrix = new MatrixUnit();
        //programObject.setUniform(gl, "modelMatrix", modelMatrix);
    }
    
    protected void afterInit(){
        
    }
    
    protected void createMesh() throws LoadResourseException{
        if(optLoadModelFile){
            MeshLoaderObjFormat meshLoader = new MeshLoaderObjFormat();
            mesh = meshLoader.load(modelsFilePath + modelFile);
            if(detectTextureNormalsOptByFile){
                if(!meshLoader.isFileHasNormalsCoords()){
                    this.optVertexNormals = false;
                }
                if(!meshLoader.isFileHasTextureCoords()){
                    this.optVeretexTextures = false;
                }
            }
            
            
            
        }           
    }
    
    protected Vector extractCoords(float[] coords, int elemSize, int elemNum){
        Vector v = new Vector(elemSize);
        int S = elemNum * elemSize;
        int k = 0;
        for(int i = S; i < S + elemSize; i++, k++){
            v.values[k] = coords[i];
        }
        return v;
    }
    
    protected Vector3  countTanOrBitan(float[] textureCoords,  Matrix33 A, int k, int offset){
           float u0 = textureCoords[k * 6 + offset];
           float u1 = textureCoords[k * 6 + 2 + offset];
           float u2 = textureCoords[k * 6 + 4 + offset];
           Vector3 b = new Vector3(u1 - u0, u2 - u0, 0);
           
           Vector3 tanget = A.inverse().multiply(b);
           return tanget;
    }
    
    public float[] countTangents(){
       
        float[] vertexesCoords = mesh.getVertexesCoords();
        float[] textureCoords = mesh.getTextureCoords();
        float[] normalCoords = mesh.getNormalsCoords();
        
         int triangleCount = vertexesCoords.length / 12;
        //float[] res = new float[vertexesCoords.length];
        
        VertexAggregator resAggregator = new VertexAggregator( vertexesCoords.length / 4, 3);
        
        for(int k = 0; k < triangleCount ; k++){
            
            int r = k * 3*4; 
            int r0ind = r; 
            int r1ind = r + 4; 
            int r2ind = r1ind + 4; 
            
            Matrix33 A = new Matrix33();
            //Проход по вершинам
            for(int j = 0; j < 3; j++){
                A.values[0][j] = vertexesCoords[r1ind + j] - vertexesCoords[r0ind + j];
                A.values[1][j] = vertexesCoords[r2ind + j] - vertexesCoords[r0ind + j];
            }
             /*Vector3 tanget = new Vector3();
            for(int j = 0; j < 3; j++){
                tanget.values[j] = vertexesCoords[r0ind + j] - vertexesCoords[r1ind + j];
            }*/
            //Считаем нормаль
          /* Vector3 v1 = A.getRow(0).toVector3();
           Vector3 v2 = A.getRow(1).toVector3();*/
         
           Vector3 N = extractCoords(normalCoords, 3, k*3).toVector3();
           A.setRow(N, 2); 
           
           
          Vector3 tanget =  countTanOrBitan(textureCoords, A, k, 0);
          
           //Касательный базис одинаков во всём треугольнике, поэтому просто повторяем его в каждой вершине
           resAggregator.addVertex(tanget);
           resAggregator.addVertex(tanget);
           resAggregator.addVertex(tanget);
           // M.values[1][j]
        }
        
        float[] res = resAggregator.getVertexCoords();
        return res;
    }
    
    protected void loadTextures() throws LoadResourseException{

        texture = loadTexture(modelsFilePath + getTextureFile());
        if(optBumpMapping){
            bumpMappingTexture = loadTexture(modelsFilePath + getBumpMappingTextureFile());
        }
    }
    
    protected Texture loadTexture(String fileName) throws LoadResourseException{
        String[] parts = fileName.split("\\.");
        String ext = parts[parts.length - 1].toUpperCase();
        return initializeTexture(fileName, ext);
    }
    
    protected void initConfig(){
        //TODO: init model vertexes count e.t.c
       
    }
           
    protected void initBuffer(float[] coords, int bufferType) {
       initBuffer(coords, bufferType,  GL4.GL_ARRAY_BUFFER);
    }
    
    protected void initBuffer(float[] coords, int bufferType, int glBufferType, IntBuffer bufferStore) {
        gl.glBindBuffer(glBufferType, bufferStore.get(bufferType));
        FloatBuffer buffer = GLBuffers.newDirectFloatBuffer(coords);
        gl.glBufferData(glBufferType, coords.length * 4, buffer, GL4.GL_STATIC_DRAW);
        gl.glBindBuffer(glBufferType, 0);
    }
    protected void initBuffer(int[] coords, int bufferType, int glBufferType, IntBuffer bufferStore) {
        gl.glBindBuffer(glBufferType, bufferStore.get(bufferType));
        IntBuffer buffer = GLBuffers.newDirectIntBuffer(coords);
        gl.glBufferData(glBufferType, coords.length * 4, buffer, GL4.GL_STATIC_DRAW);
        gl.glBindBuffer(glBufferType, 0);
    }
    protected void initBuffer(float[] coords, int bufferType, int glBufferType) {
        initBuffer(coords,  bufferType,  glBufferType, buffers);
    }
    
    protected void initVertexes(){
        initBuffer(mesh.getVertexesCoords(), BUFFER_VERTEX);
    }
    
    protected void initBuffers(){
       
        gl.glBindVertexArray(vertexArrayObject.get(0));
        gl.glGenBuffers(buffersCount, buffers);
        
        initVertexes();
        if(optVertexIndexes) initBuffer(mesh.getVertexesIndexes(), BUFFER_VERTEX_INDEXES, GL4.GL_ARRAY_BUFFER, buffers);
        if(optVeretexTextures) initBuffer( mesh.getTextureCoords(), BUFFER_TEXTURE_COORDS );
        if(optVertexNormals) initBuffer(mesh.getNormalsCoords(), BUFFER_NORMALS_COORDS);
        if(optHasColors){
           initBuffer(mesh.getVertexesColors(), BUFFER_VETREX_COLORS); 
        } 
        if(optBumpMapping) {
            initBuffer( this.countTangents(), BUFFER_TANGET_COORDS);
        }
        initCustomBuffers();
        //BUFFER_NORMALS_COORDS
        setupVBA();
    }
    protected void initCustomBuffers(){
        
    }
    
    protected void setupVBaCustomBuffers(){
        
    }
    protected int getBufferId(int part){
        if(buffersAssociations.containsKey(part)){
            return buffersAssociations.get(part);
        } else {
            buffersAssociations.put(part, curBufNum);
            return curBufNum++;
        }
        
    }
    
    protected void bindBufferToAttr(int vertAttr, int bufferId, int attrDim){
        gl.glEnableVertexAttribArray(vertAttr);
        gl.glBindBuffer(gl.GL_ARRAY_BUFFER, buffers.get(bufferId));
        // Второй параметр - размер буффера, для текстур 2, например
        gl.glVertexAttribPointer(vertAttr, attrDim, GL4.GL_FLOAT, false, 0, 0);
        gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, 0);
    }
    
    protected void setupVBA(){
        
        gl.glBindVertexArray(vertexArrayObject.get(0));
        
        bindBufferToAttr(0, BUFFER_VERTEX, mesh.getVertexCoordsNum());
        
 
        if(optVeretexTextures){
            bindBufferToAttr(1, BUFFER_TEXTURE_COORDS, mesh.getTextureCoordsNum());
        }
       
        if(optVertexNormals){
            bindBufferToAttr(2, BUFFER_NORMALS_COORDS, mesh.getNormalCoordsNum());
        }
        
        if(optHasColors){

            bindBufferToAttr(3, BUFFER_VETREX_COLORS, mesh.getColorCoordsNum());
        }
         
        if(optBumpMapping){            
            bindBufferToAttr(4, BUFFER_TANGET_COORDS, mesh.getNormalCoordsNum());
        }        
        
        if(optVertexIndexes){
           gl.glBindBuffer(gl.GL_ELEMENT_ARRAY_BUFFER, buffers.get(BUFFER_VERTEX_INDEXES));
        }
        setupVBaCustomBuffers();
        
        gl.glBindVertexArray(0);        

    }
    

 
    protected void buildShaders() throws LoadResourseException{
       ShadersStore shadersStrore =  ShadersStore.getInstance();
       ShaderProgOptions options = new ShaderProgOptions();
       
       options.setUseGeometryShader(optLoadGeomShader);
       options.setUseTransformationFeedback(optUseTransformFeedback);
       if(optUseTransformFeedback){
           options.setFeedbackVaryings(feedbackVayrings);
       }
       
       
       GLSLProgramObject prog = shadersStrore.getShaderProgram(shaderProgName, options);
       this.shadersPrograms.put(shaderProgName, prog);
    }
    
    public void addShaderProgram( String progName, ShaderProgOptions options) throws LoadResourseException{
       ShadersStore shadersStrore =  ShadersStore.getInstance();
       
       GLSLProgramObject prog = shadersStrore.getShaderProgram(progName, options);
       this.shadersPrograms.put(progName, prog);
    }
    
    private Texture initializeTexture(String textureFile, String fileType) throws LoadResourseException{

        Texture t = null;

        try {
            t = TextureIO.newTexture(this.getClass().getResource( textureFile), false, fileType); 

            
            t.setTexParameteri(gl, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_LINEAR);
            t.setTexParameteri(gl, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_LINEAR);
            t.setTexParameteri(gl, GL4.GL_TEXTURE_WRAP_S, GL4.GL_CLAMP_TO_EDGE); //GL4.
            t.setTexParameteri(gl, GL4.GL_TEXTURE_WRAP_T, GL4.GL_CLAMP_TO_EDGE);

        } catch (IOException | GLException ex) {
            throw new LoadResourseException("Can't load texture:"  + textureFile);
            
        }
       
        return t;
    }

    /**
     * @return the modelMatrix
     */
    public Matrix getModelMatrix() {
        return modelMatrix;
    }

    /**
     * @param modelMatrix the modelMatrix to set
     */
    public void setModelMatrix(Matrix44 modelMatrix) {
        this.modelMatrix = modelMatrix;
    }
    
    public void clearModelMatrix(){
        this.modelMatrix = new MatrixUnit();
    }
    
    public void bodyRotateX(float anlge){
        modelMatrix =  modelMatrix.multiply(new MatrixRotationX(anlge));
    }
     
    public void bodyRotateY(float anlge){
        //modelMatrix = new MatrixUnit();
        modelMatrix =  modelMatrix.multiply(new MatrixRotationY(anlge));
    }
    
     public void bodyRotateZ(float anlge){
        modelMatrix =  modelMatrix.multiply(new MatrixRotationZ(anlge));
    }
   
    
    public void bodyTranlate(Vector3 deltaVec){
       
        modelMatrix =  modelMatrix.multiply(new MatrixTranslation(deltaVec));
    }
    
    public void bodyScale(float koof){
        Vector3 vec = new Vector3(koof, koof, koof);
        
        modelMatrix =  modelMatrix.multiply(new MatrixScale(vec));
    }
     
    public void bodyScale(Vector3 scalesVec){
        objScaleVec = scalesVec;
    }
    
    public void incRotateAngleY(float anlge){
        modelMatrix =  modelMatrix.multiply(new MatrixRotationY(anlge));
    }
      


    /**
     * @return the optHasVisual
     */
    public boolean isOptHasVisual() {
        return optHasVisual;
    }

    /**
     * @param optHasVisual the optHasVisual to set
     */
    public void setOptHasVisual(boolean optHasVisual) {
        this.optHasVisual = optHasVisual;
    }

    /**
     * @return the optIsShaderProgSpecial
     */
    public boolean isOptIsShaderProgSpecial() {
        return optIsShaderProgSpecial;
    }

    /**
     * @param optIsShaderProgSpecial the optIsShaderProgSpecial to set
     */
    public void setOptIsShaderProgSpecial(boolean optIsShaderProgSpecial) {
        this.optIsShaderProgSpecial = optIsShaderProgSpecial;
    }

   

    /**
     * @return the vertexesColors
     */
    public Vector[] getVertexesColors() {
        return vertexesColors;
    }

    /**
     * @param vertexesColors the vertexesColors to set
     */
    public void setVertexesColors(Vector[] vertexesColors) {
        this.vertexesColors = vertexesColors;
    }

    /**
     * @return the textureFile
     */
    public String getTextureFile() {
        return textureFile;
    }

    /**
     * @param textureFile the textureFile to set
     */
    public void setTextureFile(String textureFile) {
        this.textureFile = textureFile;
    }

    /**
     * @return the modelFile
     */
    public String getModelFile() {
        return modelFile;
    }

    /**
     * @param modelFile the modelFile to set
     */
    public void setModelFile(String modelFile) {
        this.modelFile = modelFile;
        optLoadModelFile = true;
    }

    /**
     * @return the objScaleVec
     */
    public Vector getObjScaleVec() {
        return objScaleVec;
    }

    /**
     * @param objScaleVec the objScaleVec to set
     */
    public void setObjScale(Vector3 objScaleVec) {
        this.objScaleVec = objScaleVec;
    }
    
    public void setObjScale(float k) {
        setObjScale(new Vector3(k, k, k));
    }
    /**
     * @return the objRotVec
     */
    public Vector getObjRotVec() {
        return objRotVec;
    }

    /**
     * @param objRotVec the objRotVec to set
     */
    public void setObjRotVec(Vector3 objRotVec) {
        this.objRotVec = objRotVec;
    }

    /**
     * @return the objTranslateVec
     */
    public Vector getObjTranslateVec() {
        return objTranslateVec;
    }

    /**
     * @param objTranslateVec the objTranslateVec to set
     */
    public void setObjTranslateVec(Vector3 objTranslateVec) {
        this.objTranslateVec = objTranslateVec;
    }
    
    public void incObjTranslateVec(Vector3 deltaVec){
       objTranslateVec = objTranslateVec.plus(deltaVec);
    }
    
    public void incObjRotVec(Vector deltaVec){
       objRotVec = objRotVec.plus(deltaVec);
    }
    
    public void incObjScaleVec(Vector3 deltaVec){
       objScaleVec = objScaleVec.plus(deltaVec);
    }
    
    protected Matrix calcModelMatrix(){
        Matrix M = new Matrix44();
        
       /* Matrix R = Vector.getFullRotationMatrix(objRotVec);
        
        MatrixChainOperations matrixChain = new MatrixChainOperations();
        matrixChain.add(new MatrixTranslation(objTranslateVec));
        matrixChain.add(R);
        matrixChain.add(new MatrixScale(objScaleVec.toVector3()));
        
        return matrixChain.getResult();
        */
        return new  MatrixUnit();
        //matrixChain.add(new MatrixTranslation(objTranslateVec));
        
    }

    /**
     * @return the detectTextureNormalsOptByFile
     */
    public boolean isDetectTextureNormalsOptByFile() {
        return detectTextureNormalsOptByFile;
    }

    /**
     * @param detectTextureNormalsOptByFile the detectTextureNormalsOptByFile to set
     */
    public void setDetectTextureNormalsOptByFile(boolean detectTextureNormalsOptByFile) {
        this.detectTextureNormalsOptByFile = detectTextureNormalsOptByFile;
    }

    /**
     * @return the optLoadGeomShader
     */
    public boolean isOptLoadGeomShader() {
        return optLoadGeomShader;
    }

    /**
     * @param optLoadGeomShader the optLoadGeomShader to set
     */
    public void setOptLoadGeomShader(boolean optLoadGeomShader) {
        this.optLoadGeomShader = optLoadGeomShader;
    }

  


    /**
     * @return the shadersPrograms
     */
    public LinkedHashMap<String, GLSLProgramObject> getShadersPrograms() {
        return shadersPrograms;
    }
}
