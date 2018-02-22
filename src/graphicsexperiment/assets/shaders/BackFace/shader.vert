#version 330
layout(location = 0) in vec4 position;
layout (location = 1) in vec2 vertexUV;
layout (location = 2) in vec3 vertexNormal;

uniform mat4 MVP;
uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec4 pos;
out vec3 vert_normal;

void main(){
    gl_Position =  MVP* position; 
    pos = gl_Position;
    vert_normal = (transpose(inverse(modelMatrix))* vec4(vertexNormal, 0.0)).xyz;
}