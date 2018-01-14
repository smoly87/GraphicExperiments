/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package engine.softRenderer;

import com.jogamp.opengl.util.texture.TextureCoords;
import engine.exception.LoadResourseException;
import engine.meshLoader.MeshLoaderObjFormat;
import engine.mesh.Mesh;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
import math.linearAlgebra.Matrix;
import math.linearAlgebra.Vector;
import math.linearAlgebra.Vector3;
import math.sceneCalculations.SceneCalculations;
import utils.ArrayUtils;
import utils.IoReader;

/**
 *
 * @author Andrey
 */
public class SoftRenderer {
    protected Graphics canvas;
    protected int width;
    protected int height;
    protected float[] vertexCoords;
    protected float[] textureCoords;
    
    protected int textureCoordsNum;
    protected int vertCoordsNum;
    protected int IndOffset = 1;
    
    protected Matrix viewPortMatrix;
    protected Matrix projectionMatrix;
    protected Matrix viewMatrix;
    protected Matrix modelMatrix;
    
    protected Matrix FullTransformMatr;
    BufferedImage img ;
    BufferedImage imgTexture;
    
    protected String assetsFilepath = "/graphicsexperiment/assets/";
    protected String shadersFilePath = assetsFilepath + "shaders/"; 
    protected String modelsFilePath = assetsFilepath + "models/"; 
    
    
    protected boolean useTexture;
    Random rnd ;
    
    protected  float [] zBuffer;
    protected int[] imgData;
    
    public SoftRenderer(Graphics canvas, int width, int height){
        this.canvas = canvas;
        this.width = width;
        this.height = height;
        initMatricies();
        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
       
        rnd = new Random();
    }
    
    protected void initMatricies(){
        FullTransformMatr = SceneCalculations.calcViewPortMatrix(width - 1, height - 1);
    }
    
    
    protected Vector getVertexByNum(int vertexInd){
        int offset = vertexInd * vertCoordsNum; 
        Vector vecRes = new Vector(vertCoordsNum);
        for(int i = 0; i < vertCoordsNum; i++){
            vecRes.values[i] = vertexCoords[offset + i];
        }
        return vecRes;  
    }
    
    protected Vector getTextureCoordsByNum(int vertexInd){
        if(!useTexture){
            return new Vector(2);
        }
        int offset = vertexInd * textureCoordsNum; 
        Vector vecRes = new Vector(textureCoordsNum);
        for(int i = 0; i < textureCoordsNum; i++){
            vecRes.values[i] = textureCoords[offset + i];
        }
        return vecRes;  
    }
    
    protected Vector boundBox(Vector[] vectors){
        float minX = vectors[0].values[0];
        float minY = vectors[0].values[1];
        
        float maxX = vectors[0].values[0];
        float maxY = vectors[0].values[1];
        
        for(int i = 0; i < vectors.length; i++){
            if(vectors[i].values[0] < minX) minX = vectors[i].values[0];
            if(vectors[i].values[1] < minY) minY = vectors[i].values[1];
            if(vectors[i].values[0] > maxX) maxX = vectors[i].values[0];
            if(vectors[i].values[1] > maxY) maxY = vectors[i].values[1];
        }
        
        return new Vector(new float[]{minX, minY, maxX, maxY});
    }
    
    
    protected void getBaracentricCoords(){
    }
    
    protected boolean isInTriangle(Vector baracentricCoords){
        //Как max delta относительно счиать
        float maxDelta = 0.01f;
        for(int i = 0 ; i < 3; i++){
            if(baracentricCoords.values[i] < 0 && Math.abs(baracentricCoords.values[i]) > maxDelta){
                return false;
            }
        }
        return true;
    }
    
    protected Vector findBaracentricCoords(Vector AB, Vector AC, Vector PA){
        //VEctors for solving system
        Vector uv1 = new Vector(new float[]{AB.values[0], AC.values[0], PA.values[0]});
        Vector uv2 = new Vector(new float[]{AB.values[1], AC.values[1], PA.values[1]});
        Vector uv = Vector3.cross(uv1, uv2);
        //uv = uv.normalise();
        

        /*if (Math.abs(uv.values[2] ) > 0.01f) {
            return new Vector(new float[]{-1.0f, -1.0f, -1.0f});
        }*/
        
        
        uv.values[0] = uv.values[0] / uv.values[2];
        uv.values[1] = uv.values[1] / uv.values[2];
        
      
        
        return new Vector(new float[]{ 1.0f - (uv.values[0] + uv.values[1]) , uv.values[0], uv.values[1]});
    }
    
    protected Vector findBaracentricCoordKramer(Vector AB, Vector AC, Vector PA){
        // В правой части с минусом! Ax + b = 0 
        Vector B = PA.multiply(-1.0f);
        Matrix M = new Matrix(2, 2);
        M.setColumn(AB, 0);
        M.setColumn(AC, 1);
        float D = M.det();
        if(Math.abs(D) <= 0.000001){
            return new Vector(new float[]{-1.0f, -1.0f, -1.0f});
        }
        //Find determinant for first var
        M.setColumn(B, 0);
        float D1 = M.det();
        //Find determinant for second var
        M.setColumn(AB, 0);
        M.setColumn(B, 1);
        float D2 = M.det();
        
        float u = D1 / D;
        float v = D2 / D;
        
        return new Vector(new float[]{(1 - u - v), u, v });
       
        
    }
    
