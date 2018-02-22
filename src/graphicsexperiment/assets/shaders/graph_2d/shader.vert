#version 330

layout(location = 0) in float position;
layout(location = 1) in float value;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 MVP;

void main(){
	gl_Position = MVP* vec4(position, value, 1.0, 1.0);
}