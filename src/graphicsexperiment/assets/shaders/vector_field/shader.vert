#version 330

layout(location = 0) in vec4 position;
//layout(location = 3) in vec4 vertexColor;

out vec4 fragColor;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 MVP;
void main(){
    gl_Position = MVP* position;
	fragColor = vec4(0.5, 0.5, 0.2, 1.0);//vertexColor;
}