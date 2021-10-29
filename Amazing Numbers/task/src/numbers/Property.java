package numbers;

public enum Property {

    EVEN, ODD, BUZZ, DUCK, PALINDROMIC, GAPFUL, SPY, SQUARE, SUNNY, JUMPING, SAD, HAPPY;

    public static Property[] getProperties(long number) {
        Property[] properties = new Property[Property.values().length];
        int i = 0;

        if (isEven(number))         properties[i++] = EVEN;
        if (isOdd(number))          properties[i++] = ODD;
        if (isBuzz(number))         properties[i++] = BUZZ;
        if (isDuck(number))         properties[i++] = DUCK;
        if (isPalindromic(number))  properties[i++] = PALINDROMIC;
        if (isGapful(number))       properties[i++] = GAPFUL;
        if (isSpy(number))          properties[i++] = SPY;
        if (isSquare(number))       properties[i++] = SQUARE;
        if (isSunny(number))        properties[i++] = SUNNY;
        if (isJumping(number))      properties[i++] = JUMPING;
        if (isSad(number))          properties[i++] = SAD;
        if (isHappy(number))        properties[i]   = HAPPY;

        return trim(properties);
    }

    public static boolean hasProperties(long number, Property[] expectedProperties) {
        for (Property property : expectedProperties) {
            switch (property) {
                case EVEN:
                    if (!isEven(number)) return false;
                    break;
                case ODD:
                    if (!isOdd(number)) return false;
                    break;
                case BUZZ:
                    if (!isBuzz(number)) return false;
                    break;
                case DUCK:
                    if (!isDuck(number)) return false;
                    break;
                case PALINDROMIC:
                    if (!isPalindromic(number)) return false;
                    break;
                case GAPFUL:
                    if (!isGapful(number)) return false;
                    break;
                case SPY:
                    if (!isSpy(number)) return false;
                    break;
                case SQUARE:
                    if (!isSquare(number)) return false;
                    break;
                case SUNNY:
                    if (!isSunny(number)) return false;
                    break;
                case JUMPING:
                    if (!isJumping(number)) return false;
                    break;
                case SAD:
                    if (!isSad(number)) return false;
                    break;
                case HAPPY:
                    if (!isHappy(number)) return false;
                    break;
                default:
                    break;
            }
        }
        return true;
    }

    public static Property[] parseIncludedProperties(String[] input) {
        Property[] properties = new Property[input.length - 2];

        for (int i = 0, j = 2; i < properties.length; i++, j++) {
            if (!input[j].startsWith("-")) {
                properties[i] = Property.valueOf(input[j].toUpperCase());
            }
        }
        return properties;
    }

    public static Property[] parseExcludedProperties(String[] input) {
        Property[] properties = new Property[input.length - 2];

        for (int i = 0, j = 2; i < properties.length; i++, j++) {
            if (input[j].startsWith("-")) {
                properties[i] = Property.valueOf(input[j].substring(1).toUpperCase());
            }
        }
        return properties;
    }

    public static Property[] trim(Property[] inputProperties) {
        int size = 0;

        for (Property value : inputProperties) {
            if (value != null) {
                size++;
            }
        }
        Property[] properties = new Property[size];

        for (int i = 0, j = 0; i < inputProperties.length && j < properties.length; i++) {
            if (inputProperties[i] != null) {
                properties[j] = inputProperties[i];
                j++;
            }
        }
        return properties;
    }

    public static boolean isHappy(long number) {
        int result = getSumOfDigitsSquares(number);
        int count = 0;

        while (result != 1 && result != number && count != 300) {
            result = getSumOfDigitsSquares(result);
            count++;
        }

        return result == 1;
    }

    private static int getSumOfDigitsSquares(long number) {
        String[] digits = String.valueOf(number).split("");
        int sequenceNumber = 0;

        for (String digit : digits) {
            sequenceNumber += Math.pow(Integer.parseInt(digit), 2);
        }

        return sequenceNumber;
    }

    public static boolean isSad(long number) {
        return !isHappy(number);
    }

    public static boolean isJumping(long number) {
        String[] stringDigits = String.valueOf(number).split("");
        if (stringDigits.length != 1) {
            int[] digits = new int[stringDigits.length];

            for (int i = 0; i < stringDigits.length; i++) {
                digits[i] = Integer.parseInt(stringDigits[i]);
            }

            if (digits.length == 2) {
                return differenceByOne(digits[0], digits[1]);
            }

            for (int i = 1; i < digits.length - 1; i++) {
                if (!differenceByOne(digits[i], digits[i - 1]) || !differenceByOne(digits[i], digits[i + 1])) {
                    return false;
                }
            }
        }
        return true;
    }

    private static boolean differenceByOne(int firstDigit, int secondDigit) {
        return Math.abs(firstDigit - secondDigit) == 1;
    }

    public static boolean isSunny(long number) {
        return isSquare(number + 1);
    }

    public static boolean isSquare(long number) {
        return (long) Math.sqrt(number) * (long) Math.sqrt(number) == number;
    }

    public static boolean isBuzz(long number) {
        return number % 7 == 0 || String.valueOf(number).endsWith("7");
    }

    public static boolean isDuck(long number) {
        return String.valueOf(number).substring(1).contains("0");
    }

    public static boolean isPalindromic(long number) {
        String[] digits = String.valueOf(number).split("");
        int middle = digits.length % 2 == 1 ? digits.length / 2 : digits.length / 2 + 1;

        for (int i = 0, j = digits.length - 1; i < middle; i++, j--) {
            if (!digits[i].equals(digits[j])) {
                return false;
            }
        }
        return true;
    }

    public static boolean isGapful(long number) {
        String stringNumber = String.valueOf(number);
        return stringNumber.length() >= 3 && number % constructNumberFromFirstAndLastDigits(stringNumber) == 0;
    }

    public static boolean isSpy(long number) {
        String[] stringDigits = String.valueOf(number).split("");
        int[] digits = new int[stringDigits.length];

        for (int i = 0; i < stringDigits.length; i++) {
            digits[i] = Integer.parseInt(stringDigits[i]);
        }

        return sumDigits(digits) == multiplyDigits(digits);
    }

    private static int sumDigits(int[] digits) {
        int sum = 0;

        for (int digit : digits) {
            sum += digit;
        }

        return sum;
    }

    private static int multiplyDigits(int[] digits) {
        int product = 1;

        for (int digit : digits) {
            product *= digit;
        }

        return product;
    }

    private static int constructNumberFromFirstAndLastDigits(String stringNumber) {
        return Integer.parseInt(
                String.format("%c%c", stringNumber.charAt(0), stringNumber.charAt(stringNumber.length() - 1)));
    }

    public static boolean isEven(long number) {
        return number % 2 == 0;
    }

    public static boolean isOdd(long number) {
        return number % 2 == 1;
    }
}
