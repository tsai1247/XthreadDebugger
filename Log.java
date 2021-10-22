
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;

public class Log {
    public static void LogAppend(String message)
    {
        FileWriter writer;
        try {
            writer = new FileWriter(new File("./output.log"), true);
            
            Timestamp timestamp = new Timestamp(System.currentTimeMillis());
            writer.append(String.format("[%s] %s\n", timestamp,  message));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
