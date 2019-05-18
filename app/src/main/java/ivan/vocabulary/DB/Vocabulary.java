/**
 * @author: Ivan Mykolenko
 * @date: 24.04.2019
 */
package ivan.vocabulary.DB;

/**
 * Class to facilitate work with vocabulary records retrieved from the database.
 */
public class Vocabulary {
    private int id;
    private String name, table;
    private boolean undoDelition = false;


    public Vocabulary(){}
    public Vocabulary(String name, String table){
        this.name = name;
        this.table = table;
    }
    public Vocabulary(int id, String name, String table){
        this.id = id;
        this.name = name;
        this.table = table;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean getUndo (){
        return undoDelition;
    }
    public void undo (){
        undoDelition = true;
    }
}
