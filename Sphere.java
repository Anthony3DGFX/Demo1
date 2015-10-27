/*
*@Author: Anthony Cohn-Richardby
*/
import com.jogamp.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.texture.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.gl2.GLUT;
import com.jogamp.opengl.awt.GLCanvas;
import java.nio.ByteBuffer;
import java.nio.Buffer;
import java.util.Arrays;



public class Sphere {
        
    public Sphere(double radius, double[] center) {
        this.radius = radius;
        this.center = new double[3];
        this.texture = new SphereTexture();
        //3D only
        System.arraycopy(center, 0, this.center, 0, 3); 
    }

    public void draw(GL2 gl, GLUT glut) {
        gl.glPushMatrix();
          gl.glTranslated(center[0], center[1], center[2]);
          glut.glutSolidSphere(radius, 20, 20);
        gl.glPopMatrix();
        drawTexture(gl, glut);
    }

    public void drawTexture(GL2 gl, GLUT glut) {
        System.out.println(texture.buffer.get(1));
        TextureData.Flusher flusher = 
            new TextureData.Flusher(){
                public void flush(){
                }
            };
        TextureData data = new TextureData(gl.getGLProfile(), GL2.GL_RGBA, 800, 600, 0, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, false, false, false, (Buffer) texture.buffer, flusher);
        Texture texture = new Texture(gl, data);
    }

    public double[] findQuadratic(double[] orig, 
                    double[] dir) {
        //Collision makes quadratic with the following:
        double a = Math.pow(dir[0], 2) + Math.pow(dir[1], 2) + Math.pow(dir[2], 2);
        double b = 2*(dir[0]*(orig[0]-center[0])+dir[1]*(orig[1]-center[1])+dir[2]*(orig[2]-center[2]));
        double c = Math.pow(orig[0]-center[0], 2) + Math.pow(orig[1]-center[1], 2)+ Math.pow(orig[2]-center[2], 2)-Math.pow(radius, 2);

        return new double[]{a, b, c};
    }

    public boolean doesCollide(double[] consts) {
        if((Math.pow(consts[1], 2) - 4*consts[0]*consts[1]) >= 0){
            return true;
        }
        else{
            return false;
        }
    }

    public double[] findIntersect(double[] orig, double[] dir,
                    double[] consts) {
        double uPlus = (-consts[1] + Math.sqrt(Math.pow(consts[1], 2) - 4*consts[0]*consts[2]))/(2*consts[0]);
        double uMinus = (-consts[1] - Math.sqrt(Math.pow(consts[1], 2) - 4*consts[0]*consts[2]))/(2*consts[0]);
        double[] inter = new double[3];

        for(int i=0; i<3; i++){
            inter[i] = orig[i] + uPlus*dir[i];
        }

        return inter;
    }

    public void updateTexture(double[] intersect) {
        System.out.println("CALLING PAINT3DV");
        texture.paint3dv(intersect);
    }

    public class SphereTexture {
        
        public SphereTexture(int r, int g, int b, int a) {
            //int[] fillColour = {r, g, b, a};
            //buffer = new int[WIDTH][HEIGHT][4]; 
                    
       /*   for(int i=0; i<WIDTH; i++){
                for(int j=0; j<HEIGHT; j++){
                    System.arraycopy(fillColour, 0,
                        buffer[i][j], 0, 4);
                }
            }*/
            buffer = ByteBuffer.allocate(WIDTH*HEIGHT*4);
            Arrays.fill(buffer.array(), (byte) 10);
        }

        public SphereTexture() {
                this(0, 0, 0, 0);
        }

        public void paint3dv(double[] inter) {
            System.out.println("ENTERED 3DV");
            paint3d(inter[0], inter[1], inter[2]);
        }

        public void paint3d(double x, double y, double z) {
            double angle = Math.tan(y/x);
            //Stretch brush width as you get closer to the top of the sphere
            double paintWidth = (WIDTH/Math.abs(Math.cos(angle)))*10;
            int paintWidthI = (int) Math.floor(paintWidth);
            int xI = (int) Math.floor(x);
                    
            int yI = (int) Math.floor(y);

            paint(xI, yI, paintWidthI, 10); 
        }

        //Simply writes to the buffer
        //Sort out wrapping
        public void paint(int topX, int topY, int width,
                        int height) {

            for(int i=0; i<4*width*height; i+=4*WIDTH){
                for(int j=i; j<i+4*WIDTH; j+=4){
                    for(int x=0; x<4; x++){
                        buffer.put(i+j+x, (byte) paintColour[x]);
                    }
                }
            }
        } 

        public ByteBuffer getBuffer(){
            return this.buffer;
        }


        private ByteBuffer buffer;
        public int[] paintColour = {255, 255, 255, 0};

        public static final int WIDTH = 800;
        public static final int HEIGHT = 600;
    }

    private double radius;
    private double[] center;
    private SphereTexture texture;
}

