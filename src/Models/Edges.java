package Models;

import java.util.HashMap;

public class Edges {
    public HashMap<String,Edge> edges=new HashMap<String,Edge>();
    HashMap<String,Edge> sortEdges=new HashMap<String,Edge>();

    public void print(){
        for(Edge e:this.edges.values()){
            //e.print();
        }
    }
    public void printTag(){
        for(String tag:this.sortEdges.keySet()){
            System.out.println("EdgesNum: "+tag);
            this.sortEdges.get(tag).printTag();
        }
    }
    public void printnearEdges(){
        for(Edge e:this.edges.values()){
            e.print();
        }
    }

    public Edge hasVertex(Vertex v){
        Edge tempEdge=new Edge("no hasVertex");
       /** for(Edge edge:this.edges.values()){
            if(edge.getEv().isEquals(v) || edge.getSv().isEquals(v)){
                tempEdge=edge;
                break;
            }
        }**/
        for(Edge edge:v.nearEs.edges.values()){
            if(this.edges.containsKey(edge.getTag())){
                tempEdge=edge;
                break;
            }
        }
        return tempEdge;
    }

    public Edges divide() {
        if(this.edges.isEmpty()) return new Edges();
        Edges tempEs=new Edges();
        Edge targetE = new Edge();
        int size=this.edges.size();
        int num=0;

        targetE = this.edges.entrySet().iterator().next().getValue();
        tempEs.edges.put(targetE.getTag(), targetE);
        tempEs.sortEdges.put(String.valueOf(num++),targetE);
        this.edges.remove(targetE.getTag());
        Vertex targetV=targetE.getEv();

        while (num > 0 && num < size) {
            Edge tempEdge=this.hasVertex(targetV);
            if(!tempEdge.getTag().equals("no hasVertex")){
                tempEs.edges.put(tempEdge.getTag(), tempEdge);
                tempEs.sortEdges.put(String.valueOf(num++),tempEdge);
                this.edges.remove(tempEdge.getTag());
                if(tempEdge.getSv().isEquals(targetV)){
                    targetV=tempEdge.getEv();
                }else if(tempEdge.getEv().isEquals(targetV)){
                    targetV=tempEdge.getSv();
                }
            }else{
                break;
            }
        }
        return tempEs;
    }

    public Vertex getCenterOfCircle() {
        double X = 0;
        double Y=0;
        double Z=0;

        for (Edge e:this.edges.values()){
            X+=e.getSv().getX()+e.getEv().getX();
            Y+=e.getSv().getY()+e.getEv().getY();
            Z+=e.getSv().getZ()+e.getEv().getZ();
        }
        X/=this.edges.size()*2;
        Y/=this.edges.size()*2;
        Z/=this.edges.size()*2;
        Vertex centerV=new Vertex(X,Y,Z);
        return centerV;
    }
}
