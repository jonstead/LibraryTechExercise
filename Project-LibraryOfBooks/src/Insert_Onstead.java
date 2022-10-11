
/**
 * @file Insert_Onstead.java
 */
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Insert_Onstead")
public class Insert_Onstead extends HttpServlet {
   private static final long serialVersionUID = 1L;

   public Insert_Onstead() {
      super();
   }

   protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      String Book = request.getParameter("Book");
      String First = request.getParameter("First");
      String Last = request.getParameter("Last");
      String Pages = request.getParameter("Pages");
      String Words = request.getParameter("Words");

      Connection connection = null;
      String insertSql = " INSERT INTO LibraryTable (Book, First, Last, Pages, Words) values (?, ?, ?, ?, ?)";

      try {
         DBConnectionOnstead.getDBConnection();
         connection = DBConnectionOnstead.connection;
         PreparedStatement preparedStmt = connection.prepareStatement(insertSql);
         preparedStmt.setString(1, Book);
         preparedStmt.setString(2, First);
         preparedStmt.setString(3, Last);
         preparedStmt.setString(4, Pages);
         preparedStmt.setString(5, Words);
         preparedStmt.execute();
         connection.close();
      } catch (Exception e) {
         e.printStackTrace();
      }

      // Set response content type
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      String title = "Insert Book to Library table";
      String docType = "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
      out.println(docType + //
            "<html>\n" + //
            "<head><title>" + title + "</title></head>\n" + //
            "<body bgcolor=\"#f0f0f0\">\n" + //
            "<h2 align=\"center\">" + title + "</h2>\n" + //
            "<ul>\n" + //

            "  <li><b>Book Title</b>: " + Book + "\n" + //
            "  <li><b>Authors First Name</b>: " + First + "\n" + //
            "  <li><b>Authors Last Name</b>: " + Last + "\n" + //
            "  <li><b>Total Pages</b>: " + Pages + "\n" + //
            "  <li><b>Total Words</b>: " + Words + "\n" + //

            "</ul>\n");

      out.println("<a href=\"/Project-LibraryOfBooks/search_onstead.html\">Search for a Book in the Library</a> <br>");
      out.println("</body></html>");
   }

   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
      doGet(request, response);
   }

}
