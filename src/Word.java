import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.Timer;

//TODO refactor: the constructors and parameters where there is word. Some of them may be unnecessary.

public class Word {
	String word=null;
	String definition= null;
	String exampleSentence=null;
	int status=0;
	Database db= new Database();
	Extractor ext;
	//ArrayList<String> words= new ArrayList<String>();
    ArrayList<String> exampleList=null;
    ArrayList<String> exampleLyrics=null;
    Map<String, Integer> exampleHashMap =null;
    
	public Word(String w, boolean setSeen) {
		word= w;
		definition= this.getDefinition(w);
		if(setSeen) {
			exampleSentence=this.getExampleSentence(0, setSeen);
		}
	}

	public String getDefinition(String w) {
		return db.selectDefinition(w);
	}
	
	
	private ArrayList <String> getExampleList(String w) {
			return db.selectExamples(w);
	}
	private Map<String, Integer> getExampleMap(String w) {
		return db.selectExampleMap(w);
	}
	public String getExampleSentence(int index, boolean setSeen) {
		exampleList=getExampleList(word);
		exampleHashMap=getExampleMap(word);
		String result=null;
		try {
		 result=exampleList.get(index);
		 int seen= exampleHashMap.get(result);
		 System.out.println(seen);
		 if(setSeen) {
			 db.setExampleSeen(seen);
		 }
		} catch(IndexOutOfBoundsException e){
			result="********		No more examples are available :(	************";
		}
		return result;
	}
	
	public void enterWord(String w, JFrame guiFrame) {
		if(w != null) {
		ext= new Extractor(w);
		definition=ext.extractDefinition(w);
		if(!(definition.equals(""))) {
			db.insertWord(w, definition);
			exampleLyrics=ext.extractLyrics(w);
			
			db.insertExampleLyrics(w, exampleLyrics, 0);
		
			//show success message
			JFrame f= new JFrame();
			//f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			JLabel l=new JLabel("Successfully added word.");
			f.add(l);
			f.setSize(30, 30);
			f.setUndecorated(true);
			f.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			f.setLocationRelativeTo(guiFrame);
			f.pack();
			f.setVisible(true);
			int delay = 4000; //milliseconds
			ActionListener taskPerformer = new ActionListener() {
			      public void actionPerformed(ActionEvent evt) {
			    	  f.setVisible(false); 
			    	  f.dispose(); 
			      }
			  };
			 new Timer(delay, taskPerformer).start();
			} else {
				JOptionPane.showMessageDialog(new JFrame(), "Couldn't find any definitions for '"+w+"'. Did you make a typo?" , "Failed to add new word",
				        JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public float getScore(String word) {
		int seen=db.getSeen(word);
		int later=seen-db.getSoon(word);
		float score=(float)later/seen;
		if(seen==0) {
			score=-1;
		}
		return score;
	}
	public int getStage(String word) {
		return db.getStage(word);
	}
	
//	public void setExampleSeen(String newSentence) {
//	}
	
	public String labelCard(String word) {
		
		int stage = getStage(word);
		String stageText="<html><p><span style=\"text-align:left\">Stage:"+stage+"</span>&emsp;&emsp;&emsp;";
		float score=getScore(word);
		String scoreText="<span style=\"color:olive\"><i>Perfect</i></span>";
		if(score<1 && score>=0.65) {
			scoreText="<span style=\"color:green\">Good</span>";
			
		} else if(score<0.65&& score>0.4) {
			scoreText="<span style=\"color:orange\">Not bad</span>";
		} else if (score>0&&score<=0.4) {
			scoreText="<span style=\"color:red\">Weak</span>";
		} else if (score<0) {
			scoreText="";
		}
		if(stage==5) {
			stageText="<html><p><span style=\"text-align:left\">Completed</span>&emsp;&emsp;&emsp;";
		} 
			
		String text=stageText
				+ "<div text-align:right>"+scoreText+"</div></p>"+"<br><br>"
				+ "<p style=\"text-align:center;font-size:20px\">"+word+"</p></html>";
		
		return text;
	}
	
	public JTextPane wordDetail(String word) {
		String d=(getDefinition(word)).replaceAll("\n","<br>");
		String text= "<html><p><span style=\"text-align:left;font-size:20px\">"+word+"</span></p><br>"+ d+"<br></html>";
		JTextPane textPane= new JTextPane();
		textPane.setContentType("text/html");
		textPane.setText(text);
		
		return textPane;
	}
}
