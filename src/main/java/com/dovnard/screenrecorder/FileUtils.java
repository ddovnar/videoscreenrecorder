package com.dovnard.screenrecorder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtils {
	public static boolean delete(File file) {
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
	public static long copy(File fileSrc, File fileDest) {
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
	      return count;
	   }
}
