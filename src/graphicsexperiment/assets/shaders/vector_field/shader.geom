#version 330
layout(points) in;
struct Vertex{
   vec3 position;
   vec3 value;
   vec3 color;
};
in Vertex vert[];
//out Vertex vertexOut;
layout(line_strip, max_vertices=2) out;
out vec4 fragColorG;
out vec4 vOut;
uniform mat4 MVP;


void main()
{
    Vertex v = vert[0];
	fragColorG  = vec4(v.color, 1.0);
	gl_Position = gl_in[0].gl_Position;
	//vOut = gl_Position;
    EmitVertex();
    
    gl_Position =  MVP*vec4(v.value + v.position, 1.0);
    EmitVertex();
    
    EndPrimitive();

}