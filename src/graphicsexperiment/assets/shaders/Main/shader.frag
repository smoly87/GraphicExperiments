#version 330

out vec4 outputColor;
struct Vertex{
   vec4 position;
   vec2 textureUV;
   vec3 normal;
   vec3 tanget;
};

uniform struct Light {
   vec3 position;
   vec4 ambient;
   vec4 diffuse;
   vec3 intensities; 
   
} light;
in Vertex vert;
in vec3 light_position;
in vec3 view_dir;
in vec4 shadowCoord;

uniform sampler2D myTexture;
uniform sampler2D bumpTexture;
uniform sampler2D shadowTexture;
uniform sampler2D fboTexture;

uniform vec3 viewDir;

vec3 calcNewNormal(){
    vec3 Normal = normalize(vert.normal);
    vec3 Tangent = normalize(vert.tanget);
    Tangent = normalize(Tangent - dot(Tangent, Normal) * Normal);
    vec3 Bitangent = cross(Tangent, Normal);
    vec3 BumpMapNormal = texture(bumpTexture, vert.textureUV).xyz;
    BumpMapNormal = 2.0 * BumpMapNormal - vec3(1.0, 1.0, 1.0);
    vec3 NewNormal;
    mat3 TBN = mat3(Tangent, Bitangent, Normal);
    NewNormal = TBN * BumpMapNormal;
    NewNormal = normalize(NewNormal);
    return NewNormal;
}

vec3 calcNewNormalApproach2(){
   vec3 Normal = normalize(vert.normal);
   vec3 Tangent = normalize(vert.tanget);
   vec3 Bitangent = cross(Tangent, Normal);
   
   mat3 U = mat3(Tangent, Bitangent, Normal);
   mat3 UI = inverse(U);
   
   vec3 deltaNS = texture(bumpTexture, vert.textureUV).xyz;
   
   vec3 NS = UI*Normal;
   vec3 NcS = (NS - deltaNS);
   
   vec3 NewNormal = U * NcS;
   return NewNormal;
}

void main(){
    vec4 pointColor = texture(myTexture, vert.textureUV).rgba;

	vec3 N = normalize(vert.normal);//calcNewNormalApproach2();//// calcNewNormal();//
	vec3 L = normalize(light_position);
	vec3 V = vec3(0, 0 ,1);
	
    float NdotL = max(dot(N, L), 0.0);
	float specularI = max(2 * dot(N, L) * dot(V, N) - dot(L, V), 0.0);
	
	float visibility = 1.0;
	//vec3 shadowCoordD = shadowCoord.xyz/shadowCoord.w;
	
	vec3 ProjCoords = shadowCoord.xyz ;
    vec2 UVCoords;
    UVCoords.x = 0.5 * ProjCoords.x + 0.5;
    UVCoords.y = 0.5 * ProjCoords.y + 0.5;
    float z = 0.5 * ProjCoords.z + 0.5;
    float Depth = texture(fboTexture, UVCoords).x;
   // if (Depth < (z + 0.00001))
	
	
    if (  Depth < (z + 0.00001)){
          visibility = 0.4;
    }
	/*float depthValue = texture(shadowTexture, shadowCoordD.xy).r;
    outputColor = vec4(vec3(depthValue), 1.0);*/
	outputColor =  visibility*pointColor * 0.1 +  visibility*pointColor * NdotL * 0.5 +visibility*pointColor*pow(specularI, 10) * 0.4; //;
   // outputColor =pointColor;
}