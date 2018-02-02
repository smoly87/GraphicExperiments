#version 330
#extension GL_NV_shadow_samplers_cube : enable
out vec4 outputColor;
in vec3 TexCoord0;
uniform samplerCube fboTexture;

void main(){
    
    //outputColor =textureCube(fboTexture, normalize(TexCoord0.xyz));;
	outputColor =texture(fboTexture, normalize(TexCoord0.xyz));
}
