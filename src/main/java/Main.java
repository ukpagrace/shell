import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage
        List<String> commands = builtins();
        while(true){
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();

            String[] string = input.split(" ");
            String command = string[0];
            StringBuilder parameter = new StringBuilder();
            if(string.length > 2){
                for(int i = 1; i < string.length; i++){
                    if(!string[i].isEmpty()){
                        if(i < string.length - 1){
                            parameter.append(string[i]).append(" ");
                        }else{
                            parameter.append(string[i]);
                        }
                    }

                }
            }else if(string.length > 1){
                parameter = new StringBuilder(string[1]);
            }
//            System.out.println("command" + getPath(command));
            if(!commands.contains(command) && getPath(command) != null){
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
                        if(parameter.toString().equals("0")){
                            exit(0);
                        }
                        break;
                    case("type"):
                        if (input.length() <= 5) {
                            System.out.println("type: missing argument");
                            continue;
                        }
                        if(commands.contains(parameter.toString())){
                            System.out.print(parameter + " is a shell builtin\n");
                        }else {
                            String path = getPath(parameter.toString());
                            if (path != null) {
                                System.out.println(parameter + " is " + path);
                            } else {
                                System.out.println(parameter + ": not found");
                            }
                        }
                        break;
                    case("echo"):
                        if(parameter.toString().startsWith("'") && parameter.toString().endsWith("'")){
                            System.out.println(parameter.substring(1, parameter.length()-1));
                        }else{
                            System.out.println(parameter);
                        }

                        break;
                    case("pwd"):
                        System.out.println(System.getProperty("user.dir"));
                        break;
                    case("cd"):

                        Path currentWorkingDir = Paths.get(System.getProperty("user.dir"));

                        Path path = currentWorkingDir.resolve(parameter.toString()).toAbsolutePath().normalize();
                        if(parameter.toString().equals("~")){
                            System.setProperty("user.dir", System.getenv("HOME"));
                        }
                        else if (Files.isDirectory(Path.of(path.toString()))) {
                            System.setProperty("user.dir", path.toString());
                        } else {
                            System.out.println(command + ": " + parameter + ": No such file or directory");
                        }
                        break;
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
        builtins.add("pwd");
        return builtins;
    }
}
