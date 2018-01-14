#version 330

out vec4 outputColor;

uniform sampler2D myTexture;

in vec4 vertex_color;


void main(){	
    outputColor =  vertex_color;
}