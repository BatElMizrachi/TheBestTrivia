
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/AddQuestion"})
public class AddQuestion extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException 
    {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet NewServlet</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NewServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void QuestionView(PrintWriter out)
    {
        out.println("<h1>Insert question:</h1>");
        out.println("<br>");
        out.println("<input type=\"text\" name=\"question\">");
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        Level lavel = Utils.GetLevelByUserChoose((String) request.getAttribute("Level"));
        Category category = Utils.GetCategoryByUserChoose((String) request.getAttribute("Category"));
        QuestionType questionType = Utils.GetQuestionTypeByUserChoose((String) request.getAttribute("QuestionType"));
          
        try (PrintWriter out = response.getWriter()) 
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet AddQuestion</title>");            
            out.println("</head>");
            out.println("<body>");
            if(questionType.equals(QuestionType.Open))
            {
                QuestionView(out);
                out.println("<h1>i</h1>");
            }
            else if (questionType.equals(QuestionType.YesNo))
            {
                QuestionView(out);
                
            }
            else if (questionType.equals(QuestionType.MultiplePossible))
            {
                QuestionView(out);
                
            }
            else
            {
                
            }
            
            //out.println("<h1>Servlet AddQuestion at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
