package SpeechRecognition;

import PAC.GameView;
import ai.picovoice.leopard.*;
import org.apache.commons.cli.*;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class SpeechRecognition extends Thread
{
    private final static String accessKey = "rd9J3I9zJKmWbMPPg6JbEy/efOEMTrVYQPTyxdeoxkSHYrvwLnLZXA==";
    private GameView gameView;

    private Recorder recorder;

    String modelPath;
    String libraryPath;
    Leopard leopard;

    Scanner scanner;
    KeyListener spaceListener;
    final int audioDeviceIndex = 1;

    Boolean spacePressed, notTreated, stop;

    private ArrayList<LeopardTranscript.Word> wordArrayList;

    public SpeechRecognition(GameView gameView, Recorder recorder)
    {
        HelpFormatter formatter = new HelpFormatter();

        this.gameView = gameView;
        this.recorder = recorder;
        this.wordArrayList = new ArrayList<>();

        modelPath = Leopard.MODEL_PATH;
        libraryPath = Leopard.LIBRARY_PATH;

        notTreated = false;

        leopard = null;
        try {
            leopard = new Leopard.Builder()
                    .setAccessKey(accessKey)
                    .setModelPath(modelPath)
                    .setLibraryPath(libraryPath)
                    .build();

            System.out.println("Leopard version : " + leopard.getVersion());
            System.out.println(">>> Press `CTRL+C` to exit:");
            this.recorder = recorder;
            scanner = new Scanner(System.in);
        } catch (LeopardException ex) {
            throw new RuntimeException(ex);
        }
    }
    public void run()
    {
        stop = false;
        System.out.println("Started");

        while (!stop) {
            if (gameView.getMinesweeper().getEnterPressed() && !notTreated){
                System.out.println("Recording");
                recorder.start();
                notTreated = true;
            } else if (!gameView.getMinesweeper().getEnterPressed() && notTreated){
                recorder.end();
                while (recorder.isAlive()) { }
                short[] pcm = recorder.getPCM();
                System.out.println(pcm);
                LeopardTranscript transcript = null;
                try {
                    transcript = leopard.process(pcm);
                } catch (LeopardException e) {
                    throw new RuntimeException(e);
                }
                System.out.println(transcript.getTranscriptString() + "\n");
                wordArrayList.addAll(List.of(transcript.getWordArray()));
                examineWords();
                recorder = null;
                recorder = new Recorder(audioDeviceIndex);
                notTreated = false;
            }
        }
        System.out.println("Ended");
    }

    public void end() {
        this.stop = true;
    }

    public void examineWords()
    {
        for (LeopardTranscript.Word word: wordArrayList)
        {
            if (word.getWord().equalsIgnoreCase("flag"))
            {
                gameView.getGrid().toggleFlagOnPointerPosition();
            }
            else if (word.getWord().equalsIgnoreCase("reveal") || word.getWord().equalsIgnoreCase("clear"))
            {
                gameView.getGrid().revealCellOnPointerPosition();
            }
        }
        wordArrayList.clear();
    }

    public void setSpacePressed(Boolean spacePressed) {
        this.spacePressed = spacePressed;
    }

    public void clear() {
        leopard.delete();
    }
}