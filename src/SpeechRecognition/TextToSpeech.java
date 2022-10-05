package SpeechRecognition;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
public class TextToSpeech {

    public static void main(String[] args) {
        Voice voice;//Creating object of Voice class
        voice = VoiceManager.getInstance().getVoice("kevin");//Getting voice
        if (voice != null) {
            voice.allocate();//Allocating Voice
        }
        try {
            voice.setRate(190);//Setting the rate of the voice
            voice.setPitch(150);//Setting the Pitch of the voice
            voice.setVolume(3);//Setting the volume of the voice
            voice.speak("Hello this is Tutorials Field");//Calling speak() method

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }

}