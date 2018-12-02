package com.dovnard.test;

import org.bytedeco.javacv.FrameRecorder.Exception;

import com.dovnard.screenrecorder.OpenCVScreenRecorder;
import com.dovnard.screenrecorder.ScreenRecorderListener;

public class OpenCVScreenRecorderTest implements ScreenRecorderListener {
	private OpenCVScreenRecorder scr;
	public OpenCVScreenRecorderTest() {
		scr = new OpenCVScreenRecorder(true, this);
		scr.setFolder("/home/ddovnar/Downloads/temp");
	}
	public void start() throws Exception, InterruptedException {
		scr.start();
		scr.getTask().join();
	}
	public String getRecordedFile() {
		return scr.getRecordedFile();
	}
	@Override
	public void onFrameRecorded(long frameRecorded) {
		System.out.println("Test.onFrameRecorded:"+ frameRecorded);
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
		System.out.println("Test.onStopRecording");
	}

	@Override
	public void onStartRecording() {
		System.out.println("Test.onStartRecording");
	}
	
}
