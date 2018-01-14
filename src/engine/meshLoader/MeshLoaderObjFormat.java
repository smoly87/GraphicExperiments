/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package engine.meshLoader;

import engine.exception.LoadResourseException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;
import math.linearAlgebra.Vector;
import utils.ArrayUtils;
import utils.IoReader;
import engine.mesh.Mesh;
import java.util.HashMap;
import math.linearAlgebra.Matrix33;
import math.linearAlgebra.Matrix44;
import math.linearAlgebra.Vector3;
/**
 *
 * @author Andrey
 */
public class MeshLoaderObjFormat extends BaseMeshLoader{
    protected ArrayList<Vector> textureCoordsIndexes;
    protected ArrayList<Vector> normalСoordsIndexes;  
    protected ArrayList<Vector> vertexList;
    
    protected float[] textureCoordsDirect;
    
    
    protected float[] normalCoordsDirect;
    
    protected LinkedList<Float> vertexesCoords;
    protected LinkedList<Integer> vertexesIndexes;
    protected LinkedList<Float> textureCoords;
    protected LinkedList<Float> normalCoords;
    
    protected boolean facesProcessMode = false;   
    protected boolean[] vertexesProcessed;
    
    
    protected int curTriangleNum = 0;
    // Список, куда входит данная вершина
    protected HashMap<Integer, LinkedList<Integer>> vertexTriangles ; 
    
    
    
    public MeshLoaderObjFormat() {
        super();
        vertexesCoords = new LinkedList<>();
        vertexesIndexes = new LinkedList<>();
        textureCoords = new LinkedList<>();
        normalCoords = new LinkedList<>();
        textureCoordsIndexes = new ArrayList<Vector>();
        normalСoordsIndexes = new ArrayList<Vector>();
        vertexList = new ArrayList<Vector>();
        vertexTriangles = new HashMap<>();
    }
    
    
  
    protected void processTriangle(){
        
    }
    
    public ArrayList getVertexList(){
        return vertexList;
    }
           
    @Override
    public float[] getTextureCoords() {
        return ArrayUtils.linkedListToFloatArray(textureCoords) ;
    }
    
    @Override
    public float[] getNormalsCoords() {
       return ArrayUtils.linkedListToFloatArray(normalCoords) ;
    }
    @Override
    public  float[] getVertexesCoords(){
        float[]  res = ArrayUtils.linkedListToFloatArray(vertexesCoords);
        return res;
    }
    
    @Override
    public  int[] getVertexesIndexes(){
        return ArrayUtils.linkedListToIntArray(vertexesIndexes);
    }
    
