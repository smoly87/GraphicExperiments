#version 330

layout(location = 0) in vec2 position;
layout(location = 1) in float value;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 MVP;
out float u;
void main(){
    u = (value + 1)*0.5;
	gl_Position = MVP* vec4(position, value,  1.0);
}