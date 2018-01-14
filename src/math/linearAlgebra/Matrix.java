/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package math.linearAlgebra;

import com.jogamp.opengl.math.FloatUtil;
import com.jogamp.opengl.math.Matrix4;
import java.util.BitSet;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
/*import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;*/

/**
 *
 * @author Andrey
 */
public class Matrix {
    public float[][] values;
    protected int algI = -1, algJ = -1;
    public Matrix()
    {
        
    }    
    /**
     * 
     * @param vector
     * @param isColumn define matrix type matrix - vector or matrix column
     */
    public Matrix(Vector vector, boolean isColumn){
        int N = vector.getSize();
        if(isColumn){
            values = new float[N][1];
            for(int i = 0; i < N; i++){
                values[i][0] = vector.values[i]; 
            }
        } else {
            values = new float[N][vector.getSize()];
            for(int i = 0; i < N; i++){
                values[0][i] = vector.values[i]; 
            }
        }
    }
    
    public Matrix(Vector[] vectors, boolean isColumn){
        int N = vectors[0].getSize();
        values = new float[N][N];
        if(isColumn){
            for(int k = 0; k < N; k++){
                for(int i = 0; i < N; i++){
                    values[i][k] = vectors[k].values[i];
                }
            }
        }else{
            for (int k = 0; k < N; k++) {
                System.arraycopy(vectors[k].values, 0, values[k], 0, N);
            }
        }
        
    }
    
    public Matrix(float[][] values){
        this.values = values;
    }
    
    public Matrix(int rowsCount, int columnsCount){
        this.values = new float[rowsCount][columnsCount];
    }
    
    public float[] valuesToBuffer(){
        float[] res = new float[getRowCount() * getColCount()];
        int k = 0;
        for(int i = 0; i < getRowCount(); i++){
            for(int j = 0; j < getColCount(); j++){
                res[k] = values[j][i];
                k++;
            }
        }
        
        return res;
    }
    
       
    public Vector getColumn(int colInd){
        int rowCount = this.getRowCount();
        Vector vec = new Vector(rowCount);
        for(int i = 0; i < rowCount; i++){
           vec.values[i] = values[i][colInd];
        }
        return vec;
    }
    
    public Vector getRow(int rowInd){
        int colCount = this.getRowCount();
        Vector vec = new Vector(colCount);
        for(int j = 0; j < colCount; j++){
           vec.values[j] = values[rowInd][j];
        }
        return vec;
    }
    
    public void setColumn(Vector colVec, int colInd){
        int rowCount = Math.min(this.getRowCount(), colVec.getSize()) ;
        for(int i = 0; i < rowCount; i++){
            values[i][colInd] = colVec.values[i];
        }
    }
    
     public void setRow(Vector rowVec, int rowInd){
        int colCount = Math.min(this.getRowCount(), rowVec.getSize()) ;
        for(int j = 0; j < colCount; j++){
            values[rowInd][j] = rowVec.values[j];
        }
    }
    /**
     * Multiply only on vector-column
     * @param p
     * @return 
     */ 
    public Vector multiply(Vector p){
       Matrix pW = new Matrix(p, true);
       return multiply(pW).getColumn(0);
    } 
    
    public Vector3 multiply(Vector3 p){
       return  multiply(p.toVector4()).toVector3();
    }
    
