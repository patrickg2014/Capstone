precision mediump float; 	//don't need high precision
varying vec4 vColor; 		//color for the fragment; this was output from the vertexShader
varying vec3 vNormal;
varying vec2 vTexCoord;			//interpolate the tex coords	////////

uniform sampler2D uTexture; //the texture buffer (data)

uniform vec4 La;

const vec3 LIGHT_DIR = normalize(vec3(0.0,3.0,3.0)); //direction of the light

uniform vec4 Ld;
uniform float Kd;
uniform int hasText;


void main() {

  	float diffuse = Kd*max(dot(vNormal, LIGHT_DIR), 1.0);
	
	//diffuse = diffuse * (1.0 / (1.0 + (0.10 * distance)));//////
	//ambient lighting below
 	//diffuse = diffuse + 0.3;////////
	//gl_FragColor = vec4(.25*vColor.rgb+ .75*texture2D(uTexture, vTexCoord).rgb,1.0);////////////////
	//gl_FragColor = texture2D(uTexture, vTexCoord);
	
	
	//We might need a if statement for the night scene to make the  texture object darker
	
	if(hasText==1){
		gl_FragColor = (vColor * diffuse * texture2D(uTexture, vTexCoord));/////////
	}else{
		gl_FragColor = vColor*La + clamp(vColor*Ld*diffuse,0.0,1.0); 	//gl_fragColor is built-in variable for color of fragment
	}
	
	
	
	
}

