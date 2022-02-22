package Models;

public class Tri {
    protected Vertex[] v = new Vertex[3];
    protected Edge[] e = new Edge[3];
    protected boolean[] d = new boolean[3];
    protected String tag;
    protected Tris hasCommonPoint = new Tris();
    protected Tris hasCommonBorder=new Tris();

    public Tri() {
    }

    public Tri(Vertex v0, Vertex v1, Vertex v2) {
        setV0(v0);
        setV1(v1);
        setV2(v2);
        setTag(v0.getT() + v1.getT() + v2.getT());
    }

    public Tri(String tag) {
        setTag(tag);
    }

    public void addnearTs(Tri t) {
        this.hasCommonPoint.tris.put(t.getTag(), t);
    }

    public void addnearTs(Tris t) {
        for(Tri tempT:t.tris.values()){
            this.addnearTs(tempT);
        }
    }

    public void addhasCommonBorder(Tri tri){
        this.hasCommonBorder.tris.put(tri.getTag(),tri);
    }

    public void shownearTs(){
        for(Tri t:hasCommonPoint.tris.values()){
            System.out.println(t.getTag());
        }
    }

    public boolean isHole(){
        return (this.getE0().isHole() || this.getE1().isHole() || this.getE2().isHole())?true:false;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Vertex getV0() {
        return v[0];
    }

    public void setV0(Vertex v0) {
        this.v[0] = v0;
    }

    public Vertex getV1() {
        return v[1];
    }

    public void setV1(Vertex v1) {
        this.v[1] = v1;
    }

    public Vertex getV2() {
        return v[2];
    }

    public void setV2(Vertex v2) {
        this.v[2] = v2;
    }

    public Edge getE0() {
        return e[0];
    }

    public void setE0(Edge e0) {
        this.e[0] = e0;
    }

    public Edge getE1() {
        return e[1];
    }

    public void setE1(Edge e1) {
        this.e[1] = e1;
    }

    public Edge getE2() {
        return e[2];
    }

    public void setE2(Edge e2) {
        this.e[2] = e2;
    }

    public void setE(Edge e,int num){
        if(num==0){
            this.setE0(e);
        }else if(num==1){
            this.setE1(e);
        }else if(num==2){
            this.setE2(e);
        }
    }

    public boolean getD0() {
        return d[0];
    }

    public void setD0(boolean d0) {
        this.d[0] = d0;
    }

    public boolean getD1() {
        return d[1];
    }

    public void setD1(boolean d1) {
        this.d[1] = d1;
    }

    public boolean getD2() {
        return d[2];
    }

    public void setD2(boolean d2) {
        this.d[2] = d2;
    }

    public void setD(boolean b,int num){
        if(num==0){
            this.setD0(b);
        }else if(num==1){
            this.setD1(b);
        }else if(num==2){
            this.setD2(b);
        }
    }

    public void print(){
        System.out.println("TriTag:");
        System.out.println(this.getTag());
        System.out.println("EdgeTag:");
        System.out.println(this.getE0().getTag()+" "+this.getD0()
        +"  "+this.getE1().getTag()+" "+this.getD1()
        +"  "+this.getE2().getTag()+" "+this.getD2());
        System.out.println("VertexTag:");
        System.out.println(this.getV0().getT()+" "+
        this.getV1().getT()+" "+
        this.getV2().getT());
    }

    public void printnearTris(){
        System.out.println("TriTag:"+this.getTag());
        System.out.println("nearTris:");
        hasCommonPoint.printTag();
        System.out.println();

    }

    public boolean isEquals(Tri tempT) {
        if(this.getTag().equals(tempT.getTag())){
            return true;
        }else{return false;}
    }
}
