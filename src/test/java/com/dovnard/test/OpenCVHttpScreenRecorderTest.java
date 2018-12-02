package com.dovnard.test;

import org.bytedeco.javacv.FrameRecorder.Exception;

import com.dovnard.screenrecorder.OpenCVHttpScreenRecorder;
import com.dovnard.screenrecorder.ScreenRecorderListener;

public class OpenCVHttpScreenRecorderTest implements ScreenRecorderListener {
	private OpenCVHttpScreenRecorder scr;
	public OpenCVHttpScreenRecorderTest() {
		scr = new OpenCVHttpScreenRecorder(true, this);
	}
	public void start() throws Exception, InterruptedException {
		scr.start();
		scr.getTask().join();
		
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartRecording() {
		// TODO Auto-generated method stub
		
	}

}
