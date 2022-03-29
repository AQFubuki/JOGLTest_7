package Utility;

import Models.Vertex;
import Models.Vertexs;
import glm.vec._2.Vec2;
import glm.vec._3.Vec3;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class MyUtil {
    public static glm.vec._3.Vec3 normalize(glm.vec._3.Vec3 v) {
        glm.vec._3.Vec3 result = new Vec3(1.0f);
        float module = (float) (1.0D / sqrt((double) (v.x * v.x + v.y * v.y + v.z * v.z)));
        float x = v.x * module;
        float y = v.y * module;
        float z = v.z * module;
        result.set(x, y, z);
        return result;
    }
    public static void circleFitting(Vertexs Vs) {
        double center_x = 0.0f;
        double center_y = 0.0f;
        double radius = 0.0f;
        int n = Vs.vertexs.size();
        if (n < 3) return;
        double sum_x = 0.0f, sum_y = 0.0f,
                sum_x2 = 0.0f, sum_y2 = 0.0f,
                sum_x3 = 0.0f, sum_y3 = 0.0f,
                sum_xy = 0.0f, sum_x1y2 = 0.0f, sum_x2y1 = 0.0f;
        for (Vertex v : Vs.vertexs.values()) {
            double x = v.Two_Dimension_X;
            double y = v.Two_Dimension_Y;
            double x2 = x * x;
            double y2 = y * y;
            sum_x += x;
            sum_y += y;
            sum_x2 += x2;
            sum_y2 += y2;
            sum_x3 += x2 * x;
            sum_y3 += y2 * y;
            sum_xy += x * y;
            sum_x1y2 += x * y2;
            sum_x2y1 += x2 * y;
        }
        double C, D, E, G, H, a, b, c;
        C = n * sum_x2 - sum_x * sum_x;
        D = n * sum_xy - sum_x * sum_y;
        E = n * sum_x3 + n * sum_x1y2 - (sum_x2 + sum_y2) * sum_x;
        G = n * sum_y2 - sum_y * sum_y;
        H = n * sum_x2y1 + n * sum_y3 - (sum_x2 + sum_y2) * sum_y;
        a = (H * D - E * G) / (C * G - D * D);
        b = (H * C - E * D) / (D * D - G * C);
        c = -(a * sum_x + b * sum_y + sum_x2 + sum_y2) / n;
        center_x = a / (-2);
        center_y = b / (-2);
        radius = sqrt(a * a + b * b - 4 * c) / 2;
        for (Vertex v : Vs.vertexs.values()) {
            getCircleFitting(v,center_x,center_y,radius);
        }

    }
    public static void getCircleFitting(Vertex v, double center_x, double center_y, double radius){
        System.out.println("&&&&&&&&&&&&");
        System.out.println(v.Two_Dimension_X+" "+v.Two_Dimension_Y);
        System.out.println(center_x+" "+center_y+" "+radius);
        System.out.println("&&&&&&&&&&&&");
        Vec2 center_v=new Vec2(v.Two_Dimension_X-center_x,v.Two_Dimension_Y-center_y);
        double rat=radius/center_v.length();
        v.Two_Dimension_X=rat*center_v.x+center_x;
        v.Two_Dimension_Y=rat*center_v.y+center_y;
        System.out.println("&&&&&&&&&&&&");
        System.out.println(v.Two_Dimension_X+" "+v.Two_Dimension_Y);
        System.out.println((v.Two_Dimension_X-center_x)*(v.Two_Dimension_X-center_x)
        +(v.Two_Dimension_Y-center_y)*(v.Two_Dimension_Y-center_y)+" and "+radius*radius);
        System.out.println("&&&&&&&&&&&&");
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
