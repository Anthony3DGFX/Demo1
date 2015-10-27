import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.awt.GLCanvas;

public class SphereScene {

    private GLU glu = new GLU();
    private GLUT glut = new GLUT();
    private Sphere sphere = new Sphere(1, new double[]{0.0, 0.0, 0.0});
    //Change names later
    double[] a = new double[]{0, 0, 0};
    double[] b = new double[]{0, 10, 10};
             
    public SphereScene() {
    }

    public void update() {
        double[] consts = sphere.findQuadratic(a, b);
        System.out.println(consts);
        if(sphere.doesCollide(consts)){
            double[] intersect = sphere.findIntersect(a, b, consts);
            System.out.println(intersect[0]+", "+intersect[1]+", "+intersect[2]);
        }
    }

    public void render(GL2 gl) {
        gl.glClear(GL.GL_COLOR_BUFFER_BIT
            |GL.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        glu.gluLookAt(10.0, 10.0, 10.0,
            0.0, 0.0, 0.0, 
            0.0, 1.0, 0.0);
        drawAxes(gl);
        sphere.draw(gl, glut);
                  
        gl.glLineWidth(10);
        gl.glBegin(GL2.GL_LINES);
          gl.glColor3d(0.5, 0.5, 0.5);
          gl.glVertex3dv(a, 0);
          gl.glVertex3dv(b, 0);
        gl.glEnd();
        gl.glLineWidth(1);

    }

    public void drawAxes(GL2 gl) {
        double x = 1.5, y = 1.5, z = 1.5;
        double[] color = {0.0, 0.0, .0};
        double[] end = {0, 0, 0};

        gl.glLineWidth(4);
        //draw axis
        gl.glBegin(GL2.GL_LINES);
        for(int i=0; i<3; i++) {
            //need to gen 001, 010, 100
            end[i] = 1;
            color[i] = 1;

            gl.glColor3dv(color, 0);
            gl.glVertex3d(0, 0, 0);
            gl.glVertex3dv(end, 0);
            end[i] = 0;
            color[i] = 0;
        }        
        gl.glEnd();
        gl.glLineWidth(1);
    } 
}
