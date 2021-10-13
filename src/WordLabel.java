import javax.swing.JLabel;

public class WordLabel extends JLabel {
	private String word;
	
	public WordLabel(String word) {
		this.word=word;
	}
	public String getWord() {
		return word;
	}
}
