precision mediump float; 	//don't need high precision
varying vec4 vColor; 		//color for the fragment; this was output from the vertexShader

void main() {

	
	gl_FragColor = vColor; 	//gl_fragColor is built-in variable for color of fragment
}
