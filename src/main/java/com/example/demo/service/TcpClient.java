package com.example.demo.service;

import java.io.*;
import java.net.Socket;

import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TcpClient {

    // 화자인증 엔진 요청
    public static String sendReqToServer(String reqType, String userId) {
        String serverHost = "43.202.10.41"; // 원격 서버의 호스트 주소
        int serverPort = 9101; // 원격 서버의 포트 번호

        log.info("reqType: " + reqType);
        log.info("userId: " + userId);

        String reqFileType = reqType == "registration" ? "reg" : "auth";

        try (Socket socket = new Socket(serverHost, serverPort);
             OutputStream out = socket.getOutputStream();
        ) {

            JSONObject jsonObj = new JSONObject();
            // jsonObj.put("wavPath","/home/ec2-user/wav_dir/recorded_audio_8k.wav");
            jsonObj.put("wavPath", "/home/ec2-user/wav_dir/" + userId + "_" + reqFileType + "_audio.wav"); // 녹음한 음성을 보내는게 맞음 -> 인증용이냐 등록용이냐
            jsonObj.put("modelPath", "/home/ec2-user/model_dir/" + userId + ".pt"); // 모델은 등록할때 쓴 음성이 맞음 -> 입력한 사용자명으로 모델 만들어지도록


            String jsonString = jsonObj.toString();

            System.out.println("json body string: " + jsonString);
            int jsonLen = jsonString.length(); // json의 길이

            // RequestHeader
            //String apiCode = "1000";

            String apiCode = reqType == "registration" ? "1000" : "2000"; // 등록: 1000, 인증:2000
            String resultCode = "0000"; // 고정
            String bodySize = String.format("%08d", jsonString.length());


            String header = apiCode + resultCode + bodySize;

            // 전체 요청 메시지 생성
            String requestMessage = header + jsonString;
            System.out.println("최종요청 스트링: " + requestMessage);

            // 요청 메시지 전송
            out.write(requestMessage.getBytes());

            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String response = br.readLine();
            System.out.println("response: " + response);

            String responseApiCode = response.substring(0, 4);
            String responseResultCode = response.substring(4, 8);
            String responseBodySize = response.substring(8, 16);

            String responseBody = response.substring(16);

            System.out.println("responseApiCode: " + responseApiCode);
            System.out.println("responseResultCode: " + responseResultCode);
            System.out.println("responseBodySize: " + responseBodySize);

            System.out.println("responseBody: " + responseBody);


            socket.close();
            out.close();
            in.close();
            jsonObj.clear();
            jsonObj = null;

            return responseBody;

//            byte[] responseByte = new byte[4096];
//            int bytesRead = in.read(responseByte);
//
//            if (bytesRead != -1) {
//                // 서버로부터 응답 바이트 배열을 읽기
//                String response = new String(responseByte, 0, bytesRead, "UTF-8");
//                System.out.println("서버 응답: " + response);
//
//
//                System.out.println(response);
//
//                String responseApiCode = response.substring(0,4);
//                String responseResultCode = response.substring(4,8);
//                String responseBodySize = response.substring(8,16);
//
//                String responseBody = response.substring(16);
//
//                System.out.println("apiCode: " + responseApiCode);
//                System.out.println("resultCode: " + responseResultCode);
//                System.out.println("bodySize: " + responseBodySize);
//
//                System.out.println("responseBody: " + responseBody);
//
//                return responseBody;
//
//            }

        } catch (IOException e) {
            e.printStackTrace();

            return "error";

        }
    }

    public static String sendClassificationReq(String userId, String reqType) {
//        String serverHost = "43.202.161.88"; // 분류엔진 원격 서버의 호스트 주소
        String serverHost = "43.202.10.41"; // 분류엔진 원격 서버의 호스트 주소
        int serverPort = 9101; // 원격 서버의 포트 번호

        log.info("userId: " + userId);
        log.info("reqType: " + reqType);



        try (Socket socket = new Socket(serverHost, serverPort);
             OutputStream out = socket.getOutputStream();
        ) {

            JSONObject jsonObj = new JSONObject();
            // jsonObj.put("wavPath","/home/ec2-user/wav_dir/recorded_audio_8k.wav");
            jsonObj.put("wavPath", "/home/ec2-user/wav_dir/" + userId + "_" + reqType + "_audio.wav"); // 녹음한 음성을 보내는게 맞음 -> 인증용이냐 등록용이냐

            String jsonString = jsonObj.toString();

            System.out.println("json body string: " + jsonString);
            int jsonLen = jsonString.length(); // json의 길이

            // RequestHeader
            //String apiCode = "1000";

            String apiCode = "2000"; // 등록: 1000, 인증:2000
            String resultCode = "0000"; // 고정
            String bodySize = String.format("%08d", jsonString.length());


            String header = apiCode + resultCode + bodySize;

            // 전체 요청 메시지 생성
            String requestMessage = header + jsonString;
            System.out.println("최종요청 스트링: " + requestMessage);

            // 요청 메시지 전송
            out.write(requestMessage.getBytes());

            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String response = br.readLine();
            System.out.println("response: " + response);

            String responseApiCode = response.substring(0, 4);
            String responseResultCode = response.substring(4, 8);
            String responseBodySize = response.substring(8, 16);

            String responseBody = response.substring(16);

            System.out.println("responseApiCode: " + responseApiCode);
            System.out.println("responseResultCode: " + responseResultCode);
            System.out.println("responseBodySize: " + responseBodySize);

            System.out.println("responseBody: " + responseBody);


            socket.close();
            out.close();
            in.close();
            jsonObj.clear();
            jsonObj = null;

            return responseBody;


        } catch (IOException e) {
            e.printStackTrace();

            return "error";

        }
    }
}
