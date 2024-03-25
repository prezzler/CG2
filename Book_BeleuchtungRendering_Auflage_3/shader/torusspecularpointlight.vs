#version 130
out vec3 color; 
const vec3 specularLight  = vec3(1, 1, 1); 
const vec3 lightPosition  = vec3(0,-5,-5); 
uniform float specularWeight; 
uniform float specularPow; 
		 
void main() {
	vec3 vector2Light = normalize(lightPosition - ((gl_ModelViewMatrix * gl_Vertex).xyz));
	vec3 normalEye    = normalize(gl_NormalMatrix * gl_Normal);
	vec3 reflVector   = normalize(reflect(-vector2Light, normalEye));
	vec3 viewVectorEye= -normalize((gl_ModelViewMatrix * gl_Vertex).xyz); 
	float rdotv = max(dot(reflVector, viewVectorEye), 0.0);
	float specularLightWeight = pow(rdotv, specularPow);
	vec3 specularMaterial     = vec3(specularWeight, specularWeight, specularWeight);
	color                     = specularLight * specularMaterial * specularLightWeight; 
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; 
}
