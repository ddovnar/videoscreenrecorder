package com.dovnard.screenrecorder;

import java.io.IOException;

public class Main {
	
	public static void main(String[] args) throws IOException {
		System.out.println("ScreenRecorder running...");
		
		ScreenRecorder recorder = new ScreenRecorder();
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
	}
	
}
