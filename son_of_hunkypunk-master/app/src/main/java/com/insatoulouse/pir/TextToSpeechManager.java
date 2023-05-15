package com.insatoulouse.pir;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;



import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TextToSpeechManager {

    private SpeechSynthesizer speechSynthesizer;
    private ExecutorService executor;

    public TextToSpeechManager(Context context) {
        Resources res = context.getResources();
        executor = Executors.newSingleThreadExecutor();
        int keyResId = res.getIdentifier("subscription_key", "string", context.getPackageName());
        int regionResId = res.getIdentifier("service_region", "string", context.getPackageName());
        String subscriptionKey = res.getString(keyResId);
        String serviceRegion = res.getString(regionResId);

        Log.d("TextToSpeechManager", "Subscription Key: " + subscriptionKey);
        Log.d("TextToSpeechManager", "service_region: " + serviceRegion);



        SpeechConfig speechConfig = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);
        AudioConfig audioConfig = AudioConfig.fromDefaultSpeakerOutput();
        speechSynthesizer = new SpeechSynthesizer(speechConfig, audioConfig);

    }

    public interface TextToSpeechCallback {
        void onSpeechCompleted();
        void onSpeechCanceled();
    }

    public void speakText(String text, TextToSpeechCallback callback) {
        Log.d("TextToSpeechManager", "speakText called with text: " + text);

        executor.submit(() -> {
            try {
                SpeechSynthesisResult synthesisResult = speechSynthesizer.SpeakTextAsync(text).get();
                if (synthesisResult.getReason() == ResultReason.SynthesizingAudioCompleted) {
                    Log.d("TextToSpeechManager", "Speech synthesis completed");
                    if (callback != null) {
                        callback.onSpeechCompleted();
                    }
                } else if (synthesisResult.getReason() == ResultReason.Canceled) {
                    SpeechSynthesisCancellationDetails cancellationDetails = SpeechSynthesisCancellationDetails.fromResult(synthesisResult);
                    Log.d("TextToSpeechManager", "Speech synthesis canceled. Reason: " + cancellationDetails.getReason().toString() + ", Error details: " + cancellationDetails.getErrorDetails());
                    if (callback != null) {
                        callback.onSpeechCanceled();
                    }
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
    }




    public void shutdown() {
        speechSynthesizer.close();
    }
}

