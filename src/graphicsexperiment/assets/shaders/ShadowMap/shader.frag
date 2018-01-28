#version 330
layout(location = 0) out vec4 fragmentdepth;
in vec4 pos;
void main(){
    // Not really needed, OpenGL does it anyway
    fragmentdepth = vec4(pos.z/10,1.0,1.0,1.0);
}