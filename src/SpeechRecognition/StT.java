package SpeechRecognition;

import ai.picovoice.leopard.Leopard;
import ai.picovoice.leopard.LeopardException;

public class StT {
    final String accessKey = "rd9J3I9zJKmWbMPPg6JbEy/efOEMTrVYQPTyxdeoxkSHYrvwLnLZXA==";
    public StT(){
        try {
            Leopard leopard = new Leopard.Builder().setAccessKey(accessKey).build();
        } catch (LeopardException e) {
            throw new RuntimeException(e);
        }
    }
}
