import java.io.Serializable;
import java.util.Scanner;

public abstract class QuestionBase implements IQuestionBase, Serializable 
{
    protected static Scanner reader = new Scanner(System.in);
    private String question;
    private String category;
    private String level;
    
    public abstract void AskQuestion();
    public abstract QuestionType GetQuestionType();
    
    @Override
    public String GetCategory() {
        return this.category;
    }

    @Override
    public String GetLevel() {
        return this.level;
    }

    @Override
    public void SetCategory(String category) {
        this.category = category;
    }

    @Override
    public void SetLevel(String level) {
        this.level = level;
    }
    
    public void SetQuestion(String question)
    {
        this.question = question;
    }
    public String GetQuestion()
    {
        return this.question;
    }
}
