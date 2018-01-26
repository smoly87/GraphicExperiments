#version 330
layout(location = 0) out vec4 pointColor;

struct Vertex{
   vec4 position;
   vec2 textureUV;
   vec3 normal;
   vec3 tanget;
};
in Vertex vert;
uniform sampler2D fboTexture;
uniform sampler2D shadowTexture;
void main(){
    // Not really needed, OpenGL does it anyway
  
	pointColor =texture(fboTexture, vert.textureUV).rgba;//vec4(1.0,1.0,1.0,1.0 ) ;//
}