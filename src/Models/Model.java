package Models;

import java.io.*;

public class Model {
    public Vertexs Vs = new Vertexs();
    protected Edges Es = new Edges();
    public Tris Ts = new Tris();

    public Tris deleteTs=new Tris();

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
            Edge e0 = new Edge(), e1 = new Edge(), e2 = new Edge();
            Tri tri;
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
                            tri = new Tri(v0, v1, v2);
                            //将三个点的面集加入到tri的邻近面集中
                            tri.addnearTs(v0.getnearTs());
                            tri.addnearTs(v1.getnearTs());
                            tri.addnearTs(v2.getnearTs());

                            //将tri加入到v0 v1 v2的邻近面集中，同时更新邻近面集中面的邻近面集
                            v0.addnearTs(tri);
                            v1.addnearTs(tri);
                            v2.addnearTs(tri);

                            String triTag = v0.getT() + v1.getT() + v2.getT();
                            Ts.tris.put(triTag, tri);
                            //创建边v0->v1 v1->v2 v2->v0

                            e0 = Es.edges.getOrDefault(v1.getT() + v0.getT(), new Edge(v0, v1));
                            tri.setE0(e0);
                            if (e0.getTag().equals(v0.getT() + v1.getT())) { //正序的设置 v0->v1
                                Es.edges.put(e0.getTag(), e0);//初次创建，加入边的集合
                                e0.setTri(tri);
                                tri.setD0(true);

                                v0.addnearVs(v1);//v0和v1互相加入对方的邻近点集中
                                v1.addnearVs(v0);

                                v0.addnearEs(e0);//将e0加入到v1和v0的邻近边集中 在add中同时更新边与边的邻近集合
                                v1.addnearEs(e0);

                            } else { //逆序的设置 v1->v0
                                e0.setAdjTri(tri);
                                tri.setD0(false);
                                //tri.addnearTs(e0.getTri());//有逆序时将e0的两个三角面互相加入对方的邻近面集中
                                e0.getTri().addnearTs(tri);
                            }

                            e1 = Es.edges.getOrDefault(v2.getT() + v1.getT(), new Edge(v1, v2));
                            tri.setE1(e1);
                            if (e1.getTag().equals(v1.getT() + v2.getT())) {
                                Es.edges.put(e1.getTag(), e1);
                                e1.setTri(tri);
                                tri.setD1(true);

                                v1.addnearVs(v2);
                                v2.addnearVs(v1);

                                v1.addnearEs(e1);
                                v2.addnearEs(e1);

                            } else {
                                e1.setAdjTri(tri);
                                tri.setD1(false);

                                //tri.addnearTs(e1.getTri());
                                e1.getTri().addnearTs(tri);
                            }

                            e2 = Es.edges.getOrDefault(v0.getT() + v2.getT(), new Edge(v2, v0));
                            tri.setE2(e2);
                            if (e2.getTag().equals(v2.getT() + v0.getT())) {
                                Es.edges.put(e2.getTag(), e2);
                                e2.setTri(tri);
                                tri.setD2(true);

                                v2.addnearVs(v0);
                                v0.addnearVs(v2);

                                v2.addnearEs(e2);
                                v0.addnearEs(e2);

                            } else {
                                e2.setAdjTri(tri);
                                tri.setD2(false);

                                //tri.addnearTs(e2.getTri());
                                e2.getTri().addnearTs(tri);
                            }
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

    public void modelDelete(){
        if(deleteTs.tris.isEmpty()) return;

        Tris newDeleteTs=new Tris();
        if(!deleteTs.tris.values().isEmpty()) {
            for (Tri tempT : deleteTs.tris.values()) {//遍历待删除的面集合，把他们相邻的面添加到新的删除面集合中
                if (!tempT.nearTs.tris.isEmpty()) {
                    for (Tri tempT1 : tempT.nearTs.tris.values()) {
                        if (!newDeleteTs.tris.containsKey(tempT1.tag) && !deleteTs.tris.containsKey(tempT1.tag)
                        && Ts.tris.containsKey(tempT1.tag)) {
                            newDeleteTs.tris.put(tempT1.tag, tempT1);
                        }
                    }
                }
            }
        }
        for(Tri tempT:deleteTs.tris.values()){
            modelDelete(tempT);
        }
        deleteTs.tris.clear();
        deleteTs.tris.putAll(newDeleteTs.tris);

        System.out.println("O:");
        printDeleteTs();
        //sortDeleteTs();
        System.out.println("sort:");
        printDeleteTs();
    }

