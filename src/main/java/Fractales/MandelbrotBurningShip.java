package Fractales;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class MandelbrotBurningShip extends JPanel {
    private final int width;
    private final int height;
    private final int maxIterations = 1000;
    private final double zoom = 200.0;
    private final double offsetX = -1.8;
    private final double offsetY = -0.08;

    public MandelbrotBurningShip(int width, int height) {
        this.width = width;
        this.height = height;
        setPreferredSize(new Dimension(width, height));
    }

    private int burningShip(double x, double y) {
        double zx = 0;
        double zy = 0;
        int iterations = 0;
        while (iterations < maxIterations && zx * zx + zy * zy < 4.0) {
            double temp = zx * zx - zy * zy + x;
            zy = Math.abs(2.0 * zx * zy) + y;
            zx = temp;
            iterations++;
        }
        return iterations;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double zx = x / zoom + offsetX;
                double zy = y / zoom + offsetY;
                int color = burningShip(zx, zy);
                image.setRGB(x, y, Color.HSBtoRGB(color / 256f, 1, color / (color + 8f)));
            }
        }
        g.drawImage(image, 0, 0, this);
    }

    public static void main(String[] args) {
        int width = 800;
        int height = 600;

        JFrame frame = new JFrame("Burning Ship Mandelbrot");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        MandelbrotBurningShip mandelbrotBurningShip = new MandelbrotBurningShip(width, height);
        frame.add(mandelbrotBurningShip);
        frame.pack();
        frame.setVisible(true);
    }
}