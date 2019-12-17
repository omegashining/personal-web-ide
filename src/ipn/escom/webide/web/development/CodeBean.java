package ipn.escom.webide.web.development;

import static ipn.escom.webide.constants.SystemConstants.LINESEPARATOR;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;

import ipn.escom.webide.tree.Information;
import ipn.escom.webide.tree.FileSystemBean;
import ipn.escom.webide.utilities.FileUtilities;
import ipn.escom.webide.utilities.CodeUtilities;
import ipn.escom.webide.utilities.WebUtilities;

import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

import org.ajax4jsf.component.html.HtmlAjaxCommandLink;
import org.ajax4jsf.component.html.HtmlAjaxSupport;
import org.richfaces.component.html.HtmlTreeNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Scope("session")
@Component
public class CodeBean {
	
	private HttpServletRequest request;
	private String language;
	private String path;
	private String text;
	private String code;
	private String console;
	private String input;
	private int pollTime = 1;
	private boolean pollEnabled = false;
	
	private Process process;
	private Receiver receiver;
	private Sender sender;
	private Poll poll;
	private Thread threadReceiver;
	private Thread threadSender;
	private Thread threadPoll;
	
	private Information file;

	@Autowired
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	
	public String getLanguage() {
		return language;
	}
	
	public void setLanguage(String language) {
		this.language = language;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getShortPath() {
		if(path != null && !path.equals(""))
			return path.substring(WebUtilities.getRealPath("/usuarios", request).length());
		
		return "";
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getConsole() {
		return console;
	}
	
	public void setConsole(String console) {
		this.console = console;
	}
	
	public String getInput() {
		return input;
	}
	
	public void setInput(String input) {
		this.input = input;
	}
	
	public int getPollTime() {
		return pollTime;
	}

	public void setPollTime(int poolTime) {
		this.pollTime = poolTime;
	}

	public boolean isPollEnabled() {
		return pollEnabled;
	}

	public void setPollEnabled(boolean pollEnabled) {
		this.pollEnabled = pollEnabled;
	}
	
	public String open() throws Exception {
		if(file.getType().equals("file")) {
			String content = FileUtilities.getContent(file.getPath());

			path = file.getPath();
			language = file.getExtension();
			
			if(language.equals("txt"))
				text = content;
			else
				code = content;
		}
		
		return "open";
	}
	
	public String close() {
		clearFields();
		
		return "close";
	}

	public void save() throws Exception {
		if(path != null && !path.equals("")) {
			if(language.equals("txt"))
				FileUtilities.save(path, text);
			else
				FileUtilities.save(path, code + "\n");
		}
	}
	
	public void compile() throws Exception {
		stop();
		console = "";
		
		if(path != null && !path.equals("")) {
			FileUtilities.save(path, code + "\n");
			
			String code = FileUtilities.getContent(path);
				
			if(language.equals("c"))
				CodeUtilities.insertAutoflush(path, code);
					
		    Runtime runtime = Runtime.getRuntime();
		    Process process = runtime.exec(CodeUtilities.getCompilerCommand(path, language));
		    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
		        
		    String line = "";
		    String regex = WebUtilities.getRealPath("/usuarios/", request);
		        
		    while((line = bufferedReader.readLine()) != null) {
		    	if(line.contains(regex)) {
		    		int index = line.indexOf(regex);
		    		line = line.substring(0, index) + line.substring(index + regex.length(), line.length());
		        }
		    	
		    	console += line + LINESEPARATOR;
		    }
		        
		    bufferedReader.close();

		    if(language.equals("c"))
		    	FileUtilities.save(path, code);
		    
		    if(console.equals(""))
		    	console = "ARCHIVO COMPILADO SATISFACTORIAMENTE.";
		
			updateTreeNodes();
		}
	}

	public void run() throws Exception {
		compile();
		
		if(console.equals("ARCHIVO COMPILADO SATISFACTORIAMENTE.")) {
			Runtime r = Runtime.getRuntime();
			process = r.exec(CodeUtilities.getRunCommand(path, language));
			
			OutputStream os = process.getOutputStream();
		    InputStream is = process.getInputStream();
		    InputStream es = process.getErrorStream();
		    
		    BufferedReader bris = new BufferedReader(new InputStreamReader(is), 1024);
		    BufferedReader bres = new BufferedReader(new InputStreamReader(es), 1024);
		    BufferedWriter bwos = new BufferedWriter(new OutputStreamWriter(os), 1024);

		    sender = new Sender(bwos);
		    receiver = new Receiver(bris, bres, sender);
		    poll = new Poll(receiver);
	            
		    threadSender = new Thread(sender);
		    threadReceiver = new Thread(receiver);
	        threadPoll = new Thread(poll);

		    threadReceiver.start();
		    Thread.sleep(100);
		    threadSender.start();
		    Thread.sleep(100);
		    pollEnabled = true;
	        threadPoll.start();
		}
	}
	
	public void stop() throws InterruptedException {
		if(isPollEnabled()) {
			if(process != null)
				process.destroy();
			
			if(threadSender != null)
				threadSender.interrupt();
			
			if(threadReceiver != null)
				threadReceiver.interrupt();
			
			if(threadPoll != null)
				threadPoll.interrupt();
			
			console += "EJECUCIÓN TERMINADA";
			Thread.sleep(1100);
			pollEnabled = false;
		}
	}
	
	public void insert() throws InterruptedException {
		if(isPollEnabled()) {
			receiver.addToOutput(input);
			Thread.sleep(100);
			sender.setInput(input);
			Thread.sleep(100);
		}
		
		input = "";
	}
	
	public void clearFields() {
		text = "";
		code = "";
		path = "";
		language = "";
	}

	public void updateTreeNodes() {
		FileSystemBean fileSystemBean = (FileSystemBean) WebUtilities.getBean("fileSystemBean", request);
		fileSystemBean.setNodes(null);
	}

	public void commandLinkListener(ActionEvent event) {
		HtmlAjaxSupport htmlAjaxSupport = (HtmlAjaxSupport) event.getSource();
		HtmlAjaxCommandLink htmlAjaxCommandLink = (HtmlAjaxCommandLink) htmlAjaxSupport.getParent();
		HtmlTreeNode htmlTreeNode = (HtmlTreeNode) htmlAjaxCommandLink.getParent();
		file = (Information) htmlTreeNode.getData();
	}
	
	class Poll implements Runnable {
		
		private Receiver receiver;
		private Date pollStartTime;
		
		public Poll(Receiver receiver) {
			this.receiver = receiver;
		}

		@Override
		public void run() {
			try {
				pollStartTime = new Date();
				
				do {
					Thread.sleep(500);
					console = receiver.getOutput();
					
					if((new Date().getTime()-pollStartTime.getTime())>=(pollTime*60000)) {
						stop();
						break;
					}
				} while(receiver.isEnabled());

				console += "EJECUCIÓN TERMINADA";
				Thread.sleep(1100);
				pollEnabled = false;
			} catch (InterruptedException e) {
			}
		}
		
	}
	
}

