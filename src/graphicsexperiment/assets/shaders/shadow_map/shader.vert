#version 330

layout(location = 0) in vec4 position;
layout (location = 1) in vec2 vertexUV;
layout (location = 2) in vec3 vertexNormal;
layout (location = 4) in vec3 tanget;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 normalMatrix;

struct Vertex{
   vec4 position;
   vec2 textureUV;
   vec3 normal;
   vec3 tanget;
};
uniform struct Light {
   vec3 position;
   vec4 ambient;
   vec4 diffuse;
   vec3 intensities; 
   
} light;
out Vertex vert;
out vec3 light_position;
out vec3 view_dir;
uniform mat4 MVP;
void main(){
    vert.textureUV = vertexUV;
    gl_Position =  MVP * position; 
    vert.position = gl_Position;
	vert.normal = (viewMatrix * vec4(vertexNormal, 0.0)).xyz;
	vert.tanget = (viewMatrix * vec4(tanget, 0.0)).xyz;
	//view_dir = (viewMatrix * vec4(0,0,1, 0)).xyz; 
	light_position = (viewMatrix * vec4(light.position, 0.0)).xyz;

}