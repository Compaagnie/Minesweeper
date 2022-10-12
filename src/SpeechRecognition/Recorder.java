package SpeechRecognition;

import javax.sound.sampled.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

public class Recorder extends Thread{
    private final static String accessKey = "rd9J3I9zJKmWbMPPg6JbEy/efOEMTrVYQPTyxdeoxkSHYrvwLnLZXA==";
    private TargetDataLine micDataLine = null;
    private boolean stop = false;
    private boolean isRecording = false;
    private List<Short> pcmBuffer = null;
    public Recorder(int audioDeviceIndex) {
        AudioFormat format = new AudioFormat(16000f, 16, 1, true, false);
        DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, format);
        TargetDataLine micDataLine;
        try {
            micDataLine = getAudioDevice(audioDeviceIndex, dataLineInfo);
            micDataLine.open(format);
        } catch (LineUnavailableException e) {
            System.err.println("Failed to get a valid capture device. Use --show_audio_devices to " +
                    "show available capture devices and their indices");
            System.exit(1);
            return;
        }

        this.micDataLine = micDataLine;
        this.stop = false;
        this.isRecording = false;
        this.pcmBuffer = new ArrayList<Short>();
    }

    private static TargetDataLine getDefaultCaptureDevice(DataLine.Info dataLineInfo) throws LineUnavailableException {
        if (!AudioSystem.isLineSupported(dataLineInfo)) {
            throw new LineUnavailableException("Default capture device does not support the audio " +
                    "format required by Picovoice (16kHz, 16-bit, linearly-encoded, single-channel PCM).");
        }

        return (TargetDataLine) AudioSystem.getLine(dataLineInfo);
    }

    private static TargetDataLine getAudioDevice(int deviceIndex, DataLine.Info dataLineInfo) throws LineUnavailableException {
        if (deviceIndex >= 0) {
            try {
                Mixer.Info mixerInfo = AudioSystem.getMixerInfo()[deviceIndex];
                Mixer mixer = AudioSystem.getMixer(mixerInfo);

                if (mixer.isLineSupported(dataLineInfo)) {
                    return (TargetDataLine) mixer.getLine(dataLineInfo);
                } else {
                    System.err.printf("Audio capture device at index %s does not support the audio format required by " +
                            "Picovoice. Using default capture device.", deviceIndex);
                }
            } catch (Exception e) {
                System.err.printf("No capture device found at index %s. Using default capture device.", deviceIndex);
            }
        }

        // use default capture device if we couldn't get the one requested
        return getDefaultCaptureDevice(dataLineInfo);
    }

    public void run() {
        this.isRecording = true;
        micDataLine.start();
        this.pcmBuffer.clear();

        ByteBuffer captureBuffer = ByteBuffer.allocate(512);
        captureBuffer.order(ByteOrder.LITTLE_ENDIAN);
        short[] shortBuffer = new short[256];

        while (!stop) {
            micDataLine.read(captureBuffer.array(), 0, captureBuffer.capacity());
            captureBuffer.asShortBuffer().get(shortBuffer);
            for (short value : shortBuffer) {
                this.pcmBuffer.add(value);
            }
        }
    }

    public void end() {
        this.stop = true;
    }

    public short[] getPCM() {
        short[] pcm = new short[this.pcmBuffer.size()];
        for (int i = 0; i < this.pcmBuffer.size(); ++i) {
            pcm[i] = this.pcmBuffer.get(i);
        }
        this.pcmBuffer.clear();
        return pcm;
    }
}