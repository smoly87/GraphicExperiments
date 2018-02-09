#version 330
layout(triangles) in;
struct Vertex{
   vec4 position;
   vec2 textureUV;
   vec4 normal;
   vec3 tanget;
};
in Vertex vertex[];
// Three lines will be generated: 6 vertices
layout(line_strip, max_vertices=6) out;

uniform float normal_length = 0.05;
uniform mat4 MVP;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

out vec4 vertex_color;

void main()
{
  int i;
  vec4 color = vec4(1.0,0.5,0.0,1.0);
  for(i=0; i<gl_in.length(); i++)
  {
    vec4 P = gl_in[i].gl_Position;
    vec4 N = vertex[i].normal;
    
    gl_Position = P;
    vertex_color = color;
    EmitVertex();
    
    gl_Position =   projectionMatrix*normalize( viewMatrix*modelMatrix*vertex[i].position + N * normal_length);
    vertex_color = color;
    EmitVertex();
    
    EndPrimitive();
  }
}