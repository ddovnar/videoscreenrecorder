package com.dovnard.screenrecorder;

import java.io.IOException;

public interface ScreenRecorderListener {
   public void frameRecorded(boolean fullFrame) throws IOException;
   public void recordingStopped();
}
