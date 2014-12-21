import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
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
    private static ArrayList<QuestionBase> questions;
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, FileNotFoundException, ClassNotFoundException 
    {
        String act = request.getParameter("act");
        
        if(request.getParameter("ask") != null && 
           act != null && request.getParameter("Continue").equals(act)) // ask question
        {
            int index = Integer.parseInt(request.getParameter("index")) + 1;
            int correctAnswersCount = Integer.parseInt(request.getParameter("correctAnswersCount"));
            
            try (PrintWriter out = response.getWriter()) 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet StartGame</title>");            
                out.println("</head>");
                out.println("<body>");
                AskQuestionForm(out, index, correctAnswersCount);
                out.println("</body>");
                out.println("</html>");
            }
        }
        else if(request.getParameter("Check") != null) // Check if correct and ask if wants more
        {
            int index = Integer.parseInt(request.getParameter("index"));
            QuestionBase currentQuestion = questions.get(index);
            CheckAnswer(request, currentQuestion, response, index);
        }
        else if(act != null && request.getParameter("End game").equals(act)) // show points
        {
            int numberOfQuestion = Integer.parseInt(request.getParameter("index")) + 1;
            int numberOfCorrectAnswers = Integer.parseInt(request.getParameter("correctAnswersCount"));
            int score = numberOfCorrectAnswers/numberOfQuestion;
            
            if(numberOfCorrectAnswers%numberOfQuestion >= numberOfCorrectAnswers/2)
            {
                score++;
            }
            
            ShowScoreView(response, score);
        }
        else // calc questions
        {
            CalculateQuestionList(request, response);
            int index = 0;
            
            try (PrintWriter out = response.getWriter()) 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet StartGame</title>");            
                out.println("</head>");
                out.println("<body>");
                
                if(questions.isEmpty())
                {
                    out.println("<form name=\"Failure\">");
                    out.println("<h1>There are no questions thet meet the conditions</h1>");
                    out.println("</form>");
                }
                else
                {
                    AskQuestionForm(out, index, 0);
                }
                
                out.println("</body>");
                out.println("</html>");
            }
        }
    }

    private void CheckAnswer(HttpServletRequest request, QuestionBase currentQuestion, HttpServletResponse response, int index) throws NumberFormatException, IOException {
        int correctAnswersCount = Integer.parseInt(request.getParameter("correctAnswersCount"));
        boolean isCorrect = false;
        
        if(currentQuestion.GetQuestionType() == QuestionType.Open)
        {
            if(((OpenQuestion)currentQuestion).GetAnswer().equals(request.getParameter("openAnswer")))
            {
                correctAnswersCount++;
                isCorrect = true;
            }
        }
        else if(currentQuestion.GetQuestionType() == QuestionType.YesNo)
        {
            if((((YesNoQuestion)currentQuestion).GetAnswer() && request.getParameter("yesNoAnswer").equals("Yes")) ||
                    (!((YesNoQuestion)currentQuestion).GetAnswer() && request.getParameter("yesNoAnswer").equals("No")))
            {
                correctAnswersCount++;
                isCorrect = true;
            }
        }
        else if (currentQuestion.GetQuestionType() == QuestionType.MultiplePossible)
        {
            if(((MultiplePossibleQuestion)currentQuestion).GetAnswer() ==
                    Integer.parseInt(request.getParameter("answerNumber")))
            {
                correctAnswersCount++;
                isCorrect = true;
            }
        }
        
        CheckView(response, index, correctAnswersCount, isCorrect);
    }

    private void CheckView(HttpServletResponse response, int index, int correctAnswersCount, boolean isCorrect) throws IOException {
        try (PrintWriter out = response.getWriter())
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StartGame</title>");
            out.println("</head>");
            out.println("<body>");
            
            out.println("<input type=\"hidden\" name=\"index\" value=\""+ index + "\">");
            out.println("<input type=\"hidden\" name=\"correctAnswersCount\" value=\""+ correctAnswersCount + "\">");
            
            if(isCorrect)
            {
                out.println("<form name=\"Success\">");
                out.println("<h1>Excellent!! correct answer</h1>");
                
                if(index+1 < questions.size())
                {
                    out.println("<br>");
                    out.println("<input type=\"submit\" value=\"Continue\">");
                }
                
                out.println("<br>");
                out.println("<input type=\"submit\" value=\"End game\">");
                
                out.println("</form>");
            }
            else
            {
                out.println("<form name=\"Failure\">");
                out.println("<h1>Wrong answer</h1>");
                
                if(index+1 < questions.size())
                {
                    out.println("<br>");
                    out.println("<input type=\"submit\" value=\"Continue\">");
                }
                
                out.println("<br>");
                out.println("<input type=\"submit\" value=\"End game\">");
                
                out.println("</form>");
            }
            
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void ShowScoreView(HttpServletResponse response, int score) throws IOException {
        try (PrintWriter out = response.getWriter())
        {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet StartGame</title>");
            out.println("</head>");
            out.println("<body>");
            
            if(score <= 60)
            {
                out.println("<form name=\"scoreBad\">");
                out.println("<h1>Your score is: "+ score +"</h1>");
                out.println("<h1>Maybe next time you will have better luck</h1>");
                out.println("</form>");
            }
            else if (score < 90)
            {
                out.println("<form name=\"scoreGood\">");
                out.println("<h1>Your score is: "+ score +"</h1>");
                out.println("<h1>There is room to improve, try again</h1>");
                out.println("</form>");
            }
            else
            {
                out.println("<form name=\"scoreExcellent\">");
                out.println("<h1>Your score is: "+ score +"</h1>");
                out.println("<h1>Excellent! It seems that you are an expert</h1>");
                out.println("</form>");
            }
            
            out.println("</body>");
            out.println("</html>");
        }
    }

    private void AskQuestionForm(final PrintWriter out, int index, int correctAnswersCount) {
        QuestionBase currentQuestion = questions.get(index);
        
        out.println("<form name=\"AskForm\">");
        out.println("<input type=\"hidden\" name=\"index\" value=\""+ index + "\">");
        out.println("<input type=\"hidden\" name=\"correctAnswersCount\" value=\""+ correctAnswersCount + "\">");
        out.println("<input type=\"hidden\" name=\"Check\" value=\"Yes\">");
        
        if(currentQuestion.GetQuestionType() == QuestionType.Open)
        {
            ShowQuestion(out, currentQuestion);
            out.println("<h1>Your answer:</h1>");
            out.println("<br>");
            out.println("<input type=\"text\" name=\"openAnswer\" width=\"400\" height=\"50\">");
            out.println("<br>");
            out.println("<input type=\"submit\" value=\"Send\" onsubmit=\"return validateForm()\" >"); //noya validtion
            
        }
        else if(currentQuestion.GetQuestionType() == QuestionType.YesNo)
        {
            ShowQuestion(out, currentQuestion);
            
            out.println("<h1>Your answer:</h1>");
            out.println("<br>");
            out.println("<input type=\"radio\" name=\"yesNoAnswer\" value=\"Yes\" checked>Yes");
            out.println("<br>");
            out.println("<input type=\"radio\" name=\"yesNoAnswer\" value=\"No\">No");  
        }
        else if (currentQuestion.GetQuestionType() == QuestionType.MultiplePossible)
        {
            ShowQuestion(out, currentQuestion);
            out.println("   <ol>");
            
            Map<String, String> allAnswer = ((MultiplePossibleQuestion)currentQuestion).GetAllAnswer();
            for (int i = 1; i <= allAnswer.size(); i++) {
                out.println("       <li>" + allAnswer.get(Integer.toString(i)) + "</li>");
            }
            
            out.println("   </ol>");
            
            out.println("<h1>Select answers number:</h1>");
            out.println("<input type=\"text\" name=\"answerNumber\">");
        }
        
        out.println("<br>");
        out.println("<input type=\"submit\" value=\"Send\" onsubmit=\"return validateForm()\" >"); //noya validtion
        out.println("</form>");
    }

    private void ShowQuestion(final PrintWriter out, QuestionBase currentQuestion) {
        out.println("<h1>The question is:</h1>");
        out.println("<br>");
        out.println("<h1>"+ currentQuestion.GetQuestion() +"</h1>");
        out.println("<br>");
    }

    private void CalculateQuestionList(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, ClassNotFoundException {
        //טעינת הקטגוריות שהמשתש בחר
        //טעינת הרמות קושי לכל קטגוריה
        // צריך ולוודא שלקטגוריה סומן רמה
        HashMap<String,String> CategoriesLevel = GetCategoriesLevelByUserChoose(request,response);
        
        // טעינת השאלות
        ArrayList<QuestionBase> allQuestions = new ArrayList<QuestionBase>();
        allQuestions = FileHandler.ReadQuestions();
        
        //הכנסת השאלות המתאימות לאוביקט
        questions = new ArrayList<QuestionBase>();
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
