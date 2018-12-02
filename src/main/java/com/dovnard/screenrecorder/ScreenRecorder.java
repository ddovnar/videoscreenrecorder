package com.dovnard.screenrecorder;

import java.awt.AWTException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

import org.bytedeco.javacv.FrameRecorder.Exception;

public abstract class ScreenRecorder implements Runnable {
	private boolean recording = false;
	private BufferedImage mouseCursor;
	private Robot robot;
	private long frameRecorded;
	private ScreenRecorderListener listener;
	private Thread task;
	
	public ScreenRecorder(boolean useWhiteCursor, ScreenRecorderListener listener) {
		this.listener = listener;
		try {
			String mouseCursorFile;

         	if (useWhiteCursor)
        	 	mouseCursorFile = "white_cursor.png";
         	else
        	 	mouseCursorFile = "black_cursor.png";
//System.out.println("SDDDDD:" + getClass().getClassLoader().getResource("mouse_cursors/" + mouseCursorFile).getPath());
         	URL cursorURL = getClass().getClassLoader().getResource(
               "mouse_cursors/" + mouseCursorFile);
         	//URL cursorURL = new File(System.getenv("SCREEN_RECORDER_DATA") + "/resources/mouse_cursors/" + mouseCursorFile).toURI().toURL();

         	mouseCursor = ImageIO.read(cursorURL);
         	
         	robot = new Robot();
		} catch (IOException | AWTException e) {
			e.printStackTrace();
		}

	}
	
	public BufferedImage getScreenImage() {
		Point mousePosition = MouseInfo.getPointerInfo().getLocation();
		BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
		Graphics2D grfx = image.createGraphics();

		grfx.drawImage(mouseCursor, mousePosition.x - 8, mousePosition.y - 5, null);
	      
	    grfx.setColor(Color.YELLOW);
	    grfx.setStroke(new BasicStroke(5));
	    grfx.drawOval(mousePosition.x - 5, mousePosition.y - 5, 5, 5);
	      
	    grfx.dispose();
	    return image;
	}
	
	protected synchronized void recordFrame() {
		listener.onFrameRecorded(frameRecorded);
	}
	
	public long getFrameRecorded() {
		return frameRecorded;
	}
	public void startRecording() {
		task = new Thread(this, "Screen Recorder");
		task.start();
	}
	public Thread getTask() {
		return task;
	}
	public void stopRecording() {
		recording = false;
		
	}
	public abstract void stop() throws Exception;
	public abstract void start() throws Exception;
	
	public synchronized void run() {
		long lastFrameTime = 0;
		frameRecorded = 0;
	    long time = 0;
	    recording = true;
	    listener.onStartRecording();
		while (recording) {
	         time = System.currentTimeMillis();
	         while (time - lastFrameTime < 190) {
	         //while (time - lastFrameTime < 2000) {
	            try {
	               Thread.sleep(10);
	               //Thread.sleep(20);
	            } catch (InterruptedException e) {
	            }
	            time = System.currentTimeMillis();
	         }
	         lastFrameTime = time;
	         System.out.println("FrameTime:" + lastFrameTime);

	         //try {
	         	recordFrame();
	        	frameRecorded++;
	        	
	         //} catch (Exception e) {
	         //   e.printStackTrace();
	         //   break;
	         //}
		}
		recording = false;
		listener.onStopRecording();
	}
	public boolean isRecording() {
		return this.recording;
	}
}
