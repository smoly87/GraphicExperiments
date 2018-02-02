#version 330

layout(location = 0) in vec4 position;
layout (location = 1) in vec3 vertexUV;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;


out vec3 TexCoord0;

void main(){

    vec4 pos =  projectionMatrix * viewMatrix * modelMatrix * position; 
    gl_Position = pos;
 
    TexCoord0 = position.xyz;

}