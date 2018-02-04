#version 330
layout(triangles) in;
struct Vertex{
   vec4 position;
   vec3 normal;
};
in Vertex vertex[];
// Three lines will be generated: 6 vertices
layout(line_strip, max_vertices=6) out;

uniform float normal_length = 0.2;
uniform mat4 MVP;

out vec4 vertex_color;

void main()
{
  int i;
  vec4 color = vec4(1.0,0.5,0.0,1.0);
  for(i=0; i<gl_in.length(); i++)
  {
    vec3 P = gl_in[i].gl_Position.xyz;
    vec3 N = vertex[i].normal.xyz;
    
    gl_Position = MVP * vec4(P,1.0);
    vertex_color = color;
    EmitVertex();
    
    gl_Position = MVP* vec4(P + N * normal_length, 1.0);
    vertex_color = color;
    EmitVertex();
    
    EndPrimitive();
  }
}