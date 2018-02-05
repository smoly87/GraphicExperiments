#version 330
layout(location = 0) out vec4 pointColor;

struct Vertex{
   vec4 position;
   vec2 textureUV;
   vec3 normal;
   vec3 tanget;
};
in Vertex vert;
uniform sampler2D fboTexture;
float LinearizeDepth(vec2 uv)
{
  float n = 0.1; // camera z near
  float f = 30.0; // camera z far
  float z = texture2D(fboTexture, uv).x;
  return (2.0 * n) / (f + n - z * (f - n));	
}
void main(){
    // Not really needed, OpenGL does it anyway
    //float v = texture(fboTexture, vert.textureUV).r/2.02;
	float v = LinearizeDepth( vert.textureUV);
	pointColor =vec4(v,v,v,1.0 ) ;//
	//pointColor = texture(fboTexture, vert.textureUV);//v
}