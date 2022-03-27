import Models.Model;
import Models.Tri;
import Models.Vertex;
import Utility.MyUtil;

public class deleteTest {
    public void deleteTest(){
        Model testModel=new Model();
        Vertex v0=new Vertex(0.0f,0.0f,0.0f);
        Vertex v1=new Vertex(1.0f,-6.0f,-31.0f);
        Vertex v2=new Vertex(4.0f,5.0f,21.0f);
        Vertex v3=new Vertex(2.0f,7.0f,33.0f);
        //z=-x+5y+0
        testModel.Hole_Vers.vertexs.put(v0.getTag(),v0);
        testModel.Hole_Vers.vertexs.put(v1.getTag(),v1);
        testModel.Hole_Vers.vertexs.put(v2.getTag(),v2);
        testModel.Hole_Vers.vertexs.put(v2.getTag(),v3);
        Vertex temp= MyUtil.getPlaneFittingPoint(MyUtil.planeFitting(testModel.Hole_Vers)[0],
        MyUtil.planeFitting(testModel.Hole_Vers)[1],MyUtil.planeFitting(testModel.Hole_Vers)[2],v3);
        System.out.println(temp.getX()+" "+ temp.getY()+" "+temp.getZ());
        System.out.println(MyUtil.planeFitting(testModel.Hole_Vers)[0]+" "
                + MyUtil.planeFitting(testModel.Hole_Vers)[1]+" "
                +MyUtil.planeFitting(testModel.Hole_Vers)[2]);

        //0.0 0.0 -3.269230769230769
        //1.0 1.0 -1.3076923076923075
        //4.0 4.0 4.5769230769230775
        //0.5448717948717948 1.4166666666666667 -3.269230769230769
    }
}
