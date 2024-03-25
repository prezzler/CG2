#version 130
out vec4 color; 
uniform float ambient; 
		
void main() { 
	color       = vec4(1,1,1,1) * ambient;
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; 
}
		