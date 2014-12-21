
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(urlPatterns = {"/AddQuestion"})
public class AddQuestion extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, Exception 
    {
        response.setContentType("text/html;charset=UTF-8");
        
        if(request.getParameter("forSave") != null) // view to save question 
        {
            PrintWriter out = response.getWriter();
            try 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet AddQuestion</title>");            
                out.println("</head>");
                out.println("<body>");
                
                AddQuestionByType(request);

                out.println("<form name=\"Success\">");
                out.println("<h1>The question has been saved</h1>");
                out.println("<image/>");    //noya להוסיף תמונה
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }
            catch (Exception ex) 
            {
                out.println("<form name=\"Failure\">");
                out.println("<h1>The question has not been saved</h1>");
                out.println("<image/>");    //noya להוסיף תמונה
                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }
            finally
            { 
                out.close();
            }
        }
        else if(request.getParameter("count") != null) // view - for multiple
        {
            PrintWriter out = response.getWriter();
            try 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet AddQuestion</title>");  
                
                SetJavaScriptForMultiplePossibleQuestion(out);
                
                out.println("</head>");
                out.println("<body>");
                out.println("<form name=\"showViewQuestionToAdd\" Action=\"AddQuestion\">");
                
                HiddenInputViewForMultiplePossibleQuestion(out, request);
                out.println("<h1>Insert question:</h1>");
                out.println("<input type=\"text\" name=\"question\" value=" 
                        + request.getParameter("question") 
                        + " width=\"400\" height=\"50\">");
                out.println("<br>");
                
                out.println("   <h1>Insert possible answers:</h1>");
                out.println("   <ol>");
                
                for (int i = 1; i <= Integer.parseInt(request.getParameter("count")); i++) {
                    out.println("       <li>");
                    out.println("           <input type=\"text\" name=\""+i+"\" width=\"400\" height=\"50\">");
                    out.println("       </li>");
                }
                
                out.println("   </ol>");
                
                out.println("<input type=\"hidden\" name=\"forSave\" value=\"yes\">");
                out.println("<h1>Select answers number:</h1>");
                out.println("<input type=\"text\" name=\"numberOfAnswer\">");
                
                SaveView(out);

                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }
            finally
            {
                out.close();
            }
        }
        
        else // view - to insert question 
        {
            Level lavel = Utils.GetLevelByUserChoose((String) request.getParameter("Lavel"));
            Category category = Utils.GetCategoryByUserChoose((String) request.getParameter("Category"));
            QuestionType questionType = Utils.GetQuestionTypeByUserChoose((String) request.getParameter("QuestionType"));

            PrintWriter out = response.getWriter();
            try 
            {
                out.println("<!DOCTYPE html>");
                out.println("<html>");
                out.println("<head>");
                out.println("<title>Servlet AddQuestion</title>");  
                
                SetJavaScript(questionType, out);
                
                out.println("</head>");
                out.println("<body>");
                out.println("<form name=\"showViewQuestionToAdd\" Action=\"AddQuestion\">");
                
                SetViewByQuestionType(questionType, out, request);

                out.println("</form>");
                out.println("</body>");
                out.println("</html>");
            }
            finally
            {
                out.close();
            }
        }
    }

    private void SetViewByQuestionType(QuestionType questionType, final PrintWriter out, HttpServletRequest request) {
        if(questionType.equals(QuestionType.Open))
        {
            HiddenInputView(out, request);
            QuestionView(out);
            out.println("<h1>Insert answer:</h1>");
            out.println("<input type=\"text\" name=\"openAnswer\" width=\"400\" height=\"50\">");
            out.println("<input type=\"hidden\" name=\"forSave\" value=\"yes\">");
            SaveView(out);
        }
        else if (questionType.equals(QuestionType.YesNo))
        {
            HiddenInputView(out, request);
            QuestionView(out);
            out.println("<h1>Select answer:</h1>");
            out.println("<input type=\"radio\" name=\"yesNoAnswer\" value=\"Yes\" checked>Yes");
            out.println("<br>");
            out.println("<input type=\"radio\" name=\"yesNoAnswer\" value=\"No\">No");
            out.println("<input type=\"hidden\" name=\"forSave\" value=\"yes\">");
            SaveView(out);
        }
        else if (questionType.equals(QuestionType.MultiplePossible))
        {
            QuestionView(out);
            
            out.println("<h1>Insert count of possible answers:</h1>");
            out.println("<input type=\"text\" name=\"count\" width=\"400\" height=\"50\">");
            out.println("<br>");
            out.println("<input type=\"submit\" value=\"Continue\" onsubmit=\"return validateForm()\" >");
            
            HiddenInputView(out, request);
        }
        else
        {
            out.println("<form name=\"error\">");
            out.println("<h1 name=\"error\">Error</h1>");
            out.println("</form>");
        }
    }

    private void AddQuestionByType(HttpServletRequest request) throws Exception {
        
        ArrayList<QuestionBase> allQuestions = new ArrayList<QuestionBase>();
        allQuestions = FileHandler.ReadQuestions();
        
        if(request.getParameter("openAnswer") != null)
        {
            OpenQuestion openQuestion = new OpenQuestion();
            openQuestion.SetAnswer(request.getParameter("openAnswer"));
            openQuestion.SetQuestion(request.getParameter("question"));
            openQuestion.SetCategory((String) request.getParameter("Category"));
            openQuestion.SetLevel((String) request.getParameter("Level"));
            allQuestions.add(openQuestion);
            FileHandler.WriteQuestions(allQuestions);
        }
        else if (request.getParameter("yesNoAnswer") != null)
        {
            YesNoQuestion yesNoQuestion = new YesNoQuestion();
            yesNoQuestion.SetAnswer(request.getParameter("yesNoAnswer"));
            yesNoQuestion.SetQuestion(request.getParameter("question"));
            yesNoQuestion.SetCategory((String) request.getParameter("Category"));
            yesNoQuestion.SetLevel((String) request.getParameter("Level"));
            allQuestions.add(yesNoQuestion);
            FileHandler.WriteQuestions(allQuestions);
        }
        else if (request.getParameter("numberOfAnswer") != null)
        {
            MultiplePossibleQuestion multiplePossibleQuestion = new MultiplePossibleQuestion();
            multiplePossibleQuestion.SetAnswer(Integer.parseInt(request.getParameter("numberOfAnswer")));
            multiplePossibleQuestion.SetQuestion(request.getParameter("question"));
            
            for (int i = 1; i <= Integer.parseInt(request.getParameter("count")); i++)
            {
                if(request.getParameter(Integer.toString(i)) != null &&
                        !request.getParameter(Integer.toString(i)).isEmpty())
                {
                    multiplePossibleQuestion.AddToAllAnswer(Integer.toString(i),
                            request.getParameter(Integer.toString(i)));
                }
            }
            
            multiplePossibleQuestion.SetCategory((String) request.getParameter("Category"));
            multiplePossibleQuestion.SetLevel((String) request.getParameter("Level"));
            allQuestions.add(multiplePossibleQuestion);
            FileHandler.WriteQuestions(allQuestions);
        }
        else
        {
            throw new Exception();
        }
    }

    private void SetJavaScript(QuestionType questionType, final PrintWriter out) {
        if(questionType.equals(QuestionType.Open))
        {
            out.println("<script>\n" +
                    "function validateForm() {\n" +
                    "    var x = document.forms[\"showViewQuestionToAdd\"][\"question\"].value;\n" +
                    "    var y = document.forms[\"showViewQuestionToAdd\"][\"openAnswer\"].value;\n" +
                    "    if (x==null || x==\"\") {\n" +
                    "        alert(\"Question field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (y==null || y==\"\") {\n" +
                    "        alert(\"Answer field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "}\n" +
                    "</script>");
        }
        else if (questionType.equals(QuestionType.YesNo))
        {
            out.println("<script>\n" +
                    "function validateForm() {\n" +
                    "    var x = document.forms[\"showViewQuestionToAdd\"][\"question\"].value;\n" +
                    "    if (x==null || x==\"\") {\n" +
                    "        alert(\"Question field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "}\n" +
                    "</script>");
        }
        else if (questionType.equals(QuestionType.MultiplePossible))
        {
            out.println("<script>\n" +
                    "function validateForm() {\n" +
                    "    var x = document.forms[\"showViewQuestionToAdd\"][\"question\"].value;\n" +
                    "    var y = document.forms[\"showViewQuestionToAdd\"][\"count\"].value;\n" +
                    "    if (x==null || x==\"\") {\n" +
                    "        alert(\"Question field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (y==null || y==\"\") {\n" +
                    "        alert(\"Possible answer count field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (!isNaN(parseFloat(y)) && isFinite(y)) {\n" +
                    "        alert(\"Possible answer count field must be numeric\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (y < 2) {\n" +
                    "        alert(\"Possible answer count must be bigger the 1\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "}\n" +
                    "</script>");
        }
    }
    
    private void SetJavaScriptForMultiplePossibleQuestion(final PrintWriter out) {
    
        out.println("<script>\n" +
                    "function validateForm() {\n" +
                    "    var x = document.forms[\"showViewQuestionToAdd\"][\"question\"].value;\n" +
                    "    var y = document.forms[\"showViewQuestionToAdd\"][\"numberOfAnswer\"].value;\n" +
                    "    var a = document.forms[\"showViewQuestionToAdd\"][\"1\"].value;\n" +
                    "    var b = document.forms[\"showViewQuestionToAdd\"][\"2\"].value;\n" +
                    "    if (x==null || x==\"\") {\n" +
                    "        alert(\"Question field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (y==null || y==\"\") {\n" +
                    "        alert(\"Answer field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (a==null || a==\"\") {\n" +
                    "        alert(\"First possible answer field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (b==null || b==\"\") {\n" +
                    "        alert(\"Second possible answer field must be filled out\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "    if (!isNaN(parseFloat(y)) && isFinite(y)) {\n" +
                    "        alert(\"Answer field must be numeric\");\n" +
                    "        return false;\n" +
                    "    }\n" +
                    "}\n" +
                    "</script>");
    }

    private void QuestionView(PrintWriter out){
        out.println("<h1>Insert question:</h1>");
        out.println("<input type=\"text\" name=\"question\" width=\"400\" height=\"50\">");
        out.println("<br>");
    }
    
    private void HiddenInputView(PrintWriter out, HttpServletRequest request){
        out.println("<input type=\"hidden\" name=\"Level\" value=\""+request.getParameter("Lavel")+"\">");
        out.println("<input type=\"hidden\" name=\"Category\" value=\""+request.getParameter("Category")+"\">");
    }
    
    private void HiddenInputViewForMultiplePossibleQuestion(PrintWriter out, HttpServletRequest request){
        out.println("<input type=\"hidden\" name=\"Level\" value=\""+request.getParameter("Lavel")+"\">");
        out.println("<input type=\"hidden\" name=\"Category\" value=\""+request.getParameter("Category")+"\">");
        out.println("<input type=\"hidden\" name=\"count\" value=\""+request.getParameter("count")+"\">");
    }
    
    private void SaveView(PrintWriter out){
        out.println("<br>");
        out.println("<input type=\"submit\" value=\"Save\" onsubmit=\"return validateForm()\" >");
    }
    
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(AddQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try {
            processRequest(request, response);
        } catch (Exception ex) {
            Logger.getLogger(AddQuestion.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
