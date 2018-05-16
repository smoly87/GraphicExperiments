#version 330
out vec4 outputColor;
in float u;
void main(){
    outputColor = vec4(0, u, u,1.0);
	//outputColor = vec4(1.0, 1.0, 1.0,1.0);
}