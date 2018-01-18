/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package math.sceneCalculations;

import math.linearAlgebra.Matrix;
import math.linearAlgebra.Vector;
import math.linearAlgebra.Vector3;
import math.linearAlgebra.Vector4;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Andrey
 */
public class SceneCalculationsTest {
    
    public SceneCalculationsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

   
    /**
     * Test of lookAt method, of class SceneCalculations.
     */
    @Test
    public void testLookAt() {
        System.out.println("lookAt");
        Vector3 eye = new Vector3(1.0f, 0.0f, 1.0f);
        Vector3 center =  new Vector3(0.0f, 0.0f, 0.0f);;
        Vector3 up = new Vector3(0.0f, 1.0f, 0.0f);
        
        Matrix U  = SceneCalculations.lookAt(eye, center, up);
        
        U = U.getRange(0, 0, 2, 2);
        System.out.println("Look At Matrix:" + U.det());
        System.out.println(U.toString());
        //Self axis is z'
        Vector3 expResult = new Vector3(0.0f, 0.0f, 1.0f).normalise();
        Vector3 testVec = eye.minus(center);
       
        
       
        Vector3 result = U.transpose().multiply(testVec).toVector3().normalise();
        
        
        System.out.println("Result is " + result.toString());
        assertArrayEquals(expResult.values, result.values, 0.01f);
        // TODO review the generated test code and remove the default call to fail.
       // fail("The test case is a prototype.");
    }

  
}
