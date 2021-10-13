import java.util.ArrayList;

public class TestEnterLyrics {
	public static void main(String[] args) {
		String word="lovely";
		Database db=new Database();
		Extractor ex= new Extractor(word);
		ArrayList<String> exampleLyrics=ex.extractLyrics(word);
		db.insertExampleLyrics(word, exampleLyrics,0);
	}
}
