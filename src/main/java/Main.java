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

            List<String> matchList = new ArrayList<String>();
            Pattern regex = Pattern.compile("[^\\s\"']+|\"[^\"]*\"|'[^']*'");
            Matcher regexMatcher = regex.matcher(input);
            while (regexMatcher.find()) {
                matchList.add(regexMatcher.group());
            }

            String command = matchList.getFirst();
            String parameter = "";
            if(matchList.size() > 2){
                for(int i = 1; i < matchList.size(); i++){
                    if(i < matchList.size() - 1){
                        parameter += matchList.get(i).replace("\"", " ").trim() + (" ");
                    }else{
                        parameter += matchList.get(i).replace("\"", " ").trim();
                    }
                }
            }else if(matchList.size() > 1){
                parameter = matchList.get(1).replace("\"", " ").trim();
            }

//            System.out.println("parameter " + parameter);
//            String[] stringArray = new String[matchList.size()];
//            stringArray = matchList.toArray(stringArray);
//            for(int i = 0; i < stringArray.length; i++){
//                    stringArray[i] = stringArray[i].replace("'", "").trim();
//            }
//            System.out.println("parameter " + parameter);
//            matchList.removeFirst();
//            String[] stringArray = new String[matchList.size()];
//            stringArray = matchList.toArray(stringArray);
////                System.out.println(Arrays.toString(stringArray));
//            for(int i = 0; i < stringArray.length; i++){
//                if(stringArray[i].startsWith("'")){
//                    stringArray[i] = stringArray[i].replace("'", "").trim();
//                }else{
//                    stringArray[i] = stringArray[i].replace("\"", "").trim();
//                }
//
//            }
////            '/tmp/foo/f   56' '/tmp/foo/f   52' '/tmp/foo/f   21'
//            System.out.println("string array" + Arrays.toString(stringArray));
//
////            .replace("\"", "")
//            Path workingDirectory = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
//            var commandArguments = Stream.concat(
//                    Stream.of(getPath(command)),
//                    Arrays.stream(stringArray)
//            ).toList();
//            System.out.println("command string" + Arrays.toString(commandArguments.toArray()));

            if(!commands.contains(command) && getPath(command) != null ){
//                matchList.addFirst("-c");
//                matchList.addFirst("sh");


                matchList.removeFirst();
                String[] stringArray = new String[matchList.size()];
                stringArray = matchList.toArray(stringArray);
//                System.out.println(Arrays.toString(stringArray));
                for(int i = 0; i < stringArray.length; i++){
                    if(stringArray[i].startsWith("'")){
                        stringArray[i] = stringArray[i].replace("'", "").trim();
                    }else{
                        stringArray[i] = stringArray[i].replace("\"", "").trim();
                    }

                }
                Path workingDirectory = Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();
                var commandArguments = Stream.concat(
                        Stream.of(getPath(command)),
                        Arrays.stream(stringArray)
                ).toList();
                Process process = new ProcessBuilder(commandArguments).inheritIO().directory(workingDirectory.toFile()).start();
                process.waitFor();
//                Process process = Runtime.getRuntime().exec(String.join(" ", stringArray));
//                process.getInputStream().transferTo(System.out);


//                System.out.println("matchlist to array" + Arrays.toString(matchList.toArray(new String[0])));
//                ProcessBuilder processBuilder = new ProcessBuilder(matchList.toArray(new String[0]));
//                Process process = processBuilder.start();
//                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
//
//                String line;
//                System.out.println("line" + bufferedReader.readLine());
//                while((line = bufferedReader.readLine())!= null){
//                    System.out.println(line);
//                }
            }else{
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

//                        String[] arguments = parameter.split(" ");
//                        for(int i = 0; i < arguments.length; i++){
//                            arguments[i] = arguments[i].replace("\"", "");
//                        }
//                        System.out.println(Arrays.toString(arguments));
                        if(parameter.startsWith("'") && parameter.endsWith("'")){
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
