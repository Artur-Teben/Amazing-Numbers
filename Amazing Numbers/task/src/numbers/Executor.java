package numbers;

import java.util.Scanner;

import static numbers.Printer.provideNumbersWithProperties;
import static numbers.Validator.validate;
import static numbers.Printer.printNumberProperties;
import static numbers.Printer.providePropertiesForNumbers;

public class Executor {
    private static final String INSTRUCTIONS = "Supported requests:\n" +
            "- enter a natural number to know its properties;\n" +
            "- enter two natural numbers to obtain the properties of the list:\n" +
            "  * the first parameter represents a starting number;\n" +
            "  * the second parameter shows how many consecutive numbers are to be printed;\n" +
            "- two natural numbers and properties to search for;\n" +
            "- a property preceded by minus must not be present in numbers;\n" +
            "- separate the parameters with one space;\n" +
            "- enter 0 to exit.";

    // Supported requests:
    //- enter a natural number to know its properties;
    //- enter two natural numbers to obtain the properties of the list:
    //  * the first parameter represents a starting number;
    //  * the second parameter shows how many consecutive numbers are to be printed;
    //- two natural numbers and properties to search for;
    //- a property preceded by minus must not be present in numbers;
    //- separate the parameters with one space;
    //- enter 0 to exit.

    public static void execute() {
        System.out.println("Welcome to Amazing Numbers!\n");

        System.out.println(INSTRUCTIONS);

        while (true) {
            String[] input = scanInput();

            if (isEmpty(input)) {
                System.out.println(INSTRUCTIONS);
                continue;
            } else if (readyToExit(input)) {
                break;
            } else if (validate(input)) {
                continue;
            }
            provideProperties(input);
        }
    }

    private static String[] scanInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter a request: ");
        String[] input = scanner.nextLine().split(" ");
        System.out.println();
        return input;
    }

    private static boolean isEmpty(String[] input) {
        return input[0].trim().equals("");
    }

    private static boolean readyToExit(String[] input) {
        try {
            if (input.length == 1 && Long.parseLong(input[0]) == 0) {
                System.out.println("Goodbye!");
                return true;
            }
        } catch (NumberFormatException exception) {
            return false;
        }
        return false;
    }

    private static void provideProperties(String[] input) {
        if (input.length == 1) {
            printNumberProperties(Long.parseLong(input[0]));
        } else if (input.length == 2) {
            providePropertiesForNumbers(input);
        } else if (input.length > 2) {
            provideNumbersWithProperties(input);
        }
    }
}
