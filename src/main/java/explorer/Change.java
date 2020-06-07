package explorer;

public class Change {
    private final ChangesType changeType;
    private final String new_name;
    private final String new_path;
    private final String old_name;
    private final String old_path;


    public Change(ChangesType changeType, String new_name, String new_path, String old_name, String old_path) {
        this.changeType = changeType;
        this.new_name = new_name;
        this.new_path = new_path;
        this.old_name = old_name;
        this.old_path = old_path;
    }

    public ChangesType getChangeType() {
        return changeType;
    }

    public String getNew_name() {
        return new_name;
    }

    public String getNew_path() {
        return new_path;
    }

    public String getOld_name() {
        return old_name;
    }

    public String getOld_path() {
        return old_path;
    }

    @Override
    public String toString() {
        return "Тип: " + changeType + " Имя фаайла: " + old_name + " Путь файла старый: " + old_path;
    }
}
