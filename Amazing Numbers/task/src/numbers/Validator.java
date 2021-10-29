package numbers;

import java.util.Arrays;

import static numbers.Property.DUCK;
import static numbers.Property.EVEN;
import static numbers.Property.HAPPY;
import static numbers.Property.ODD;
import static numbers.Property.SAD;
import static numbers.Property.SPY;
import static numbers.Property.SQUARE;
import static numbers.Property.SUNNY;

public class Validator {

    private static final String WRONG_PROPERTY_MESSAGE = "The property %s is wrong.\n";
    private static final String WRONG_PROPERTIES_MESSAGE = "The properties %s are wrong.\n";
    private static final String WRONG_FIRST_NUMBER_MESSAGE = "The first parameter should be a natural number or zero.";
    private static final String WRONG_SECOND_NUMBER_MESSAGE = "The second parameter should be a natural number.";
    private static final String AVAILABLE_PROPERTIES_MESSAGE = "Available properties: %s\n";
    private static final String MUTUALLY_EXCLUSIVE_PROPERTIES_MESSAGE =
            "The request contains mutually exclusive properties: %s\nThere are no numbers with these properties.\n";
    private static final String MUTUALLY_EXCLUSIVE_OPPOSITE_PROPERTIES_MESSAGE =
            "The request contains mutually exclusive properties: [%s, -%s]\nThere are no numbers with these properties.\n";
    private static final Property[][] MUTUALLY_EXCLUSIVE_PROPERTIES = {
            {EVEN, ODD},
            {DUCK, SPY},
            {SUNNY, SQUARE},
            {SAD, HAPPY}
    };

    public static boolean validate(String[] input) {
        if (!validateNumbers(input)) {
            return true;
        }

        if (input.length > 2) {
            String[] inputProperties = new String[input.length - 2];

            for (int i = 2, j = 0; i < input.length && j < inputProperties.length; i++, j++) {
                inputProperties[j] = input[i].toUpperCase();
            }
            return validateProperties(inputProperties);
        }
        return false;
    }

    private static boolean validateNumbers(String[] input) {
        try {
            if (Long.parseLong(input[0]) < 1) {
                System.out.println(WRONG_FIRST_NUMBER_MESSAGE);
                return false;
            }
        } catch (NumberFormatException exception) {
            System.out.println(WRONG_FIRST_NUMBER_MESSAGE);
            return false;
        }
        try {
            if (input.length == 2 && Long.parseLong(input[1]) < 0) {
                System.out.println(WRONG_SECOND_NUMBER_MESSAGE);
                return false;
            }
        } catch (NumberFormatException exception) {
            System.out.println(WRONG_SECOND_NUMBER_MESSAGE);
            return false;
        }
        return true;
    }

    private static boolean validateProperties(String[] inputProperties) {
        String[] invalidProperties = getInvalidProperties(inputProperties);

        if ("".equals(invalidProperties[0])) {
            String[] mutuallyExclusiveProperties = provideMutuallyExclusiveProperties(inputProperties);

            if (mutuallyExclusiveProperties.length == 0) {
                return false;
            }

            if (mutuallyExclusiveProperties[0] != null && mutuallyExclusiveProperties[1] != null) {
                if (mutuallyExclusiveProperties[0].equals(mutuallyExclusiveProperties[1])) {
                    System.out.printf(MUTUALLY_EXCLUSIVE_OPPOSITE_PROPERTIES_MESSAGE,
                            mutuallyExclusiveProperties[0], mutuallyExclusiveProperties[1]);
                    return true;
                }
                System.out.printf(MUTUALLY_EXCLUSIVE_PROPERTIES_MESSAGE, Arrays.toString(mutuallyExclusiveProperties));
                return true;
            }
        }

        if (invalidProperties.length == 1) {
            printErrorMessages(invalidProperties, WRONG_PROPERTY_MESSAGE);
        } else {
            printErrorMessages(invalidProperties, WRONG_PROPERTIES_MESSAGE);
        }

        return true;
    }

