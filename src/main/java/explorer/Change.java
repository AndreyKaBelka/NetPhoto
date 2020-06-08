package explorer;

import java.io.Serializable;

public class Change implements Serializable {
    private final ChangesType changeType;
    private final String new_name;
    private final String new_path;
    private final String old_name;
    private final String old_path;
    private int id;

    public Change(ChangesType changeType, String new_name, String new_path, String old_name, String old_path) {
        this.changeType = changeType;
        this.new_name = new_name;
        this.new_path = (new_path != null) ? new_path.substring(0, new_path.length() - 1) : null;
        this.old_name = old_name;
        this.old_path = (old_path != null) ? old_path.substring(0, old_path.length() - 1) : null;
        this.id = hashCode();
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
        StringBuilder string = new StringBuilder("");

        switch (changeType) {
            case RENAME_FOLDER: {
                string.append("< Переименование папки :");
                break;
            }
            case DELETE_FOLDER: {
                string.append("Удаление папки :");
                break;
            }
            case RENAME_PHOTO: {
                string.append("Переименование фото :");
                break;
            }
            case MOVE_PHOTO: {
                string.append("Перемещение фото :");
                break;
            }
            case COPY_PHOTO: {
                string.append("Копирование фото :");
                break;
            }
            case ADD_FOLDER: {
                string.append("Добавление папки :");
                break;
            }
        }

        if (old_name != null) {
            string.append("\n").append("Имя: ").append(old_name);
        }

        if (old_path != null) {
            string.append("\n").append("Путь: ").append(old_path);
        }

        if (new_name != null) {
            string.append("\n").append("Новое имя: ").append(new_name);
        }

        if (new_path != null) {
            string.append("\n").append("Новый путь: ").append(new_path);
        }

        string.append(" >");

        return string.toString();
    }
}
