#version 130
out vec3 color; 

const vec3 diffuseLight   = vec3(1, 1, 1); 
const vec3 lightDirection = vec3(0,-1,-1); 

uniform float diffuseWeight; 
 
void main() { 
	vec3 l = normalize(lightDirection);
	vec3 n = normalize(gl_NormalMatrix * gl_Normal); 
		
	float diffuseLightWeight = max(dot(n, l), 0);
	vec3 diffuseMaterial     = vec3(diffuseWeight, diffuseWeight, diffuseWeight);
	color                    = diffuseLight * diffuseMaterial * diffuseLightWeight; 
		
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; 
}
