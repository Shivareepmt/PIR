package com.insatoulouse.pir;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpeechRecognitionManager {
    private SpeechRecognizer speechRecognizer;
    private ExecutorService executor;

    public SpeechRecognitionManager(Context context) {
        Resources res = context.getResources();
        executor = Executors.newSingleThreadExecutor();
        int keyResId = res.getIdentifier("subscription_key", "string", context.getPackageName());
        int regionResId = res.getIdentifier("service_region", "string", context.getPackageName());
        String subscriptionKey = res.getString(keyResId);
        String serviceRegion = res.getString(regionResId);

        Log.d("SRManager", "Subscription Key: " + subscriptionKey);
        Log.d("SRManager", "service_region: " + serviceRegion);

        SpeechConfig speechConfig = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);
        AudioConfig audioConfig = AudioConfig.fromDefaultMicrophoneInput();
        speechRecognizer = new SpeechRecognizer(speechConfig, audioConfig);
    }

    public interface SpeechRecognitionCallback {
        void onRecognitionResult(String recognizedText);
        void onRecognitionCanceled();
    }

    public void startContinuousRecognition(SpeechRecognitionCallback callback) {
        speechRecognizer.recognizing.addEventListener((o, speechRecognitionResultEventArgs) -> {
            String recognizedText = speechRecognitionResultEventArgs.getResult().getText();
            Log.d("SRManager", "Recognized text: " + recognizedText);
            if (callback != null) {
                callback.onRecognitionResult(recognizedText);
            }
        });

        speechRecognizer.canceled.addEventListener((o, speechRecognitionCanceledEventArgs) -> {
            Log.d("SRManager", "Speech recognition canceled. Reason: " + speechRecognitionCanceledEventArgs.getReason().toString());
            if (callback != null) {
                callback.onRecognitionCanceled();
            }
        });

        speechRecognizer.startContinuousRecognitionAsync();
    }

    public void stopContinuousRecognition() {
        speechRecognizer.stopContinuousRecognitionAsync();
    }

    public CompletableFuture<String> stopContinuousRecognitionWithResult() {
        CompletableFuture<String> recognizedTextFuture = new CompletableFuture<>();
        speechRecognizer.recognized.addEventListener((o, speechRecognitionResultEventArgs) -> {
            String recognizedText = speechRecognitionResultEventArgs.getResult().getText();
            recognizedTextFuture.complete(recognizedText);
        });
        speechRecognizer.stopContinuousRecognitionAsync();
        return recognizedTextFuture;
    }


    public void shutdown() {
        speechRecognizer.close();
    }
}
