package net.jackwhite20.slug.interpreter;

import net.jackwhite20.slug.exception.SlugRuntimeException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class InternalFunctionRegistry {

    private static Map<String, Function<Object, Object>> functions = new HashMap<>();

    static {
        functions.put("WriteLine", new Function<Object, Object>() {

            @Override
            public Object apply(Object o) {
                System.out.println(o);

                return null;
            }
        });
    }

    public static Object execute(String functionName, Object object) {
        Function<Object, Object> function = functions.get(functionName);
        if (function != null) {
            return function.apply(object);
        }

        throw new SlugRuntimeException("function " + functionName + " not found");
    }

    public static boolean isInternal(String name) {
        return functions.containsKey(name);
    }
}
