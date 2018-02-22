#version 330

out vec4 fragmentdepth;
in vec4 pos;
in vec3 vert_normal;
void main(){
    fragmentdepth = vec4(normalize(vert_normal), pos.z);
}