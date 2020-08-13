package com.demo;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.videoio.VideoCapture;

public class Demo extends JFrame {

	private JLabel label;
	private ImageIcon icon;
	private VideoCapture capture;
	private Mat image;
	private boolean clicked = false, closed = false;

	public Demo() {
		setLayout(null);

		label = new JLabel();
		label.setBounds(0, 0, 640, 480);
		add(label);

		JButton btn = new JButton("capture");
		btn.setBounds(300, 480, 80, 40);
		add(btn);
		
		btn.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clicked = true;
			}
		});
		
		addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing (WindowEvent e) {
				super.windowClosed(e);
				capture.release();
				image.release();
				closed = true;
				System.out.println("closed");
				System.exit(0);
				
			}
			
			@Override
			public void windowDeactivated(WindowEvent e) {
				super.windowDeactivated(e);
				System.out.println("closed");
			}
			
		});
		
		setFocusable(false);
		setSize(640, 560);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
	}

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Demo d = new Demo();
				new Thread(new Runnable() {
					public void run() {
						d.startCamera();
					}
				}).start();
			}
		});

	}

	public void startCamera() {

		capture = new VideoCapture(0);
		image = new Mat();
		byte[] imageData;
		while (true) {
			capture.read(image);

			final MatOfByte buf = new MatOfByte();
			Imgcodecs.imencode(".jpg", image, buf);

			imageData = buf.toArray();

			icon = new ImageIcon(imageData);
			label.setIcon(icon);
			System.out.println(image.cols());
			if (clicked) {
				String name = JOptionPane.showInputDialog(this, "Enter image name");
				if(name == null) {
					name = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
				}
				Imgcodecs.imwrite("images/" + name +".jpg", image);
				clicked = false;
			}
			if (closed) {
				break;
			}
		}
	}

}