package com.example.demo.audio;

import java.io.File;
import java.io.IOException;

public class FileSend {

    // 인증 & 등록 음성 파일 전송
    public static String audioFileSend(String localPath, String reqType) {


        // aws pem키 절대경로
        String pemKeyPath = "/Users/seojeong/Desktop/sv_aws/ation.pem";

        // 로컬 저장경로
        String localFilePath = localPath; // 보낼 로컬 파일 경로
        System.out.println("여기 경로에 있는 음성파일 보내야함!: " + localFilePath);

        // 파일 이름 출력
        File file = new File(localFilePath);
        String fileName = "";
        if (file.exists()) {
            fileName = file.getName();
            System.out.println("파일 이름: " + fileName);
        } else {
            throw new Error("파일이 존재하지 않습니다.");
        }

        // 원격 서버 IP (현재 기준 분류 ip: 43.202.10.41, 인증&등록: 43.202.161.88)
        String hostIp = reqType == "classifi" ? "43.202.10.41" : "43.202.161.88";

        String remoteFilePath = "ec2-user@" + hostIp + ":/home/ec2-user/wav_dir/" + fileName;

        //인증
        //String remoteFilePath = "ec2-user@43.202.10.41:/home/ec2-user/wav_dir/" + fileName; // 로컬에서 scp로 보낸 wav 파일이 저장되는 경로

        // 분류
        //String remoteFilePath = "ec2-user@43.202.161.88:/home/ec2-user/wav_dir/" + fileName;

        try {
            // scp 명령 실행
            String scpCommand = "scp -i " + pemKeyPath + " " + localFilePath + " " + remoteFilePath;

            // scp 명령 실행
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", scpCommand);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("파일이 성공적으로 전송되었습니다.");
                return "success";
            } else {
                System.out.println("파일 전송 실패");
                return "file send error";
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return "error";
    }

}
