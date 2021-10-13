import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

public class LibraryTab extends JPanel{
	private Database db;
	private Word w;
	private JPanel libraryPanel,learningPanel, learnedPanel, wordDetailPanel;
	private JLabel jl;
	private JButton backButton;
	private ArrayList<String> learning, learned;
	private GridLayout gridLayout;
	private CardLayout cardLayout;
	private ImageIcon backIcon;
	

	LibraryTab(){
		db = new Database();
		cardLayout=new CardLayout();
		
		setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
		
//		JScrollPane scrollLibrary = new JScrollPane(libraryTab);
//		scrollLibrary.getVerticalScrollBar().setUnitIncrement(16);
		
		libraryPanel = new JPanel();
		learningPanel = new JPanel();
		learnedPanel = new JPanel();
		wordDetailPanel= new JPanel();
		
		JScrollPane scrollLearning = new JScrollPane(learningPanel);
		JScrollPane scrollLearned = new JScrollPane(learnedPanel);
		scrollLearning.getVerticalScrollBar().setUnitIncrement(16);
		scrollLearned.getVerticalScrollBar().setUnitIncrement(16);

		libraryPanel.setLayout(cardLayout);
		
		JPanel comboBoxPane = new JPanel();
		String comboBoxItems[] = { "Learning", "Learned" };
		JComboBox cb = new JComboBox(comboBoxItems);
		cb.setEditable(false);
		cb.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String selected=(String) ((JComboBox)e.getSource()).getItemAt(cb.getSelectedIndex());
				cardLayout.show(libraryPanel,selected);
				System.out.println(selected);

			}
			
		});
		comboBoxPane.add(cb);
		
		learning = getLearning();
		learned = getLearned();
		
		int rows=learning.size();
		gridLayout = new GridLayout((rows/3)+1,3);
		gridLayout.setHgap(10);
		gridLayout.setVgap(5);

		learningPanel.setLayout(gridLayout);
		for(String word:learning) {
			w= new Word(word,false);
			learningPanel.add(getWordDetailLabel(word));
		}
		
		rows=learned.size();
		gridLayout = new GridLayout((rows/3)+1,3);
		gridLayout.setHgap(10);
		gridLayout.setVgap(5);
		
		learnedPanel.setLayout(gridLayout);
		for(String word:learned) {
			w= new Word(word,false);
			learnedPanel.add(getWordDetailLabel(word));
		}
		System.out.println(rows);
		if(rows<=2) {
		learnedPanel.setBorder(BorderFactory.createEmptyBorder(0,10,600,10));
		}
//		 libraryPanel.add(learningPanel);
		 
//		 libraryPanel.add(scrollPane);
		 
		add(comboBoxPane);
		libraryPanel.add(scrollLearning,"Learning");
		libraryPanel.add(wordDetailPanel,"detail");
		libraryPanel.add(scrollLearned,"Learned");
		add(libraryPanel); 
		cardLayout.show(libraryPanel,"Learning");

	}
	private ArrayList<String> getLearned() {
		
		return db.getLearned();
	}
	private ArrayList<String> getLearning() {
		
		return db.getLearning();
	}
	
	public JLabel getWordDetailLabel(String word) {
		JLabel jl = new WordLabel(word);
		jl.setText(w.labelCard(word));
		jl.setBorder(BorderFactory.createLineBorder(Color.PINK));
		jl.setPreferredSize(new Dimension(50,100));
		jl.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
//				System.out.println(((WordLabel)e.getComponent()).getWord());
				JTextPane textPane= w.wordDetail(((WordLabel)e.getComponent()).getWord());
				textPane.setOpaque(false);
				textPane.setPreferredSize(new Dimension(500, 500));
				wordDetailPanel.removeAll();
				wordDetailPanel.add(getBackButton());
				wordDetailPanel.add(new JScrollPane(textPane));
				cardLayout.show(e.getComponent().getParent().getParent().getParent().getParent(),"detail");
//				System.out.println(e.getComponent().getParent().getParent().getParent());
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}
		});
		return jl;
	}
	protected JButton getBackButton() {
		backIcon= new ImageIcon("arrow-left-line.png");
		backButton=new JButton(backIcon);
		backButton.setContentAreaFilled(false);
		backButton.setFocusPainted(false);
	//	backButton.setBorderPainted(false);
		backButton.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
				cardLayout.show(e.getComponent().getParent().getParent(),"Learning");
				System.out.println(e.getComponent().getParent().getParent());
				wordDetailPanel.removeAll();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

		
		});
		return backButton;
	}	
}
