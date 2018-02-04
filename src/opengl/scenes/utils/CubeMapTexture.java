/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package opengl.scenes.utils;
import com.jogamp.common.util.IOUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import javax.media.opengl.GL;
import static javax.media.opengl.GL.GL_TEXTURE_CUBE_MAP;
import javax.media.opengl.GL3;
import javax.media.opengl.GL4;
import static javax.media.opengl.GL4.*;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLException;
/**
 *
 * @author Andrey
 */
public class CubeMapTexture {
  private static final String[] suffixes = { "px", "nx", "py", "ny", "pz", "nz" };
  private static final int[] targets = { GL_TEXTURE_CUBE_MAP_POSITIVE_X,
                                         GL_TEXTURE_CUBE_MAP_NEGATIVE_X,
                                         GL_TEXTURE_CUBE_MAP_POSITIVE_Y,
                                         GL_TEXTURE_CUBE_MAP_NEGATIVE_Y,
                                         GL_TEXTURE_CUBE_MAP_POSITIVE_Z,
                                         GL_TEXTURE_CUBE_MAP_NEGATIVE_Z };

  public static Texture loadFromStreams(GL4 gl,
                                        ClassLoader scope,
                                        String basename,
                                        String suffix, boolean mipmapped) throws IOException, GLException {
    Texture cubemap = TextureIO.newTexture(GL_TEXTURE_CUBE_MAP);

    for (int i = 0; i < suffixes.length; i++) {
      String resourceName = basename + suffixes[i] + "." + suffix;
      String fileSuffix = IOUtil.getFileSuffix(resourceName);
      InputStream stream = scope.getResourceAsStream(resourceName.substring(1));
      TextureData data = TextureIO.newTextureData(GLContext.getCurrentGL().getGLProfile(), stream,
                                                  mipmapped,
                                                  null);
      if (data == null) {
        throw new IOException("Unable to load texture " + resourceName);
      }
      cubemap.updateImage(gl, data, targets[i]);
    }

    return cubemap;
  }
  
  
  public static Texture loadFromStreamsGl(GL4 gl,
                                        ClassLoader scope,
                                        String basename,
                                        String suffix, boolean mipmapped) throws IOException, GLException {
    Texture cubemap = TextureIO.newTexture(GL_TEXTURE_CUBE_MAP);

    for (int i = 0; i < suffixes.length; i++) {
      String resourceName = basename + suffixes[i] + "." + suffix;
      String fileSuffix = IOUtil.getFileSuffix(resourceName);
      InputStream stream = scope.getResourceAsStream(resourceName.substring(1));
      TextureData data = TextureIO.newTextureData(GLContext.getCurrentGL().getGLProfile(), stream,
                                                  mipmapped,
                                                  null);
      if (data == null) {
        throw new IOException("Unable to load texture " + resourceName);
      }
      cubemap.updateImage(gl, data, targets[i]);
     
     
    }

    return cubemap;
  }
  
  
 
}
