import Utility.MyUtil;
import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

import java.awt.event.KeyEvent;

public class Camera {

    public glm.vec._3.Vec3 Position = new Vec3(1.0f);
    public glm.vec._3.Vec3 Forward = new Vec3(1.0f);
    public glm.vec._3.Vec3 Right = new Vec3(1.0f);
    public glm.vec._3.Vec3 Up = new Vec3(1.0f);
    public glm.vec._3.Vec3 Worldup = new Vec3(1.0f);
    public float Pitch, Yaw;
    float speed = 0.3f;

    public Camera() {
    }

    public Camera(glm.vec._3.Vec3 position, float pitch, float yaw, glm.vec._3.Vec3 worldup) {
        Position = position;
        Worldup = worldup;
        Pitch = pitch;
        Yaw = yaw;
        changeViewMatrix();
    }

    public Mat4 getViewMatrix() {
        Mat4 result = new Mat4();
        Vec3 center = new Vec3();
        //glm.Glm.add(Position,Forward.x,Forward.y,Forward.z,center);
        glm.vec._3.Vec3.add(Position, Forward.x, Forward.y, Forward.z, center);
        glm.glm.lookAt(Position, center, Worldup, result);

        return result;
    }

    private void changeViewMatrix() {
        Forward.x = (float) (Math.cos(Pitch) * Math.sin(Yaw));
        Forward.y = (float) Math.sin(Pitch);
        Forward.z = (float) (Math.cos(Pitch) * Math.cos(Yaw));
        //Right = glm.vec._3.Vec3.cross(Forward, Worldup, Right);
        //Right = MyUtil.normalize(Right);//单位化
        Right = MyUtil.normalize(glm.vec._3.Vec3.cross(Forward, Worldup, Right));
        Up = MyUtil.normalize(glm.vec._3.Vec3.cross(Forward, Right, Up));

    }

    public void moveRadians(float deltaX, float deltaY) {
        Pitch -= deltaY * 0.005f;
        Yaw -= deltaX * 0.005f;
        changeViewMatrix();
    }

    public void moveCamera(int command) {
        Vec3 change = new Vec3(1.0f);
        switch (command) {
            case KeyEvent.VK_W://前进
                change.set(Forward.x * speed, Forward.y * speed, Forward.z * speed);
                Vec3.add(Position, change.x, change.y, change.z, Position);
                break;
            case KeyEvent.VK_S://后退
                change.set(Forward.x * speed, Forward.y * speed, Forward.z * speed);
                Vec3.add(Position, -change.x, -change.y, -change.z, Position);
                break;
            case KeyEvent.VK_A://左
                change.set(Right.x * speed, Right.y * speed, Right.z * speed);
                Vec3.add(Position, -change.x, -change.y, -change.z, Position);

                break;
            case KeyEvent.VK_D://右
                change.set(Right.x * speed, Right.y * speed, Right.z * speed);
                Vec3.add(Position, change.x, change.y, change.z, Position);
                break;
            case KeyEvent.VK_SPACE://上
                change.set(Up.x * speed, Up.y * speed, Up.z * speed);
                Vec3.add(Position, -change.x, -change.y, -change.z, Position);
                break;
            case KeyEvent.VK_C://下
                change.set(Up.x * speed, Up.y * speed, Up.z * speed);
                Vec3.add(Position, change.x, change.y, change.z, Position);
                break;
        }


    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }
}
