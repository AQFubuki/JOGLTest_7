package Models;

public class Edge {
    protected Vertex sv = new Vertex();
    protected Vertex ev = new Vertex();
    protected Tri Tri =null;
    protected Tri adjTri =null;
    protected String tag;
    //protected Vertexs nearVs = new Vertexs();
    protected Edges nearEs = new Edges();
    //protected Tris nearTs = new Tris();

    public Edge() {
    }

    public Edge(Vertex sv, Vertex ev) {
        this.sv = sv;
        this.ev = ev;
        this.tag = sv.getT() + ev.getT();
    }
    public Edge(String tag){
        this.setTag(tag);
    }

   /** public void addnearVs(Vertex v) {
        this.nearVs.vertexs.put(v.getT(), v);
    }**/

    public void addnearEs(Edge e) {
        this.nearEs.edges.put(e.getTag(), e);
    }

   /** public void addnearTs(Tri t) {
        this.nearTs.tris.put(t.getTag(), t);
    }
    **/

    /**public void shownearVs(){
        for(Vertex v:nearVs.vertexs.values()){
            System.out.println(v.getT());
        }
    }**/

    public void shownearEs(){
        for(Edge e:nearEs.edges.values()){
            System.out.println(e.getTag());
        }
    }

    public void shownearTs(){
        System.out.println(getTri().getTag());
        System.out.println(getAdjTri().getTag());
    }

    public Vertex getSv() {
        return sv;
    }

    public void setSv(Vertex sv) {
        this.sv = sv;
    }

    public Vertex getEv() {
        return ev;
    }

    public void setEv(Vertex ev) {
        this.ev = ev;
    }

    public Tri getTri() {
        return Tri;
    }

    public void setTri(Tri tri) {
        Tri = tri;
    }

    public Tri getAdjTri() {
        return adjTri;
    }

    public void setAdjTri(Tri adjTri) {
        this.adjTri = adjTri;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public boolean isHole(){
        return (this.Tri==null || this.adjTri==null)?true:false;
    }

    public void printTag(){
        System.out.println(this.getTag());
    }

    public void print(){
        System.out.println("EdgeTag:");
        this.printTag();
        System.out.println("sv:"+sv.getT());
        System.out.println("ev:"+ev.getT());
        if(Tri!=null){
            System.out.println("Tri:"+Tri.getTag());
        }else{System.out.println("Tri:null");}

        if(adjTri!=null){
            System.out.println("adjTri:"+adjTri.getTag());
        }else{System.out.println("adjTri:null");}
        System.out.println("nearEdges:");
        nearEs.printTag();

    }
    public boolean isEquals(String t){
        return this.getTag().equals(t);
    }
    public boolean isEquals(Edge e){
        return this.isEquals(e.getTag());
    }
}
