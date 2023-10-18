package com.example.demo.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class TcpClient {

    public static String sendReqToServer() {
        String serverHost = "43.202.10.41"; // 원격 서버의 호스트 주소
        int serverPort = 9101; // 원격 서버의 포트 번호

        try (Socket socket = new Socket(serverHost, serverPort);
             OutputStream out = socket.getOutputStream();
             InputStream in = socket.getInputStream();
        ) {

            JSONObject jsonObj = new JSONObject();
            jsonObj.put("wavPath","/home/ec2-user/wav_dir/recorded_audio_8k.wav");
            jsonObj.put("modelPath","/home/ec2-user/model_dir/enroll.pt");


            String jsonString = jsonObj.toString();

            System.out.println("json body string: " + jsonString);
            int jsonLen = jsonString.length(); // json의 길이

            // RequestHeader
            String apiCode = "1000";
            String resultCode = "0000";
            String bodySize = String.format("%08d", jsonString.length());


            String header = apiCode + resultCode + bodySize;

            // 전체 요청 메시지 생성
            String requestMessage = header + jsonString;
            System.out.println("최종요청 스트링: " + requestMessage);

            // 요청 메시지 전송
            out.write(requestMessage.getBytes());

            byte[] responseByte = new byte[4096];
            int bytesRead = in.read(responseByte);

            if (bytesRead != -1) {
                // 서버로부터 응답 바이트 배열을 읽기
                String response = new String(responseByte, 0, bytesRead, "UTF-8");
                System.out.println("서버 응답: " + response);


                System.out.println(response);

                String responseApiCode = response.substring(0,4);
                String responseResultCode = response.substring(4,8);
                String responseBodySize = response.substring(8,16);

                String responseBody = response.substring(16);

                System.out.println("apiCode: " + responseApiCode);
                System.out.println("resultCode: " + responseResultCode);
                System.out.println("bodySize: " + responseBodySize);

                System.out.println("responseBody: " + responseBody);

                return responseBody;

            }

            socket.close();
            out.close();
            in.close();
            jsonObj.clear();
            jsonObj = null;

            return "error";

        } catch (IOException e) {
            e.printStackTrace();

            return "error";

        }
    }
}
