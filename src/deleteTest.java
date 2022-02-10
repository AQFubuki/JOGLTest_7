import Models.Model;
import Models.Vertex;

public class deleteTest {
    public void deleteTest(){
        Vertex A=new Vertex("A",true);
        Vertex B=new Vertex("B",true);
        Vertex C=new Vertex("C",true);
        Vertex D=new Vertex("D",true);
        Vertex E=new Vertex("E",true);
        Vertex F=new Vertex("F",true);
        Model testM=new Model();
        testM.CreateTri(A,B,C);
        testM.CreateTri(B,A,E);
        testM.CreateTri(A,F,E);
        testM.CreateTri(A,C,F);
        testM.CreateTri(C,B,D);

        testM.modelDelete("ABC");
        //testM.printVsnear();
        System.out.println("----------");
        testM.modelDelete("ACF");
        System.out.println("----------");
        testM.modelDelete("CBD");
        testM.printEdgesnearEdges();
    }
}
