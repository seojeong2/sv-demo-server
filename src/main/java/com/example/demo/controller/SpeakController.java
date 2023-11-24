package com.example.demo.controller;

import com.example.demo.audio.AudioRecorder;
import com.example.demo.audio.FileSend;
import com.example.demo.dto.RequestData;
import com.example.demo.dto.ResponseMessage;
import com.example.demo.service.TcpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.sound.sampled.Mixer;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class SpeakController {


    private static AudioRecorder audioRecorder;
    private static TcpClient tcpClient;

    private static FileSend fileSend;

    //@CrossOrigin(origins = "https://172.20.10.4")
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/record/registration")
    @ResponseBody
    public String voiceReg(@RequestBody RequestData requestData) {

        log.info("calling recordAudio");
        log.info("requestData: " + requestData.getUserId());


        Mixer.Info desiredMixerInfo = audioRecorder.findDesiredMicrophone(); // 마이크 정보 찾음


        log.info("사용할 마이크 믹서: " + desiredMixerInfo.getName());

        if(desiredMixerInfo != null) {
            audioRecorder.startRecording(desiredMixerInfo, audioRecorder.recordDuration, requestData.getUserId(), "registration"); // 음성 녹음 및 저장
            log.info("file record success");


            // scp 되는지 확인 필요!
            String localFilePath = "/Users/seojeong/Downloads/demo/wav/" + requestData.getUserId() + "_reg_audio.wav";
            String fileSendResponse = fileSend.audioFileSend(localFilePath,"registration");
            log.info("fileSendResponse: " + fileSendResponse);


            if (fileSendResponse == "success") {
                String response = tcpClient.sendReqToServer("registration", requestData.getUserId()); // 화자인증 서버로 등록 요청 전송
                return response;
            } else {
                return "파일 전송 실패";
            }

            //String response = tcpClient.sendReqToServer("registration", requestData.getUserId()); // 화자인증 서버로 등록 요청 전송

            //return new ResponseEntity<>(new ResponseMessage("success", "요청 성공"), HttpStatus.OK);
            //return response;
        } else {
            log.info("error");
            //return new ResponseEntity<>(new ResponseMessage("error","요청 에러"), HttpStatus.BAD_REQUEST);
            return "error";
        }

    }

    @PostMapping("/record/authentication")
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    public String voiceAuth(@RequestBody RequestData requestData) {

        log.info("calling voiceAuth");
        log.info("requestData: " + requestData.getUserId());

        Mixer.Info desiredMixerInfo = audioRecorder.findDesiredMicrophone();

        // 마이크 입력부 찾기
        if (desiredMixerInfo != null) {
            audioRecorder.startRecording(desiredMixerInfo, audioRecorder.authDuration, requestData.getUserId(),"authentication"); // 음성 녹음 미 저장
            log.info("auth file record success");


            // scp 되는지 확인 필요!
            String localFilePath = "/Users/seojeong/Downloads/demo/wav/" + requestData.getUserId() + "_auth_audio.wav";
            String fileSendResponse = fileSend.audioFileSend(localFilePath,"authentication");
            log.info("fileSendResponse: " + fileSendResponse);

            if (fileSendResponse == "success") {
                String response = tcpClient.sendReqToServer("authentication", requestData.getUserId());
                return response;
            } else {
                return "파일 전송 실패";
            }



        } else {
            log.info("error");
            return "error";
        }

    }

    @PostMapping("/record/classification")
    @CrossOrigin(origins = "http://localhost:3000")
    @ResponseBody
    public String voiceClassification(@RequestBody RequestData requestData) {
        log.info("calling voiceClassification");
        log.info("requestData: " + requestData.getUserId());

        Mixer.Info desiredMixerInfo = audioRecorder.findDesiredMicrophone();

        // 마이크 입력부 찾기
        if (desiredMixerInfo != null) {
            audioRecorder.startRecording(desiredMixerInfo, audioRecorder.classificationDuration, requestData.getUserId(),"classification"); // 음성 녹음 미 저장
            log.info("classify file record success");

            // scp 되는지 확인 필요!
            String localFilePath = "/Users/seojeong/Downloads/demo/wav/" + requestData.getUserId() + "_classifi_audio.wav";
            String fileSendResponse = fileSend.audioFileSend(localFilePath,"classifi");
            log.info("fileSendResponse: " + fileSendResponse);

            if (fileSendResponse == "success") {
                String response = tcpClient.sendClassificationReq(requestData.getUserId(),"classifi");
                return response;
            } else {
                return "파일 전송 실패";
            }



        } else {
            log.info("error");
            return "error";
        }
    }

}
