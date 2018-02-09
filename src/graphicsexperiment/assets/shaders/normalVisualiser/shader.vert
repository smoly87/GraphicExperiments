#version 330

layout(location = 0) in vec4 position;
layout (location = 1) in vec2 vertexUV;
layout (location = 2) in vec3 vertexNormal;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
//uniform mat3 normTransformMatrix;
uniform mat4 normalMatrix ;
/*uniform struct Light {
   vec3 position;
   vec3 intensities; //a.k.a the color of the light
   vec4 ambient; // фоновое
   vec4 diffuse;
} light;*/

uniform mat4 MVP;
struct Vertex{
   vec4 position;
   vec2 textureUV;
   vec4 normal;
   vec3 normalEnd;
   vec3 tanget;
};

out Vertex vertex;

void main(){
    gl_Position = MVP*position; 
    vertex.position = position;
	vec4 newNorm =  transpose(inverse(viewMatrix*modelMatrix)) * vec4(vertexNormal, 0.0);	
	vertex.normal = newNorm;
}