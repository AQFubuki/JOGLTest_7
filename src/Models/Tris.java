package Models;

import java.util.HashMap;

public class Tris {
    public HashMap<String ,Tri>tris=new HashMap<String,Tri>();

    public void print(){
        for(Tri t:this.tris.values()){
            t.print();
        }
    }

    public void printNearTris(){
        for(Tri t:this.tris.values()){
            t.printnearTris();
        }
    }

    public void printTag(){
        for(Tri t:this.tris.values()){
            System.out.println(t.getTag());
        }
    }
}
