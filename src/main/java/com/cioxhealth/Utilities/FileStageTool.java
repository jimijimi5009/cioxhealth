/**
 * 
 */
package com.cioxhealth.Utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileStageTool {

	public static boolean dropAfile(String sourcePath, String destPath, String fileName) {
		boolean fileDropped = false;

		Path sourceFile = Paths.get(sourcePath + "\\" + fileName);
		Path destFile = Paths.get(destPath + "\\" + fileName);
		try {
			if (Files.exists(sourceFile)) {
				if (Files.notExists(destFile)) {
					Files.copy(sourceFile, destFile);
					System.out.println("File is staged successfully.");
				} else {
					Files.copy(sourceFile, destFile, StandardCopyOption.REPLACE_EXISTING);
					System.out.println("File already exist in destination path.");
				}
				fileDropped = true;
			} else {
				System.out.println("File is missing in source path or File name is not matching.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return fileDropped;
	}
}
