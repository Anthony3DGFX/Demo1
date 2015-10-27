import java.awt.*;
import java.awt.event.*;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.*;

public class SphereMain extends Frame implements GLEventListener, KeyListener, ActionListener {
        
    private static final int WIDTH = 640;
    private static final int HEIGHT = 480;
    private static final float NEAR_CLIP = 0.1f;
    private static final float FAR_CLIP = 100.0f;

    private GLCanvas canvas;
    private SphereScene scene;

        
    public static void main(String[] args) {
        SphereMain main = new SphereMain();
        main.setVisible(true);
    }

    public SphereMain() {
        super("Collision Test");
        setSize(WIDTH, HEIGHT);

        GLProfile glp = GLProfile.getDefault();
        GLCapabilities caps = new GLCapabilities(glp);
        canvas = new GLCanvas(caps);
        add(canvas, "Center");

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        canvas.addGLEventListener(this);

        FPSAnimator animator =
            new FPSAnimator(canvas, 1);
        animator.start();

        scene = new SphereScene();
        }

    public void actionPerformed(ActionEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void init(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClearColor(0, 0, 0, 1);
        gl.glEnable(GL2.GL_DEPTH_TEST);
        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glFrontFace(GL2.GL_CCW);
        gl.glCullFace(GL2.GL_BACK);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glEnable(GL2.GL_LIGHT0);
        gl.glEnable(GL2.GL_NORMALIZE);
        gl.glEnable(GL2.GL_TEXTURE_2D);

    }

    public void reshape(GLAutoDrawable drawable, int x, int y,
                    int width, int height) {
        GL2 gl = drawable.getGL().getGL2();

        float fAspect = (float) width/height;
        float fovy = 60.0f;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();

        float top = (float) Math.tan(Math.toRadians(
                                    fovy*0.5))*NEAR_CLIP;
                
        float bottom = -top;
        float left = fAspect*bottom;
        float right = fAspect*top;
        
        gl.glFrustum(left, right, bottom, top, NEAR_CLIP,
                        FAR_CLIP);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
    }

    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        scene.update();
        scene.render(gl);
            
    }

    public void dispose(GLAutoDrawable drawable) {
    }
}
