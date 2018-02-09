#version 330
out vec4 outputColor;
uniform samplerCube fboTexture;

in vec3 TexCoord0;
in vec3 viewDir;
in vec3 lightDir;
in vec3 N;
vec3 computeRefractedVector(vec3 incidence, vec3 normal, float eta)
{
 float etaIdotN = eta * dot(incidence, normal);
 float k = 1.0 - eta*eta + etaIdotN*etaIdotN;

 if (k < 0.0)
  return reflect(incidence, normal);
 else
  return eta * incidence - normal * (etaIdotN + sqrt(k));
}
void main(){
    

	vec3 TexCoord1 = TexCoord0;
	//TexCoord1.y = -TexCoord1.y;
	//outputColor =texture(fboTexture, normalize(TexCoord1.xyz));
	vec3 vReflected = reflect(viewDir, normalize(N));
	//vReflected.y = -vReflected.y;
	outputColor =texture(fboTexture, vReflected);
	vec3 vRefracted = computeRefractedVector(viewDir, normalize(N), 1.0/1.5);
	/*vRefracted.z= -vRefracted.z;
	vRefracted.y= -vRefracted.y;*/
	
	
	outputColor =texture(fboTexture, vRefracted);
}
