/*
 * 
 */
package com.cioxhealth.Utilities;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileReaderTool {
	
	private static final Pattern freqCodeRegex = Pattern.compile(":[A-B]:[0-9]");

	
	public static String readFile(Path filePath) {
		String text = "";

		if (!fileExists(filePath))
			return text;
		try {
			File f = new File(filePath.toString());
			Scanner scanner = new Scanner(f);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				text = text + "\n" + line;
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return text;
	}

	public static boolean fileExists(Path filePath) {
		return Files.exists(filePath);
	}

	public static String readTestFile(String testCaseName) {
		String fileText = "";
		try {
			String jsonFilePath = System.getProperty("user.dir") + "\\src\\test\\java\\com\\ccx\\TestData\\"
					+ testCaseName + ".json";
			JSONObject jsonObject = JsonTool.readJson(jsonFilePath);
			String sourcePath = (String) jsonObject.get("SourcePath");
			String fileName = (String) jsonObject.get("FileName");
			Path sourceFile = Paths.get(sourcePath + "\\" + fileName);
			fileText = FileReaderTool.readFile(sourceFile);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		return fileText;
	}

	public static String readTestFile(String appName, String testCaseName) {
		String fileText = "";
		try {
			String jsonFilePath = System.getProperty("user.dir") + "\\src\\test\\java\\com\\ccx\\TestData\\" + appName
					+ "\\" + testCaseName + ".json";
			JSONObject jsonObject = JsonTool.readJson(jsonFilePath);
			String sourcePath = (String) jsonObject.get("SourcePath");
			String fileName = (String) jsonObject.get("FileName");
			Path sourceFile = Paths.get(sourcePath + "\\" + fileName);
			fileText = FileReaderTool.readFile(sourceFile);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}

		return fileText;
	}

	public static List<Path> getFilesInFolder(Path folderPath, long timeTestStarted) {
		// Get all files in directory
		File directory = folderPath.toFile();
		File[] files = directory.listFiles();
		
		// Remove any files that don't meet date modified time so we don't have to bother sorting them
		List<File> filesToUse = new ArrayList<File>();
		for (File file : files)
		{
			if (file.lastModified() >= timeTestStarted)
				filesToUse.add(file);			
		}
		files = filesToUse.toArray(new File[0]);
		
		// Sort by last Modified descending
		Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());

		// Only add files matching time criteria
		List<Path> filesWithinTimeframe = new ArrayList<Path>();
		for (File file : files) {
			// If file has been created since we started the test, add it to list
			Long lastModified = file.lastModified();
			if (lastModified >= timeTestStarted) {
				filesWithinTimeframe.add(file.toPath());
			} else {
				break;
			}
		}

		return filesWithinTimeframe;
	}




	public static String searchFolderUntilFileContains(String folderPath, String textToMatch, long startTime) {
		// Loop (until file found or timeout reached)
		int elapsedSec = 0;
		int timeoutSec = 120 * 60;
		int waitSec = 5 * 60;
		boolean fileFound = false;
		String fileText = "";

		while (!fileFound && elapsedSec < timeoutSec) {
			// Output current status
			System.out.println("Checking folder | Total Time Elapsed: " + elapsedSec + " Second(s) out of " + timeoutSec
					+ " Seconds | Checking every " + waitSec + " Second(s)");

			// Get all files in folder based
			List<Path> allFiles = FileReaderTool.getFilesInFolder(Path.of(folderPath), startTime);
			System.out.println("Total Files Within Date Criteria: " + allFiles.size());

			// Loop through all files and check if it contains textToMatch
			for (Path fPath : allFiles) {
				fileText = FileReaderTool.readFile(fPath);
				if (fileText.contains(textToMatch)) {
					fileFound = true;
					break;
				}
			}

			// If file not found -> wait
			if (!fileFound) {
				try {
					Thread.sleep(waitSec * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				elapsedSec += waitSec;
			}
		}

		return fileText;
	}

	
	
	public static String getFreqNumber(String fileText) throws IOException, ParseException {
		String freqId = "";
		if (!fileText.isEmpty()) {
			// Finds the last occurence of "CLM*" and gets the text after it
			Matcher matchingString = freqCodeRegex.matcher(fileText);

			//freqId = fileText.substring(fileText.lastIndexOf("CLM*") + 4).trim();
			if(matchingString.find()) {
				freqId = matchingString.group(0).trim();
			}
			System.out.println("FreqId: " + freqId);
			
			// Checks to see if their's text after the Unid
			if (freqId.lastIndexOf(':') != -1) {
				// If there's text after the unique id remove it all
				freqId = freqId.substring(freqId.lastIndexOf(':')+1);
			}
		}

		return freqId;
	}
	
}
