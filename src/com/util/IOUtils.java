package com.util;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class IOUtils {
	
	/**
	 * Load file contents as string
	 * 
	 * @param target
	 * @return
	 * @throws IOException
	 */
	public static String readToString(File target) throws IOException {
		return new String(Files.readAllBytes(target.toPath()), StandardCharsets.UTF_8);
	}
	
}
