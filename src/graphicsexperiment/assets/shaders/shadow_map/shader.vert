#version 330
layout(location = 0) in vec4 position;

uniform mat4 lightMVP;
out vec4 pos;

void main(){
    gl_Position =  lightMVP * position; 
    pos = gl_Position;
}