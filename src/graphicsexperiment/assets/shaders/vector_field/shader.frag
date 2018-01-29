#version 330
out vec4 outputColor;
in vec4 fragColorG;

void main(){
    outputColor = fragColorG;
	//outputColor = vec4(1.0, 1.0, 1.0,1.0);
}