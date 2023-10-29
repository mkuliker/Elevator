package IoC;

import Commands.Command;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

public class InitCommand implements Command {

    static ThreadLocal<Object> currentScopes =
            new ThreadLocal<Object>();

    static Map<String, Function<Object[], Object>> rootScope =
            new ConcurrentHashMap<>();

    static boolean initialized = false;

    @Override
    public void execute() {
        if (initialized)
            return;
        rootScope.put(
                "IoC.Scope.Current.Set",
                (Object[] args) -> new SetCurrentScopeCommand(args[0])
        );
        rootScope.put(
                "IoC.Scope.Current.Clear",
                (Object[] args) -> new ClearCurrentScopeCommand()
        );

        rootScope.put(
                "IoC.Scope.Current",
                (Object[] args) -> currentScopes.get() != null ? currentScopes.get() : rootScope
        );

        rootScope.put(
                "IoC.Scope.Create.Empty",
                (Object[] args) -> new HashMap<String, Function<Object[], Object>>()
        );

        rootScope.put(
                "IoC.Scope.Create",
                (Object[] args) ->
                {
                    var creatingScope = IoC.<Map<String, Function<Object[], Object>>>resolve("IoC.Scope.Create.Empty");

                    if (args.length > 0) {
                        var parentScope = args[0];
                        creatingScope.put("IoC.Scope.Parent", (Object[] args0) -> parentScope);
                    } else {
                        var parentScope = IoC.<Map<String, Function<Object[], Object>>>resolve("IoC.Scope.Current");
                        creatingScope.put("IoC.Scope.Parent", (Object[] args0) -> parentScope);
                    }
                    return creatingScope;
                }
        );

        rootScope.put(
                "IoC.Register",
                (Object[] args) -> new RegisterIocDependencyCommand((String) args[0], (Function<Object[], Object>) args[1])
        );

        var p = IoC.<Command>resolve(
                "Update IoC Resolve Dependency Strategy",(Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>>)
                (BiFunction<String, Object[], Object> oldStrategy) ->
                        (String dependency, Object[] args) ->
                        {
                            var scope = currentScopes.get() != null ? currentScopes.get() : rootScope;
                            var dependencyResolver = new DependencyResolver(scope);
                            return dependencyResolver.resolve(dependency, args);
                        }
        );

        p.execute();

        initialized = true;

    }
}
