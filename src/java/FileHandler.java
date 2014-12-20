
import java.util.ArrayList;
import java.util.List;
import java.io.*;

public class FileHandler 
{
    public static ArrayList<QuestionBase> ReadQuestions() throws FileNotFoundException, IOException, ClassNotFoundException
    {
        try
        {
            FileInputStream File = new FileInputStream("data.dat");
            ObjectInputStream ois = new ObjectInputStream(File);
            ArrayList<QuestionBase> allQuestions = (ArrayList<QuestionBase>)ois.readObject();
            ois.close();

            return allQuestions;
        }
        catch(Exception e)
        {
            return new ArrayList<QuestionBase>();
        }
    }

    public static void WriteQuestions(ArrayList<QuestionBase> allQuestions) throws FileNotFoundException, IOException
    {
        File FileQuestions = new File("data.dat");
        FileQuestions.delete();
        
        FileOutputStream File = new FileOutputStream("data.dat");
        ObjectOutputStream oos = new ObjectOutputStream(File);
        oos.writeObject(allQuestions);
        oos.close();
    }
}
