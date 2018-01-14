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

uniform sampler2D myTexture;
uniform sampler2D bumpTexture;

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

void main(){
    vec4 pointColor = texture(myTexture, vert.textureUV).rgba;
	vec3 N = normalize(vert.normal);// calcNewNormal();//
	vec3 L = normalize(light_position);
	vec3 V = vec3(0, 0 ,1);
	
    float NdotL = max(dot(N, L), 0.0);
	float specularI = max(2 * dot(N, L) * dot(V, N) - dot(L, V), 0.0);
	outputColor =  pointColor * 0.1 +  pointColor * NdotL * 0.5 +pointColor*pow(specularI, 10) * 0.4; //;
   // outputColor =pointColor;
}