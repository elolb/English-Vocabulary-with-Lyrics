import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;


public class SrsList {
	ArrayList<String> wordList= new ArrayList<String>();
	Database db = new Database();
	int laterMultiplier=2;
	LocalDateTime currentDate;  	
	LocalDateTime lastStatusUpdate;
	int leftoverHours;	//from last status update

	public SrsList() {
		currentDate=LocalDateTime.now();
		lastStatusUpdate=getLastStatusUpdateDateTime();
		leftoverHours=getLeftoverHours();
	}
	
	private int getLeftoverHours() {
		return db.getLeftoverHours();
	}

	private LocalDateTime getLastStatusUpdateDateTime() {
		return db.getLastStatusUpdateDateTime();
	}
	
	private void setLastStatusUpdate(LocalDateTime ldt) {
		lastStatusUpdate=ldt;
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm");
		String date=ldt.format(formatter);
		db.setLastStatusUpdate(date);
	}
	
	private static long hoursBetween(LocalDateTime from, LocalDateTime to) {
        return ChronoUnit.HOURS.between(from, to);
    }
	
	public ArrayList<String> getWordList(int status) {
		wordList=db.getWordList(status);
		return wordList; 
	}
	
	public ArrayList<String> getWordList() {
		wordList=db.getWordList();
		return wordList; 
	}
	
	public String getNextWord() {
		wordList=getWordList();
		return wordList.get(0);
	}
	

	public void soonerOrLater(String word, String soonerOrLater, int exampleCount) {

		int change=1;
		db.incrementSeen(word);
		if(soonerOrLater.equals("soon")){
			change=-1;
			db.incrementSoon(word);
		}
		db.changeStage(change, word);
		int stage= db.getStage(word);
		if(stage<5) {
		int newStatus=stage+1;
		db.updateStatus(word, newStatus);
		} 
	}
	


	public void updateAllStatus() {
		int hoursSinceUpdate=(int) hoursBetween(lastStatusUpdate, currentDate);
		int daysSinceUpdate=(hoursSinceUpdate+leftoverHours)/24;
		if(daysSinceUpdate>=1) {
			db.updateAllStatus(-daysSinceUpdate);
			currentDate=LocalDateTime.now();
			setLastStatusUpdate(currentDate);
			leftoverHours=(hoursSinceUpdate+leftoverHours)-(24*daysSinceUpdate);
			}
	
	
	}
}
