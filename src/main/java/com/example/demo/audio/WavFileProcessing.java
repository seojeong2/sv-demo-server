package com.example.demo.audio;

import java.io.IOException;

public class WavFileProcessing {


    public static void sendWavFileToServer() {
        try{
            ProcessBuilder processBuilder = new ProcessBuilder("scp","/Users/seojeong/Downloads/demo/wav/recorded_audio.wav","ec2-user@43.202.10.41:/home/ec2-user/test_dir");
            Process process = processBuilder.start();

            // 실행 결과 출력
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("SCP 성공");
            } else {
                System.out.println("SCP 실패");
            }
        } catch (IOException | InterruptedException e ){
            e.printStackTrace();
        }
    }
}
