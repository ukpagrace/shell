import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage
        while(true){
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();


            if(input.contains("exit 0")){
                    break;
            }
            else if(input.contains("echo")){
                System.out.print(input.substring(5) + "\n");
            }else{
                System.out.print(input + ": command not found\n");
            }
        }

    }
}
