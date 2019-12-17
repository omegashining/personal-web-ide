package ipn.escom.webide.web.development;

import static ipn.escom.webide.constants.SystemConstants.LINESEPARATOR;

import java.io.BufferedReader;
import java.io.IOException;

public class Receiver implements Runnable {
	
    private BufferedReader bris;
    private BufferedReader bres;
    private Sender sender;
    private String output;
	private boolean enabled;

    public Receiver(BufferedReader bris, BufferedReader bres, Sender sender) {
        this.bris = bris;
        this.bres = bres;
        this.sender = sender;
        this.output = "";
        this.enabled = true;
    }

    public void run() {
        try {
            String lineis = null;
            String linees = null;

            while((lineis = bris.readLine()) != null || (linees = bres.readLine()) != null) {
                if(lineis != null)
                    output += lineis + LINESEPARATOR;

                if(linees != null)
                    output += linees + LINESEPARATOR;
            }

            bris.close();
            bres.close();
            sender.close();
            enabled = false;
        } catch (IOException ex) {
        }
    }

	public String getOutput() {
		return output;
	}
	
	public boolean isEnabled() {
		return enabled;
	}

	public void addToOutput(String line) {
		output += line + LINESEPARATOR;
	}

}
