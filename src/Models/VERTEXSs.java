package Models;

import Utility.MyUtil;

import java.util.HashMap;

public class VERTEXSs {
    public HashMap<String,Vertexs>VERTEXSs=new HashMap<String,Vertexs>();
    int num=0;
    public void setVERTEXSs(EDGESs ESs){
        this.VERTEXSs.clear();
        this.num=0;
        for(String EsNum:ESs.EDGESs.keySet()){
            Vertexs Vs=new Vertexs();
            Edges es=ESs.EDGESs.get(EsNum);
            for(String EdgeNum:es.sortEdges.keySet()){
                Edge e=es.sortEdges.get(EdgeNum);
                Vs.vertexs.put(e.getSv().getTag(),e.getSv());
                Vs.vertexs.put(e.getEv().getTag(),e.getEv());
            }
            this.VERTEXSs.put(EsNum,Vs);
            this.setPlaneFittng();
        }
    }

    public void setPlaneFittng(){
        for(Vertexs Vs:this.VERTEXSs.values()){
            double a[]= MyUtil.planeFitting(Vs);
            for(Vertex v:Vs.vertexs.values()){
                v.setPLANE_FITTING_Z(MyUtil.setPlaneFittingPoint(a[0],a[1],a[2],v));
            }
        }
    }
}
