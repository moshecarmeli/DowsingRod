package edu.ucsb.cs.capstone.letmypeoplecode.dowsingrod;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by dimberman on 1/30/14.
 */
public class NoiseGenerator {



    //private final double duration = 0.1; // seconds
    private final double sampleRate = 24000;
    private int numSamples = 0;
    //private final double sample[] = new double[numSamples];
    private final double freqOfTone = 1000; // hz

    private byte generatedSnd[];

    void genTone(double duration){
        // fill out the array
        numSamples = (int)(duration * sampleRate);
        generatedSnd = new byte[2 * numSamples];
        double sample[] = new double[numSamples];
        for (int i = 0; i < numSamples; ++i) {
            sample[i] = Math.sin(2 * Math.PI * i / (sampleRate/freqOfTone));
        }

        // convert to 16 bit pcm sound array
        // assumes the sample buffer is normalised.
        int idx = 0;
        for (double dVal : sample) {
            short val = (short) (dVal * 32767);
            generatedSnd[idx++] = (byte) (val & 0x00ff);
            generatedSnd[idx++] = (byte) ((val & 0xff00) >>> 8);
        }
    }

    void playSound(){
        AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
                8000, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, numSamples,
                AudioTrack.MODE_STATIC);
        audioTrack.write(generatedSnd, 0, numSamples);
        audioTrack.play();
    }
}
