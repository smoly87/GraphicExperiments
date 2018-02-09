#version 330

layout(location = 0) in vec4 position;
layout (location = 1) in vec3 vertexUV;
layout (location = 2) in vec3 vertexNormal;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 MVP;

uniform mat4 normalMatrix;

uniform vec3 cameraPosW;

uniform struct Light {
   vec3 position;
   vec4 ambient;
   vec4 diffuse;
   vec3 intensities; 
   
} light;

out vec3 TexCoord0;
out vec3 viewDir;
out vec3 lightDir;
out vec3 N;
void main(){

    vec4 pos = MVP * position; 
    gl_Position = pos;
 
    TexCoord0 = position.xyz;
   /* viewDir = vec3(( modelMatrix * position).xyz - cameraPosW);
	N = (normalMatrix * vec4(vertexNormal, 0.0)).xyz;*/
	viewDir = ( modelMatrix * position).xyz -cameraPosW;
	N = (modelMatrix*vec4(vertexNormal, 0.0)).xyz;//(transpose(inverse(modelMatrix)) * vec4(vertexNormal, 1.0)).xyz;
}