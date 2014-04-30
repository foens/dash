package dk.au.cs.dash.instrumentation;

import com.microsoft.z3.Context;
import dk.au.cs.dash.Parameter;
import dk.au.cs.dash.PreparedProgram;
import dk.au.cs.dash.cfg.Graph;
import dk.au.cs.dash.symbolic.ExtendFrontier;
import dk.au.cs.dash.type.DashType;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Map;

import static dk.au.cs.dash.symbolic.ExecuteSymbolic.getInitialSymbolicVariableName;
import static java.util.Objects.requireNonNull;

public class TestRunner {
    private static final String CLASSPATH = "sootOutput/";
    private final Context ctx;
    private final PreparedProgram preparedProgram;
    private final ArrayList<Parameter> parameters;
    private final String className;
    private final String methodName;
    private final Graph g;

    public TestRunner(Context ctx, PreparedProgram preparedProgram, ArrayList<Parameter> parameters, String className, String methodName, Graph g) {
        this.ctx = ctx;
        this.preparedProgram = preparedProgram;
        this.parameters = parameters;
        this.className = className;
        this.methodName = methodName;
        this.g = g;
    }

    public void createAndCallTest(Variables variables, ArrayList<Boolean> nonDetChoices, int runId) {
        try {

            Variables externalVariableMap = createExternalVariableMap(variables);
            InstrumentationHelper helper = new InstrumentationHelper(preparedProgram, externalVariableMap, nonDetChoices, ctx, runId, g);
            Class<?> instance = createInstance(className, preparedProgram, helper);

            Arguments arguments = createArguments(variables);
            final Method method = instance.getDeclaredMethod(methodName, arguments.argumentTypes);
            makeAccessible(method);
            method.invoke(null, arguments.testArgs);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.getCause().printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Arguments createArguments(Variables variables) {
        Arguments arguments = new Arguments(parameters.size());

        for (int i = 0; i < parameters.size(); i++) {
            Parameter parameter = parameters.get(i);
            DashType dashType = DashType.from(parameter.type);
            arguments.argumentTypes[i] = dashType.toJavaType();

            String parameterName = getInitialSymbolicVariableName(parameter.name);
            arguments.testArgs[i] = variables.get(parameterName);
            if (dashType == DashType.bool) {
                int intValue = (int) arguments.testArgs[i];
                arguments.testArgs[i] = convertToBoolean(intValue);
            }
        }
        return arguments;
    }

    private void makeAccessible(final Method method) {
        AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                method.setAccessible(true);
                return null;
            }
        });
    }

    private Boolean convertToBoolean(int intValue) {
        if(intValue != 0 && intValue != 1)
            throw new RuntimeException("bool value was not 0 or 1");
        return intValue != 0;
    }

    private Class<?> createInstance(final String className, final PreparedProgram preparedProgram, final InstrumentationHelper helper) {
        return (Class<?>) AccessController.doPrivileged(new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                try {
                    URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{new File(CLASSPATH).toURI().toURL()});

                    for (String otherClassNames : preparedProgram.getPreparedClasses()) {
                        Class<?> aClass = urlClassLoader.loadClass(InstrumentationBuilder.getInstrumentedName(otherClassNames));
                        Field helperField = aClass.getField(InstrumentationBuilder.HELPER_FIELD_NAME);
                        helperField.set(null, helper);
                    }
                    return urlClassLoader.loadClass(InstrumentationBuilder.getInstrumentedName(className));
                } catch (ClassNotFoundException | MalformedURLException | IllegalAccessException | NoSuchFieldException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }



    private Variables createExternalVariableMap(Variables variables) {
        Variables externalVariableMap = new Variables();
        for (Map.Entry<String, Integer> variable : variables.entrySet()) {
            String name = variable.getKey();
            if(name.contains(ExtendFrontier.MAGIC_PREFIX_SEPARATOR))
                externalVariableMap.put(name, variable.getValue());
        }
        return externalVariableMap;
    }

    private static class Arguments {
        public Class<?>[] argumentTypes;
        public Object[] testArgs;

        public Arguments(int size) {
            argumentTypes = new Class<?>[size];
            testArgs = new Object[size];
        }
    }
}
