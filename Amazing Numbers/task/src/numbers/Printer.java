package numbers;

import static numbers.Property.getProperties;
import static numbers.Property.hasProperties;
import static numbers.Property.isBuzz;
import static numbers.Property.isDuck;
import static numbers.Property.isEven;
import static numbers.Property.isGapful;
import static numbers.Property.isHappy;
import static numbers.Property.isJumping;
import static numbers.Property.isOdd;
import static numbers.Property.isPalindromic;
import static numbers.Property.isSad;
import static numbers.Property.isSpy;
import static numbers.Property.isSquare;
import static numbers.Property.isSunny;
import static numbers.Property.parseExcludedProperties;
import static numbers.Property.parseIncludedProperties;
import static numbers.Property.trim;

public class Printer {

    public static void printNumberProperties(long number) {
        System.out.printf("Properties of %s\n", appendQuotes(number));
        System.out.printf("\t\t   happy: %b\n", isHappy(number));
        System.out.printf("\t\t\t sad: %b\n", isSad(number));
        System.out.printf("\t\t jumping: %b\n", isJumping(number));
        System.out.printf("\t\t   sunny: %b\n", isSunny(number));
        System.out.printf("\t\t  square: %b\n", isSquare(number));
        System.out.printf("\t\t\tbuzz: %b\n", isBuzz(number));
        System.out.printf("\t\t\tduck: %b\n", isDuck(number));
        System.out.printf("\t palindromic: %b\n", isPalindromic(number));
        System.out.printf("\t\t  gapful: %b\n", isGapful(number));
        System.out.printf("\t\t\t spy: %b\n", isSpy(number));
        System.out.printf("\t\t\teven: %b\n", isEven(number));
        System.out.printf("\t\t\t odd: %b\n", isOdd(number));
    }

    public static void providePropertiesForNumbers(String[] input) {
        long firstNumber = Long.parseLong(input[0]);
        int numbersCount = Integer.parseInt(input[1]);

        long[] numbers = new long[numbersCount];

        long j = firstNumber;
        for (int i = 0; i < numbersCount; i++, j++) {
            numbers[i] = j;
        }

        printNumbersProperties(numbers);
    }

    public static void provideNumbersWithProperties(String[] input) {
        long firstNumber = Long.parseLong(input[0]);
        int numbersCount = Integer.parseInt(input[1]);
        Property[] includedProperties = parseIncludedProperties(input);
        Property[] excludedProperties = parseExcludedProperties(input);

        findNumbersWithProperties(firstNumber, numbersCount, trim(includedProperties), trim(excludedProperties));
    }

    private static void findNumbersWithProperties(long firstNumber,
                                                  int numbersCount,
                                                  Property[] includedProperties,
                                                  Property[] excludedProperties) {
        long[] numbersWithProperties = new long[numbersCount];
        int numberIndex = 0;

        for (long number = firstNumber; numberIndex < numbersCount; number++) {

            if (includedProperties.length != 0 && excludedProperties.length == 0) {
                if (hasProperties(number, includedProperties)) {
                    numbersWithProperties[numberIndex] = number;
                    numberIndex++;
                }
            } else if (includedProperties.length == 0 && excludedProperties.length != 0) {
                if (!hasProperties(number, excludedProperties)) {
                    numbersWithProperties[numberIndex] = number;
                    numberIndex++;
                }
            } else {
                if (hasProperties(number, includedProperties) && !hasProperties(number, excludedProperties)) {
                    numbersWithProperties[numberIndex] = number;
                    numberIndex++;
                }
            }
        }

        printNumbersProperties(numbersWithProperties);
    }

    private static void printNumbersProperties(long[] numbers) {
        for (long number : numbers) {
            StringBuilder result = new StringBuilder(String.format("\t\t\t%s is ", appendQuotes(number)));

            Property[] properties = getProperties(number);

            for (Property property : properties) {
                appendProperty(result, property.name().toLowerCase());
            }

            System.out.println(result);
        }
    }

    private static String appendQuotes(long number) {
        String stringNumber = String.valueOf(number);
        StringBuilder result = new StringBuilder();

        for (int j = 1, i = stringNumber.length() - 1; i >= 0; i--, j++) {
            if (j % 3 == 0) {
                result.append(stringNumber.charAt(i)).append(",");
            } else {
                result.append(stringNumber.charAt(i));
            }
        }
        result.reverse();

        return result.toString().startsWith(",") ? result.substring(1) : result.toString();
    }

    private static void appendProperty(StringBuilder result, String property) {
        if (result.toString().endsWith("is ")) {
            result.append(property);
        } else {
            result.append(String.format(", %s", property));
        }
    }
}
