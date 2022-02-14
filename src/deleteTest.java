import Models.Model;
import Models.Tri;
import Models.Vertex;

public class deleteTest {
    public void deleteTest(){
        /**Vertex A=new Vertex("A",true);
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
        System.out.println("-------");**/
        Model testSort=new Model();
        testSort.sortDeleteTs.tris.put("1",new Tri("ABC"));
        testSort.sortDeleteTs.tris.put("11",new Tri("bcd"));
        testSort.sortDeleteTs.tris.put("21",new Tri("cde"));
        testSort.sortDeleteTs.tris.put("2",new Tri("def"));
        testSort.sortDeleteTs.tris.put("efg",new Tri("efg"));
        testSort.sortDeleteTs.tris.put("ghi",new Tri("ghi"));
        testSort.sortDeleteTs.tris.put("22",new Tri("def"));
        testSort.sortDeleteTs.tris.put("3",new Tri("def"));
        testSort.sortDeleteTs.tris.put("4",new Tri("def"));
        testSort.sortDeleteTs.tris.put("5",new Tri("def"));
        testSort.sortDeleteTs.tris.put("6",new Tri("def"));
        testSort.sortDeleteTs.tris.put("7",new Tri("def"));
        testSort.sortDeleteTs.tris.put("8",new Tri("def"));
        testSort.sortDeleteTs.tris.put("9",new Tri("def"));
        testSort.sortDeleteTs.tris.put("10",new Tri("def"));

        //testSort.sortDeleteTS[0].tris.put("1",new Tri("qwe"));
        testSort.setSortDeleteTS();
        testSort.printDeleteTS();




    }
}
