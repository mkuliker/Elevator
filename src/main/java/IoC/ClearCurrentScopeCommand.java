package IoC;

import Commands.Command;

public class ClearCurrentScopeCommand implements Command {
    public void execute()
    {
        InitCommand.currentScopes.set(null);
    }
}
