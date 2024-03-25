#version 130
out vec3 color;
vec3 ambientLight    = vec3(1, 1, 1);
vec3 ambientMaterial = vec3(.2, .2, .2); 

void main() {
	color       = ambientLight * ambientMaterial;
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; 
}