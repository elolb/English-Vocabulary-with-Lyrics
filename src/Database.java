
 import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//TODO improve: design the database again, use dependencies to eliminate some of the code when removing word from tables.
//so the database itself handles some controls/errors instead of Java code.
//TODO delete: learned table -> just check for stage>4

 public class Database {
     private Connection connect() {
         // SQLite connection string
         String url = "jdbc:sqlite:srs.db";
         Connection conn = null;
         try {
             conn = DriverManager.getConnection(url);
         } catch (SQLException e) {
             System.out.println(e.getMessage());
         }
         return conn;
     }
     
     public String selectDefinition(String w){
         String sql = "SELECT Definition FROM Word WHERE Word='"+w+"';";
         String definition=null;
         try (Connection conn = this.connect();
              Statement stmt  = conn.createStatement();
              ResultSet rs    = stmt.executeQuery(sql)){
        	 while ( rs.next() ) {
                 definition = rs.getString("definition");
              }
              rs.close();
              stmt.close();
              conn.close();
         } catch (SQLException e) {
             System.out.println(e.getMessage());
         }
		return definition;
     }
//unnecessary?
     public ArrayList<String> getWordList(int status){
    	 String sql = "SELECT Word FROM Word WHERE Status='"+status+"';";
         ArrayList<String> wordList=new ArrayList<String>();
         try (Connection conn = this.connect();
              Statement stmt  = conn.createStatement();
              ResultSet rs    = stmt.executeQuery(sql)){
        	 while ( rs.next() ) {
                 wordList.add(rs.getString("word"));
              }
              rs.close();
              stmt.close();
              conn.close();
         } catch (SQLException e) {
             System.out.println(e.getMessage());
         }
		return wordList;	 
     }

     public ArrayList<String> getWordList(){
    	 String sql = "SELECT Word FROM Word WHERE Status<=0 AND Stage<5;";
         ArrayList<String> wordList=new ArrayList<String>();
         try (Connection conn = this.connect();
              Statement stmt  = conn.createStatement();
              ResultSet rs    = stmt.executeQuery(sql)){
        	 while ( rs.next() ) {
                 wordList.add(rs.getString("word"));
              }
              rs.close();
              stmt.close();
              conn.close();
         } catch (SQLException e) {
             System.out.println(e.getMessage());
         }
		return wordList;	 
     }
     
	public ArrayList<String> selectExamples(String w) {
		String sql = "SELECT Example FROM Example WHERE Word='"+w+"'AND (Seen=0 OR Seen IS NULL);";
        ArrayList<String> exampleList=new ArrayList<String>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
       	 while ( rs.next() ) {
                exampleList.add(rs.getString("Example"));
             }
             rs.close();
             stmt.close();
             conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		return exampleList;
	}
    
	public void setExampleSeen(int rowid) {
		String sql = "UPDATE Example SET Seen=1 WHERE ROWID=?";
        try(Connection conn = this.connect();
       		 PreparedStatement stmt= conn.prepareStatement(sql)){
		 stmt.setInt(1, rowid);
		 System.out.println(rowid);
		 stmt.executeUpdate();
		 }
         catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
      public void updateStatus(String word,int newStatus){
    	  
    	 String sql = "UPDATE Word SET Status=? WHERE Word=?";
    	 
         try(Connection conn = this.connect();
        		 PreparedStatement stmt= conn.prepareStatement(sql)){
		 stmt.setInt(1, newStatus);
		 stmt.setString(2, word);
		 stmt.executeUpdate();
		 stmt.close();
         conn.close();
		 }
             catch (SQLException e) {
             System.out.println(e.getMessage());
         }
       }

	public void updateAllStatus(int change) {
		String sql = "UPDATE Word SET Status=Status+? WHERE Stage<5";
		try(Connection conn = this.connect();
       		 PreparedStatement stmt= conn.prepareStatement(sql)){
		 stmt.setInt(1, change);
		 stmt.executeUpdate();
		 stmt.close();
         conn.close();
         }
		
		catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}

	public String getLastStatusUpdate() {
		String sql = "SELECT [Last Status Update] FROM Variable";
        String lastStatusUpdate=null;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
       	 while ( rs.next() ) {
                lastStatusUpdate=rs.getString("Last Status Update");
             }
             rs.close();
             stmt.close();
             conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		return lastStatusUpdate;
	}

	public String setLastStatusUpdate(String date) {
		String sql = "UPDATE Variable SET [Last Status Update]=?";
        String lastStatusUpdate=null;
        try(Connection conn = this.connect();
       		 PreparedStatement stmt= conn.prepareStatement(sql)){
		 stmt.setString(1, date);
		 stmt.executeUpdate();
		 }
         catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		return lastStatusUpdate;
	}
	
	public LocalDateTime getLastStatusUpdateDateTime() {
		String date=getLastStatusUpdate();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd/HH/mm");
		LocalDateTime lastStatusUpdateDateTime= LocalDateTime.parse(date, formatter);
		return lastStatusUpdateDateTime;
	}

	public int getLeftoverHours() {
		String sql = "SELECT [Leftover Hours] FROM Variable";
        int leftoverHours=-1;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
       	 while ( rs.next() ) {
       		leftoverHours=rs.getInt("Leftover Hours");
             }
             rs.close();
             stmt.close();
             conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
		return leftoverHours;
	}
      
     public void insertWord(String word, String definition) {
    	 String sql = "INSERT INTO Word VALUES (?,?,0,0,0,0)";
         try(	 Connection conn = this.connect();
        		 PreparedStatement stmt= conn.prepareStatement(sql)){
 		 stmt.setString(1, word);
 		 stmt.setString(2, definition);
 		 stmt.executeUpdate();
 		 }
          catch (SQLException e) {
             System.out.println(e.getMessage());
         }
     }
     
	public void insertExampleLyrics(String w, ArrayList<String> exampleLyrics, int index) {
		String sql = "INSERT INTO Example (Word, Example) VALUES (?, ?) ";
		int i=index;
		try(Connection conn = this.connect();
       		 PreparedStatement stmt= conn.prepareStatement(sql)){
        		for(; i<exampleLyrics.size(); i++) {	 
        			stmt.setString(1, w);
        			stmt.setString(2, exampleLyrics.get(i));
        			stmt.executeUpdate();	
        		}
        		conn.close();
        		}
         catch (SQLException e) {
             System.out.println(e.getMessage());
        	 if (e.getMessage().contains("SQLITE_CONSTRAINT_UNIQUE")) {
        		 insertExampleLyrics(w, exampleLyrics, ++i);
        		 }
         }	
		}

	public void changeStage(int change, String word) {
		int stage= getStage(word)+change;
		if(stage<0) {
			stage=0;
		}
		String sql = "UPDATE Word SET Stage=? WHERE Word=?";
    	
         try(Connection conn = this.connect();
        		 PreparedStatement stmt= conn.prepareStatement(sql)){
		 stmt.setInt(1, stage);
		 stmt.setString(2, word);
		 stmt.executeUpdate();
		 stmt.close();
         conn.close();
		 }
             catch (SQLException e) {
             System.out.println(e.getMessage());
         }
	}

	public int getStage(String word) {
		String sql = "SELECT Stage FROM Word WHERE Word='"+word+"';";
        int stage=-1;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
       	 while ( rs.next() ) {
       		 	stage=rs.getInt("Stage");
             }
             rs.close();
             stmt.close();
             conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
		return stage;
	}
	public int getSeen(String word) {
		String sql = "SELECT Seen FROM Word WHERE Word='"+word+"';";
        int stage=-1;
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
       	 while ( rs.next() ) {
       		 	stage=rs.getInt("Seen");
             }
             rs.close();
             stmt.close();
             conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        
		return stage;
	}
	public int getSoon(String word) {
		String sql = "SELECT Soon FROM Word WHERE Word='"+word+"';";
		int stage=-1;
		try (Connection conn = this.connect();
				Statement stmt  = conn.createStatement();
				ResultSet rs    = stmt.executeQuery(sql)){
			while ( rs.next() ) {
				stage=rs.getInt("Soon");
			}
			rs.close();
			stmt.close();
			conn.close();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		return stage;
	}
	public void incrementSeen(String word) {
		String sql = "UPDATE Word SET Seen=Seen+1 WHERE Word=?";
   	 
        try(Connection conn = this.connect();
       		 PreparedStatement stmt= conn.prepareStatement(sql)){
		 stmt.setString(1, word);
		 stmt.executeUpdate();
		 stmt.close();
        conn.close();
		 }
            catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}
	
	public void incrementSoon(String word) {
		String sql = "UPDATE Word SET Soon=Soon+1 WHERE Word=?";
   	 
        try(Connection conn = this.connect();
       		 PreparedStatement stmt= conn.prepareStatement(sql)){
		 stmt.setString(1, word);
		 stmt.executeUpdate();
		 stmt.close();
        conn.close();
		 }
            catch (SQLException e) {
            System.out.println(e.getMessage());
        }
	}


	
	

	public Map<String,Integer> selectExampleMap(String w) {
		String sql = "SELECT Example,ROWID FROM Example WHERE Word='"+w+"'AND (Seen=0 OR Seen IS NULL);";
     
        Map<String,Integer> exampleMap = new HashMap<>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
       	 while ( rs.next() ) {
                exampleMap.put(rs.getString("Example"),rs.getInt("ROWID"));
             }
             rs.close();
             stmt.close();
             conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		return exampleMap;
	}
	
    public ArrayList<String> getLearning(){
   	 String sql = "SELECT Word FROM Word WHERE Stage<5;";
        ArrayList<String> wordList=new ArrayList<String>();
        try (Connection conn = this.connect();
             Statement stmt  = conn.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
       	 while ( rs.next() ) {
                wordList.add(rs.getString("word"));
             }
             rs.close();
             stmt.close();
             conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		return wordList;	 
    }
    public ArrayList<String> getLearned(){
    	String sql = "SELECT Word FROM Word WHERE Stage=5;";
    	ArrayList<String> wordList=new ArrayList<String>();
    	try (Connection conn = this.connect();
    			Statement stmt  = conn.createStatement();
    			ResultSet rs    = stmt.executeQuery(sql)){
    		while ( rs.next() ) {
    			wordList.add(rs.getString("word"));
    		}
    		rs.close();
    		stmt.close();
    		conn.close();
    	} catch (SQLException e) {
    		System.out.println(e.getMessage());
    	}
    	return wordList;	 
    }
}

	/*
	 * public void insertLearnedExample(String word, int exampleCount) { // TODO
	 * Auto-generated method stub String sql = "DELETE FROM Example WHERE Word=?";
	 * 
	 * try(Connection conn = this.connect(); PreparedStatement stmt=
	 * conn.prepareStatement(sql)){ stmt.setString(1, word); stmt.executeUpdate();
	 * stmt.close(); conn.close(); } catch (SQLException e) {
	 * System.out.println(e.getMessage()); } }
	 */
 

	
