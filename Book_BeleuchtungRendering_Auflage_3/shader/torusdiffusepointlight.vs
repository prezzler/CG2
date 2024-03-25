#version 130
out vec3 color; 	
const vec3 diffuseLight   = vec3(1, 1, 1); 
const vec3 lightPosition  = vec3(0,-5,-5); 
uniform float diffuseWeight; 
		
void main() { 
	vec3 vector2Light 		= normalize(lightPosition - ((gl_ModelViewMatrix * gl_Vertex).xyz));
	vec3 normalEye    		= normalize(gl_NormalMatrix * gl_Normal);
	float diffuseLightWeight 	= max(dot(normalEye, vector2Light), 0);
	vec3 diffuseMaterial     	= vec3(diffuseWeight, diffuseWeight, diffuseWeight);
	color                    	= diffuseLight * diffuseMaterial * diffuseLightWeight; 
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex; 
}
