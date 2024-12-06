import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage
        ArrayList<String> commands = new ArrayList<>();


        commands.add("exit");
        commands.add("echo");
        commands.add("type");
        while(true){
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            if(input.equals("exit 0")){
                    break;
            }else if(input.startsWith("type")){
                if (input.length() <= 5) {
                    System.out.println("type: missing argument");
                    continue;
                }
                String command = input.substring(5);
                if(commands.contains(command)){
                    System.out.print(command + " is a shell builtin\n");
                }else{
                    String path = getPath(command);
                    if(path != null){
                        System.out.println(command + " is " + path);
                    }else{
                        System.out.println(command + ": not found");
                    }
                }
            }else if(input.startsWith("echo")){
                System.out.println(input.substring(5));
            }else{
                System.out.println(input + ": command not found");
            }
        }

    }

    public static String getPath(String command){
            for(String path: System.getenv("PATH").split(":")) {
                Path file = Path.of(path, command);
                if(Files.isRegularFile(file)){
                    return file.toString();
                }
            }
            return null;
    }
}