    public void modelDelete(Tri t){
        //首先把t从t的邻近面关系中删除
        for(Tri tempNearT:t.nearTs.tris.values()){
            //tempNearT.nearTs.tris.remove(t.tag);
            tempNearT.nearTs.tris.entrySet().remove(t);
        }
        //再删除面和边的关系
        if(t.getD0()){
            t.getE0().setTri(new Tri());
        }else{
            t.getE0().setAdjTri(new Tri());
        }
        if(t.getD1()){
            t.getE1().setTri(new Tri());
        }else{
            t.getE1().setAdjTri(new Tri());
        }
        if(t.getD2()){
            t.getE2().setTri(new Tri());
        }else{
            t.getE2().setAdjTri(new Tri());
        }

        //将面从点的邻近面集中删除
        t.getV0().nearTs.tris.remove(t);
        if(t.getV0().nearTs.tris.isEmpty()){//如果该点不存在任何一个面时，将其删除
            modelDeleteVertex(t.getV0());
        }

        t.getV1().nearTs.tris.remove(t);
        if(t.getV1().nearTs.tris.isEmpty()){//如果该点不存在任何一个面时，将其删除
            modelDeleteVertex(t.getV1());
        }

        t.getV2().nearTs.tris.remove(t);
        if(t.getV2().nearTs.tris.isEmpty()){//如果该点不存在任何一个面时，将其删除
            modelDeleteVertex(t.getV2());
        }
        Ts.tris.remove(t.tag);
    }

    private void modelDeleteVertex(Vertex v) {
        //首先删除边
        for(Edge tempE:v.nearEs.edges.values()){
            modelDeleteEdge(tempE);
        }
        //删除邻近点关系
        for(Vertex tempV:v.nearVs.vertexs.values()){
            tempV.nearVs.vertexs.remove(v.tag);
        }
        //删除点
        this.Vs.vertexs.remove(v.tag);
    }

    private void modelDeleteEdge(Edge e) {
        //删除边和点的关系
        e.getEv().nearEs.edges.remove(e.tag);
        e.getSv().nearEs.edges.remove(e.tag);
        //删除邻近边关系
        for(Edge tempE:e.getEv().nearEs.edges.values()){
            tempE.nearEs.edges.remove(e);
        }
        for(Edge tempE:e.getSv().nearEs.edges.values()){
            tempE.nearEs.edges.remove(e);
        }
        this.Es.edges.remove(e.tag);//从边集中删除
    }

    private void sortDeleteTs() {
        Tri targetT=new Tri();
        Tris newDeleteTs=new Tris();
        int num=0;
        boolean isNear=false;
        targetT=deleteTs.tris.entrySet().iterator().next().getValue();
        newDeleteTs.tris.put(String.valueOf(num++),targetT);
        deleteTs.tris.remove(targetT.getTag());

        while(num>0 && num<deleteTs.tris.size()){
            for(Tri tempT:targetT.nearTs.tris.values()){
                if(deleteTs.tris.containsKey(tempT.getTag())){
                    targetT=tempT;
                    isNear=true;
                    break;
                }
            }
            if(isNear) {
                newDeleteTs.tris.put(String.valueOf(num++), targetT);
                deleteTs.tris.remove(targetT.getTag());
                isNear=false;
            }else{
                num=-1;
            }
        }
        deleteTs.tris.putAll(newDeleteTs.tris);
    }

    private  void printDeleteTs(){
        for(String tag:deleteTs.tris.keySet()){
            System.out.println(tag+" ");//+deleteTs.tris.getOrDefault(tag,new Tri("no tri")).getTag());
        }
    }
}
