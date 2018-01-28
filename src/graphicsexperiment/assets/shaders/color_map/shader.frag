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

void main(){
    // Not really needed, OpenGL does it anyway
  
	pointColor =vec4(1.0,texture(fboTexture, vert.textureUV).r/100,1.0,1.0 ) ;//
	//pointColor = texture(fboTexture, vert.textureUV).rgba;//v
}