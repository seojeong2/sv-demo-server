package com.example.demo.controller;

import com.example.demo.audio.AudioRecorder;
import com.example.demo.dto.RequestData;
import com.example.demo.dto.ResponseMessage;
import com.example.demo.service.TcpClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    //@CrossOrigin(origins = "https://172.20.10.4")
    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping("/record/registration")
    @ResponseBody
    public String recordAudio(@RequestBody RequestData requestData) {

        log.info("calling recordAudio");
        log.info("requestData: " + requestData.getUserId());


        Mixer.Info desiredMixerInfo = audioRecorder.findDesiredMicrophone();

        if(desiredMixerInfo != null) {
            audioRecorder.startRecording(desiredMixerInfo, audioRecorder.recordDuration, requestData.getUserId());
            log.info("file record success");

            String response = tcpClient.sendReqToServer();


            //return new ResponseEntity<>(new ResponseMessage("success", "요청 성공"), HttpStatus.OK);
            return response;
        } else {
            log.info("error");
            //return new ResponseEntity<>(new ResponseMessage("error","요청 에러"), HttpStatus.BAD_REQUEST);
            return "error";
        }

    }
}
