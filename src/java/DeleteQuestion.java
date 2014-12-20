/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Bat-El
 */
@WebServlet(urlPatterns = {"/DeleteQuestion"})
public class DeleteQuestion extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException {
        
        // הצגת כל השאלות
         ArrayList<QuestionBase> allQuestions = new ArrayList<QuestionBase>();
        try {
            allQuestions = FileHandler.ReadQuestions();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DeleteQuestion.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Show All Q</title>");   
            out.println("<link href=\"Style/bootstrap-theme.min.css\" rel=\"stylesheet\" type=\"text/css\"/>");
            out.println("<link href=\"Style/bootstrap-theme.css\" rel=\"stylesheet\" type=\"text/css\"/>");
            out.println("<link href=\"Style/bootstrap.min.css\" rel=\"stylesheet\" type=\"text/css\"/>");
            out.println("<link href=\"Style/bootstrap.css\" rel=\"stylesheet\" type=\"text/css\"/>");
            out.println("</head>");
            out.println("<body>");
            out.println(ShowForDelete(allQuestions));
            out.println("</body>");
            out.println("</html>");
        }
    }

    protected String ShowForDelete ( ArrayList<QuestionBase> allQuestions)
    {
        String listQuestion = "<div class=\"panel panel-info\">List of question</div>";
        listQuestion += "<ul class=\"list-group\">";
        int index = 1;

        for (QuestionBase question : allQuestions) 
        {
            listQuestion+= "<li class=\"list-group-item\">" + question.GetQuestion() + "</li>";
            index++;
        }
        
        listQuestion += "</ul>";
        if (index == 1)
            return "<div class=\"alert alert-danger\" role=\"alert\">There are no questions</div>";
        
        return listQuestion;
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
            throws ServletException, IOException {
        processRequest(request, response);
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
        processRequest(request, response);
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