    protected void renderTriangle(Vertex v1, Vertex v2, Vertex v3){
        
        Vector p1 = v1.getScreenCoords();
        Vector p2 = v2.getScreenCoords();
        Vector p3 = v3.getScreenCoords();
        
        Color curColor = new Color(rnd.nextInt(254), rnd.nextInt(254), rnd.nextInt(254));
        int color = curColor.getRGB();

       
        Vector AB = p2.minus(p1);
        Vector AC = p3.minus(p1);
        
        
        Vector TV1 = v1.getTextureCoords();
        Vector TV2 = v2.getTextureCoords();
        Vector TV3 = v3.getTextureCoords();
        
        /*Vector TAB = TV2.minus(TV1);
        Vector TAC = TV3.minus(TV1);*/
        
        Vector boundBox = this.boundBox(new Vector[]{p1, p2, p3});
        int minX = (int)boundBox.values[0];
        int minY = (int)boundBox.values[1];
        int maxX = (int)boundBox.values[2];
        int maxY = (int)boundBox.values[3];
        
        
        
        for(int x = minX; x <= maxX; x++ ){
           for(int y = minY; y <= maxY; y++ ){
               Vector P = new Vector(new float[]{x , y});
               Vector PA = p1.minus(P);
               Vector uv = findBaracentricCoords(AB, AC, PA);
               //Point not in triangle
               if(!isInTriangle(uv)) continue;
               
               //Интерполируем z-координату
               Vector vertZ = new Vector(new float[]{v1.getCoords().getValues()[2],v2.getCoords().getValues()[2], v3.getCoords().getValues()[2] }); 
               float Pz =  vertZ.dot(vertZ, uv);
               if(Pz > zBuffer[y * width + x]){
                   zBuffer[y * width + x] = Pz;
               } else{
                   continue;
               }
               
               
               Vector vi1 = TV1.multiply( uv.values[0]);
               Vector vi2 = TV2.multiply(uv.values[1]);
               Vector vi3 = TV3.multiply(uv.values[2]);
               
               Vector textureColVec = vi1.plus(vi2).plus(vi3);
               //Убираем небольшие отрицательные числа - погрешность вычислений!
               textureColVec.values[0] = Math.abs(textureColVec.values[0]);
               textureColVec.values[1] = Math.abs(textureColVec.values[1]);
               
               int tX = (int) (textureColVec.values[0] * (imgTexture.getWidth()));
               int tY = (int) (textureColVec.values[1]  * (imgTexture.getHeight() ));
             // System.out.println( "|" + textureColVec );
               //System.out.println(tX +";" + tY);
               if(tX > imgTexture.getWidth()  - 1) tX = imgTexture.getWidth() - 1;
               if(tY > imgTexture.getHeight()  - 1) tY = imgTexture.getHeight()- 1;
               
               
               
               try{
                  color = imgTexture.getRGB(tX, tY);
               } catch(Exception e){
                   System.out.println(tX + ";" + tY  + "|" + textureColVec );
                   
               }
               
               
               //
               
               //img.setRGB(x, y, color);
               imgData[y * width + x] = color;
               
           } 
        }
    }
    
    protected int getTextureColor(){
        int res = 0;
        return res;
    }
    
    protected Vector toScreenCoords(Vector p){
        return FullTransformMatr.multiply(p);
    }
    
    protected Vertex createVertexByIndex(int vertexNum){
        Vertex v = new Vertex();
        Vector coords = getVertexByNum(vertexNum);
        v.setCoords(coords);
        v.setScreenCoords(toScreenCoords(coords));
        Vector texCoords = getTextureCoordsByNum(vertexNum);
        v.setTextureCoords(texCoords);
        return v;
        
    }
    
    // For correction direction of y axis. Principial not in matrix of projection!
    protected BufferedImage flipImageVert(BufferedImage img){
          AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
          tx.translate(0, -img.getHeight(null));
          AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
          return op.filter(img, null);
    }
    
    public BufferedImage render(){
               
        imgData = new int[width * height];
        zBuffer = new float[width * height];
        Arrays.fill(zBuffer, -1000.0f); 
        MeshLoaderObjFormat meshLoader = new MeshLoaderObjFormat();
        Mesh mesh = null;
        try {
            mesh = meshLoader.load(modelsFilePath + "african_head.obj");
            InputStream inStream = IoReader.class.getResourceAsStream(modelsFilePath + "african_head_diffuse.jpg");
            imgTexture = ImageIO.read(inStream);
           
            
            imgTexture = flipImageVert(imgTexture);
            
         } catch (Exception e) {
             System.err.println("Error in load resources for soft renderer: " + e.getMessage());
         }
        
        //System.out.println("color of :" + imgTexture.getRGB(imgTexture.getHeight() - 2, imgTexture.getWidth() -2 ) );
        
        int vertCount = mesh.getVertexesCount();
        vertCoordsNum = mesh.getVertexCoordsNum();
        textureCoordsNum = mesh.getTextureCoordsNum();
        vertexCoords = mesh.getVertexesCoords();
        
        useTexture = meshLoader.isFileHasTextureCoords();
        if(useTexture){
            textureCoords = mesh.getTextureCoords();
        }
        
        
        
        
        int[] vertIndexes = mesh.getVertexesIndexes();
        
        //Цикл по треугольникам - Каждые три вершины - это треугольник
        int trianglesCount = vertCount / (3 * vertCoordsNum);
        for(int k = 0; k < trianglesCount; k++){
            int kOffset = k * 3;
            renderTriangle(
                    createVertexByIndex(kOffset), 
                    createVertexByIndex(kOffset + 1), 
                    createVertexByIndex(kOffset + 2)
            );
        }
        //System.out.println("Render finished");
        
        img.setRGB(0, 0, width, height, imgData, 0, width);
        
        return flipImageVert(img);
    }
    
     
}
