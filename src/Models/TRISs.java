package Models;

import java.util.HashMap;

public class TRISs {
    public HashMap<String,Tris>TRISs=new HashMap<String, Tris>();
    int num=0;
    public void setTRISs(Tris Ts){
        this.TRISs.clear();
        Tris tempTs=new Tris();
        tempTs.tris.putAll(Ts.tris);
        while(!tempTs.tris.isEmpty()){
            Tris newTs=tempTs.divide();
            this.TRISs.put(String.valueOf(num++),newTs);
        }
    }
}
