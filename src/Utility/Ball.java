package Utility;

import Models.Model;
import Models.Vertex;

public class Ball extends Model {
    double Side_Length=1.0f;
    double Height=Math.sqrt(8)*Side_Length;
    public Ball(int layer){
        if(layer%2==0){
            Vertex v0=new Vertex(0.0f,0.0f,Height*layer/2);
            Vertex v1=new Vertex(0.0f,Side_Length*Math.sqrt(3)/3,Height*layer/2-Height);
            Vertex v2=new Vertex(Side_Length/2,Math.sqrt(3)/6,Height*layer/2-Height);
            Vertex v3=new Vertex(-Side_Length/2,Math.sqrt(3)/6,Height*layer/2-Height);
            this.CreateTri(v0,v3,v2);
            this.CreateTri(v0,v2,v2);
            this.CreateTri(v0,v1,v3);
        }
    }
}