     protected void processFile(String fileName) throws LoadResourseException{
        InputStream inStream;
        try {
             inStream = IoReader.class.getResourceAsStream(fileName);
        } catch(Exception e){
            throw new LoadResourseException("Can't load model .obj file: " + fileName);
        }
        
        Scanner scanner = new Scanner(inStream);
        
        try {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if(line.length() == 0) continue;
                this.processLine(line);
            }
        } finally {
            scanner.close();
        }
    }
     
    @Override    
    public  Mesh load(String fileName) throws LoadResourseException{
        curTriangleNum = 0;
        try{
            processFile(fileName);
        } catch(LoadResourseException error){
            throw new LoadResourseException("Error while load .obj file " + fileName + " : " + error.getMessage());
        }
        Mesh resMesh = new Mesh();
        
        resMesh.setVertexesCount(this.getVertexesCount());
        resMesh.setIndexesCount(indexesCount);
        
        resMesh.setVertexesCoords(getVertexesCoords());
        resMesh.setVertexesIndexes(getVertexesIndexes());
        resMesh.setTextureCoords(getTextureCoords());
        resMesh.setNormalsCoords(getNormalsCoords());
         
        return resMesh;
    }
    
 
      
    protected boolean processLine(String expr){
        if(expr.indexOf("#") > -1){
            expr = expr.substring(0, expr.indexOf("#"));
            System.out.println(expr);
        }
        expr = expr.trim();
        expr = expr.replace("  ", " ");
        
        String[] lineParts = expr.split(" ");
        switch(lineParts[0]){
            case "v":
                int coordsCount = lineParts.length - 1;
                int coordsCountParsed = coordsCount;
                //Размерность вектора координат x, y, z, w Обычно
                Vector vertex = new Vector(vertexCoordsNum);
                int vI = 0;
                for(int i = 1; i<=coordsCount; i++){
                    try {
                        if (lineParts[i].length() > 0) {
                             float curVal = Float.valueOf(lineParts[i]);
                             //vertexesCoords.add(curVal);
                             vertex.values[vI] = curVal;
                             vI++;
                        } else {
                            coordsCountParsed--;
                        }
                    } catch (NumberFormatException e){
                        coordsCountParsed--;
                    }
                    
                }
                if (coordsCountParsed < 4){
                    //vertexesCoords.add(new Float(1.0));
                    vertex.values[3] = 1.0f;
                }
                vertexList.add(vertex);
                vertexesCount++;
                break;
            case "f":
                
                if(!facesProcessMode){
                    textureCoordsDirect = new float[vertexesCount  * textureCoordsNum];
                    facesProcessMode = true;
                    vertexesProcessed = new boolean[vertexesCount];
                    
                    normalCoordsDirect = new float[vertexesCount  * normalCoordsNum];
                }
                
               
               
                for(int i = 1; i< lineParts.length; i++){
                    //Индекс вершины
                  
                    String[] indexesExpr = lineParts[i].split("/");
                    int vertexNum = Integer.valueOf(indexesExpr[0] ) - indexOffeset;
                   
                    //vertexesIndexes.add(vertexNum);
                    
                    Vector curVertex = vertexList.get(vertexNum);
                    for(int k = 0; k < vertexCoordsNum; k++){
                        vertexesCoords.add(curVertex.values[k]);
                    }
                    
                    
                    if (indexesExpr.length > 1) {
                        int textureCoordIndex = Integer.valueOf(indexesExpr[1]) - indexOffeset;
                        Vector textureVec = textureCoordsIndexes.get(textureCoordIndex);

                        for (int k = 0; k < textureCoordsNum; k++) {
                            textureCoords.add(textureVec.values[k]);
                        }
                        
                    } else{
                        fileHasTextureCoords = false;
                    }

                    if (indexesExpr.length > 2) {
                        int normCoordIndex = Integer.valueOf(indexesExpr[2]) - indexOffeset;
                        Vector normVec = normalСoordsIndexes.get(normCoordIndex);

                        for (int k = 0; k < normalCoordsNum; k++) {
                            normalCoords.add(normVec.values[k]);
                        }
                        
                    } else{
                        fileHasNormalsCoords = false;
                    }
                    
                    
                  /* if (vertexesProcessed[vertexNum]) continue;
                    vertexesProcessed[vertexNum] = true;*/
                    
                    
                   /* if (indexesExpr.length > 1 && indexesExpr[1] !="" &&  indexesExpr[1] !=" ") {
                        //Индекс текстурных координат
                        int textureCoordIndex = Integer.valueOf(indexesExpr[1]) - indexOffeset;
                        
                        indexBlockToDirectNumSet(vertexNum, textureCoordIndex,
                                textureCoordsIndexes, textureCoordsDirect, textureCoordsNum
                        );
                    } else {
                        fileHasTextureCoords = false;
                    }
                    
                    if (indexesExpr.length > 2 && indexesExpr[2] !="" && indexesExpr[2] !=" ") {

                        //Индекс нормалей
                        int normalCoordIndex = Integer.valueOf(indexesExpr[2]) - indexOffeset;
                        indexBlockToDirectNumSet(vertexNum, normalCoordIndex,
                                normalСoordsIndexes, normalCoordsDirect, normalCoordsNum
                        );
                    } else{
                        fileHasNormalsCoords = false;
                    }*/
    
                }
                break;
                
            case "vt":
                Vector textureСoordsVector = processBlockCoords (lineParts, textureCoordsNum);
                //System.out.println(textureСoordsVector.getSize());
                textureCoordsIndexes.add(textureСoordsVector);
                break;
                
            case "vn":
                Vector normalСoordsVector = processBlockCoords (lineParts, normalCoordsNum);
                normalСoordsIndexes.add(normalСoordsVector);
                break;
                
        }
        return true;
    }

    @Override
    public int getVertexesCount() {
        return vertexesCoords.size() ;//To change body of generated methods, choose Tools | Templates.
    }
    
    
    protected void indexBlockToDirectNumSet(int vertexNum, int index, ArrayList<Vector> indexStorage,  float[] blockDirectArray, int blockCoordsNum){
        int blockCoordsOffset = vertexNum * blockCoordsNum ;
        Vector valueVector =  indexStorage.get(index);
         //Put coords by indexes
        for(int j = 0; j < blockCoordsNum; j++){
            blockDirectArray[blockCoordsOffset + j] = valueVector.values[j];
        }
    }
    
    
    protected Vector processBlockCoords(String[] lineParts, int blockCoordsNum){
        int coordCount = 0;
        Vector resСoordsVector = new Vector(blockCoordsNum);
        //До размерности вектора текстурных координат
        int ind = 0;
        int i = 1;
        while ((ind < blockCoordsNum) && (i < lineParts.length)) {
            String curTexCoord = lineParts[i];
            if (curTexCoord.length() == 0) {
                i++;
                continue;
            }

            boolean hasErrorFlag = false;
            try {
                resСoordsVector.values[ind] = Float.valueOf(lineParts[i]);
            } catch (NumberFormatException e) {
                hasErrorFlag = true;
            }

            i++;
            if (hasErrorFlag) {
                //System.out.println("Er:" + lineParts[i]);
                continue;
            }

            ind++;
        }
        return resСoordsVector;
    }
    
}
