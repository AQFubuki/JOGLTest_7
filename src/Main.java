import com.jogamp.opengl.*;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import glm.mat._4.Mat4;
import sun.security.pkcs11.wrapper.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static sun.security.pkcs11.wrapper.Constants.*;

public class Main {
    public static void main(String[] args) {

        /**String nums="0";
        int numi=0;
        System.out.println(nums);
        numi++;
        nums=String.valueOf(numi);
        System.out.println(nums);**/
        /**GLCanvas canvas=new GLCanvas();
         JFrame frame=new JFrame();
         frame.getContentPane().add(canvas);
         canvas.addGLEventListener(new MyListener());
         FPSAnimator animator=new FPSAnimator(canvas,60,true);
         **/
        //运行GUI代码在事件分发线程以保证线程安全
        SwingUtilities.invokeLater(() -> {
            //创建OpenGL渲染画布
            GLCanvas canvas = new GLCanvas();
            MyListener myListener=new MyListener();
            canvas.addGLEventListener(myListener);
            //canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
            canvas.setPreferredSize(new Dimension(1200, 900));

            //创建一个动画器以一个指定的帧率驱动画布的display()方法
            //final  FPSAnimator animator=new FPSAnimator(canvas,Constants.FPS,true);
            final FPSAnimator animator = new FPSAnimator(canvas, 60, true);

            //常见顶级容器，并将画布添加进去
            final JFrame frame = new JFrame();
            frame.getContentPane().add(canvas);
            frame.addWindowListener(new WindowAdapter() {
                //监听窗口关闭事件，当窗口关闭时执行
                @Override
                public void windowClosing(WindowEvent e) {
                    //用一个专用线程运行stop()方法，确保动画在程序退出前停止
                    new Thread(() -> {
                        if (animator.isStarted()) animator.stop();
                        System.exit(0);
                    }).start();
                }
            });
            MouseAdapter adapter=new MouseAdapter() {
                float lastX,lastY;
                boolean isFirstMouse=true;
                boolean isPress=true;
                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    isPress=true;
                    System.out.println("press");
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    isPress=false;
                    System.out.println("release");
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    super.mouseMoved(e);
                    if (isPress) {
                        if (isFirstMouse) {
                            lastX = e.getX();
                            lastY = e.getY();
                            isFirstMouse = false;
                        }
                        float deltaX = 0;
                        deltaX = e.getX() - lastX;
                        float deltaY = 0;
                        deltaY = e.getY() - lastY;
                        if(deltaX>=20 || deltaX<=-20)deltaX=0;
                        if(deltaY>=20 || deltaY<=-20)deltaY=0;

                        myListener.getCamera().moveRadians(deltaX, deltaY);

                        lastX = e.getX();
                        lastY = e.getY();

                    }
                }
            };
            canvas.addMouseMotionListener(adapter);
            canvas.addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    super.keyPressed(e);
                    myListener.getCamera().moveCamera(e.getKeyCode());
                    myListener.deleteTri(e.getKeyCode());

                }
            });

            canvas.setFocusable(true);
            frame.setTitle("MyTitle");
            frame.pack();
            frame.setVisible(true);
            animator.start();//开始动画循环
        });
    }
}
