package com.booktree.booktreespring;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;

@SpringBootTest
class BooktreeSpringApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	void 파일테스트() throws IOException {
		String filePath = "C:\\Users\\choi\\Desktop\\booktree\\booktree-spring\\src\\main\\java\\com\\booktree\\booktreespring\\Image\\";
		String fileName = "4e1463c7-52e7-4f57-9642-7a6fa6476d7d.jpg";

		File file = new File(filePath + fileName);
		System.out.println(file.toPath());

		FileItem fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
		try {
			System.out.println(2);
			InputStream input = new FileInputStream(file);
			OutputStream os = fileItem.getOutputStream();
			IOUtils.copy(input, os);
			System.out.println(3);
			// Or faster..
			// IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
		} catch (IOException ex) {
			// do something.
		}
		MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
		System.out.println(fileName + "파일을 반환합니다. ");
	}
}
