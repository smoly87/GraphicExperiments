/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

/**
 *
 * @author Andrey
 */
public class IoReader {
    
    public static String readFile(String fileName) throws IOException{
        InputStream inStream = IoReader.class.getResourceAsStream(fileName);
        StringBuffer buffer;
        
        try {
            buffer = readFileToBuffer(fileName);
        } catch(IOException e){
            throw new IOException("Could not read file:" + fileName);
        }
        
        return buffer.toString();
    }
    
    public static StringBuffer readFileToBuffer(String fileName) throws IOException{
        InputStream inStream = IoReader.class.getResourceAsStream(fileName);
        StringBuffer buffer;
        
        try {
            buffer = readFileFromStream(inStream);
        } catch(IOException e){
            throw new IOException("Could not read file:" + fileName);
        }
        
        return buffer;
    }
    
    public static StringBuffer readFileFromStream(InputStream ins) throws IOException {
        if (ins == null) {
            throw new IOException("Could not read from stream.");
        }
        StringBuffer buffer = new StringBuffer();
        Scanner scanner = new Scanner(ins);
        
        try {
            while (scanner.hasNextLine()) {
                buffer.append(scanner.nextLine() + "\n");
            }
        } finally {
            scanner.close();
        }
        
        return buffer;
    }
}
