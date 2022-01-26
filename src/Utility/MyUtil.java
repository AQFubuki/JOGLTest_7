package Utility;

import glm.vec._3.Vec3;

public class MyUtil {
    public static glm.vec._3.Vec3 normalize(glm.vec._3.Vec3 v){
        glm.vec._3.Vec3 result=new Vec3(1.0f);
        float module=(float)(1.0D / Math.sqrt((double)(v.x * v.x + v.y * v.y + v.z * v.z)));
        float x=v.x*module;
        float y=v.y*module;
        float z=v.z*module;
        result.set(x,y,z);
        return result;
    }
}
