package com.dovnard.screenrecorder;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.imageio.ImageIO;

import org.bytedeco.javacpp.avutil;
import org.bytedeco.javacv.FFmpegFrameRecorder;
import org.bytedeco.javacv.FrameRecorder.Exception;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class OpenCVHttpScreenRecorder extends ScreenRecorder {
	private FFmpegFrameRecorder recorderMP4;
	private Java2DFrameConverter grabberConverter;
	private ServerSocket server;
	private Socket socket;
	private final String boundary = "stream";
	public OpenCVHttpScreenRecorder(boolean useWhiteCursor, ScreenRecorderListener listener) {
		super(useWhiteCursor, listener);
        grabberConverter = new Java2DFrameConverter();
        try {
			server = new ServerSocket(8080);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void stop() throws Exception {
		stopRecording();
	}
	@Override
	public void start() throws Exception {
		try {
			socket = server.accept();
			writeHeader(socket.getOutputStream(), boundary);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("Start error");
		}
		startRecording();
	}
	
	@Override
	protected synchronized void recordFrame() {
		BufferedImage image = super.getScreenImage();
	      
    	//try {
    		//recorderMP4.record(grabberConverter.convert(image), avutil.AV_PIX_FMT_ARGB );
    		System.out.println("Frame captured");
    		
    		try {
//                Date today = new Date();
//                String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + today;
//                socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
    			if (isRecording()) {
	    			OutputStream outputStream = socket.getOutputStream();
	    			ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    			ImageIO.write(image, "jpg", baos);
	    			byte[] imageBytes = baos.toByteArray();
	                outputStream.write(("Content-type: image/jpeg\r\n" +
	                        "Content-Length: " + imageBytes.length + "\r\n" +
	                        "\r\n").getBytes());
	                outputStream.write(imageBytes);
	                outputStream.write(("\r\n--" + boundary + "\r\n").getBytes());
	                outputStream.flush();
    			} else {
    				String httpResponse = "HTTP/1.1 200 OK\r\n\r\n" + "NOT RECORDING";
    				socket.getOutputStream().write(httpResponse.getBytes("UTF-8"));
    			}
            } catch(IOException ex) {
            	ex.printStackTrace();
            	try {
            		socket = server.accept();
            		writeHeader(socket.getOutputStream(), boundary);
            	} catch(IOException iex) {
            		iex.printStackTrace();
            	}
            }
    		
    		System.gc();
    	//} catch (org.bytedeco.javacv.FrameRecorder.Exception e){  
    	//	e.printStackTrace();  
    	//}
		super.recordFrame();
	}
	
	public Mat bufferedImageToMat(BufferedImage bi) {
		  Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
		  byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
		  mat.put(0, 0, data);
		  return mat;
	}
	
	private void writeHeader(OutputStream stream, String boundary) throws IOException {
        stream.write(("HTTP/1.0 200 OK\r\n" +
                "Connection: close\r\n" +
                "Max-Age: 0\r\n" +
                "Expires: 0\r\n" +
                "Cache-Control: no-store, no-cache, must-revalidate, pre-check=0, post-check=0, max-age=0\r\n" +
                "Pragma: no-cache\r\n" +
                "Content-Type: multipart/x-mixed-replace; " +
                "boundary=" + boundary + "\r\n" +
                "\r\n" +
                "--" + boundary + "\r\n").getBytes());
    }
}
