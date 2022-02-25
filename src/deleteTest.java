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
        Vertex A1=new Vertex("A1",true);
        Vertex B1=new Vertex("B1",true);
        Vertex C1=new Vertex("C1",true);
        Vertex D1=new Vertex("D1",true);
        Vertex E1=new Vertex("E1",true);
        Vertex F1=new Vertex("F1",true);
        Vertex G1=new Vertex("G1",true);
        Vertex H1=new Vertex("H1",true);
        testModel.CreateTri(A,B,C);
        testModel.CreateTri(B,A,D);
        testModel.CreateTri(D,A,E);
        testModel.CreateTri(A,C,E);
        testModel.CreateTri(E,C,H);
        testModel.CreateTri(H,C,G);
        testModel.CreateTri(G,C,B);
        testModel.CreateTri(G,B,F);
        testModel.CreateTri(F,B,D);

        testModel.CreateTri(A1,B1,C1);
        testModel.CreateTri(B1,A1,D1);
        testModel.CreateTri(D1,A1,E1);
        testModel.CreateTri(A1,C1,E1);
        testModel.CreateTri(E1,C1,H1);
        testModel.CreateTri(H1,C1,G1);
        testModel.CreateTri(G1,C1,B1);
        testModel.CreateTri(G1,B1,F1);
        testModel.CreateTri(F1,B1,D1);

        testModel.setHole();
        testModel.printHole();
    }
}
