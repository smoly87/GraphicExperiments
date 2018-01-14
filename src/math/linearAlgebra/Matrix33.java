/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package math.linearAlgebra;

/**
 *
 * @author Andrey
 */
public class Matrix33 extends Matrix{
    public Matrix33(){
        super(3, 3);
    }
    
    public Matrix33(Vector vector, boolean isColumn) {
        super(vector, isColumn);
    }

    public Matrix33(float[][] values) {
        super(values);
    }
    
    //Получаем из мптрицы 4*4 отрезаем последнюю строку и столбец
    public Matrix33(Matrix srcMatr){
        int length = srcMatr.values.length - 1;
        this.values = new float[3][3];
        
        for (int i = 0; i < length; i++) {
            System.arraycopy(srcMatr.values[i], 0, values[i], 0, 3);
        }
    }
}
