package IoC;
import java.util.function.BiFunction;
import java.util.function.Function;

public class IoC {
     static BiFunction<String, Object[], Object> strategy =
            (String dependency, Object[] args) ->
    {
        if ("Update IoC Resolve Dependency Strategy".equals(dependency))
        {
            return new UpdateIocResolveDependencyStrategyCommand(
                    (Function<BiFunction<String, Object[], Object>, BiFunction<String, Object[], Object>>)args[0]
            );
        }
        else
        {
            throw new IllegalArgumentException("Dependency " +dependency +" is not found.");
        }

    };

    public static <T> T resolve(String dependency,  Object... args)
    {
        return (T) strategy.apply(dependency, args);
    }

}
