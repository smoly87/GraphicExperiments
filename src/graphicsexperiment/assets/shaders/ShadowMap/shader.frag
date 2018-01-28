#version 330
layout(location = 0) out float fragmentdepth;
in vec4 pos;
void main(){
    // Not really needed, OpenGL does it anyway
    fragmentdepth = pos.z;
}