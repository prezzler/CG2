#version 130
out vec4 color;
uniform float ambient;
uniform float diffuseWeight; 
uniform float specularWeight; 
uniform float specularPow;
uniform vec3 lightDirection;

void main() {
	vec3 lr = (lightDirection - (gl_ModelViewProjectionMatrix * gl_Vertex).xyz);
	vec3 l = normalize(lr);
	vec3 n = gl_NormalMatrix*gl_Normal;
	vec3 v = -normalize((gl_ModelViewMatrix*gl_Vertex).xyz); 
	vec3 r = reflect(l,n);		
	float specular = max(0., pow(dot(r,v), specularPow)); 
	float diffuse  = dot(n,l);	
	color       = vec4(1,1,1,1) * (ambient + diffuse*diffuseWeight + specular*specularWeight);
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
}
