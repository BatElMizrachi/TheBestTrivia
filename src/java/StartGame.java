import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet(urlPatterns = {"/StartGame"})
public class StartGame extends HttpServlet 
{
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException, ClassNotFoundException 
    {
        //טעינת הקטגוריות שהמשתש בחר
        //טעינת הרמות קושי לכל קטגוריה
        // צריך ולוודא שלקטגוריה סומן רמה
        HashMap<String,String> CategoriesLevel = GetCategoriesLevelByUserChoose(request,response);
        
        // טעינת השאלות
        ArrayList<QuestionBase> allQuestions = new ArrayList<QuestionBase>();
        allQuestions = FileHandler.ReadQuestions();

        //הכנסת השאלות המתאימות לאוביקט
        ArrayList<QuestionBase> questions = new ArrayList<QuestionBase>();
        for (QuestionBase question : allQuestions) 
        {
            if ((CategoriesLevel.containsKey(question.GetCategory()))
                    && (CategoriesLevel.containsValue(question.GetLevel())))
            {
                questions.add(question);
            }
        }
        
        //ערבוב נתונים
        Collections.shuffle(questions, new Random());
        
        //הצגת שאלות
        
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StartGame</title>");            
            out.println("</head>");
            out.println("<body>");
     
            out.println("<h1>Servlet StartGame at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }
    
    protected HashMap<String,String> GetCategoriesLevelByUserChoose(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
       // ArrayList<String,String> categoryLevelUser = new ArrayList<String,String>();
        HashMap<String,String> categoryLevelUser = new HashMap<String,String>();
        String[] category = request.getParameterValues("Category");
        String categoryLevel;
        
        for (int i = 0; i < category.length; i++) 
        {
            categoryLevel = "Level" + category[i];
            categoryLevelUser.put(category[i], request.getParameter(categoryLevel)) ;
        }
        
        return categoryLevelUser;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException {
        try {
            processRequest(request, response);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(StartGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
