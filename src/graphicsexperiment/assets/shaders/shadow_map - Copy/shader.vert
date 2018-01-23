#version 330
layout(location = 0) in vec4 position;

uniform mat4 lightMVP;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
out vec4 pos;

void main(){
    gl_Position =  projectionMatrix*lightMVP *modelMatrix* position; 
    pos = gl_Position;
}