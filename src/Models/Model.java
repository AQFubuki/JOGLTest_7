package Models;

import java.io.*;
import java.util.Iterator;
import java.util.Map;

public class Model {
    public Vertexs Vs = new Vertexs();
    protected Edges Es = new Edges();
    public Tris Ts = new Tris();

    public Tris deleteTs = new Tris();
    public TRISs deleteTSs = new TRISs();
    public Tris[] sortDeleteTS = new Tris[]{new Tris(), new Tris(), new Tris(), new Tris(), new Tris(),
            new Tris(), new Tris(), new Tris(), new Tris(), new Tris(), new Tris()
    };

    public Tris Hole_Tri = new Tris();
    public TRISs Hole_Tris = new TRISs();
    public Tris[] sortHole_Tri = new Tris[]{
            new Tris(), new Tris(), new Tris(), new Tris(), new Tris(),
            new Tris(), new Tris(), new Tris(), new Tris(), new Tris()
    };

    public Edges Hole_Edge = new Edges();
    public EDGESs Hole_Edges = new EDGESs();

    public Model() {
    }

    public Model(String fileName) {
        File file = new File(fileName);
        Reader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(file));
            int tempchar;//记录当前字符的数值 回车\r 13 换行\n 10 空格32 每行末尾\r\n
            int vertexNum = 3;//记录当前三角面点的序号,大于2表示当前字符串不是点的坐标信息
            String temp = ""; //存放当前正在读取的字符串
            Vertex v0 = new Vertex(), v1 = new Vertex(), v2 = new Vertex();
            while ((tempchar = reader.read()) != -1) { //判断是否读到文件末尾
                if (((char) tempchar) != '\r' && (char) tempchar != '\n') {
                    temp = temp + (char) tempchar;
                    if (temp.equals("  outer loop")) {
                        vertexNum = 0;
                        temp = "";
                    }//开始创建三角面
                    if (temp.equals("    vertex  ")) {
                        temp = "";
                    }
                } else {
                    if ((char) tempchar == '\r' && vertexNum < 3 && !temp.equals("")) {
                        //大于2表示不是点坐标行，为空则是outer loop
                        Vertex tempVertex = Vs.vertexs.getOrDefault(temp, new Vertex(temp));
                        //如果存在则返回对象，如果不存在则新建对象返回
                        Vs.vertexs.put(tempVertex.tag, tempVertex);
                        //插入哈希表中，如果已存在则直接覆盖
                        if (vertexNum == 0) {
                            v0 = tempVertex;
                        } else if (vertexNum == 1) {
                            v1 = tempVertex;
                        } else if (vertexNum == 2) {
                            v2 = tempVertex;
                            //读完v2点开始创建三角面
                            CreateTri(v0, v1, v2);
                        }
                        vertexNum++;
                    }
                    temp = "";
                }
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void CreateTri(Vertex v0, Vertex v1, Vertex v2) {
        Vs.vertexs.put(v0.getT(), v0);
        Vs.vertexs.put(v1.getT(), v1);
        Vs.vertexs.put(v2.getT(), v2);
        //三个点互相加入邻近点集中 点与点
        v0.addnearVs(v1);
        v0.addnearVs(v2);
        v1.addnearVs(v0);
        v1.addnearVs(v2);
        v2.addnearVs(v0);
        v2.addnearVs(v1);

        Tri tri = new Tri(v0, v1, v2);
        //将三个点的面集加入到tri的邻近面集中 点与面
        tri.addnearTs(v0.getnearTs());
        tri.addnearTs(v1.getnearTs());
        tri.addnearTs(v2.getnearTs());

        //将tri加入到v0 v1 v2的邻近面集中，同时更新邻近面集中面的邻近面集
        v0.addnearTs(tri);
        v1.addnearTs(tri);
        v2.addnearTs(tri);

        //创建边v0->v1 v1->v2 v2->v0
        CreateEdge(v0, v1, tri, 0);
        CreateEdge(v1, v2, tri, 1);
        CreateEdge(v2, v0, tri, 2);

        //将创建好的面添加进面集合中
        String triTag = v0.getT() + v1.getT() + v2.getT();
        Ts.tris.put(triTag, tri);
    }

    private void CreateEdge(Vertex v0, Vertex v1, Tri tri, int num) {
        Edge e = new Edge();
        if(this.Es.edges.containsKey(v0.getT()+v1.getT())){
            e=Es.edges.get(v0.getT()+v1.getT());
        }else{
        e = Es.edges.getOrDefault(v1.getT() + v0.getT(), new Edge(v0, v1));}

        if (e.getTag().equals(v0.getT() + v1.getT())) { //正序的设置 v0->v1
            Es.edges.put(e.getTag(), e);//初次创建，加入边的集合
            e.setTri(tri);
            tri.setD(true, num);
            v0.addnearEs(e);//点与边 将e0加入到v1和v0的邻近边集中 在add中同时更新边与边的邻近集合
            v1.addnearEs(e);
            if(e.getAdjTri()!=null){
                tri.addhasCommonBorder(e.getAdjTri());
                e.getAdjTri().addhasCommonBorder(tri);
            }

        } else { //逆序的设置 v1->v0
            e.setAdjTri(tri);
            tri.setD(false, num);
            //设置公共边
            tri.addhasCommonBorder(e.getTri());
            e.getTri().addhasCommonBorder(tri);
            //tri.addnearTs(e.getTri());//有逆序时将e0的两个三角面互相加入对方的邻近面集中
            //e.getTri().addnearTs(tri); //面与面
        }
        tri.setE(e, num);
    }

    public void modelDelete() {
        if (deleteTs.tris.isEmpty()) return;

        Tris newDeleteTs = new Tris();
        for (Tri tempT : deleteTs.tris.values()) {//遍历待删除的面集合，把他们相邻的面添加到新的删除面集合中
            if (!tempT.hasCommonPoint.tris.isEmpty()) {
                for (Tri tempT1 : tempT.hasCommonPoint.tris.values()) {
                    if (!newDeleteTs.tris.containsKey(tempT1.getTag())
                            && !deleteTs.tris.containsKey(tempT1.getTag())
                            && this.Ts.tris.containsKey(tempT1.getTag())) {
                        newDeleteTs.tris.put(tempT1.getTag(), tempT1);
                    }
                }
            }
        }
        for (Tri tempT : deleteTs.tris.values()) {
            modelDelete(tempT);
        }
        deleteTs.tris.clear();
        deleteTs.tris.putAll(newDeleteTs.tris);
        deleteTs.sort();
        deleteTSs.setTRISs(deleteTs);
        this.setSortDeleteTS();

        //System.out.println("*******************");
        //this.printDeleteTS();
        //System.out.println("*******************");
        this.setHole();
        System.out.println("划分个数："+this.Hole_Edges.EDGESs.size());
    }

    public void addDeleteTs(Tri tri) {
        this.deleteTs.tris.put(tri.getTag(), tri);
        deleteTs.sort();
        deleteTSs.setTRISs(deleteTs);
        this.setSortDeleteTS();
        //System.out.println("*******************");
        //this.printDeleteTS();
        //System.out.println("*******************");

    }

    public void modelDelete(String tag) {
        for (Tri t : this.Ts.tris.values()) {
            if (t.getTag().equals(tag)) {
                modelDelete(t);
                break;
            }
        }

    }

    public void modelDelete(Tri t) {
        //首先把t从t的邻近面关系中删除
        for (Tri tempNearT : t.hasCommonPoint.tris.values()) {
            tempNearT.hasCommonPoint.tris.remove(t.tag);
            //tempNearT.nearTs.tris.entrySet().remove(t);
        }

        //再删除面和边的关系
        modelEdgeDeleteTri(t, 0);
        modelEdgeDeleteTri(t, 1);
        modelEdgeDeleteTri(t, 2);


        //将面从点的邻近面集中删除
        t.getV0().nearTs.tris.remove(t.tag);
        if (t.getV0().nearTs.tris.isEmpty()) {//如果该点不存在任何一个面时，将其删除
            modelDeleteVertex(t.getV0());
        }

        t.getV1().nearTs.tris.remove(t.tag);
        if (t.getV1().nearTs.tris.isEmpty()) {//如果该点不存在任何一个面时，将其删除
            modelDeleteVertex(t.getV1());
        }

        t.getV2().nearTs.tris.remove(t.tag);
        if (t.getV2().nearTs.tris.isEmpty()) {//如果该点不存在任何一个面时，将其删除
            modelDeleteVertex(t.getV2());
        }
        Ts.tris.remove(t.tag);
    }

    private void modelEdgeDeleteTri(Tri t, int i) {
        if (i == 0) {
            modelE0DeleteTri(t);
        } else if (i == 1) {
            modelE1DeleteTri(t);
        } else if (i == 2) {
            modelE2DeleteTri(t);
        }
    }

    private void modelE0DeleteTri(Tri t) {
        if (t.getD0()) {
            t.getE0().setTri(null);
        } else {
            t.getE0().setAdjTri(null);
        }
        if (t.getE0().Tri == null && t.getE0().adjTri == null) {
            modelSEDeleteEdge(t.getE0());
        }
    }

    private void modelE1DeleteTri(Tri t) {
        if (t.getD1()) {
            t.getE1().setTri(null);
        } else {
            t.getE1().setAdjTri(null);
        }
        if (t.getE1().Tri == null && t.getE1().adjTri == null) {
            modelSEDeleteEdge(t.getE1());
        }
    }

    private void modelE2DeleteTri(Tri t) {
        if (t.getD2()) {
            t.getE2().setTri(null);
        } else {
            t.getE2().setAdjTri(null);
        }
        if (t.getE2().Tri == null && t.getE2().adjTri == null) {
            modelSEDeleteEdge(t.getE2());
        }
    }

    private void modelDeleteVertex(Vertex v) {
        //首先删除边
        for (Iterator<Map.Entry<String, Edge>> it = v.nearEs.edges.entrySet().iterator();
             it.hasNext(); ) {
            Map.Entry<String, Edge> item = it.next();
            Edge tempE = item.getValue();
            if (v.isEquals(tempE.getEv())) {
                modelEvDeleteEdge(tempE);
            } else if (v.isEquals(tempE.getSv())) {
                modelSvDeleteEdge(tempE);
            } else {
                System.out.println("ERROR:modelDeleteVertex");
            }
            //modelDeleteEdge(tempE);
            it.remove();
        }
        //for(Edge tempE:v.nearEs.edges.values()){
        //    modelDeleteEdge(tempE);
        //}
        //删除邻近点关系
        for (Vertex tempV : v.nearVs.vertexs.values()) {
            tempV.nearVs.vertexs.remove(v.tag);
        }
        //删除点
        this.Vs.vertexs.remove(v.tag);
    }

    private void modelSvDeleteEdge(Edge e) {
        e.getEv().nearEs.edges.remove(e.tag);
        modelDeleteEdge(e);
    }

    private void modelEvDeleteEdge(Edge e) {
        e.getSv().nearEs.edges.remove(e.tag);
        modelDeleteEdge(e);
    }

    private void modelSEDeleteEdge(Edge e) {
        e.getSv().nearEs.edges.remove(e.tag);
        e.getEv().nearEs.edges.remove(e.tag);
        modelDeleteEdge(e);
    }

    private void modelDeleteEdge(Edge e) {
        //删除边和点的关系
        //e.getEv().nearEs.edges.remove(e.tag);
        //e.getSv().nearEs.edges.remove(e.tag);

        //解除两点之间的关系
        //System.out.println("SV:" + e.getSv().getT());
        //System.out.println("EV:" + e.getEv().getT());
        e.getEv().nearVs.vertexs.remove(e.getSv().getT());
        e.getSv().nearVs.vertexs.remove(e.getEv().getT());
        //删除邻近边关系
        for (Edge tempE : e.getEv().nearEs.edges.values()) {
            tempE.nearEs.edges.remove(e.getTag());
        }
        for (Edge tempE : e.getSv().nearEs.edges.values()) {
            tempE.nearEs.edges.remove(e.getTag());
        }
        this.Es.edges.remove(e.tag);//从边集中删除
    }

    public void setSortDeleteTS() {
        //sortDeleteTS[10].tris.putAll(sortDeleteTs.tris);
        for (int i = 0; i < 11; i++) {
            sortDeleteTS[i].tris.clear();
        }
        int num = 10;
        for (Tris Ts : this.deleteTSs.TRISs.values()) {
            for (String numTag : Ts.sortTris.keySet()) {
                //tag未更改，说明没有参与排序
                if (numTag.equals(Ts.sortTris.get(numTag).getTag())) {
                    num = 10;
                } else {
                    num = Integer.valueOf(numTag.substring(numTag.length() - 1)).intValue();
                    if (num < 0 || num > 9) {
                        System.out.println("ERROR:setSortDeleteTS");
                        return;
                    }
                }
                sortDeleteTS[num].tris.put(Ts.sortTris.get(numTag).getTag() + numTag, Ts.sortTris.get(numTag));
            }
        }
    }

    public void setHole() {
        this.Hole_Tri.tris.clear();
        this.Hole_Edge.edges.clear();
        for (Edge edge : this.Es.edges.values()) {
            if (edge.isHole()) {
                this.Hole_Edge.edges.put(edge.getTag(), edge);
                Tri tri = new Tri();
                if (edge.getTri() != null) {
                    tri = edge.getTri();
                } else if (edge.getAdjTri() != null) {
                    tri = edge.getAdjTri();
                }
                if (!this.Hole_Tri.tris.containsKey(tri.getTag()) && this.Ts.tris.containsKey(tri.getTag())) {
                    this.Hole_Tri.tris.put(tri.getTag(), tri);
                }
            }
        }
        this.Hole_Edges.setEDGESs(this.Hole_Edge);
        this.Hole_Tris.setTRISs(this.Hole_Edges, this.Hole_Tri);
        this.setSortHole_Tri();
    }

    public void setSortHole_Tri() {
        for (int i = 0; i < 10; i++) {
            sortHole_Tri[i].tris.clear();
        }
        int num = 0;
        for (Tris Ts : this.Hole_Tris.TRISs.values()) {
            for (String numTag : Ts.sortTris.keySet()) {
                //tag未更改，说明有未参与排序的三角
                if (numTag.equals(Ts.sortTris.get(numTag).getTag())) {
                    System.out.println("ERROR:setSortHle_Edge");
                    return;
                } else {
                    num = Integer.valueOf(numTag.substring(numTag.length() - 1)).intValue();
                    if (num < 0 || num > 9) {
                        System.out.println("ERROR:setSortHle_Edge");
                        return;
                    }
                }
                sortHole_Tri[num].tris.put(Ts.sortTris.get(numTag).getTag() + numTag, Ts.sortTris.get(numTag));
                sortHole_Tri[num].sortTris.put(Ts.sortTris.get(numTag).getTag() + numTag, Ts.sortTris.get(numTag));
            }
        }
    }

    public void printDeleteTs() {
        for (String tag : deleteTs.tris.keySet()) {
            System.out.println(tag + " " + deleteTs.tris.getOrDefault(tag, new Tri("no tri")).getTag());
        }
        System.out.println("//////////////");
        for (String tag : deleteTs.sortTris.keySet()) {
            System.out.println(tag + " " + deleteTs.sortTris.getOrDefault(tag, new Tri("no tri")).getTag());
        }
    }

    public void printDeleteTS() {
        for (String tag : deleteTs.tris.keySet()) {
            System.out.println(tag + " " + deleteTs.tris.getOrDefault(tag, new Tri("no tri")).getTag());
        }
        System.out.println("//////////////");
        for (int i = 0; i < 11; i++) {
            System.out.println("``````" + i + "号：``````");
            for (String tag : sortDeleteTS[i].tris.keySet()) {
                System.out.println(tag + " " + sortDeleteTS[i].tris.getOrDefault(tag, new Tri("no tri")).getTag());
            }
        }

    }

    public void printTris() {
        Ts.print();
    }

    public void printNearTris() {
        Ts.printNearTris();
    }

    public void printEdgesnearEdges() {
        Es.printnearEdges();
    }

    public void printVsnear() {
        Vs.printTag();
        Vs.print();
        System.out.println();
    }

    public void printHole(){
        //this.Hole_Edge.printTag();
        this.Hole_Edges.printTag();
        this.Hole_Tris.print();
        for(int i=0;i<10;i++){
            System.out.println("++++ "+i+" ++++");
            this.sortHole_Tri[i].print();
        }
    }

    public void repair(){
        if(this.Hole_Edge.edges.isEmpty()){
            return;
        }
        for(Edges es:this.Hole_Edges.EDGESs.values()){
            Vertex center=es.getCenterOfCircle();
            for(Edge e:es.edges.values()){
                if(e.getTri()==null){
                    this.CreateTri(e.getSv(),e.getEv(),center);
                }else if(e.getAdjTri()==null){
                    this.CreateTri(e.getEv(),e.getSv(),center);
                }
            }
        }
        this.setHole();
        this.deleteTs.tris.clear();
        //还应该分情况清除deleteTs
    }
}