import Models.Model;
import Models.Tri;
import Models.Vertex;

public class deleteTest {
    public void deleteTest(){
        Vertex A=new Vertex("A",true);
        Vertex B=new Vertex("B",true);
        Vertex C=new Vertex("C",true);
        Vertex D=new Vertex("D",true);
        Vertex E=new Vertex("E",true);
        Vertex F=new Vertex("F",true);
        Vertex G=new Vertex("G",true);
        Vertex H=new Vertex("H",true);
        Vertex I=new Vertex("I",true);
        Vertex J=new Vertex("J",true);
        Vertex K=new Vertex("K",true);
        Vertex L=new Vertex("L",true);
        Vertex M=new Vertex("M",true);
        Vertex N=new Vertex("N",true);

        Model testM=new Model();
        testM.CreateTri(A,B,C);
        testM.CreateTri(A,C,D);
        testM.CreateTri(C,B,E);
        testM.CreateTri(G,B,F);
        testM.CreateTri(G,E,B);
        testM.CreateTri(E,G,H);
        testM.CreateTri(H,C,E);
        testM.CreateTri(K,C,H);
        testM.CreateTri(D,C,K);
        testM.CreateTri(G,F,I);
        testM.CreateTri(G,I,J);
        testM.CreateTri(G,J,H);
        testM.CreateTri(H,J,K);
        testM.CreateTri(I,F,L);
        testM.CreateTri(I,L,J);
        testM.CreateTri(M,J,L);
        testM.CreateTri(K,J,M);
        testM.CreateTri(M,L,N);

        testM.addDeleteTs(testM.Ts.tris.getOrDefault("ABC",new Tri("no add delete tri")));

        testM.printDeleteTs();
        System.out.println("-------");
        testM.modelDelete();
        testM.printDeleteTs();
        System.out.println("-------");
        testM.modelDelete();
        testM.printDeleteTs();
        System.out.println("-------");
        testM.modelDelete();
        testM.printDeleteTs();
        System.out.println("-------");




    }
}
