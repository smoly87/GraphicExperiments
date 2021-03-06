#version 330

layout(location = 0) in vec4 position;
layout (location = 1) in vec2 vertexUV;
layout (location = 2) in vec3 vertexNormal;
layout (location = 4) in vec3 tanget;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 normalMatrix;

uniform mat4 MVP;
uniform mat4 lightMVP;
uniform vec3 cameraPosW;

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
out vec4 shadowCoord;
out vec3 viewDir;
out vec3 lightDir;

void main(){
    vert.textureUV = vertexUV;
    gl_Position =  MVP * position; 
    vert.position = gl_Position;
	/*mat4 normalV = inverse(transpose(viewMatrix*modelMatrix));
	vert.normal = (normalV * vec4(vertexNormal, 1.0)).xyz;*/
	vert.normal = (transpose(inverse(modelMatrix))* vec4(vertexNormal, 0.0)).xyz;
	vert.tanget = (transpose(inverse(modelMatrix)) * vec4(tanget, 0.0)).xyz;
	viewDir = ( modelMatrix * position).xyz -cameraPosW;
	lightDir = light.position-( modelMatrix * position).xyz;
	//view_dir = (viewMatrix * vec4(0,0,1, 0)).xyz; 
	light_position = light.position;
	shadowCoord = projectionMatrix*lightMVP *modelMatrix* position;

}