package IoC;

import Commands.Command;

public class SetCurrentScopeCommand implements Command {
    Object scope;

    public SetCurrentScopeCommand(Object scope)
    {
        this.scope = scope;
    }

    public void execute()
    {
        InitCommand.currentScopes.set(scope);
    }
}
