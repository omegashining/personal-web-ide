package ipn.escom.webide.web.development;

import static ipn.escom.webide.constants.SystemConstants.LINESEPARATOR;

import java.io.BufferedWriter;
import java.io.IOException;

public class Sender implements Runnable {
	
    private BufferedWriter bwos;
    private String input;

    public Sender(BufferedWriter bwos) {
        this.bwos = bwos;
    	this.input = null;
    }

    public void run() {
        try {
            while(bwos != null) {
            	bwos.flush();
            	
                if(input != null) {
                	bwos.write(input);
                	bwos.write(LINESEPARATOR);
                	bwos.flush();
                	input = null;
                }
            }
        } catch (IOException ex) {
        }
    }

	public void setInput(String input) {
		this.input = input;
	}

	public void close() throws IOException {
		bwos.close();
        bwos = null;
    }

}
