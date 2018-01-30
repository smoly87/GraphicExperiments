#version 330

layout(location = 0) in vec3 position;
layout(location = 2) in vec3 value;
layout(location = 3) in vec3 vertexColor;

struct Vertex{
   vec3 position;
   vec3 value;
   vec3 color;
};

out Vertex vert;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;
uniform mat4 MVP;
out vec4 fragColor;
void main(){
    
	vert.position = position;
	vert.value = value;
	vert.color = vertexColor;
	
	//fragColor = vec4(0.5, 0.8, 0.5,1.0);
	gl_Position = MVP* vec4(position,1.0);
}