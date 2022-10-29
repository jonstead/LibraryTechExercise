import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Search_Onstead")
public class Search_Onstead extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public Search_Onstead() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String keyword = request.getParameter("keyword");
      String searchType = request.getParameter("searchType");
      String minWords = request.getParameter("minWords");
      int minWordsCount;
      if(minWords.isEmpty()) {
          minWordsCount = 0;
      }
      else {
    	  minWordsCount = Integer.valueOf(minWords);
      }
      search(keyword, searchType, minWordsCount, response);
   }

   void search(String keyword, String searchType, int minWordsCount, HttpServletResponse response) throws IOException {
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Library Database Result";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + //
            "transitional//en\">\n"; //
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h1 align=\"center\">" + title + "</h1>\n");

      Connection connection = null;
      PreparedStatement preparedStatement = null;
      keyword = keyword.toLowerCase();
      try {
         DBConnectionOnstead.getDBConnection();
         connection = DBConnectionOnstead.connection;
         String Main = "";

         if (keyword.isEmpty()) {
            String selectSQL = "SELECT * FROM LibraryTable";
            preparedStatement = connection.prepareStatement(selectSQL);
            Main = "Book";
         }
         else {
        	 if(searchType.equals("Book") || searchType.isEmpty() || (searchType.equals("Author First") == false && searchType.equals("Author Last") == false))
        	 {
        		 String selectSQL = "SELECT * FROM LibraryTable WHERE Book LIKE ?";
        		 String BookTitle = keyword + "%";
        		 preparedStatement = connection.prepareStatement(selectSQL);
        		 preparedStatement.setString(1, BookTitle);
        		 Main = "Book";
        	 }
        	 else if(searchType.equals("Author First"))
        	 {
        		 String selectSQL = "SELECT * FROM LibraryTable WHERE First LIKE ?";
        		 String authorFirst = keyword + "%";
        		 preparedStatement = connection.prepareStatement(selectSQL);
        		 preparedStatement.setString(1, authorFirst);
        		 Main = "First";
        	 }
        	 else if(searchType.equals("Author Last"))
        	 {
        		 String selectSQL = "SELECT * FROM LibraryTable WHERE Last LIKE ?";
        		 String authorLast = keyword + "%";
        		 preparedStatement = connection.prepareStatement(selectSQL);
        		 preparedStatement.setString(1, authorLast);
        		 Main = "Last";
        	 }
         }
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            int id = rs.getInt("id");
            String Book = rs.getString("Book").trim();
            String First = rs.getString("First").trim();
            String Last = rs.getString("Last").trim();
            String Pages = rs.getString("Pages").trim();
            String Words = rs.getString("Words").trim();
        	String WordsPlaceHold = Words;
            if(isNumeric(Words) == false) {
            	Words = "0";
            }
            if(Main.equals("Book")) {
            	if ((keyword.isEmpty() || Book.toLowerCase().contains(keyword)) && minWordsCount <= Integer.valueOf(Words)) {
            		out.println("ID: " + id + ", ");
            		out.println("Book Title: " + Book + ", ");
            		out.println("Authors First Name: " + First + ", ");
            		out.println("Authors Last Name: " + Last + "<br>");
            		out.println("Total Pages: " + Pages + "<br>");
            		if(isNumeric(WordsPlaceHold) == false) {
                		out.println("Total Words: " + WordsPlaceHold + "<br>");
            		}
            		else {
            			out.println("Total Words: " + Words + "<br>");
            			}
            		}
            	}
            else if(Main.equals("First")) {
            	if ((keyword.isEmpty() || First.toLowerCase().contains(keyword)) && minWordsCount <= Integer.valueOf(Words)) {
            		out.println("ID: " + id + ", ");
            		out.println("Book Title: " + Book + ", ");
            		out.println("Authors First Name: " + First + ", ");
            		out.println("Authors Last Name: " + Last + "<br>");
            		out.println("Total Pages: " + Pages + "<br>");
            		if(isNumeric(WordsPlaceHold) == false) {
                		out.println("Total Words: " + WordsPlaceHold + "<br>");
            		}
            		else {
            			out.println("Total Words: " + Words + "<br>");
            			}
            		}
            	}
            else if(Main.equals("Last")) {
            	if ((keyword.isEmpty() || Last.toLowerCase().contains(keyword)) && minWordsCount <= Integer.valueOf(Words)) {
            		out.println("ID: " + id + ", ");
            		out.println("Book Title: " + Book + ", ");
            		out.println("Authors First Name: " + First + ", ");
            		out.println("Authors Last Name: " + Last + "<br>");
            		out.println("Total Pages: " + Pages + "<br>");
            		if(isNumeric(WordsPlaceHold) == false) {
                		out.println("Total Words: " + WordsPlaceHold + "<br>");
            		}
            		else {
            			out.println("Total Words: " + Words + "<br>");
            			}
            		}
            	}
            }
         out.println("<a href=\"/Project-LibraryOfBooks/insert_onstead.html\">Insert a Book into the Library</a> <br>");
         out.println("</body></html>");
         rs.close();
         preparedStatement.close();
         connection.close();
      } catch (SQLException se) {
         se.printStackTrace();
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         try {
            if (preparedStatement != null)
               preparedStatement.close();
         } catch (SQLException se2) {
         }
         try {
            if (connection != null)
               connection.close();
         } catch (SQLException se) {
            se.printStackTrace();
         }
      }
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

   public static boolean isNumeric(String str) {
	   return str.matches("-?\\d+(\\.\\d+)?");
	   }
}
