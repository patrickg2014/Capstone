uniform mat4 uMVPMatrix; 	// A constant representing the combined modelview/projection matrix. We use this for positioning
uniform vec4 uColor; 		// constant color for the point(s) being drawn
attribute vec4 aPosition; 	// Per-vertex position information we will pass in
varying vec4 vColor; 		// out : the ultimate color of the vertex
void main() {
	vColor = uColor;
	//vColor = vec4(1.0f,1.0f,1.0f,1.0f); //if we just want to hard-code as white
  	gl_PointSize = 4.0; //for points
  	gl_Position = uMVPMatrix * aPosition;
}