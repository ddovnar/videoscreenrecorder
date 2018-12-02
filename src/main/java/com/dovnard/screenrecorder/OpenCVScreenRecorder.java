package com.dovnard.screenrecorder;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;

public class OpenCVScreenRecorder extends ScreenRecorder {
	private String folder;
	private FFmpegFrameRecorder recorderMP4;
	private Java2DFrameConverter grabberConverter;
	private File tempFile;
	private String recordedFile;
	public OpenCVScreenRecorder(boolean useWhiteCursor, ScreenRecorderListener listener) {
		super(useWhiteCursor, listener);
        grabberConverter = new Java2DFrameConverter();
	}
	public File getTempFile() {
		return tempFile;
	}
	public String getFolder() {
		return folder;
	}
	public void setFolder(String folder) {
		this.folder = folder;
	}
	
	public String getRecordedFile() {
		return recordedFile;
	}
	
	@Override
	public void stop() throws Exception {
		stopRecording();
		recorderMP4.stop();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Date date = new Date();
		String recordFileName = "video" + dateFormat.format(date);		
		String absoluteDiskPath = getFolder() + "/" + recordFileName;
		if (!absoluteDiskPath.endsWith(".mp4"))
			absoluteDiskPath = absoluteDiskPath + ".mp4";
		File target = new File( absoluteDiskPath );
		FileUtils.copy(tempFile, target);
		FileUtils.delete(tempFile);
		
		System.out.println("Recorded file:" + absoluteDiskPath);
		recordedFile = absoluteDiskPath;
	}

	@Override
	public void start() throws Exception {
		try {
			tempFile = File.createTempFile("temp", "mp4");
			recordedFile = "";
			recorderMP4 = new FFmpegFrameRecorder(tempFile.getAbsolutePath(),1920,1080);
	        recorderMP4.setFrameRate(5);  
	        //recorderMP4.setVideoCodec(avcodec.AV_CODEC_ID_H264); // windows working
	        recorderMP4.setVideoCodec(avcodec.AV_CODEC_ID_MPEG4);//linux working
	        //recorderMP4.setVideoCodec(avcodec.AV_CODEC_ID_FLV1);
	        recorderMP4.setVideoBitrate(1000);
	        recorderMP4.setFormat("mp4");
	        //recorderMP4.setFormat("fvl");
	        recorderMP4.setVideoQuality(25);
	        recorderMP4.setVideoOption("threads", "1");
	        recorderMP4.start();
	        
	        startRecording();
		} catch(IOException ex) {
			ex.printStackTrace();
			throw new Exception("TempFile not created");
		}
	}
	
	@Override
	protected synchronized void recordFrame() {
		BufferedImage image = super.getScreenImage();
	      
    	try {
    		recorderMP4.record(grabberConverter.convert(image), avutil.AV_PIX_FMT_ARGB );
    		System.gc();
    	} catch (org.bytedeco.javacv.FrameRecorder.Exception e){  
    		e.printStackTrace();  
    	}
		super.recordFrame();
	}
}
