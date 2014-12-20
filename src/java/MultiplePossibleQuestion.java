
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class MultiplePossibleQuestion extends QuestionBase
{
    private int answer;
    private Map<String, String> allAnswers;

    public MultiplePossibleQuestion() {
        this.allAnswers = new HashMap<String, String>();
    }
    
    @Override
    public QuestionType GetQuestionType() {
        return QuestionType.MultiplePossible;
    }

    public void SetAnswer(int answer)
    {
        this.answer = answer;
    }
    
    public int GetAnswer()
    {
        return this.answer;
    }
    
    public void SetAllAnswer(Map<String, String> allAnswers)
    {
        this.allAnswers.putAll(allAnswers);
    }
    
    public void SetAllAnswer(HashMap<String, String> allAnswers)
    {
        this.allAnswers.putAll(allAnswers);
    }
    
    public void AddToAllAnswer(String key, String answer)
    {
        this.allAnswers.put(key, answer);
    }
    public Map<String, String> GetAllAnswer()
    {
        return this.allAnswers;
    }
    
    public void ShowAllAnswers()
    {
        for (Map.Entry<String,String> currentAnswer : this.allAnswers.entrySet()) 
        {
            System.out.println(currentAnswer.getKey() + "." + currentAnswer.getValue());
        }
    }
    
    public String GetCorrectAnswer()
    {
        return this.allAnswers.get(this.answer);
    }


}