    public  Matrix multiply(Matrix B){
        //Test on size
        int rowCount = this.getRowCount();
        int colCount = B.getColCount();
        Matrix C = new Matrix(rowCount, colCount);
        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < colCount; j++){
                //Inner cycle with summator row * column -concreate column summ
                float summ = 0;
                for(int k = 0; k < rowCount; k++){
                    summ += this.values[i][k] * B.values[k][j]; // По k индексы сократились
                }
                C.values[i][j] = summ;
            }
            
        } 
        return C;
    }

    public  Matrix plus(Matrix B){
        //Test on size
      
        int rowCount = this.getRowCount();
        int colCount = B.getColCount();
        Matrix C = new Matrix(rowCount, colCount);
        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < colCount; j++){
               C.values[i][j] = this.values[i][j] + B.values[i][j];
            }
        }
        return C;
    }
    
    public Matrix changeSign(){
       
        int rowCount = this.getRowCount();
        int colCount = this.getColCount();
         Matrix res = new Matrix(rowCount, colCount);
        
        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < colCount; j++){
               res.values[i][j] = -this.values[i][j] ;
            }
        }
        return res;
    }
    
    public float det(){
        // Раскладываем определитель по первой строке - сдвиг индексов, либо (-1) в степени продумать
        /*BitSet rmRows = new BitSet(colCount);
        BitSet rmCols = new BitSet(rowCount); , colJ  BitSet rmRows, BitSet rmCols*/
      /*  if(values.length == 2 && values[0].length == 2){
            return values[0][0] * values[1][1] - values[0][1] * values[1][0];
        }*/
        return detWithLeviChivitta(-1, -1);  
    }
    
    /**
     * Calculate algebraic addition
     * @param colJ index of element col, for what we search algebraic addition
     * @param level Номер строки по которой раскрываем - всегда спускаемся вниз
     * @param rmRows Removed params
     * @param rmCols
     * @return 
     */
    protected float minorDet(int level, int colJ){
        int colCount = getColCount();
        boolean searchFirst = (level == colCount - 1) ;
       
        float d = 0;
        int rowI = level;
        
        /*if(rowI == algI) {
            if(rowI < getRowCount() - 1) rowI++;
        }*/
        
        for(int j = level; (j < colCount && j != colJ); j ++){
            if(searchFirst) {
                return values[rowI][j];
            }
            float sgn = ((j + rowI) % 2 == 0) ? 1 : -1; ; // + 2, т.к в Java нумерация с нуля
            float aM = sgn *  minorDet(level + 1, j);
            d += values[rowI][j] * aM;
        }
        return d;
    }
    
    protected int[] getLeviChevvitaIndexes(int N, int startElement, boolean clockWise){
        int[] res = new int[N]; 
        int kI = startElement;
        int step = clockWise ? 1 : -1;
        
        if(N == 2){
            if ((clockWise && startElement == N) || 
                    (!clockWise && startElement == 1)) return null;
            
        }
        for(int i = 1; i <= N; i++){
            res[i - 1] = kI - 1; // offset, т.к в Java нумерация индексов с нуля
            kI+= step;
            if(kI > N) kI = 1;
            if(kI == 0) kI = N;
        }
        return res;
    }
    
    public float detWithLeviChivitta(int skipRowI, int skipColJ){
        //Сначала чётные перестановки по часовой стрелке
        int N = getRowCount();
        int rowCount = getRowCount();
        if(skipRowI > -1) N--;
        
        int[] rowsInd = new int[N];
        int[] colsInd = new int[N];
        int rI = 0;
        int rJ = 0;
        
        for(int i = 0; i < rowCount; i++){
            if(i != skipRowI) {
                rowsInd[rI] = i;
                rI++;
            }
            if(i != skipColJ) {
                colsInd[rJ] = i;
                rJ++;
            }
        }

        float sum = 0;
        for(int k = 1; k <= N; k++){
            int[] evenPerm = getLeviChevvitaIndexes(N, k, true);
            int[] oddPerm = getLeviChevvitaIndexes(N, k, false);
            
            float pEven = 1;
            float pOdd = 1;
            
            for(int i = 0; i < N; i++){
               if(evenPerm != null) pEven *= values[rowsInd[i]][colsInd[evenPerm[i]]];
               if(oddPerm != null) pOdd *= values[rowsInd[i]][colsInd[oddPerm[i]]];
            }
            
            sum += pEven - pOdd;
        }
        
        return sum;
    }
    
    protected float algebraicAdd(int rowI, int colI){
        return detWithLeviChivitta(rowI, colI);
    }
    
   
            
    protected double[][] convertDataToDouble(float[][] values){
        int rowCount = this.getRowCount();
        int colCount = this.getColCount();
        
        double[][] res = new double[rowCount][colCount];
        
        for(int i = 0; i < rowCount; i++){
           for(int j = 0; j < colCount; j++){ 
               res[i][j] = values[i][j];
           } 
        }
         
        return res;
    }
    protected float[][] convertDataToFloat(double[][] values){
        int rowCount = this.getRowCount();
        int colCount = this.getColCount();
        
        float[][] res = new float[rowCount][colCount];
        
        for(int i = 0; i < rowCount; i++){
           for(int j = 0; j < colCount; j++){ 
               res[i][j] =(float) values[i][j];
           } 
        }
         
        return res;
    }
    
    public Matrix inverse(){
        int rowCount = this.getRowCount();
        int colCount = this.getColCount();
        
        Matrix res = new Matrix(rowCount, colCount);
        RealMatrix M =   MatrixUtils.createRealMatrix(convertDataToDouble(this.values));
        M = MatrixUtils.inverse(M);
        res.values = convertDataToFloat(M.getData());
        
        return res;
        //MatrixUtils.inverse(null);
        
       /* int rowCount = this.getRowCount();
        int colCount = this.getColCount();
        Matrix A = new Matrix(rowCount, colCount);
        
        float Det = detWithLeviChivitta(-1, -1);
        
        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < colCount; j++){
                A.values[i][j] = algebraicAdd(i, j) / Det;
            }
        }
        
        float detA = A.det();
        if( Math.abs( 1/Det - detA) > 0.001){
            System.err.println("Inverse of Matrix with error!!!" + (1/Det - detA));
        }
        
        return A;*/
        
    }
    public Matrix transpose(){
        int rowCount = this.getRowCount();
        int colCount = this.getColCount();
        Matrix T = new Matrix(rowCount, colCount);
        for(int i = 0; i < rowCount; i++){
            for(int j = 0; j < colCount; j++){
                T.values[i][j] = this.values[j][i];
            }
        }
        return T;
    }
            
    protected Matrix inversedGauss(){
        int rowCount = this.getRowCount();
        int colCount = this.getColCount();
        Matrix A = new Matrix(rowCount, colCount * 2);
        for(int i = 0; i < rowCount; i++){
            A.values[i][i + colCount] = 1.0f; // Добавляем
        }
        
        for(int k = 0; k < rowCount; k++){
        }
        
        return A;
    }

    @Override
    public String toString() {
        int rowCount = this.getRowCount();
        int colCount = this.getColCount();
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rowCount; i++) {
            sb.append("{");
            for (int j = 0; j < colCount; j++) {
                sb.append(Float.toString(values[i][j]));
                if (j < colCount - 1) {
                    sb.append(",");
                }
            }
            sb.append("}");
            sb.append("\n");//if(i < rowCount - 1)
        }
      
        return sb.toString();
    }
    
    
    /**
     * @return the colCount
     */
    public int getColCount() {
        return values[0].length;
    }

    /**
     * @return the rowCount
     */
    public int getRowCount() {
        return values.length;
    }
}
