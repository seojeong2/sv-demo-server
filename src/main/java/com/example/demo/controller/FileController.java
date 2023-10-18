package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Controller
public class FileController {

    @GetMapping("/readFile")
    @ResponseBody
    public String readFile() {
        // 로컬 파일 경로 지정
        String filePath = "/Users/seojeong/Downloads/demo/wav/recorded_audio.wav";

        try {
            // 파일 읽기
            byte[] fileContent = Files.readAllBytes(Paths.get(filePath));
            String content = new String(fileContent);

            // 파일 내용 콘솔에 출력
            System.out.println(content);

            return "파일 내용: " + content;
        } catch (IOException e) {
            e.printStackTrace();
            return "파일을 읽을 수 없음";
        }
    }

}
