import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage
        List<String> commands = builtins();
        String[] pathArray = System.getenv("PATH").split(":");
        while(true){
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            String[] string = input.split(" ");
            String command = string[0];
            String parameter = "";

            if(string.length > 2){
                for(int i = 1; i < string.length; i++){
                    if(i < string.length - 1){
                        parameter += string[i] +  " ";
                    }else{
                        parameter += string[i];
                    }
                }
            }else if(string.length > 1){
                parameter = string[1];
            }
            System.out.println(System.getenv("PATH"));
            if(Arrays.asList(pathArray).contains(command)){
                ProcessBuilder processBuilder = new ProcessBuilder(string);
                Process process = processBuilder.start();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while((line = bufferedReader.readLine())!= null){
                    System.out.println(line);
                }
            }else{
                switch(command){
                    case("exit"):
                        if(parameter.equals("0")){
                            break;
                        }
                    case("type"):
                        if (input.length() <= 5) {
                            System.out.println("type: missing argument");
                            continue;
                        }
                        if(commands.contains(parameter)){
                            System.out.print(parameter + " is a shell builtin\n");
                        }else {
                            String path = getPath(parameter);
                            if (path != null) {
                                System.out.println(parameter + " is " + path);
                            } else {
                                System.out.println(parameter + ": not found");
                            }
                        }
                    case("echo"):
                        System.out.println(parameter);


                    default:
                        System.out.println(input + ": command not found");

                }
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

    private static List<String> builtins() {
        List<String> builtins = new ArrayList<>();
        builtins.add("exit");
        builtins.add("echo");
        builtins.add("type");
        return builtins;
    }
}
