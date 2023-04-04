package com.insatoulouse.pir;

import android.content.Context;
import android.content.res.Resources;

import com.microsoft.cognitiveservices.speech.*;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;


import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TextToSpeechManager {

    private SpeechSynthesizer speechSynthesizer;

    public TextToSpeechManager(Context context) {
        Resources res = context.getResources();
        int keyResId = res.getIdentifier("subscription_key", "string", context.getPackageName());
        int regionResId = res.getIdentifier("service_region", "string", context.getPackageName());
        String subscriptionKey = res.getString(keyResId);
        String serviceRegion = res.getString(regionResId);

        SpeechConfig speechConfig = SpeechConfig.fromSubscription(subscriptionKey, serviceRegion);
        AudioConfig audioConfig = AudioConfig.fromDefaultSpeakerOutput();
        speechSynthesizer = new SpeechSynthesizer(speechConfig, audioConfig);
    }

    public void speakText(String text) {
        Future<SpeechSynthesisResult> result = speechSynthesizer.SpeakTextAsync(text);
        try {
            SpeechSynthesisResult synthesisResult = result.get();
            if (synthesisResult.getReason() == ResultReason.SynthesizingAudioCompleted) {
                // Speech synthesis completed successfully
            } else if (synthesisResult.getReason() == ResultReason.Canceled) {
                SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(synthesisResult);
                // Speech synthesis was cancelled
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public void shutdown() {
        speechSynthesizer.close();
    }
}

