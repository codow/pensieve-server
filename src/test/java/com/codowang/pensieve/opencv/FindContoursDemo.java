package com.codowang.pensieve.opencv;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

class FindContours {
    private Mat srcGray = new Mat();
    private JFrame frame;
    private JLabel imgSrcLabel;
    private JLabel imgContoursLabel;
    private static final int MAX_THRESHOLD = 255;
    private int threshold = 100;
    private Random rng = new Random(12345);
    private int width = 1024;
    private int height = 768;

    public FindContours(String[] args) {
        String filename = args.length > 0 ? args[0] : "../data/HappyFish.jpg";
        Mat src = Imgcodecs.imread(filename);
        if (src.empty()) {
            System.err.println("Cannot read image: " + filename);
            System.exit(0);
        }
        Imgproc.cvtColor(src, srcGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.blur(srcGray, srcGray, new Size(3, 3));
        // Create and set up the window.
        frame = new JFrame("Finding contours in your image demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Set up the content pane.
        BufferedImage img = (BufferedImage)HighGui.toBufferedImage(src);
        addComponentsToPane(frame.getContentPane(), img);
        // Use the content pane's default BorderLayout. No need for
        // setLayout(new BorderLayout());
        // Display the window.
        // frame.pack();
        // 最大化
//        frame.getMaximizedBounds();
        frame.setResizable(false);
        frame.setLocation(200, 200);
        frame.setSize(width, height);
        frame.setVisible(true);
        update();
    }
    private void addComponentsToPane(Container pane, BufferedImage img) {
        if (!(pane.getLayout() instanceof BorderLayout)) {
            pane.add(new JLabel("Container doesn't use BorderLayout!"));
            return;
        }
        JPanel sliderPanel = new JPanel();
        sliderPanel.setLayout(new BoxLayout(sliderPanel, BoxLayout.PAGE_AXIS));
        sliderPanel.add(new JLabel("Canny threshold: "));
        JSlider slider = new JSlider(0, MAX_THRESHOLD, threshold);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                threshold = source.getValue();
                update();
            }
        });
        sliderPanel.add(slider);
        pane.add(sliderPanel, BorderLayout.PAGE_START);
        // 获取容器宽高
        // 计算二分宽高
        int imageWidth = width / 2 - 10;
        int imageHeight = (int)(img.getHeight() * 1.0d / img.getWidth() * imageWidth);
        JPanel imgPanel = new JPanel();
        Image newImg = img.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        imgSrcLabel = new JLabel(new ImageIcon(newImg));
        imgPanel.add(imgSrcLabel);
        Mat blackImg = Mat.zeros(srcGray.size(), CvType.CV_8U);
        Image newBlackImg = HighGui.toBufferedImage(blackImg).getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        imgContoursLabel = new JLabel(new ImageIcon(newBlackImg));
        imgPanel.add(imgContoursLabel);
        pane.add(imgPanel, BorderLayout.CENTER);
    }
    private void update() {
        Mat cannyOutput = new Mat();
        Imgproc.Canny(srcGray, cannyOutput, threshold, threshold * 2);
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(cannyOutput, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat drawing = Mat.zeros(cannyOutput.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            Scalar color = new Scalar(rng.nextInt(256), rng.nextInt(256), rng.nextInt(256));
            Imgproc.drawContours(drawing, contours, i, color, 2, Imgproc.LINE_8, hierarchy, 0, new Point());
        }
        BufferedImage img = (BufferedImage)HighGui.toBufferedImage(drawing);
        int imageWidth = width / 2 - 10;
        int imageHeight = (int)(img.getHeight() * 1.0d / img.getWidth() * imageWidth);
        Image newBlackImg = img.getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH);
        imgContoursLabel.setIcon(new ImageIcon(newBlackImg));
        frame.repaint();
    }
}

public class FindContoursDemo {
    public static final String PDF_SOURCE_DIR = "D:\\Users\\codowang\\Desktop\\ocr\\src";
    public static final String PDF_TARGET_DIR = "D:\\Users\\codowang\\Desktop\\ocr\\target";

    public static void main(String[] args) {
        // Load the native OpenCV library
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 设置
        String targetDirName = "copybook";
        String imageName = "0.png";

        String imagePath = PDF_TARGET_DIR + File.separator + targetDirName + File.separator + imageName;

        String[] invokeArgs = new String[]{ imagePath };

        // Schedule a job for the event dispatch thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FindContours(invokeArgs);
            }
        });
    }
}