    private static void printErrorMessages(String[] invalidProperties, String wrongPropertyMessage) {
        System.out.printf(wrongPropertyMessage, Arrays.toString(invalidProperties));
        System.out.printf(AVAILABLE_PROPERTIES_MESSAGE, Arrays.toString(Property.values()));
    }

    private static String[] getInvalidProperties(String[] properties) {
        StringBuilder invalidProperties = new StringBuilder();
        Property[] predefinedProperties = Property.values();

        for (String property : properties) {
            boolean contains = false;
            for (Property predefinedProperty : predefinedProperties) {
                if (property.startsWith("-")) {
                    property = property.substring(1);
                }

                if (predefinedProperty.name().equals(property)) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                invalidProperties.append(String.format("%s ", property));
            }
        }
        return invalidProperties.toString().split(" ");
    }

    private static String[] provideMutuallyExclusiveProperties(String[] inputProperties) {
        String[] includedProperties = new String[inputProperties.length];
        String[] excludedProperties = new String[inputProperties.length];

        for (int j = 0, i = 0, e = 0; j < inputProperties.length; j++) {
            if (inputProperties[j].startsWith("-")) {
                excludedProperties[e] = inputProperties[j];
                e++;
            } else {
                includedProperties[i] = inputProperties[j];
                i++;
            }
        }

        includedProperties = trimArray(includedProperties);
        excludedProperties = trimArray(excludedProperties);

        String[] mutuallyExclusiveIncludedProperties =
                getMutuallyExclusiveProperties(includedProperties);

        if (mutuallyExclusiveIncludedProperties.length != 0) {
            return mutuallyExclusiveIncludedProperties;
        }

        String[] mutuallyExclusiveExcludedProperties =
                getMutuallyExclusiveProperties(excludedProperties);

        if (mutuallyExclusiveExcludedProperties.length != 0) {
            return mutuallyExclusiveExcludedProperties;
        }

        String commonProperty = getCommonProperty(includedProperties, excludedProperties);

        if (commonProperty != null) {
            return new String[] {commonProperty, commonProperty};
        }

        return new String[]{};
    }

    public static String[] trimArray(String[] includedProperties) {
        int size = 0;

        for (String property : includedProperties) {
            if (property == null) break;
            size++;
        }
        String[] properties = new String[size];
        System.arraycopy(includedProperties, 0, properties, 0, properties.length);

        return properties;
    }

    private static String getCommonProperty(String[] includedProperties, String[] excludedProperties) {
        if (includedProperties.length == 0 || excludedProperties.length == 0) {
            return null;
        }
        for (String includedProperty : includedProperties) {
            for (String excludedProperty : excludedProperties) {
                if (includedProperty.equals(excludedProperty.substring(1))) {
                    return includedProperty;
                }
            }
        }
        return null;
    }

    private static String[] getMutuallyExclusiveProperties(String[] inputProperties) {
        String[] mutuallyExclusiveProperties = new String[2];

        if (inputProperties.length == 0) {
            return new String[]{};
        }

        boolean hasFirstProperty;
        for (Property[] mutuallyExclusivePropertiesPair : MUTUALLY_EXCLUSIVE_PROPERTIES) {
            hasFirstProperty = false;
            for (int j = 0; j < mutuallyExclusivePropertiesPair.length; j++) {
                for (String property : inputProperties) {
                    String tempMutuallyExclusiveProperty;

                    if (property.startsWith("-")) {
                        tempMutuallyExclusiveProperty = String.format("-%s", mutuallyExclusivePropertiesPair[j]);
                    } else {
                        tempMutuallyExclusiveProperty = mutuallyExclusivePropertiesPair[j].name();
                    }

                    if (j == 0 && tempMutuallyExclusiveProperty.equals(property)) {
                        hasFirstProperty = true;
                        mutuallyExclusiveProperties[0] = property;
                    } else if (j == 1 && hasFirstProperty && tempMutuallyExclusiveProperty.equals(property)) {
                        mutuallyExclusiveProperties[1] = property;
                        return mutuallyExclusiveProperties;
                    }
                }
            }
        }
        return new String[]{};
    }
}
