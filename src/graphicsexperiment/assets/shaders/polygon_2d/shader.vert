#version 330

layout(location = 0) in vec3 position;


uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 MVP;

void main(){
	gl_Position = MVP* vec4(position,1.0);
}