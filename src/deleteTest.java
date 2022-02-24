import Models.Model;
import Models.Tri;
import Models.Vertex;

public class deleteTest {
    public void deleteTest(){
        Model testModel=new Model();
        Vertex A=new Vertex("A",true);
        Vertex B=new Vertex("B",true);
        Vertex C=new Vertex("C",true);
        Vertex D=new Vertex("D",true);
        Vertex E=new Vertex("E",true);
        Vertex F=new Vertex("F",true);
        Vertex G=new Vertex("G",true);
        Vertex H=new Vertex("H",true);
        testModel.CreateTri(A,B,C);
        testModel.CreateTri(B,A,D);
        testModel.CreateTri(D,A,E);
        testModel.CreateTri(A,C,E);
        testModel.CreateTri(E,C,H);
        testModel.CreateTri(H,C,G);
        testModel.CreateTri(G,C,B);
        testModel.CreateTri(G,B,F);
        testModel.CreateTri(F,B,D);

        testModel.setHole();
        testModel.printHole();
    }
}
