import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.lang.System.exit;

public class Main {
    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage
        List<String> commands = builtins();
        while(true){
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            int i = 0;
            ArrayList<String> parameters = new ArrayList<>();

            StringBuilder sb;

              while(i < input.length()){
                if(input.charAt(i) == '\''){
                    i++;
                    sb = new StringBuilder();
                    while(i < input.length() && input.charAt(i) != '\''){
                            sb.append(input.charAt(i++));
                    }
                    parameters.add(sb.toString());
                }else if( input.charAt(i) == '"'){
                    i++;
                    sb = new StringBuilder();
                    while(i < input.length() && input.charAt(i) != '"'){
                        if(input.charAt(i) == '\\' && (input.charAt(i+1) == '\\' ||
                            input.charAt(i+1) == '$' ||
                            input.charAt(i+1) == '"')
                        ){
                            i++;
                            sb.append(input.charAt(i));
                        }else{
                            sb.append(input.charAt(i));
                        }
                        i++;
                    }
                    parameters.add(sb.toString());
                }else if(!Character.isWhitespace(input.charAt(i)) && input.charAt(i) != '\'' && input.charAt(i) != '"'  ){
                        sb = new StringBuilder();
                      while(i < input.length() && !Character.isWhitespace(input.charAt(i))){
                          if(input.charAt(i) == '\\'){
                              i++;
                              sb.append(input.charAt(i));
                          }else{
                              sb.append(input.charAt(i));
                          }
                          i++;
                      }
                      parameters.add(sb.toString());
                  }
                  i++;
              }
              String command = parameters.getFirst();


            if(!commands.contains(command) && getPath(command) != null ){
                Path workingDirectory = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
//                var commandArguments = Stream.concat(
//                        Stream.of(getPath(command)),
//                        Arrays.stream(stringArray)
//                ).toList();
                Process process = new ProcessBuilder(parameters).inheritIO().directory(workingDirectory.toFile()).start();
                process.waitFor();

            }else{
                parameters.removeFirst();
                String parameter = String.join(" ", parameters);
                switch(command){
                    case("exit"):
                        if(parameter.equals("0")){
                            exit(0);
                        }
                        break;
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
                        break;
                    case("echo"):
                        System.out.println(parameter);
                        break;
                    case("pwd"):
                        System.out.println(System.getProperty("user.dir"));
                        break;
                    case("cd"):

                        Path currentWorkingDir = Paths.get(System.getProperty("user.dir"));

                        Path path = currentWorkingDir.resolve(parameter).toAbsolutePath().normalize();
                        if(parameter.equals("~")){
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
