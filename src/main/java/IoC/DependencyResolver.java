package IoC;

import java.util.Map;
import java.util.function.Function;

public class DependencyResolver {
    Map<String, Function<Object[], Object>> dependencies;

    public DependencyResolver(Object scope) {
        dependencies = (Map<String, Function<Object[], Object>>) scope;
    }

    public Object resolve(String dependency, Object[] args) {
        var dependencies = this.dependencies;

        while (true) {
            if (dependencies.containsKey(dependency)) {
                return dependencies.get(dependency).apply(args);
            } else if (dependencies.containsKey("IoC.Scope.Parent")) {
                dependencies = (Map<String, Function<Object[], Object>>) dependencies.get("IoC.Scope.Parent");
            } else {
                throw new IllegalArgumentException("Dependency " + dependency + " is not found.");
            }
        }
    }
}