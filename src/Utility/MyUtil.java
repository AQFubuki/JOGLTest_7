package Utility;

import Models.Vertex;
import Models.Vertexs;
import glm.vec._3.Vec3;

import static java.lang.Math.abs;

public class MyUtil {
    public static glm.vec._3.Vec3 normalize(glm.vec._3.Vec3 v) {
        glm.vec._3.Vec3 result = new Vec3(1.0f);
        float module = (float) (1.0D / Math.sqrt((double) (v.x * v.x + v.y * v.y + v.z * v.z)));
        float x = v.x * module;
        float y = v.y * module;
        float z = v.z * module;
        result.set(x, y, z);
        return result;
    }

    public static double[] planeFitting(Vertexs Vs) {
        double x = 0.0f, xx = 0.0f, xy = 0.0f,
                y = 0.0f, yy=0.0f,yz = 0.0f,
                z = 0.0f,zx=0.0f;
        double[] a = new double[3];
        int num = Vs.vertexs.size();
        for (Vertex v : Vs.vertexs.values()) {
            x += v.getX();
            xx += v.getX() * v.getX();
            xy += v.getX() * v.getY();

            y += v.getY();
            yz += v.getY() * v.getZ();
            yy+=v.getY()*v.getY();

            z += v.getZ();
            zx+=v.getZ()*v.getX();
        }
        double denominator = getDeter3(
                xx, xy, x,
                xy, yy, y,
                x, y, num);
        if (abs(denominator) < 0.000000001) {
            a[0] = 0.0f;
            a[1] = 0.0f;
            a[2] = 0.0f;
        } else {
            a[0] = getDeter3(
                    zx, xy, x,
                    yz, yy, y,
                    z, y, num) / denominator;
            a[1] = getDeter3(
                    xx, zx, x,
                    xy, yz, y,
                    x, z, num) / denominator;
            a[2] = getDeter3(
                    xx, xy, zx,
                    xy, yy, yz,
                    x, y, z) / denominator;
        }
        return a;
    }

    public static Vertex getPlaneFittingPoint(double a0, double a1, double a2, Vertex temp) {
        double Z = temp.getX() * a0 + temp.getY() * a1 + a2;
        return new Vertex(temp.getX(), temp.getY(), Z);
    }
    public static double setPlaneFittingPoint(double a0,double a1,double a2,Vertex temp){
        double Z = temp.getX() * a0 + temp.getY() * a1 + a2;
        return Z;
    }
    public static double setPlaneFittingPoint(double a0,double a1,double a2,double X,double Y){
        double Z = X * a0 + Y * a1 + a2;
        return Z;
    }

    public static double getDeter3(double a11, double a12, double a13,
                                   double a21, double a22, double a23,
                                   double a31, double a32, double a33) { //计算三阶行列式
        return a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32
                - a13 * a22 * a31 - a12 * a21 * a33 - a11 * a23 * a32;
    }
}
