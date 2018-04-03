package net.jackwhite20.slug.interpreter;

import net.jackwhite20.slug.exception.SlugRuntimeException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;

public class InternalFunctionRegistry {

    private static Map<String, Function<List<Object>, Object>> functions = new HashMap<>();

    static {
        functions.put("WriteLine", params -> {
            if (params.size() == 1) {
                System.out.println(params.get(0));
                return null;
            }

            throw new SlugRuntimeException("internal function WriteLine needs exactly one argument");
        });
        functions.put("Random", args -> {
            int value = -1;

            if (args.size() == 1) {
                value = ThreadLocalRandom.current().nextInt((int) args.get(0));
            } else if (args.size() == 2) {
                value = ThreadLocalRandom.current().nextInt((int) args.get(0), (int) args.get(1));
            }

            return value;
        });
        functions.put("ReadLine", arg -> {
            Scanner scanner = new Scanner(System.in);

            String line = scanner.nextLine();

            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                return line;
            }
        });
    }

    static Object execute(String functionName, List<Object> params) {
        Function<List<Object>, Object> function = functions.get(functionName);
        if (function != null) {
            return function.apply(params);
        }

        throw new SlugRuntimeException("function " + functionName + " not found");
    }

    public static boolean isInternal(String name) {
        return functions.containsKey(name);
    }
}
