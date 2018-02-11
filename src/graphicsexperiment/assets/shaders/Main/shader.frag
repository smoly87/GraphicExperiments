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
uniform sampler2D fboTexture;

in vec3 viewDir;
in vec3 lightDir;

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
   vec3 Tangent = vert.tanget;
   Tangent = normalize(Tangent - dot(Tangent, Normal) * Normal);
   vec3 Bitangent = cross( Tangent, Normal);

   mat3 U = mat3(Tangent, Bitangent, Normal);
    
   vec3 BumpMapNormal = texture(bumpTexture, vert.textureUV).rgb;
   BumpMapNormal = normalize(2.0 * BumpMapNormal - vec3(1.0, 1.0, 1.0));
  //BumpMapNormal = vec3(BumpMapNormal.z, BumpMapNormal.y, BumpMapNormal.x);
   
   vec3 NewNormal = U * BumpMapNormal;
   return normalize(NewNormal);
}

void main(){
    vec4 pointColor = texture(myTexture, vert.textureUV).rgba;

	vec3 N =calcNewNormalApproach2();////// calcNewNormal();//;  
	//N = normalize(vert.normal);
	vec3 L = normalize(lightDir);
	vec3 V = vec3(0, 0 ,1);
	
    float NdotL = max(dot(N, L), 0.0);
	float specularI = max(2 * dot(N, L) * dot(V, N) - dot(L, V), 0.0);
	
	float visibility = 1.0;
	vec3 shadowCoordD = shadowCoord.xyz/shadowCoord.w;
	
	vec3 ProjCoords = shadowCoord.xyz/shadowCoord.w ;
    vec3 UVCoords;
    UVCoords = 0.5 * ProjCoords + 0.5;
	
    float Depth = texture(fboTexture, UVCoords.xy).r;
   // if (Depth < (z + 0.00001))
	
	
    if (  (UVCoords.z - 0.00001) < Depth){
        //  visibility = 0.1;
    }
	float depthValue = texture(fboTexture, shadowCoordD.xy).r;
    //outputColor = vec4(vec3(depthValue), 1.0);
	outputColor = vec4(N,1.0);
	outputColor =   visibility*pointColor * 0.1 +  visibility*pointColor * NdotL * 0.5 +visibility*pointColor*pow(specularI, 10) * 0.4; //;
   // outputColor =pointColor;
}