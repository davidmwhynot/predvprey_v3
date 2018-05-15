import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAnimatorControl;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.FPSAnimator;
import static com.jogamp.opengl.GL.*;  // GL constants
import static com.jogamp.opengl.GL2.*; // GL2 constants
import static com.jogamp.opengl.GL2ES3.GL_QUADS;

/**
 * Ngn
 * @author David Whynot
 * @version v1.0.0
 */
@SuppressWarnings("serial")
public class Ngn extends GLCanvas implements GLEventListener, KeyListener {
	// TEST DATA
	// for drawing cells properly
	int xSize = 3;
	int ySize = 3;
	int[][] testData = { // {x, y, z, r, g, b}
		{0,2,-1, 0, 0, 1},
		{1,2,-1, 1, 0, 1},
		{1,0,-1, 1, 0, 0},
		{1,1,-1, 1, 0, 0},
		{0,-1,-1, 1, 0, 0},
		{-1,0,-1, 1, 0, 0},
		{-1,-1,-1, 1, 0, 0},
		{1,-1,-1, 1, 0, 0},
		{-1,1,-1, 1, 0, 0},
		{0,0,-2, 1, 0, 0},
		{0,1,-2, 1, 0, 0},
		{1,0,-2, 1, 0, 0},
		{1,1,-2, 1, 0, 0},
		{0,-1,-2, 1, 0, 0},
		{-1,0,-2, 1, 0, 0},
		{-1,-1,-2, 1, 0, 0},
		{1,-1,-2, 1, 0, 0},
		{-1,3,-1, 0, 1, 0}
	};
	// int testData = 8;

	private float angle = 0;
	private float velocity = 0.5f;


	// Define constants for the top-level container
	private static String TITLE = "Fullscreen Template";
	private static final int FPS = 60; // animator's target frames per second
	private static final int FOV_Y = 100;
	private static final int Z_NEAR = 1;
	private static final int Z_FAR = 15;
	private static final float BG_R = 0.0f;
	private static final float BG_G = 0.0f;
	private static final float BG_B = 0.0f;
	private static final float BG_A = 0.0f;
	private static final float GAP_SIZE = 0.1f;

