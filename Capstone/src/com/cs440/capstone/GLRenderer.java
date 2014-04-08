package com.cs440.capstone;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

/**
 * This class represents a custom OpenGL renderer--all of our "drawing" logic goes here.
 * 
 * @author Patrick Green; code adapted from Google and LearnOpenGLES
 */
@SuppressLint("NewApi")
public class GLRenderer implements GLSurfaceView.Renderer 
{
	private static final String TAG = "Scene Renderer"; //for logging/debugging
	private Context context; //for accessing assets
	private boolean mShowText;
	private int mTextPos;
	public String inside="";
	public boolean insidebool=false;
	public ArrayList<Building> nearList=new ArrayList<Building> ();
	public ArrayList<Float> xPos=new ArrayList<Float> ();
	public ArrayList<Float> yPos=new ArrayList<Float> ();
	//some constants given our model specifications
	public final int POSITION_DATA_SIZE = 3;	
	public final int NORMAL_DATA_SIZE = 3;
	public final int COLOR_DATA_SIZE = 4; //in case we may want it!
	public final int TEXTURE_DATA_SIZE = 2;
	public final int BYTES_PER_FLOAT = 4;
	public final int BYTES_PER_SHORT = 2;

	//Matrix storage
	private float[] mModelMatrix = new float[16]; //to store current model matrix
	private float[] mViewMatrix = new float[16];
	private float[] mProjectionMatrix = new float[16];
	private float[] mMVMatrix = new float[16]; //to store the current modelview matrix
	private float[] mMVPMatrix = new float[16]; //combined MVP matrix

	//Color storage
	private final float[] mColorGrey;

	//axis points (for debugging)
	private final FloatBuffer mAxisBuffer;
	private final int mAxisCount;
	private final float[] axislightNormal = {0,0,3};

	//OpenGL Handles
	private int mPerVertexProgramHandle; //our "program" (OpenGL state) for drawing (uses some lighting!)
	private int mMVMatrixHandle; //the combined ModelView matrix
	private int mMVPMatrixHandle; //the combined ModelViewProjection matrix
	private int mPositionHandle; //the position of a vertex
	private int mNormalHandle; //the position of a vertex
	private int mColorHandle; //the color to paint the model
	private int mlightHandle;//the shader model
	private int mAmbientLightHandle; //Ambient light  pointer
	private int mDiffuseLightHandle; // Diffuse pointer
	private int mShineLightHandle; // Shininess pointer
	private int mHasTextHandle;

	
	private final int mCircleVertexCount;
	private final FloatBuffer mCircleData;
	private final int mCubeVertexCount;
	private final FloatBuffer mCubeData;
	private final FloatBuffer mCubeTextureBuffer;
	private int mPointProgramHandle;
	public static float angleInDegrees;
	
	//for textures
	private int mTextureBufferHandle; //memory location of texture buffer (data)
	private int mTextureHandle; //pointer into shader
	private int mTextureCoordHandle; //pointer into shader]
	private int ourWidth;
	private int ourHeight;
	public static final String TEX_FILE = "ups.png";
	
	
	
	/**
	 * Constructor should initialize any data we need, such as model data
	 */
	public GLRenderer(Context context)
	{	
		this.context = context; //so we can fetch from assets

		
		//set up some example colors. Can add more as needed!
		mColorGrey = new float[] {0.8f, 0.8f, 0.8f, 1.0f};
		ModelFactory models = new ModelFactory();

		// more models can go here!
		float[] circleData = models.getSphereData(ModelFactory.ROUGH_SPHERE);
		mCircleVertexCount = circleData.length
				/ (POSITION_DATA_SIZE + NORMAL_DATA_SIZE);
		mCircleData = ByteBuffer
				.allocateDirect(circleData.length * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder()).asFloatBuffer(); // generate
																			// buffer
		mCircleData.put(circleData); // put the float[] into the buffer and set
												// the position
		float[] cubeData = models.getCubeData();
		mCubeVertexCount = cubeData.length
				/ (POSITION_DATA_SIZE + NORMAL_DATA_SIZE);
		mCubeData = ByteBuffer
				.allocateDirect(cubeData.length * BYTES_PER_FLOAT)
				.order(ByteOrder.nativeOrder()).asFloatBuffer(); // generate
																	// buffer
		mCubeData.put(cubeData); // put the float[] into the buffer and set the
									// position
		
		float[] cubeTexData = models.getCubeTextureData();
		mCubeTextureBuffer = ByteBuffer.allocateDirect(cubeTexData.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer(); //generate buffer
		mCubeTextureBuffer.put(cubeTexData); //put the float[] into the buffer and set the position
		//axis
		float[] axisData = models.getCoordinateAxis();
		mAxisCount = axisData.length/POSITION_DATA_SIZE;
		mAxisBuffer = ByteBuffer.allocateDirect(axisData.length * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder()).asFloatBuffer(); //generate buffer
		mAxisBuffer.put(axisData); //put the float[] into the buffer and set the position

	}

	/**
	 * This mehod is called when the rendering surface is first created; more initializing stuff goes here.
	 * Initialize OpenGL program components here
	 */
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) 
	{
		//flags to enable depth work
		GLES20.glEnable(GLES20.GL_CULL_FACE); //remove back faces
		GLES20.glEnable(GLES20.GL_DEPTH_TEST); //enable depth testing
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);

		
		// Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
		//This is a good place to compile the shaders from Strings into actual executables. We use a helper method for that
		GLES20.glEnable(GLES20.GL_CULL_FACE); //remove back faces
		GLES20.glEnable(GLES20.GL_DEPTH_TEST); //enable depth testing
		GLES20.glDepthFunc(GLES20.GL_LEQUAL);


