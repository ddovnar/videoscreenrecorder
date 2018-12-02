package com.dovnard.test;

import java.io.File;

import org.bytedeco.javacv.FrameRecorder.Exception;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ScreenRecorderTestSuite {
	@BeforeTest
	protected void onBeforeTest() {
		System.out.println("Test started");
	}
	@AfterTest
	protected void onAfterTest() {
		System.out.println("Test finish");
	}
	
	@Test(priority = 1, description="")
	public void test1() throws Exception, InterruptedException {
		OpenCVScreenRecorderTest scr = new OpenCVScreenRecorderTest();
		scr.start();
		System.out.println("Video file recorded: " + scr.getRecordedFile());
		boolean fileOk = false;
		if (scr.getRecordedFile() != null) {
			File f = new File(scr.getRecordedFile());
			fileOk = f.exists();
		}
		Assert.assertEquals(fileOk, true);
	}
}
