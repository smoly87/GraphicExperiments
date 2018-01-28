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
    float v = texture(fboTexture, vert.textureUV).r/1.2;
	pointColor =vec4(v,v,v,1.0 ) ;//
	//pointColor = texture(fboTexture, vert.textureUV).rgb;//v
}