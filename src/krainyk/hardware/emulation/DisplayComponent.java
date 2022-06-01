package krainyk.hardware.emulation;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class DisplayComponent extends JPanel {
    public static int IMAGE_SIZE = 128;
    private BufferedImage imageForDisplay = new BufferedImage(128, 128, BufferedImage.TYPE_USHORT_565_RGB);
    private DisplayController displayController = new DisplayController(this);
    private int pointx = 0, pointy = 0;

    public DisplayComponent() {
        super();
        Random rnd = new Random();
        for (int i = 0; i < IMAGE_SIZE; i++) {
            for (int j = 0; j < IMAGE_SIZE; j++) {
                short val = (short)rnd.nextInt(65536);
                byte r = (byte)((val >> 11) & 0x1f);
                byte g = (byte)((val >> 5) & 0x3f);
                byte b = (byte)(val & 0x1f);
                int rgb = (r << 11) + (g << 5) + b;
                imageForDisplay.setRGB(i, j, rgb);
            }
        }
    }

    public void clearImage(int color) {
        for (int i = 0; i < IMAGE_SIZE; i++) {
            for (int j = 0; j < IMAGE_SIZE; j++) {
                imageForDisplay.setRGB(i, j, color);
            }
        }
    }

    public void receiveData(byte[] buffer, int len) {
        // consider each 2 bytes as a value of a pixel
        for (int i = 0; i < len / 2; i++) {
            int rgb = (buffer[i] << 8) + buffer[i*2+1];
            imageForDisplay.setRGB(pointx, pointy, rgb);
            pointx++;
            System.out.println(pointx + " " + pointy);
            if (pointx == IMAGE_SIZE) {
                pointx = 0;
                pointy++;
                System.out.println(pointy);
                if (pointy == IMAGE_SIZE) {
                    pointy = 0;
                }
            }
        }
    }

    public void setRGB(int x, int y, int RGBval) {
        imageForDisplay.setRGB(x, y, RGBval);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imageForDisplay, 0, 0, null);
    }

    @Override
    public int getWidth() {
        return IMAGE_SIZE;
    }

    @Override
    public int getHeight() {
        return IMAGE_SIZE;
    }
}
