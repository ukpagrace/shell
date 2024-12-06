import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static ArrayList<String> commands = new ArrayList<>();
    private static String command;
    private static Boolean[] found = {false};

    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage

        commands.add("exit");
        commands.add("echo");
        commands.add("type");
        while(true){
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            String path = System.getenv("PATH");
            String[] pathArray = path.split(":");

            if(input.equals("exit 0")){
                    break;
            }else if(input.startsWith("type")){
                if (input.length() <= 5) {
                    System.out.println("type: missing argument");
                    continue;
                }
                command = input.substring(5);
                found[0] =  false;
                Arrays.asList(pathArray).forEach(element -> {
                    File directory = new File(element);
                    recursiveAccessFile(directory);
                });
                if(!found[0]){
                    if(commands.contains(command)){
                        System.out.print(command + " is a shell builtin\n");
                    }else{
                        System.out.print(command + ": not found\n");
                    }
                }


            }else if(input.startsWith("echo")){
                System.out.println(input.substring(5));
            }else{
                System.out.println(input + ": command not found");
            }
        }

    }

    public static void recursiveAccessFile(File directory){

        File[] files = directory.listFiles();

        if(files != null){
            for(File file: files){
                    if(file.isFile() && file.getName().equals(command)){
                        System.out.println(command + " is " + file.getAbsolutePath());
                        found[0] = true;
                        return;
                    }
                    if(file.isDirectory()){
                        recursiveAccessFile(file);
                    }

            }

        }
    }
}
