#version 330
layout(location = 0) in vec4 position;
layout (location = 1) in vec2 vertexUV;
layout (location = 2) in vec3 vertexNormal;
layout (location = 4) in vec3 tanget;

struct Vertex{
   vec4 position;
   vec2 textureUV;
   vec3 normal;
   vec3 tanget;
};
out Vertex vert;
uniform mat4 MVP;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
out vec4 pos;

void main(){
    gl_Position =  MVP *modelMatrix* position; 
    vert.position = gl_Position;
	vert.textureUV = vertexUV;
}