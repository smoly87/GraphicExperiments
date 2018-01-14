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
public class Vector4 extends Vector{

    public Vector4(){
        super(4);
    }
    
    public Vector4(float x, float y, float z, float w){
        super();
        values = new float[]{x, y, z, w};
    }
    
    public Vector4(Vector3 vec){
        super();
        values = new float[]{vec.values[0], vec.values[1], vec.values[2], 1.0f};
    }
    
    public Vector3 toVector3(){
        return new Vector3(this.values[0], this.values[1], this.values[2]);
    }
     
  
    
}
