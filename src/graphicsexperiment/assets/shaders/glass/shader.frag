#version 330
out vec4 outputColor;
in vec3 TexCoord0;
uniform samplerCube fboTexture;

void main(){
    
    //outputColor =textureCube(fboTexture, normalize(TexCoord0.xyz));;
	outputColor =texture(fboTexture, normalize(TexCoord0.xyz));
}
