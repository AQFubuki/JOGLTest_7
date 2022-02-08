package Models;

import java.util.HashMap;

public class Vertexs {
    public HashMap<String,Vertex> vertexs=new HashMap<String,Vertex>();

    public void printTag(){
        for(Vertex v:this.vertexs.values()){
            v.printTag();
        }
    }

    public void print(){
        for(Vertex v:this.vertexs.values()){
            System.out.println("VertexTag:");
            v.printTag();
            System.out.println("VertexnearVertexs:");
            v.printnearVertexs();
            System.out.println("VertexEdges:");
            v.printnearEdges();
            System.out.println("VertexTris:");
            v.printnearTris();
        }
    }
}
