package com.dovnard.screenrecorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacpp.avcodec;
import org.bytedeco.javacv.FrameRecorder.Exception;

public class ScreenRecorder {
	private boolean whiteCursor = true;
	private File tempFile;
	private FFmpegFrameRecorder recorderMP4;
	private BufferedImage mouseCursor;
	private Robot robot;
	private Java2DFrameConverter grabberConverter;
	private String SCREEN_RECORDER_DATA = "/home/ddovnar/Downloads/temp";
	
	public ScreenRecorder() {
		try {
			String mouseCursorFile;

         	if (whiteCursor)
        	 	mouseCursorFile = "white_cursor.png";
         	else
        	 	mouseCursorFile = "black_cursor.png";
//System.out.println("SDDDDD:" + getClass().getClassLoader().getResource("mouse_cursors/" + mouseCursorFile).getPath());
         	URL cursorURL = getClass().getClassLoader().getResource(
               "mouse_cursors/" + mouseCursorFile);
         	//URL cursorURL = new File(System.getenv("SCREEN_RECORDER_DATA") + "/resources/mouse_cursors/" + mouseCursorFile).toURI().toURL();

         	mouseCursor = ImageIO.read(cursorURL);
         	
         	robot = new Robot();
         	grabberConverter = new Java2DFrameConverter();
		} catch (IOException | AWTException e) {
			e.printStackTrace();
		}
	}
	public void start() throws IOException {
		tempFile = File.createTempFile("temp", "mp4");
		
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
	}
	
	public void stop() throws IOException {
		recorderMP4.stop();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		Date date = new Date();
		String recordFileName = "video" + dateFormat.format(date);		
		String absoluteDiskPath = SCREEN_RECORDER_DATA + "/" + recordFileName;
		if (!absoluteDiskPath.endsWith(".mp4"))
			absoluteDiskPath = absoluteDiskPath + ".mp4";
		File target = new File( absoluteDiskPath );
		copy(tempFile, target);
		delete(tempFile);
		
		System.out.println("Recorded file:" + absoluteDiskPath);
	}
	public boolean delete(File file) {
	      if (file.isDirectory()) {
	         String[] children = file.list();
	         for (int i = 0; i < children.length; i++) {
	            boolean success = delete(new File(file, children[i]));
	            if (!success) {
	               return false;
	            }
	         }
	      }

	      // The directory is now empty so delete it
	      return file.delete();
	   }
	public static String getPath(String fileName) {
	      File file = new File(fileName);
	      return fileName.substring(0, fileName.indexOf(file.getName()));
	   }
	public void copy(File fileSrc, File fileDest) {
	      byte[] buffer = new byte[5000];
	      long count = 0;
	      int sizeRead;

	      try {
	         FileInputStream iStream = new FileInputStream(fileSrc);

	         new File(getPath(fileDest.toString())).mkdirs();

	         FileOutputStream oStream = new FileOutputStream(fileDest);

	         sizeRead = iStream.read(buffer);
	         while (sizeRead > 0) {
	            oStream.write(buffer, 0, sizeRead);
	            oStream.flush();
	            count += sizeRead;
	            
	            sizeRead = iStream.read(buffer);
	         }

	         iStream.close();
	         oStream.flush();
	         oStream.close();

	         
	      } catch (IOException e) {
	         e.printStackTrace();
	      }
	   }
	public BufferedImage recordScreen() {
	      Point mousePosition = MouseInfo.getPointerInfo().getLocation();
	      BufferedImage image = robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	      //image.setRGB( 0, 0, 0x112233 );
	      Graphics2D grfx = image.createGraphics();

	      grfx.drawImage(mouseCursor, mousePosition.x - 8, mousePosition.y - 5,
	            null);
	      
	      
	      grfx.setColor(Color.YELLOW);
	      grfx.setStroke(new BasicStroke(5));
	      grfx.drawOval(mousePosition.x - 5, mousePosition.y - 5, 5, 5);
	      
	      grfx.dispose();
	      
	      if (this.recorderMP4 != null) {
	    	  try {
	    		  //recorderMP4.start();
	    		  
	    		  recorderMP4.record(grabberConverter.convert(image), avutil.AV_PIX_FMT_ARGB );
	    		  System.gc();
	    	  } catch (org.bytedeco.javacv.FrameRecorder.Exception e){  
	    		  e.printStackTrace();  
		      }
	      }
	      return image;
	   }
}
