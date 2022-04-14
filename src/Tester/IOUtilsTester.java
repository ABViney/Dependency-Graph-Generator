package Tester;

import java.io.File;
import java.io.IOException;

import com.util.IOUtils;

public class IOUtilsTester {
	
	public static void main(String[] args) {
		try {
			String content = IOUtils.readToString(new File("DGG-roadmap.txt"));
			System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
