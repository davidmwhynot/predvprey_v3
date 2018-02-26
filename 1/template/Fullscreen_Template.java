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
 * Fullscreen_Template
 * @author David Whynot
 * @version v1.0.0
 */
@SuppressWarnings("serial")
public class Fullscreen_Template extends GLCanvas implements GLEventListener, KeyListener {
	// Define constants for the top-level container
	private static String TITLE = "Fullscreen Template";
	private static final int FPS = 60; // animator's target frames per second
	private static final int FOV_Y = 45;
	private static final int Z_NEAR = 1;
	private static final int Z_FAR = 100;
	private static final float BG_R = 0.0f;
	private static final float BG_G = 0.0f;
	private static final float BG_B = 0.0f;
	private static final float BG_A = 0.0f;

	/** The entry main() method to setup the top-level container and animator */
	public static void main(String[] args) {
		// Run the GUI codes in the event-dispatching thread for thread safety
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// Create the OpenGL rendering canvas
				GLCanvas canvas = new Fullscreen_Template();

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
	public Fullscreen_Template() {
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
		gl.glLoadIdentity();
		gl.glBegin(GL_QUADS);
			gl.glColor3d(1, 0, 0); // red
			gl.glVertex3d(1, 1, -50);
			gl.glVertex3d(-1, 1, -50);
			gl.glVertex3d(-1, -1, -50);
			gl.glVertex3d(1, -1, -50);
		gl.glEnd();
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
