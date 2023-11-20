package JOGL_3D;

import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class SistemaSolar implements GLEventListener, KeyListener {
    private float traslacionTierra = 0.0f;
    private float rotacionTierra = 0.0f;
    private float traslacionLuna = 0.0f;
    private float rotacionLuna = 0.0f;
    private boolean modoRotacion = true; // Por defecto
    private boolean modoTraslacion = false;

    public static void main(String[] args) {
        GLProfile glprofile = GLProfile.getDefault();
        GLCanvas glcanvas = new GLCanvas();
        glcanvas.addGLEventListener(new SistemaSolar());
        glcanvas.setSize(640, 480);

        final JFrame jframe = new JFrame("Sistema Solar");
        jframe.getContentPane().add(glcanvas);
        jframe.setSize(jframe.getContentPane().getPreferredSize());
        jframe.setVisible(true);
        jframe.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final FPSAnimator animator = new FPSAnimator(glcanvas, 60, true);
        animator.start();

        glcanvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == 'q' || e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    animator.stop();
                    jframe.dispose();
                    System.exit(0);
                }
            }
        });
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        // Configuración de OpenGL
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        // Liberar recursos
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        GL2 gl = drawable.getGL().getGL2();
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
        gl.glColor3d(1, 0, 0);

        final float distTierraSol = 4.0f;
        final float distTierraLuna = 1.5f;

        // SOL
        gl.glColor3f(1.0f, 1.0f, 0.0f);
        glutWireSphere(gl, 1.0, 20, 20);

        // TIERRA
        gl.glPushMatrix();
        gl.glTranslatef(distTierraSol * (float) Math.cos(traslacionTierra),
                distTierraSol * (float) Math.sin(traslacionTierra), 0);
        gl.glRotatef(rotacionTierra, 0, 0, 1.0f);
        gl.glColor3f(0.0f, 0.0f, 1.0f);
        glutWireSphere(gl, 0.8, 10, 10);
        gl.glPopMatrix();

        // LUNA
        gl.glPushMatrix();
        gl.glTranslatef(distTierraSol * (float) Math.cos(traslacionTierra),
                distTierraSol * (float) Math.sin(traslacionTierra), 0);
        gl.glTranslatef(distTierraLuna * (float) Math.cos(traslacionLuna),
                distTierraLuna * (float) Math.sin(traslacionLuna), 0);
        gl.glRotatef(rotacionLuna, 0, 0, 1.0f);
        gl.glColor3f(0.5f, 0.5f, 0.5f);
        glutWireSphere(gl, 0.4, 10, 10);
        gl.glPopMatrix();

        drawable.swapBuffers();
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int x, int y, int width, int height) {
        GL2 gl = glAutoDrawable.getGL().getGL2();
        float ar = (float) width / (float) height;
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        gl.glOrtho(-6.4, 6.4, -4.8, 4.8, 1.0, -1.0);
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyChar()) {
            case 'r':
                modoRotacion = true;
                modoTraslacion = false;
                break;
            case 't':
                modoRotacion = false;
                modoTraslacion = true;
                break;
            case 'n':
                modoRotacion = true;
                modoTraslacion = true;
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    private void glutWireSphere(GL2 gl, double radius, int slices, int stacks) {
        glutWireSphere(gl, 0, 0, radius, slices, stacks);
    }

    private void glutWireSphere(GL2 gl, double x, double y, double z, double radius, int slices, int stacks) {
        glutWireSphere(gl, x, y, z, radius, slices, stacks, 1.0f, 1.0f);
    }

    private void glutWireSphere(GL2 gl, double x, double y, double z, double radius, int slices, int stacks, float rx, float ry) {
        int i, j;

        for (j = 0; j <= stacks; j++) {
            double latitude1 = Math.PI / 2.0 - j * Math.PI / stacks;
            double sinLat1 = Math.sin(latitude1);
            double cosLat1 = Math.cos(latitude1);
            double latitude2 = Math.PI / 2.0 - (j + 1) * Math.PI / stacks;
            double sinLat2 = Math.sin(latitude2);
            double cosLat2 = Math.cos(latitude2);

            gl.glBegin(GL2.GL_QUAD_STRIP);
            for (i = 0; i <= slices; i++) {
                double longitude = (i == slices ? 0.0 : i * 2.0 * Math.PI / slices);
                double sinLong = Math.sin(longitude);
                double cosLong = Math.cos(longitude);
                double x1 = cosLong * cosLat1;
                double y1 = sinLong * cosLat1;
                double z1 = sinLat1;
                double x2 = cosLong * cosLat2;
                double y2 = sinLong * cosLat2;
                double z2 = sinLat2;

                gl.glNormal3d(x1, y1, z1);
                gl.glTexCoord2d(i / (double) slices, j / (double) stacks);
                gl.glVertex3d(x + rx * radius * x1, y + ry * radius * y1, z + radius * z1);

                gl.glNormal3d(x2, y2, z2);
                gl.glTexCoord2d(i / (double) slices, (j + 1) / (double) stacks);
                gl.glVertex3d(x + rx * radius * x2, y + ry * radius * y2, z + radius * z2);
            }
            gl.glEnd();
        }
    }
}
