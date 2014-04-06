uniform mat4 uMVMatrix;		//MVMatrix
uniform mat4 uMVPMatrix;	//MVPMatrix
attribute vec4 aPosition;	//Per-vertex position information we will pass in
attribute vec3 aNormal;		//Per-vertex normal information we will pass in.
attribute vec4 aColor;		//Per-vertex color information we will pass in.
varying vec4 vColor; 			//color to interpolate
varying vec3 vNormal;			//normal to interpolate

void main() {

  vec3 modelViewVertex = vec3(uMVMatrix * aPosition);
  vec3 modelViewNormal = normalize(vec3(uMVMatrix * vec4(aNormal, 0.0)));
  vColor = aColor;
  vNormal = modelViewNormal;
  gl_Position = uMVPMatrix * aPosition;
}
