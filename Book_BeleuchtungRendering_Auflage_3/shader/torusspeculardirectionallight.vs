#version 130
out vec3 color; 
const vec3 specularLight  = vec3(1, 1, 1); 
const vec3 lightDirection = vec3(0,-1,-1); 	
uniform float specularWeight; 
uniform float specularPow; 
 
void main() {
	vec3 l = normalize(lightDirection); 
	vec3 n = gl_NormalMatrix * gl_Normal;
	vec3 v = -normalize((gl_ModelViewProjectionMatrix*gl_Vertex).xyz); 
	vec3 r = reflect(l,n); 
	float specularLightWeight = max(0., pow(dot(r,v), specularPow));
	vec3 specularMaterial     = vec3(specularWeight, specularWeight, specularWeight);
	color                     = specularLight * specularMaterial * specularLightWeight; 		
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; 
}
