
import java.util.Scanner;

public class OpenQuestion extends QuestionBase
{
    private String answer;

    @Override
    public QuestionType GetQuestionType() {
        return QuestionType.Open;
    }
    
    public void SetAnswer(String answer)
    {
        this.answer = answer;
    }
    
    public String GetAnswer()
    {
        return this.answer;
    }

    
}
