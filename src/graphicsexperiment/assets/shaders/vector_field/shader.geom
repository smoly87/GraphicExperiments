#version 330
layout(points) in;
struct Vertex{
   vec3 position;
   vec3 value;
   vec3 color;
};
in Vertex vertex[];
//out Vertex vertexOut;

in vec4 fragColor[];

layout(line_strip, max_vertices=2) out;
out vec4 fragColorG;
uniform mat4 MVP;

/*void copyVertAttr(Vertex v){
   newVert.position = v.position;
   newVert.value = v.value;
   newVert.color = v.color;
   return newVert;
}*/
void main()
{


    Vertex v = vertex[0];
 /*   fragColor = vec4(v.color, 1.0);
	fragColor = vec4(1.0, 1.0, 1.0, 1.0);*/
	fragColorG = fragColor[0];
	gl_Position = gl_in[0].gl_Position;
    EmitVertex();
    
    gl_Position = MVP*vec4(v.position + v.value, 1.0);
	/*fragColor = vec4(v.color, 1.0);
	fragColor = vec4(1.0, 1.0, 1.0, 1.0);*/
    EmitVertex();
    
    EndPrimitive();
	
 /* fragColor = fragColors[0]; // Point has only one vertex

    gl_Position = gl_in[0].gl_Position + vec4(-0.1, 0.1, 0.0, 0.0);
    EmitVertex();
fragColor = fragColors[0];
    gl_Position = gl_in[0].gl_Position + vec4(0.1, 0.1, 0.0, 0.0);
    EmitVertex();

    EndPrimitive();*/
}