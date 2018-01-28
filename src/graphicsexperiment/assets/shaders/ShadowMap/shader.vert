#version 330
layout(location = 0) in vec4 position;

uniform mat4 lightMVP;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
out vec4 pos;
uniform mat4 MVP;
void main(){
     gl_Position =  MVP* position; 
    pos = gl_Position;

}