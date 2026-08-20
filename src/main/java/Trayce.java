import java.util.Scanner;

public class Trayce {
    public static void main(String[] args) {
        String banner = " _____\n"
                + "|_   _| __ __ _ _   _  ___ ___\n"
                + "  | |  '__/ _` | | | |/ __/ _ \\\n"
                + "  | | | | | (_| | |_| | (_|  __/\n"
                + "  |_| |_|  \\__,_|\\__, |\\___\\___|\n"
                + "                  __/ |\n"
                + "                 |___/\n";
        System.out.println(banner);
        System.out.println("Hello! I'm Trayce. What can I do for you?");

        String separator = "_".repeat(80);
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println(separator);
                String command = scanner.nextLine();
                System.out.println(separator);

                if (command.equalsIgnoreCase("bye")) {
                    System.out.println("Bye. Hope to see you again soon!");
                    break;
                }

                System.out.println(command);
                System.out.println(separator);
            }
        }
    }
}
