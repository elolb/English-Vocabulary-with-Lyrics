import java.util.ArrayList;
import java.util.regex.Pattern;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// TODO decide: another extract method for when level is specified or? 
// TODO add class: process/check sentences before adding to database

public class Extractor {
	private String word;
	String definition;
	ArrayList<String> lyrics=new ArrayList<String>();
//	private String level;
	
	public Extractor(String word) {
		this.word=word;
		extract();
	}
/*	
	public Extractor(String word, String level) {
		this.word=word;
		this.level=level;;
		extract();
	}
*/	

	public void extract() {
		definition=extractDefinition(word);
	//	extractSentences(word);
		lyrics=extractLyrics(word);
	}

	public String extractDefinition(String word) {
		String dictionaryUrl="https://www.oxfordlearnersdictionaries.com/definition/english/";
		String postUrl="_1";
		String url=dictionaryUrl+word+postUrl;
		definition="";
        int count=1;
		try {
            Document document = Jsoup.connect(url).get();
            Elements block = document.select(".senses_multiple .sense");
            if(block.isEmpty()) {
            	block=document.select(".sense_single .sense");
            }
            String cf;
            String v;
            String def;
            for(Element el: block) {
            	boolean colon=false;
            	definition+=count+". ";
            	cf=el.select(".cf").text();
            	v=el.select(".v").text();
            	if(!(cf.isEmpty())) {
            		definition+=cf;
            		System.out.print(cf+" ");
            		colon=true;
            	}
            	if(!(v.isEmpty())) {
            		definition+=v;
            		System.out.print(v+" ");
            		colon=true;
            	}
            	def=el.select(".def").text();
            	if(colon) {
            		definition+=":";
            	}
            	definition+=" "+def+"\n";
            	count++;
            }
            
        	} catch (Exception e) {
            e.printStackTrace();
        }
		System.out.println("DEFINITION:\n"+definition);
		return definition;
	}
	
//	private ArrayList<String> extractSentences(String word) {
//		return null;
//	}
	
	public ArrayList<String> extractLyrics(String word) {
		ArrayList<String> lyrics = new ArrayList<String>();
		String dictionaryUrl="https://www.lyrics.com/lyrics/";
		String url=dictionaryUrl+word;
		
		try {
            Document document = Jsoup.connect(url).get();
            Elements body = document.select(".sec-lyric.clearfix");
            Elements lyric;
            Elements title;
            Elements artist;
            for(Element el: body) {
            	lyric = el.select(".lyric-body");
            	title=el.select(".lyric-meta-title");
            	artist=el.select(".lyric-meta-album-artist");
            	if(artist.isEmpty()) {
            		artist=el.select(".lyric-meta-artists");
            	}
            	lyrics.add("..."+lyric.text()+"...\n\n\t"+title.text()+" by "+artist.text()+"\n");   	
            }   
        } catch (Exception e) {
            e.printStackTrace();
        	}
		System.out.println("LYRICS:\n"+lyrics);
		return lyrics;
	}
}
