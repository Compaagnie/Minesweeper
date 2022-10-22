package SpeechRecognition;

import PAC.GameView;
import PAC.Minesweeper;
import PAC.Roguelike.PowerUps.ActivePowerUp;
import PAC.Roguelike.RoguelikeView;
import ai.picovoice.leopard.Leopard;
import ai.picovoice.leopard.LeopardException;
import ai.picovoice.leopard.LeopardTranscript;

import java.util.ArrayList;
import java.util.List;


public class SpeechRecognition extends Thread
{
    private final static String accessKey = "rd9J3I9zJKmWbMPPg6JbEy/efOEMTrVYQPTyxdeoxkSHYrvwLnLZXA==";
    private GameView gameView;
    private Recorder recorder;

    private final Minesweeper minesweeper;
    String modelPath;
    String libraryPath;
    Leopard leopard;

    List<String> flagKeyWords = List.of(new String[]{
            "flag","flagged","place","flung","lag"
    });
    List<String> revealKeyWords = List.of(new String[]{
            "reveal","clear","propagate"
    });

    final int audioDeviceIndex = -1;

    boolean notTreated;
    boolean stop;

    private ArrayList<LeopardTranscript.Word> wordArrayList;

    public SpeechRecognition(Minesweeper minesweeper)
    {
        this.wordArrayList = new ArrayList<>();
        this.minesweeper = minesweeper;
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
        } catch (LeopardException ex) {
            throw new RuntimeException(ex);
        }
        minesweeper.setSpeechInitiated();
    }
    public void run()
    {
        stop = false;
        System.out.println("Started");

        while (!stop)
        {
            gameView = minesweeper.getGameView();
            if (gameView != null) {
                if (gameView.getEnterPressed() && !notTreated) {
                    System.out.println("Recording");
                    recorder = new Recorder(audioDeviceIndex);
                    recorder.start();
                    notTreated = true;
                } else if (!gameView.getEnterPressed() && notTreated) {
                    recorder.end();
                    while (recorder.isAlive()) {
                    }
                    short[] pcm = recorder.getPCM();
                    LeopardTranscript transcript = null;
                    try {
                        transcript = leopard.process(pcm);
                    } catch (LeopardException ignored) {
                    }
                    if (transcript != null) {
                        System.out.println(transcript.getTranscriptString() + "\n");
                        wordArrayList.addAll(List.of(transcript.getWordArray()));
                        examineWords();
                    }
                    recorder = null;
                    notTreated = false;
                }
            }
        }
        System.out.println("Ended");
        leopard.delete();
    }

    public void end() {
        this.stop = true;
    }

    public void examineWords()
    {
        for (LeopardTranscript.Word word: wordArrayList)
        {
            if (flagKeyWords.contains(word.getWord().toLowerCase()))
            {
                gameView.getGrid().toggleFlagOnPointerPosition();
                break;
            }
            else if (revealKeyWords.contains(word.getWord().toLowerCase()))
            {
                gameView.getGrid().revealCellOnPointerPosition();
                break;
            }
            //Power Ups
            else if (gameView.isRogueLike())
            {
                for(ActivePowerUp powerUp : ActivePowerUp.values())
                {
                    if(powerUp.getKeyWords().contains(word.getWord().toLowerCase()))
                    {
                        ((RoguelikeView) gameView).usePowerUp(powerUp);
                        break;
                    }
                }
            }
        }
        wordArrayList.clear();
    }
}
