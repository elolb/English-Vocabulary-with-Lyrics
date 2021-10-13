
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

// TODO add: tab of learned words with seen examples and word scores and stage.
// TODO add: buttons for  <-going back and forth-> between examples
// TODO fix: Changing the word makes the cardTA shrink.
public class Gui extends JFrame {

	private static final long serialVersionUID = -4989487849567880588L;
	private JPanel btnPanel, definitionPanel, whenPanel, eastPanel, reviewPanel;
	private LibraryTab libraryTab;
	private JTabbedPane tabbedPane;
	private JTextField wordTF;
	private JTextArea cardTA, definitionTA;
	private JButton anotherExampleBtn, definitionBtn, soonerBtn, laterBtn;
	private JLabel orLabel;
	private int wordTFMaxH, exampleCount;
	private JMenuBar menuBar;
	private JMenuItem addWordMenuItem, creditsMenuItem;
	private SrsList srsL;
	private Word word;
	private String currentWord;
	
	public Gui() {
		srsL= new SrsList();
		menuBar = new JMenuBar();

		tabbedPane = new JTabbedPane(); 
		
		wordTFMaxH=20;
		exampleCount=1;
		
		orLabel= new JLabel("or");
		orLabel.setBorder(BorderFactory.createEmptyBorder(0,10,0,10));
		
		btnPanel= new JPanel();
		eastPanel= new JPanel();		
		eastPanel.setLayout(new BoxLayout(eastPanel, BoxLayout.PAGE_AXIS));

		definitionPanel= new JPanel();
		definitionPanel.setLayout(new BoxLayout(definitionPanel, BoxLayout.PAGE_AXIS));
		whenPanel= new JPanel();
		whenPanel.setLayout(new BoxLayout(whenPanel, BoxLayout.LINE_AXIS));

		addWordMenuItem = new JMenuItem((new AbstractAction("Add new word") {
		    public void actionPerformed(ActionEvent e) {
		    	String w = JOptionPane.showInputDialog(null,
	                        "Enter the word.", null);
		    	word= new Word(w, false);
				word.enterWord(w, getFrame());
				start();
		    }
		}));
		creditsMenuItem = new JMenuItem((new AbstractAction("Disclaimer") {
			public void actionPerformed(ActionEvent e) {
				String disclaimer="<html>All lyrics are taken from <a href=\">www.lyrics.com\">www.lyrics.com</a><br>"+
			"All word definitions are taken from <a href=\">www.oxfordlearnersdictionaries.com\">www.oxfordlearnersdictionaries.com</a></html>";
				JOptionPane.showMessageDialog(null, disclaimer, "Disclaimer", JOptionPane.PLAIN_MESSAGE);
				
			}
		}));

		menuBar.add(addWordMenuItem);
		menuBar.add(creditsMenuItem);
		
		cardTA= new JTextArea("Card info here");
		definitionTA= new JTextArea("definition comes here");
		wordTF= new JTextField("word comes here");		
		
		wordTF.setMaximumSize(new Dimension(Integer.MAX_VALUE ,wordTFMaxH));
		
		cardTA.setLineWrap(true);
		cardTA.setWrapStyleWord(true);
		cardTA.setEditable(false);
		cardTA.setOpaque(false);
		definitionTA.setEditable(false);
		definitionTA.setOpaque(false);
		definitionTA.setPreferredSize(new Dimension(300,300));
		definitionTA.setLineWrap(true);
		definitionTA.setWrapStyleWord(true);
		JScrollPane scrollPane = new JScrollPane(definitionTA);


		wordTF.setEditable(false);
		
        definitionTA.setForeground(Color.BLUE);
		anotherExampleBtn= new JButton("Another Example");
		anotherExampleBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(currentWord!=null){
					String newSentence=word.getExampleSentence(exampleCount, true);
					cardTA.setText(newSentence);
					incrementExampleCount();
//				word.setExampleSeen(newSentence);
				}
			}
		});
		definitionBtn= new JButton("See Definition");
		definitionBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(currentWord!=null){
				definitionTA.setText(word.definition);
				revalidate();
//				pack();		// keep this commented out so button panel stays at bottom even after a button is clicked
					}
				}
			});
		
		soonerBtn= new JButton("Sooner");
		laterBtn= new JButton("Later");
		
		soonerBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(currentWord!=null){srsL.soonerOrLater( word.word ,"soon", exampleCount);
				 
				start();
				}
			}
			
		});
		laterBtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				if(currentWord!=null){ srsL.soonerOrLater( word.word ,"late", exampleCount);
				 start();
				}	
			}
			
		});
		
		libraryTab = new LibraryTab();
		reviewPanel = new JPanel();
		reviewPanel.add(cardTA, BorderLayout.CENTER); 
		
		
		
		definitionPanel.add(wordTF);
		definitionPanel.add(scrollPane);
		
		whenPanel.add(soonerBtn);
		whenPanel.add(orLabel);
		whenPanel.add(laterBtn);
		
		eastPanel.add(definitionPanel);
		eastPanel.add(whenPanel);
		eastPanel.setBorder(BorderFactory.createEmptyBorder(0,0,50,20));
		reviewPanel.add(eastPanel, BorderLayout.EAST); 
		
		btnPanel.add(anotherExampleBtn);
		btnPanel.add(definitionBtn);
		reviewPanel.add(btnPanel, BorderLayout.SOUTH);	
		
		tabbedPane.addTab("Review", null, reviewPanel,
                "Review your words");
		tabbedPane.addTab("Library", null, libraryTab, "View all your words" );
		
		add(tabbedPane);
		setJMenuBar(menuBar);
		setTitle("MVP");
		setSize(700,700);
		this.setLocationRelativeTo(null); // center the frame
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public void start() {
		restartExampleCount();
		try {		
		srsL.updateAllStatus();
		currentWord=srsL.getNextWord();
		definitionTA.setText("");
		wordTF.setText(currentWord);
		word= new Word(currentWord,true);
		cardTA.setText(word.exampleSentence);
		} catch (IndexOutOfBoundsException e){
			cardTA.setText("********		You don't have any words to review. Go take a walk.	************");
			wordTF.setText("");
			definitionTA.setText("");
			currentWord=null;
		}
	}
	public void restartExampleCount() {
		this.exampleCount=1;
	}
	public void incrementExampleCount() {
		(this.exampleCount)++;
	}
	
	public JFrame getFrame() {
		 return this;
	}
	
	
	public static void main(String[] args) {
		Gui gui= new Gui();
		gui.start();
		
	//  looks better without pack's resizing.
	//	gui.pack();
		
	}
}
