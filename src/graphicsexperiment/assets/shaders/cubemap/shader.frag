#version 330

out vec4 outputColor;
in vec3 TexCoord0;
uniform samplerCube myTexture;

void main(){
    
    outputColor =texture(myTexture, normalize(TexCoord0.xyz));;
}
