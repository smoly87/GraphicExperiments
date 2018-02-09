#version 330
out vec4 outputColor;
uniform samplerCube fboTexture;

in vec3 TexCoord0;
in vec3 viewDir;
in vec3 lightDir;
in vec3 N;

void main(){
    

	//outputColor =texture(fboTexture, normalize(TexCoord0.xyz));
	vec3 vReflected = reflect(viewDir, N);
	vReflected.x = -vReflected.x;
	outputColor =texture(fboTexture, vReflected);
}