		//This is a good place to compile the shaders from Strings into actual executables. We use a helper method for that
		int vertexShaderHandle = GLUtilities.loadShader(GLES20.GL_VERTEX_SHADER, "diffuseVertex.glsl", context);
		int fragmentShaderHandle = GLUtilities.loadShader(GLES20.GL_FRAGMENT_SHADER, 
				"lightingFragment.glsl", context);
		mPerVertexProgramHandle = GLUtilities.createAndLinkProgram(vertexShaderHandle, fragmentShaderHandle, 
				new String[] {"aPosition","aNormal","aColor","aTexCoord"}); //and then we throw them into a program

		int pointVertexShaderHandle = GLUtilities.loadShader(GLES20.GL_VERTEX_SHADER, "pointVertex.glsl", context);
		int pointFragmentShaderHandle = GLUtilities.loadShader(GLES20.GL_FRAGMENT_SHADER, "fragment.glsl", context);
		mPointProgramHandle = GLUtilities.createAndLinkProgram(pointVertexShaderHandle, pointFragmentShaderHandle, 
				new String[] {"aPosition"});
		//Get pointers to the shader's variables (for use elsewhere)
		mMVPMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "uMVPMatrix");
		mMVMatrixHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "uMVMatrix");
		mPositionHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "aPosition");
		mNormalHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "aNormal");
		mColorHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "aColor");
		
		//Dealing with shader stuff here
		
		mlightHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "lightPos");
		mAmbientLightHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "La");
		mDiffuseLightHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "Ld");
		mShineLightHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "Kd");
		mHasTextHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "hasText");
		mTextureBufferHandle = GLUtilities.loadTexture(TEX_FILE, context);
		mTextureHandle = GLES20.glGetUniformLocation(mPerVertexProgramHandle, "uTexture");
		mTextureCoordHandle = GLES20.glGetAttribLocation(mPerVertexProgramHandle, "aTexCoord");
		
	}
	
	

	/**
	 * Called whenever the surface changes (i.e., size due to rotation). Put viewing initialization stuff 
	 * (that depends on the size) here!
	 */
	@Override
	public void onSurfaceChanged(GL10 unused, int width, int height) 
	{
		GLES20.glViewport(0, 0, width, height); // Set the OpenGL viewport (basically the canvas) to the same size as the surface.

		//Set View Matrix
		Matrix.setLookAtM(mViewMatrix, 0, 
				0.0f, 0.0f, 10.0f, //eye's location
				0.0f, 0.0f, 0.0f, //point we're looking at
				0.0f, 1.0f, 0.0f //direction that is "up" from our head
				);

		//tweak the camera
		Matrix.translateM(mViewMatrix, 0, 0, 0, 3f);
		//Matrix.rotateM(mViewMatrix,0, 30, 1f, 0, 0);
		
		
		//Set Projection Matrix.
		final float ratio = (float) width / height; //aspect ratio  1!!!!!
		//final float left = -ratio;	final float right = ratio;
		//final float bottom = -1; final float top = 1;
		//final float near = 1.0f; final float far = 50.0f;
		ourWidth = width;
		ourHeight = height;
		Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
		//Matrix.perspectiveM(mProjectionMatrix, 0, 90f, ratio, near, far);
	}

	/**
	 * This is like our "onDraw" method; it says what to do each frame
	 */
	@Override
	public void onDrawFrame(GL10 unused) 
	{
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT); //start by clearing the screen for each frame
		
		GLES20.glUseProgram(mPerVertexProgramHandle); //tell OpenGL to use the shader program we've compiled
		GLES20.glUniform4f(mAmbientLightHandle, 1f, 1.0f, 0.9f, 1.0f);
		//GLES20.glUniform4f(mDiffuseLightHandle, 0.8f, 0.8f, 0.8f, 1.0f);
		//GLES20.glUniform1f(mShineLightHandle, 0.2f);
		
		//pass in textures to OpenGL
	    GLES20.glActiveTexture(GLES20.GL_TEXTURE0); //specify which texture we're going to be using (basically select what will be the "current" texture)
	    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextureBufferHandle); //bind the texture to the "current" texture
	    GLES20.glUniform1i(mTextureHandle, 0); //pass the texture into the shader
		
	   // Log.d("Setup", "Nearlist is  working!!!!!!!x = " + ourWidth + " and y = "+ ourHeight);
	    float halfWide = ourWidth/2;
	    float halfTall = ourHeight/2;
	    Log.d("Setup", "Nearlist is " + nearList.size());
	    for(int i=0; i<nearList.size(); i++){
	    	
	    	float x = (xPos.get(i) - halfWide)/ourWidth;
	    	float y = (yPos.get(i) - halfTall)/ourHeight;
	    	Log.d("Setup", "Nearlist is  working!!!!!!!x = " + x + " and y = "+ y);
	    	drawLogo(x,y,0f);
	    }
	    
	    if(nearList.size()<1){
	    	Log.d("Setup", "Nearlist is not working..." + nearList.size());
	    	drawLogo(0,1f,0);
	    }
	    
		//drawLogo(5f,0f,0f);
		//drawLogo(0f,0f,0f);
		
		
		
		//drawAxis(); //so we have guides on coordinate axes, for debugging
	}
	
	public void drawLogo(float x, float y, float z){
		Matrix.setIdentityM(mModelMatrix, 0);
		
		
		//Matrix.scaleM(mModelMatrix, 0, .3f, .3f, .3f);
		Matrix.translateM(mModelMatrix, 0, x,y,z);
		Matrix.scaleM(mModelMatrix, 0, .3f, .3f, .3f);
		mTextureBufferHandle = GLUtilities.loadTexture("ups.png", context);
		GLES20.glUniform1i(mHasTextHandle, 1);
		mCubeTextureBuffer.position(0); //reset buffer start to 0 (where data starts)
		GLES20.glVertexAttribPointer(mTextureCoordHandle, TEXTURE_DATA_SIZE, GLES20.GL_FLOAT, false, 0, mCubeTextureBuffer);
		GLES20.glEnableVertexAttribArray(mTextureCoordHandle); //doing this explicitly/separately
		drawPackedTriangleBuffer(mCubeData, mCubeVertexCount, mModelMatrix, mColorGrey, false);
		GLES20.glUniform1i(mHasTextHandle, 0);
		GLES20.glUniform1f(mShineLightHandle, 1.0f);
		Matrix.setIdentityM(mModelMatrix, 0);
		
	}
	
	

	private void drawIndexedTriangleBuffer(FloatBuffer vertexBuffer, FloatBuffer normalBuffer, ShortBuffer indexBuffer, int indexCount,
			float[] modelMatrix, float[] color)
	{
		//Calculate MV and MVPMatrix. Note written as MVP, but really P*V*M
		Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, modelMatrix, 0);  //"M * V"
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0); //"MV * P"

		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVMatrix, 0); //put combined matrixes in the shader variables
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

		vertexBuffer.position(0); //reset buffer start to 0 (where data starts)
		GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, vertexBuffer);
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		normalBuffer.position(0); //reset buffer start to 0 (where data starts)
		GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, 0, normalBuffer);
		GLES20.glEnableVertexAttribArray(mNormalHandle);

		//color data
		GLES20.glVertexAttrib4fv(mColorHandle, color, 0);
		
		//setup the index buffer
		indexBuffer.position(0);
		GLES20.glDrawElements(GLES20.GL_TRIANGLES, indexCount, GLES20.GL_UNSIGNED_SHORT, indexBuffer); //draw by indices
		//GLUtilities.checkGlError("glDrawElements");
	}

	/**
	 * Draws a triangle buffer with the given modelMatrix and single color. 
	 * Note the view matrix is defined per program.
	 */			
	private void drawPackedTriangleBuffer(FloatBuffer buffer, int vertexCount, float[] modelMatrix, float[] color)
	{		
		//Calculate MV and MVPMatrix. Note written as MVP, but really P*V*M
		Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, modelMatrix, 0);  //"M * V"
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0); //"MV * P"

		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVMatrix, 0); //put combined matrixes in the shader variables
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


		final int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE) * BYTES_PER_FLOAT; //how big of steps we take through the buffer

		buffer.position(0); //reset buffer start to 0 (where the position data starts)
		GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, buffer); //note the stride lets us step over the normal data!
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		buffer.position(POSITION_DATA_SIZE); //shift pointer to where the normal data starts
		GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, stride, buffer); //note the stride lets us step over the position data!
		GLES20.glEnableVertexAttribArray(mNormalHandle);

		//put color data in the shader variable
		GLES20.glVertexAttrib4fv(mColorHandle, color, 0);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount); //draw the vertices
	}	
	
	/**
	 * Draws a triangle buffer with the given modelMatrix and single color. 
	 * Note the view matrix is defined per program.
	 */			
	private void drawPackedTriangleBuffer(FloatBuffer buffer, int vertexCount, float[] modelMatrix, float[] color, boolean hasTexture)
	{		
		//Calculate MV and MVPMatrix. Note written as MVP, but really P*V*M
		Matrix.multiplyMM(mMVMatrix, 0, mViewMatrix, 0, modelMatrix, 0);  //"M * V"
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0); //"MV * P"

		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVMatrix, 0); //put combined matrixes in the shader variables
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);


		int stride = (POSITION_DATA_SIZE + NORMAL_DATA_SIZE) * BYTES_PER_FLOAT; //how big of steps we take through the buffer
		if(hasTexture)
			stride += TEXTURE_DATA_SIZE * BYTES_PER_FLOAT;
		
		buffer.position(0); //reset buffer start to 0 (where the position data starts)
		GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, stride, buffer); //note the stride lets us step over the normal data!
		GLES20.glEnableVertexAttribArray(mPositionHandle);

		buffer.position(POSITION_DATA_SIZE); //shift pointer to where the normal data starts
		GLES20.glVertexAttribPointer(mNormalHandle, NORMAL_DATA_SIZE, GLES20.GL_FLOAT, false, stride, buffer); //note the stride lets us step over the position data!
		GLES20.glEnableVertexAttribArray(mNormalHandle);

		if(hasTexture)
		{
			buffer.position(POSITION_DATA_SIZE+NORMAL_DATA_SIZE);
			GLES20.glVertexAttribPointer(mTextureCoordHandle, TEXTURE_DATA_SIZE, GLES20.GL_FLOAT, false, stride, buffer);
			GLES20.glEnableVertexAttribArray(mTextureCoordHandle);
		}
		
		//put color data in the shader variable
		GLES20.glVertexAttrib4fv(mColorHandle, color, 0);

		GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount); //draw the vertices
	}			

	//draws the coordinate axis (for debugging)
	private void drawAxis()
	{
		Matrix.setIdentityM(mModelMatrix, 0);
		Matrix.multiplyMM(mMVMatrix, 0, mModelMatrix, 0, mViewMatrix, 0);  //M * V
		Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVMatrix, 0); //P * MV 

		GLES20.glUniformMatrix4fv(mMVMatrixHandle, 1, false, mMVMatrix, 0);
		GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mMVPMatrix, 0);

		// Pass in the position information
		mAxisBuffer.position(0); //reset buffer start to 0 (just in case)
		GLES20.glVertexAttribPointer(mPositionHandle, POSITION_DATA_SIZE, GLES20.GL_FLOAT, false, 0, mAxisBuffer); 
		GLES20.glEnableVertexAttribArray(mPositionHandle);
		GLES20.glDisableVertexAttribArray(mNormalHandle); //turn off the buffer version of normals
		GLES20.glVertexAttrib3fv(mNormalHandle, axislightNormal, 0); //pass particular normal (so points are bright)

		//GLES20.glDisableVertexAttribArray(mColorHandle); //just in case it was enabled earlier
		GLES20.glVertexAttrib4fv(mColorHandle, mColorGrey, 0); //put color in the shader variable

		GLES20.glDrawArrays(GLES20.GL_POINTS, 0, mAxisCount); //draw the axis (as points!)
	}

}
