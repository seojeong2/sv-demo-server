package com.example.demo.audio;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sound.sampled.*;

import java.io.File;
import java.io.IOException;


@Slf4j
@Service
public class AudioRecorder {


    public static final int recordDuration = 10;

    // 마이크 정보 찾기
    public static Mixer.Info findDesiredMicrophone() {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        for(Mixer.Info info: mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(info);

            if(mixer.isLineSupported(new TargetDataLine.Info(TargetDataLine.class,null))) {
                System.out.println("마이크 정보: " + info.getName());
                System.out.println("마이크 설명: " + info.getDescription());

                return info;
            }
        }
        return null;
    }

    public static void startRecording(Mixer.Info mixerInfo, int maxDuration, String userId) {
        try{
            Mixer mixer = AudioSystem.getMixer(mixerInfo);
            TargetDataLine line = (TargetDataLine) mixer.getLine(new TargetDataLine.Info(TargetDataLine.class,null));

            AudioFormat format = new AudioFormat(44100,16,2,true,false);
            line.open(format);
            line.start();


            File audioFile = new File("/Users/seojeong/Downloads/demo/wav/" + userId + "_recorded_audio.wav");

            long startTime = System.currentTimeMillis();

            while(System.currentTimeMillis() - startTime < maxDuration * 1000) {

            }

            // 마이크 중지 및 녹음 파일 저장
            stopRecordingAndSave(line, audioFile);
        } catch ( LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public static void stopRecordingAndSave(TargetDataLine line, File audioFile) {
        try{
            line.stop();
            line.close();

            AudioInputStream audioInputStream = new AudioInputStream(line);


            // 파일에 저장
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);

            log.info("음성 데이터를 파일로 저장했습니다.");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//    public static void audioRecord() {
//
//        // 모든 믹서 정보 가져오기
//        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
//        Mixer.Info desiredMixerInfo = null;
//
//        // 마이크를 찾기 위한 루프
//        for (Mixer.Info info: mixerInfos) {
//            Mixer mixer = AudioSystem.getMixer(info);
//
//            // 마이크로 사용 가능한 라인인지 확인
//            if(mixer.isLineSupported(new TargetDataLine.Info(TargetDataLine.class, null))) {
//                System.out.println("마이크 정보: "+ info.getName());
//                System.out.println("마이크 설명: " + info.getDescription());
//
//                desiredMixerInfo = info;
//
//                break;
//            }
//        }
//
//        if (desiredMixerInfo != null) {
//            System.out.println("사용할 마이크 믹서: " + desiredMixerInfo.getName());
//
//            try{
//                Mixer mixer = AudioSystem.getMixer(desiredMixerInfo);
//                TargetDataLine line = (TargetDataLine) mixer.getLine(new TargetDataLine.Info(TargetDataLine.class,null));
//
//                // 오디오 포맷 설정
//                AudioFormat format = new AudioFormat(44100,16,2,true,false);
//
//                // 마이크 라인 열기
//                line.open(format);
//                line.start();
//
//                System.out.println("마이크 입력 시작...");
//
//                // 오디오 입력을 저장할 파일 설정(wav 형식)
//                File audioFile = new File("/Users/seojeong/Downloads/demo/wav/recorded_audio.wav"); // 경로변경
//
//                AudioInputStream audioInputStream = new AudioInputStream(line);
//
//
//                // 파일에 저장
//                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
//
//                System.out.println("음성 데이터를 파일로 저장했습니다.");
//
//                // 마이크 입력 닫기
//                line.stop();
//                line.close();
//
//
//            } catch(LineUnavailableException | IOException e) {
//                e.printStackTrace();
//            }
//        } else{
//            System.out.println("사용 가능한 마이크를 찾지 못했습니다.");
//            log.info("사용 가능한 마이크를 찾지 못했습니다.");
//        }
//
//    }
}