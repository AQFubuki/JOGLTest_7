package Models;

import java.util.HashMap;

public class Edges {
    HashMap<String,Edge> edges=new HashMap<String,Edge>();

    public void print(){
        for(Edge e:this.edges.values()){
            //e.print();
        }
    }
    public void printTag(){
        for(Edge e:this.edges.values()){
            e.printTag();
        }
    }
    public void printnearEdges(){
        for(Edge e:this.edges.values()){
            e.print();
        }
    }
}
