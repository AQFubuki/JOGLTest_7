import Models.Model;
import Models.Tri;
import Models.Tris;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;
import sun.reflect.generics.tree.Tree;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeSelectionModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;

public class SwingFrame {
    public Models.Model testModel = new Model(System.getProperty("user.dir") + "/src/stl/Twoman.stl");
    public void init(){
        //运行GUI代码在事件分发线程以保证线程安全
        SwingUtilities.invokeLater(() -> {
            //创建OpenGL渲染画布
            GLCanvas canvas = new GLCanvas();
            MyListener myListener = new MyListener(testModel);
            canvas.addGLEventListener(myListener);
            //canvas.setPreferredSize(new Dimension(CANVAS_WIDTH, CANVAS_HEIGHT));
            canvas.setPreferredSize(new Dimension(1200, 900));

            //创建一个动画器以一个指定的帧率驱动画布的display()方法
            final FPSAnimator animator = new FPSAnimator(canvas, 60, true);

            //创建树形展示结构

            //JPanel TriTreePanel=new JPanel();
            //Box TreeBox=Box.createVerticalBox();
            DefaultMutableTreeNode Root=new DefaultMutableTreeNode();
            DefaultTreeCellRenderer renderer=new DefaultTreeCellRenderer();
            renderer.setFont(new Font("StSong",Font.BOLD,25));
            DefaultTreeSelectionModel selectionModel=new DefaultTreeSelectionModel();
            selectionModel.setSelectionMode(DefaultTreeSelectionModel.SINGLE_TREE_SELECTION);
            //JList<JTree> TriTreeList=new JList<JTree>();
            for(Tri tri:testModel.Ts.tris.values()){
                //创建DefaultMutableTreeNode对象代表结点
                DefaultMutableTreeNode TriRoot=this.initTriTree(tri);
                Root.add(TriRoot);
            }
            //创建JTree对象
            JTree TriTree=new JTree(Root);
            TriTree.setCellRenderer(renderer);
            TriTree.setSelectionModel(selectionModel);
            TriTree.addTreeSelectionListener(new TreeSelectionListener() {
                @Override
                public void valueChanged(TreeSelectionEvent e) {
                    DefaultMutableTreeNode note=
                            (DefaultMutableTreeNode)TriTree.getLastSelectedPathComponent();
                    String name=note.toString();
                    myListener.selectChange(name);
                }
            });
            //TriTreePanel.add(TriTree);
            //TreeBox.add(new JScrollPane((TriTree)));
            //TriTreePanel.setLayout(new BoxLayout(TriTreePanel,BoxLayout.Y_AXIS));

            JButton button=new JButton("切换");
            JPanel jPanel=new JPanel();
            jPanel.add(TriTree);
            jPanel.add(button);
            JSplitPane BandTJSP=new JSplitPane(JSplitPane.VERTICAL_SPLIT,new JScrollPane(TriTree),button);
            BandTJSP.setOneTouchExpandable(true);//一触即展
            BandTJSP.setContinuousLayout(true);//连续布局
            //设置分隔条
            JSplitPane MainJSP=new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,canvas,BandTJSP);//左边为画布，右边为功能区
            MainJSP.setOneTouchExpandable(true);//一触即展
            MainJSP.setContinuousLayout(true);//连续布局
            MainJSP.setDividerLocation(1200);//设置初始位置



            //常见顶级容器，并将画布添加进去
            final JFrame frame = new JFrame();
            frame.setPreferredSize(new Dimension(1600, 900));
            frame.getContentPane().add(MainJSP);
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
            MouseAdapter adapter = new MouseAdapter() {
                float lastX, lastY;
                boolean isFirstMouse = true;
                boolean isPress = true;

                @Override
                public void mousePressed(MouseEvent e) {
                    super.mousePressed(e);
                    isPress = true;
                    System.out.println("press");
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    super.mouseReleased(e);
                    isPress = false;
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
                        float deltaX = e.getX() - lastX;
                        float deltaY = e.getY() - lastY;
                        if (deltaX >= 20 || deltaX <= -20) deltaX = 0;
                        if (deltaY >= 20 || deltaY <= -20) deltaY = 0;

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
                    myListener.repairTri(e.getKeyCode());

                }
            });
            canvas.setFocusable(true);
            frame.setTitle("MyTitle");
            frame.pack();
            frame.setVisible(true);
            animator.start();//开始动画循环
        });
    }
    public DefaultMutableTreeNode initTriTree(Tri tri_root){
        DefaultMutableTreeNode TriRoot=new DefaultMutableTreeNode(tri_root.getTag());//根节点三角面
        DefaultMutableTreeNode Vertexs=this.initVertexs(tri_root);
        DefaultMutableTreeNode Edges=this.initEdges(tri_root);
        DefaultMutableTreeNode Directions=this.initDirections(tri_root);
        DefaultMutableTreeNode Tri_hasCommonPoint=this.initTris(tri_root.hasCommonPoint,"有公共点的三角面");
        DefaultMutableTreeNode Tri_hasCommonBorder=this.initTris(tri_root.hasCommonBorder,"有公共边的三角面");
        //组装结点之间的关系
        TriRoot.add(Vertexs);
        TriRoot.add(Edges);
        TriRoot.add(Directions);
        TriRoot.add(Tri_hasCommonPoint);
        TriRoot.add(Tri_hasCommonBorder);

        return TriRoot;
    }
    public DefaultMutableTreeNode initVertexs(Tri tri){
        DefaultMutableTreeNode VerRoot=new DefaultMutableTreeNode("点");
        DefaultMutableTreeNode Ver0=new DefaultMutableTreeNode("点0:"+tri.getV0().getTag());
        DefaultMutableTreeNode Ver1=new DefaultMutableTreeNode("点1:"+tri.getV1().getTag());
        DefaultMutableTreeNode Ver2=new DefaultMutableTreeNode("点2:"+tri.getV2().getTag());
        VerRoot.add(Ver0);
        VerRoot.add(Ver1);
        VerRoot.add(Ver2);
        return VerRoot;
    }
    public DefaultMutableTreeNode initEdges(Tri tri){
        DefaultMutableTreeNode EdgeRoot=new DefaultMutableTreeNode("边");
        DefaultMutableTreeNode Edge0=new DefaultMutableTreeNode("边0:"+tri.getE0().getTag());
        DefaultMutableTreeNode Edge1=new DefaultMutableTreeNode("边1:"+tri.getE1().getTag());
        DefaultMutableTreeNode Edge2=new DefaultMutableTreeNode("边2:"+tri.getE2().getTag());
        EdgeRoot.add(Edge0);
        EdgeRoot.add(Edge1);
        EdgeRoot.add(Edge2);
        return EdgeRoot;
    }
    public DefaultMutableTreeNode initDirections(Tri tri){
        DefaultMutableTreeNode DirRoot=new DefaultMutableTreeNode("边的方向");
        DefaultMutableTreeNode Dir0=new DefaultMutableTreeNode("方向0:"+tri.getD0());
        DefaultMutableTreeNode Dir1=new DefaultMutableTreeNode("方向1:"+tri.getD1());
        DefaultMutableTreeNode Dir2=new DefaultMutableTreeNode("方向2:"+tri.getD2());
        DirRoot.add(Dir0);
        DirRoot.add(Dir1);
        DirRoot.add(Dir2);
        return DirRoot;
    }
    public DefaultMutableTreeNode initTris(Tris tris,String name){
        DefaultMutableTreeNode Root=new DefaultMutableTreeNode(name);
        for(Tri tri:tris.tris.values()) {
            DefaultMutableTreeNode TriNode = new DefaultMutableTreeNode(tri.getTag());
            Root.add(TriNode);
        }
        return Root;
    }
    public void selectChange(String name){

    }
}
