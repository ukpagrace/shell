import java.io.File;
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

            String path = System.getenv("PATH").substring(5);
            String[] pathArray = path.split(":");

            if(input.equals("exit 0")){
                    break;
            }else if(input.startsWith("type")){
                String command = input.substring(5);
                Boolean[] found = {false};
                Arrays.asList(pathArray).forEach(element -> {
                    File directory = new File(element);
                    File[] files = directory.listFiles();

                    if(files != null){
                        for(File file: files){
                            System.out.println(file.getName());
                            if(file.getName().equals(command)){
                                System.out.println(command + " is " + file + "\n");
                                found[0] = true;
                            }
                        }
                    }
                });
                if(!found[0]){
                    if(commands.contains(command)){
                        System.out.print(input.substring(5) + " is a shell builtin\n");
                    }else{
                        System.out.print(input.substring(5) + ": not found\n");
                    }
                }


            }else if(input.startsWith("echo")){
                System.out.print(input.substring(5) + "\n");
            }else{
                System.out.print(input + ": command not found\n");
            }
        }

    }
}
