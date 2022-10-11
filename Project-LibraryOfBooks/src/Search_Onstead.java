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
      search(keyword, response);
   }

   void search(String keyword, HttpServletResponse response) throws IOException {
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
      try {
         DBConnectionOnstead.getDBConnection();
         connection = DBConnectionOnstead.connection;

         if (keyword.isEmpty()) {
            String selectSQL = "SELECT * FROM LibraryTable";
            preparedStatement = connection.prepareStatement(selectSQL);
         } else {
            String selectSQL = "SELECT * FROM LibraryTable WHERE Book LIKE ?";
            String BookTitle = keyword + "%";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, BookTitle);
         }
         ResultSet rs = preparedStatement.executeQuery();

         while (rs.next()) {
            int id = rs.getInt("id");
            String Book = rs.getString("Book").trim();
            String First = rs.getString("First").trim();
            String Last = rs.getString("Last").trim();
            String Pages = rs.getString("Pages").trim();
            String Words = rs.getString("Words").trim();

            if (keyword.isEmpty() || Book.contains(keyword)) {
               out.println("ID: " + id + ", ");
               out.println("Book Title: " + Book + ", ");
               out.println("Authors First Name: " + First + ", ");
               out.println("Authors Last Name: " + Last + "<br>");
               out.println("Total Pages: " + Pages + "<br>");
               out.println("Total Words: " + Words + "<br>");
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

}
