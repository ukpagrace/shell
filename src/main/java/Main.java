import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {
        // Uncomment this block to pass the first stage
        while(true){
            System.out.print("$ ");

            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();


            if(input.contains("exit")){
                String[] myArray = input.split(" ");
                if(myArray.length == 2){
                    System.exit(Integer.parseInt(myArray[1]));
                }
            }else{
                System.out.print(input + ": command not found\n");
            }
        }

    }
}
