package com.dovnard.screenrecorder;

import java.io.IOException;

import org.bytedeco.javacv.FrameRecorder.Exception;

public class Main implements ScreenRecorderListener {
	private OpenCVScreenRecorder scr;
	public void start() throws Exception {
		scr = new OpenCVScreenRecorder(true, this);
		scr.setFolder("/home/ddovnar/Downloads/temp");
		scr.start();
	}
	public static void main(String[] args) throws IOException {
		System.out.println("ScreenRecorder running...");
		Main m = new Main();
		m.start();
	}
	/*public static void main1(String[] args) throws IOException {
		System.out.println("ScreenRecorder running...");
		
		ScreenRecorderTest recorder = new ScreenRecorderTest();
		recorder.start();
		
		long lastFrameTime = 0;
		long frameRecorded = 0;
	    long time = 0;
		while (true) {
	         time = System.currentTimeMillis();
	         while (time - lastFrameTime < 190) {
	         //while (time - lastFrameTime < 2000) {
	            try {
	               Thread.sleep(10);
	               //Thread.sleep(20);
	            } catch (Exception e) {
	            }
	            time = System.currentTimeMillis();
	         }
	         lastFrameTime = time;
	         System.out.println("FrameTime:" + lastFrameTime);

	         try {
	            //recordFrame();
	        	 recorder.recordScreen();
	        	 frameRecorded++;
	        	 System.out.println("Record frame: " + frameRecorded);
	        	 if (frameRecorded > 100) {
	        		 break;
	        	 }
	         } catch (Exception e) {
	            e.printStackTrace();
	            break;
	         }
	      }
		
		recorder.stop();
	}*/
	@Override
	public void onFrameRecorded(long frameRecorded) {
		System.out.println("Main onFrameRecorded..." + frameRecorded);
		if (frameRecorded > 20) {
			try {
				scr.stop();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	@Override
	public void onStopRecording() {
		System.out.println("Main onStopRecording...");
	}
	@Override
	public void onStartRecording() {
		System.out.println("Main onStartRecording...");
	}
	
}
