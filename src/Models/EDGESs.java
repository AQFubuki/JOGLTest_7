package Models;

import java.util.HashMap;

public class EDGESs {
    public HashMap<String, Edges> EDGESs = new HashMap<String, Edges>();
    int num = 0;

    public void setEDGESs(Edges Es) {
        this.EDGESs.clear();
        this.num = 0;
        Edges tempEs = new Edges();
        tempEs.edges.putAll(Es.edges);
        while (!tempEs.edges.isEmpty()) {
            Edges newEs = tempEs.divide();
            this.EDGESs.put(String.valueOf(num++), newEs);
        }
    }
}