	/** The entry main() method to setup the top-level container and animator */
	public static void main(String[] args) {
		// Run the GUI codes in the event-dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Create the OpenGL rendering canvas
				GLCanvas canvas = new Ngn();

				// Create an animator that drives canvas' display() at the specified FPS.
				final FPSAnimator animator = new FPSAnimator(canvas, FPS, true);

				// Create the top-level container
				JFrame frame = new JFrame(); // Swing's JFrame (or AWT's Frame)
				frame.getContentPane().add(canvas);
				frame.setTitle(TITLE);
				frame.setUndecorated(true);     // no decoration such as title bar
				frame.setExtendedState(Frame.MAXIMIZED_BOTH);  // full screen mode
				frame.setVisible(true);
				animator.start(); // start the animation loop
			}
		});
	}

	// Setup OpenGL Graphics Renderer

	private GLU glu;  // for the GL Utility

	/** Constructor to setup the GUI for this Component */
	public Ngn() {
		this.addGLEventListener(this);
		this.addKeyListener(this); // for Handling KeyEvents
		this.setFocusable(true);
		this.requestFocus();
	}

	// ------ Implement methods declared in GLEventListener ------

	/**
	* Called back immediately after the OpenGL context is initialized. Can be used
	* to perform one-time initialization. Run only once.
	*/
	@Override
	public void init(GLAutoDrawable drawable) {
		System.out.println("init");
		GL2 gl = drawable.getGL().getGL2();      // get the OpenGL graphics context
		glu = new GLU();                         // get GL Utilities
		gl.glClearColor(BG_R, BG_G, BG_B, BG_A); // set background (clear) color
		gl.glClearDepth(1.0f);      // set clear depth value to farthest
		gl.glEnable(GL_DEPTH_TEST); // enables depth testing
		gl.glDepthFunc(GL_LEQUAL);  // the type of depth test to do
		gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST); // best perspective correction
		gl.glShadeModel(GL_SMOOTH); // blends colors nicely, and smoothes out lighting
	}

	/**
	* Call-back handler for window re-size event. Also called when the drawable is
	* first set to visible.
	*/
	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		System.out.println("reshape");
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context

		if (height == 0)
			height = 1;   // prevent divide by zero
		float aspect = (float)width / height;

		// Set the view port (display area) to cover the entire window
		gl.glViewport(0, 0, width, height);

		// Setup perspective projection, with aspect ratio matches viewport
		gl.glMatrixMode(GL_PROJECTION);  // choose projection matrix
		gl.glLoadIdentity();             // reset projection matrix
		glu.gluPerspective(FOV_Y, aspect, Z_NEAR, Z_FAR); // fov y, aspect, zNear, zFar

		// Enable the model-view transform
		gl.glMatrixMode(GL_MODELVIEW);
		gl.glLoadIdentity(); // reset
	}

	/**
	* Called back by the animator to perform rendering.
	*/
	@Override
	public void display(GLAutoDrawable drawable) {
		// System.out.println("display"); // for debug only (causes perf hit when uncommented b/c of large cl volume)
		GL2 gl = drawable.getGL().getGL2();  // get the OpenGL 2 graphics context
		gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear color and depth buffers

		// ----- RENDER OBJECTS -----
		// template square

		// gl.glLoadIdentity();
		// gl.glBegin(GL_QUADS);
		// 	gl.glColor3d(1, 0, 0); // red
		// 	gl.glVertex3d(1, 1, -50);
		// 	gl.glVertex3d(-1, 1, -50);
		// 	gl.glVertex3d(-1, -1, -50);
		// 	gl.glVertex3d(1, -1, -50);
		// gl.glEnd();

		// red points
		// for(int x = testData / -2; x < testData / 2; ++x) {
		// 	for(int y = testData / -2; y < testData / 2; ++y) {
		// 		for(int z = testData / -2; z < testData / 2; ++z) {
		// 			gl.glLoadIdentity();
		// 			gl.glTranslatef(0.0f, 0.0f, -10.0f);
		//       gl.glRotatef(anglePyramid, -0.2f, 1.0f, 0.0f); // rotate about the y-axis
		// 			gl.glBegin(GL_TRIANGLES);
		// 				gl.glColor3f(1.0f, 0.0f, 0.0f);
		// 				gl.glVertex3d(x, y, z);
		// 				gl.glVertex3d(x + .5, y, z);
		// 				gl.glVertex3d(x + .5, y + .5, z);
		// 			gl.glEnd();
		//
		//       // Update the rotational angle after each refresh.
		//       // anglePyramid += speedPyramid;
		//       // angleCube += speedCube;
		// 		}
		// 	}
		// }
		// for(int x = testData / -2; x < testData / 2; ++x) {
		// 	for(int y = testData / -2; y < testData / 2; ++y) {
		// 		for(int z = testData / -2; z < testData / 2; ++z) {
		// 			gl.glLoadIdentity();
		// 			gl.glTranslatef(0.0f, 0.0f, -10.0f);
		//       gl.glRotatef(anglePyramid, -0.2f, 1.0f, 0.0f); // rotate about the y-axis
		// 			gl.glBegin(GL_TRIANGLES);
		// 				gl.glColor3f(1.0f, 0.0f, 0.0f);
		// 				gl.glVertex3d(x, y, z);
		// 				gl.glVertex3d(x + .5, y, z);
		// 				gl.glVertex3d(x + .5, y + .5, z);
		// 			gl.glEnd();
		//
		//       // Update the rotational angle after each refresh.
		//       // anglePyramid += speedPyramid;
		//       // angleCube += speedCube;
		// 		}
		// 	}
		// }

		// create red cells
		float sin = (float)Math.sin(angle);
		float cos = (float)Math.cos(angle);
		for(int[] i : testData) {
			gl.glLoadIdentity();
			gl.glTranslatef(0.0f, 0.0f, -10.0f);
			gl.glRotatef(-angle, 0.0f, 1.0f, 0.1f * sin); // rotate about the y-axis
			gl.glBegin(GL_QUADS);
				gl.glColor3f(i[3], i[4], i[5]);
				gl.glVertex3d(i[0]+(1-GAP_SIZE), i[1]+(1-GAP_SIZE), i[2]);
				gl.glVertex3d(i[0],	i[1]+(1-GAP_SIZE), i[2]);
				gl.glVertex3d(i[0], i[1], i[2]);
				gl.glVertex3d(i[0]+(1-GAP_SIZE), i[1], i[2]);
			gl.glEnd();
		}

		// red box
		// for(int x = 0; x < testData.length; ++x) {
		// 	gl.glLoadIdentity();
		// 	gl.glBegin(GL_QUADS);
		// 	gl.glColor3d(1,0,0); // red
		// 	for(int y = 0; y < testData.length; ++y) {
		// 		for(int z = 0; z < testData.length; ++z) {
		// 			gl.glVertex3i((x*2)+0, (y*2)+0, (z*50));
		// 		}
		// 	}
		// 	gl.glEnd();
		// }

		// Update the rotational angle after each refresh.
		angle += velocity;
	}


	/**
	* Called back before the OpenGL context is destroyed. Release resource such as buffers.
	*/
	@Override
	public void dispose(GLAutoDrawable drawable) { }

	@Override
	public void keyTyped(KeyEvent e) { }

	@Override
	public void keyPressed(KeyEvent e) {
		int keyCode = e.getKeyCode();
		switch (keyCode) {
		case KeyEvent.VK_ESCAPE: // quit
		// Use a dedicate thread to run the stop() to ensure that the
		// animator stops before program exits.
		new Thread() {
		@Override
		public void run() {
		GLAnimatorControl animator = getAnimator();
		if (animator.isStarted()) animator.stop();
		System.exit(0);
		}
		}.start();
		break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) { }
}
