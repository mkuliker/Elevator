package IoC;

import Commands.Command;
import java.util.Map;
import java.util.function.Function;

public class RegisterIocDependencyCommand implements Command {
    String dependency;
    Function<Object[],Object> strategy;

    public RegisterIocDependencyCommand(String dependency, Function<Object[],Object> strategy) {
        this.dependency = dependency;
        this.strategy = strategy;
    }

    @Override
    public void execute() {
        var currentScope = IoC.<Map<String, Function<Object[], Object>>>resolve("IoC.Scope.Current");
        currentScope.put(dependency, strategy);
    }
}
