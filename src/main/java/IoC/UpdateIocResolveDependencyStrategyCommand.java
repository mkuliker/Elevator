package IoC;

import Commands.Command;

import java.util.function.BiFunction;
import java.util.function.Function;

public class UpdateIocResolveDependencyStrategyCommand implements Command     {
    Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>> updateIoCStrategy;

    public UpdateIocResolveDependencyStrategyCommand(
            Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>> updater
    )
    {
        updateIoCStrategy = updater;
    }

    public void execute()
    {
        IoC.strategy = updateIoCStrategy.apply(IoC.strategy);
    }
}
