package com.booktree.booktreespring.Service;


import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.util.UUID;

@Service
public class FileService {
    String filePath = "C:\\Users\\choi\\Desktop\\booktree\\booktree-spring\\src\\main\\java\\com\\booktree\\booktreespring\\Image\\";
    /**
     * 파일 업로드
     */
    public String uploadFile(MultipartFile multipartFile){
        String filename = UUID.randomUUID().toString() + ".jpg";
        try {
            multipartFile.transferTo(new File( filePath + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filename;
    }

    /**
     * 파일 내보내기
     */
    public MultipartFile getFile(String filename) throws IOException {
        System.out.println(filename + " 파일을 디스크에서 찾습니다.");
        File file = new File( filePath + filename);
        System.out.println("여기가 오류? " + file.toPath());
        System.out.println(1);
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
        System.out.println(4);
        MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
        System.out.println(filename + "파일을 반환합니다. ");
        return multipartFile;
    }

    // MultipartFile multipartFile = new MockMultipartFile("test.xlsx", new FileInputStream(new File("/home/admin/test.xlsx")));
}
