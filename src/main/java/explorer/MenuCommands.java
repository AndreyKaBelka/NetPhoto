package explorer;

public class MenuCommands {
    private String commandName;

    public MenuCommands(){};

    public MenuCommands(String commandName){
        this.commandName = commandName;
    }

    public String getCommandName() {
        return commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public String toString() {
        return commandName;
    }
}
