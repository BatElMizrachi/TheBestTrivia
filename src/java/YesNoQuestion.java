
import java.util.Scanner;

public class YesNoQuestion extends QuestionBase
{
    private boolean answer;

    @Override
    public QuestionType GetQuestionType() {
        return QuestionType.Open;
    }

    public void SetAnswer(boolean answer)
    {
        this.answer = answer;
    }
    
    public void SetAnswer(String answer)
    {
        this.answer = answer.equals("Yes");
    }
    
    public boolean GetAnswer()
    {
        return this.answer;
    }
    
}
