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
        for(String EsNum:ESs.EDGESs.keySet()){
            Tris tempTs=new Tris();
            Edges es=ESs.EDGESs.get(EsNum);
            for(String EdgeNum:es.sortEdges.keySet()){
                Edge e=es.sortEdges.get(EdgeNum);
                Tri t=e.getTri()==null?e.getAdjTri():e.getTri();
                if(Ts.tris.containsKey(t.getTag())){
                    tempTs.tris.put(t.getTag(),t);
                    tempTs.sortTris.put(EdgeNum,t);
                }else{System.out.println("ERROR:setTRISs");}
            }
            this.TRISs.put(EsNum,tempTs);
        }
    }

    public void print(){
        for(String tag:this.TRISs.keySet()){
            System.out.println("TRISsNum:** "+tag);
            this.TRISs.get(tag).print();
        }
    }
}
