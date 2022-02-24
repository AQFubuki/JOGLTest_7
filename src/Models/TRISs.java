package Models;

import java.util.HashMap;

public class TRISs {
    public HashMap<String, Tris> TRISs = new HashMap<String, Tris>();
    int num = 0;

    public void setTRISs(Tris Ts) {
        this.TRISs.clear();
        this.num = 0;
        Tris tempTs = new Tris();
        tempTs.tris.putAll(Ts.tris);
        while (!tempTs.tris.isEmpty()) {
            Tris newTs = tempTs.divide();
            this.TRISs.put(String.valueOf(num++), newTs);
        }
    }
    public void setTRISs(EDGESs ESs,Tris Ts){
        this.TRISs.clear();
        this.num = 0;

        for(Edges es:ESs.EDGESs.values()){
            Tris tempTs=new Tris();
            for(Edge e:es.edges.values()){
                /**for(Tri t:Ts.tris.values()){
                    if(t.hasEdge(e)){
                        tempTs.tris.put(t.getTag(),t);
                    }
                }**/
                Tri t=e.getTri()==null?e.getAdjTri():e.getTri();
                if(Ts.tris.containsKey(t.getTag())){
                tempTs.tris.put(t.getTag(),t);
                }else{System.out.println("ERROR:setTRISs");}
            }
            this.TRISs.put(String.valueOf(num++),tempTs);
        }
    }
}
