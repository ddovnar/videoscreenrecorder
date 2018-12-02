package com.dovnard.screenrecorder;

import java.io.IOException;


public interface ScreenRecorderListener {
   public void onFrameRecorded(long frameRecorded);
   public void onStopRecording();
   public void onStartRecording();
}
