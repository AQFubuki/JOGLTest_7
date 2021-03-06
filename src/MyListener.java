import Models.*;
import Utility.MyUtil;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

import java.awt.event.KeyEvent;
import java.io.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.*;

public class MyListener implements GLEventListener {

    private final IntBuffer ArrayName = GLBuffers.newDirectIntBuffer(Data.VAONum);//VAO的buffer
    private final IntBuffer BufferName = GLBuffers.newDirectIntBuffer(Data.VBONum);//VBO的buffer
    private final IntBuffer TextureName = GLBuffers.newDirectIntBuffer(Data.TextureNum);//纹理的buffer

    private float selectVer[]=new float[1*3*5];//选中的三角
    private Tri selectTri=new Tri("no Tri");
    public Models.Model testModel=new Model();
   // public Models.Model testModel = new Model(System.getProperty("user.dir") + "/src/stl/Twoman.stl");
    private float frontVers[];
    private float backVers[];
    private float deleteVers[];
    private float deleteVersSort[][];
    private float hole_Tri[][];

    private ArrayList<Float> FrontVers=new ArrayList<Float>();//改用List动态存储，再临时改成数组
    private ArrayList<Float> BackVers=new ArrayList<Float>();
    private ArrayList<Float> edgeVers=new ArrayList<Float>();
    private ArrayList<Float> verVers=new ArrayList<Float>();

    private ArrayList<Float> Hole_Vers=new ArrayList<Float>();
    private ArrayList<Float> Hole_Plane_Vers=new ArrayList<Float>();
    private ArrayList<Float> Line=new ArrayList<Float>();
    private ArrayList<Float> PLANE=new ArrayList<Float>();
    private ArrayList<Float> CIRCLE=new ArrayList<Float>();
    private HashMap<Integer,ArrayList<Float>>HoleVers=new HashMap<Integer, ArrayList<Float>>();

    private int program;
    private GLU glu;

    private final glm.mat._4.Mat4 model = new Mat4();
    private glm.mat._4.Mat4 view = new Mat4();//观察矩阵
    private final glm.mat._4.Mat4 proj = new Mat4();//投影矩阵

    private Camera camera = new Camera();

    long lastTime = 0, currentTime;
    float deltaTime;

    boolean startDelete = false;
    boolean startRepair = false;
    boolean selectChange=false;
    boolean op=false;
    boolean M=true;
    boolean P=false;
    boolean C=false;

    int time=0;

    public MyListener(Model model){
        this.testModel=model;
        frontVers = new float[testModel.Ts.tris.size() * 3 * 5];//修补孔洞的时候，数量有可能会超出这个范围
        backVers= new float[testModel.Ts.tris.size() * 3 * 5];
         deleteVers = new float[testModel.Ts.tris.size() * 3 * 5];
        deleteVersSort = new float[11][testModel.Ts.tris.size() * 3 * 5];
        hole_Tri = new float[10][testModel.Ts.tris.size() * 3 * 5];
    }

    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();//获取OpenGL上下文
        //glv=new GLU();
        gl.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);//设置背景颜色
        gl.glClearDepth(1.0f);//设置最远的深度值
        gl.glEnable(GL_DEPTH_TEST);//开启深度测试
        gl.glDepthFunc(GL_LEQUAL);//the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);//best perspective correction
        //---------以下为自定义初始化代码-----------
        initModel(gl);

        initShader(gl);//初始化着色器
        initTexture(gl);//初始化纹理
        initMatrix(gl);
    }

    private void initModel(GL3 gl) {
        initFrontVers();
        initBackVers();
        //initDeleteVers();
        initedgeVers();
        initverVers();
        initSelect(selectTri);
        initHoleEdge();
        initHoleVers();
        initBuffer(gl);
    }

    private void initverVers() {
        this.verVers.clear();
        for(Vertex v:this.testModel.Vs.vertexs.values()){
            this.verVers.add((float)((v.getX()-70)*0.05));
            this.verVers.add((float)((v.getY()-70)*0.05));
            this.verVers.add((float)((v.getZ()-70)*0.05));
            this.verVers.add(0.0f);
            this.verVers.add(0.0f);
        }
    }

    private void initedgeVers() {
        this.edgeVers.clear();
        for(Edge e:this.testModel.Es.edges.values()){
            Vertex v1=e.getSv();
            Vertex v2=e.getEv();
            this.edgeVers.add((float)((v1.getX()-70)*0.05));
            this.edgeVers.add((float)((v1.getY()-70)*0.05));
            this.edgeVers.add((float)((v1.getZ()-70)*0.05));
            this.edgeVers.add(0.0f);
            this.edgeVers.add(0.0f);

            this.edgeVers.add((float)((v2.getX()-70)*0.05));
            this.edgeVers.add((float)((v2.getY()-70)*0.05));
            this.edgeVers.add((float)((v2.getZ()-70)*0.05));
            this.edgeVers.add(1.0f);
            this.edgeVers.add(0.0f);
        }
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();//获取上下文
        gl.glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);//清除颜色和深度缓存
        gl.glEnable(GL_CULL_FACE);//剔除背面

        gl.glEnable(GL_POINT_SIZE);
        gl.glEnable(GL_POINTS);
        gl.glEnable(GL_VERTEX_ARRAY_POINTER);
        gl.glPointSize(15.0f);//改变绘制点的大小
        gl.glLineWidth(5.f);

        currentTime = System.currentTimeMillis();
        deltaTime = currentTime - lastTime;
        camera.setSpeed(deltaTime * 0.015f);
        lastTime = currentTime;

        gl.glUseProgram(program);
        view = camera.getViewMatrix();
        //传入矩阵
        gl.glUniformMatrix4fv(gl.glGetUniformLocation(program, "model"), 1, false, model.toFa_(), 0);
        gl.glUniformMatrix4fv(gl.glGetUniformLocation(program, "view"), 1, false, view.toFa_(), 0);
        gl.glUniformMatrix4fv(gl.glGetUniformLocation(program, "proj"), 1, false, proj.toFa_(), 0);


        if (startDelete) {
            System.out.println("delete");
            if (!testModel.Ts.tris.isEmpty() && testModel.deleteTs.tris.isEmpty() && testModel.Ts.tris.containsKey(selectTri.getTag())) {
                //Tri targetT = testModel.Ts.tris.entrySet().iterator().next().getValue();
                testModel.addDeleteTs(selectTri);
            }
            testModel.modelDelete();
            initModel(gl);
            startDelete = false;
        }
        if (startRepair) {
            System.out.println("repair");
            testModel.repair();
            initModel(gl);
            startRepair = false;
        }
        if(selectChange){
            initModel(gl);
            selectChange=false;
        }
        if(op){
            this.testModel.Hole_Vertexs.setPlaneFittng();
            initModel(gl);
            op=false;
        }
        if(M){
            displayModel(gl);//绘制模型
        }
        if(P){
            displayPlaneFitting(gl);//绘制平面拟合
        }
        if(C){
            displayCircleFitting(gl);
        }

    }

    public void displayModel(GL3 gl){
        gl.glPointSize(5.0f);//改变绘制点的大小
        gl.glLineWidth(2.f);

        for (int i = 11; i < 13; i++) {
            Draw(i, testModel.Ts.tris.size(), gl);//11 12分别为绘制正面和背面
        }
        //应该考虑每次都把删除数组清空，否则范围不对就会显示错误
        //for(int i=0;i<11;i++){
        //Draw(i,testModel.sortDeleteTS[i].tris.size(),gl);
        //}
        //Draw(13,testModel.deleteTs.tris.size(),gl);
        DrawLine(31,28,testModel.Es.edges.size(),gl);
        DrawPoint(30,30,testModel.Vs.vertexs.size(),gl);
        for (int i = 0; i < 10; i++) {
            Draw(i + 14, i, testModel.sortHole_Tri[i].tris.size(), gl);
        }
        Draw(24,14,1,gl);
    }

    public void displayPlaneFitting(GL3 gl){
        Draw(28,testModel.Hole_Vertexs.VERTEXSs.size()*2,gl);
        DrawPoint(25,25,testModel.Hole_Vers.vertexs.size(),gl);
        DrawPoint(26,26,testModel.Hole_Vers.vertexs.size(),gl);
        DrawLine(27,27,testModel.Hole_Vers.vertexs.size(),gl);
        //DrawPoint(29,29,testModel.Hole_Vers.vertexs.size(),gl);
    }
    public void displayCircleFitting(GL3 gl){
        gl.glPointSize(15.0f);//改变绘制点的大小
        gl.glLineWidth(5.f);
        Draw(28,testModel.Hole_Vertexs.VERTEXSs.size()*2,gl);
        //DrawPoint(25,25,testModel.Hole_Vers.vertexs.size(),gl);
        DrawPoint(26,26,testModel.Hole_Vers.vertexs.size(),gl);
        //DrawLine(27,27,testModel.Hole_Vers.vertexs.size(),gl);
        DrawPoint(29,29,testModel.Hole_Vers.vertexs.size(),gl);
    }

    private void Draw(int i, int size, GL3 gl) {
        Draw(i, i, size, gl);
    }

    private void Draw(int VertexNum, int TextureNum, int size, GL3 gl) {
        gl.glActiveTexture(GL_TEXTURE0 + TextureNum);
        gl.glBindTexture(GL_TEXTURE_2D, TextureName.get(TextureNum));
        gl.glUniform1i(gl.glGetUniformLocation(program, "ourTexture"), TextureNum);
        gl.glBindVertexArray(ArrayName.get(VertexNum));
        gl.glDrawArrays(GL_TRIANGLES, 0, size * 3);
    }
    private void DrawPoint(int VertexNum,int TextureNum,int size,GL3 gl){
        gl.glActiveTexture(GL_TEXTURE0 + TextureNum);
        gl.glBindTexture(GL_TEXTURE_2D, TextureName.get(TextureNum));
        gl.glUniform1i(gl.glGetUniformLocation(program, "ourTexture"), TextureNum);
        gl.glBindVertexArray(ArrayName.get(VertexNum));
        gl.glDrawArrays(GL_POINTS, 0, size);
    }
    private void DrawLine(int VertexNum,int TextureNum,int size,GL3 gl){
        gl.glActiveTexture(GL_TEXTURE0 + TextureNum);
        gl.glBindTexture(GL_TEXTURE_2D, TextureName.get(TextureNum));
        gl.glUniform1i(gl.glGetUniformLocation(program, "ourTexture"), TextureNum);
        gl.glBindVertexArray(ArrayName.get(VertexNum));
        gl.glDrawArrays(GL_LINES, 0, size*2);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
    }

    private void initBuffer(GL3 gl) {
        //设置VAO和VBO
        gl.glGenVertexArrays(Data.VAONum, ArrayName);
        gl.glGenBuffers(Data.VBONum, BufferName);
        initBuffer(11, FrontVers, gl);//设置前面
        initBuffer(12, BackVers, gl);//设置背面
        initBuffer(13, deleteVers, gl);//设置删除面
        for (int i = 0; i < 11; i++) {
            initBuffer(i, deleteVersSort[i], gl);
        }
        for (int i = 14; i < 24; i++) {
            initBuffer(i, HoleVers.get(i-14), gl);
        }
        initBuffer(24,selectVer,gl);
        initBuffer(25,Hole_Vers,gl);//第一阶段拟合点展示
        initBuffer(26,Hole_Plane_Vers,gl);
        initBuffer(27,Line,gl);
        initBuffer(28,PLANE,gl);
        initBuffer(29,CIRCLE,gl);
        initBuffer(30,verVers,gl);
        initBuffer(31,edgeVers,gl);
    }
    private void initBuffer(int num,ArrayList<Float> VersList,GL3 gl) {
        if (VersList == null) return;
        float Vers[] = new float[VersList.size()];
        for (int i = 0, length = VersList.size(); i < length; i++) {
            Vers[i] = VersList.get(i);
        }
        this.initBuffer(num, Vers, gl);
    }

    private void initBuffer(int num, float[] Vers, GL3 gl) {
        gl.glBindVertexArray(ArrayName.get(num));
        FloatBuffer VertexBuffer = GLBuffers.newDirectFloatBuffer(Vers);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, BufferName.get(num));
        gl.glBufferData(GL.GL_ARRAY_BUFFER,
                VertexBuffer.capacity() * Float.BYTES,
                VertexBuffer, GL.GL_DYNAMIC_DRAW);
        /**
         * GL_STATIC_DRAW ：数据不会或几乎不会改变。
         * GL_DYNAMIC_DRAW：数据会被改变很多。
         * GL_STREAM_DRAW ：数据每次绘制时都会改变。
         **/
        gl.glEnableVertexAttribArray(0);//通道0读取顶点坐标
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(1);//通道1读取纹理坐标
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
        /**
         * 参数1，读取数据传入GPU的通道编号，可用0~15
         * 参数2，每组数据包含的数据个数
         * 参数3，数据类型（OPENGL预设类型）
         * 参数4，是否标准化，默认false
         * 参数5，步长
         * 参数6，偏移量
         **/
        //解绑
        gl.glBindVertexArray(0);
        gl.glBindBuffer(GL2.GL_ARRAY_BUFFER, 0);
    }

    private void initShader(GL3 gl) {
        String[] vertexSource = new String[1];
        String[] fragmentSource = new String[1];
        String v = getSource(System.getProperty("user.dir") + "/src/shader/vertexShader.txt");
        vertexSource[0] = v;
        String f = getSource(System.getProperty("user.dir") + "/src/shader/fragmentShader.txt");
        fragmentSource[0] = f;
        int[] length1 = {vertexSource[0].length()};
        int[] length2 = {fragmentSource[0].length()};
        int vertexShader = gl.glCreateShader(GL2ES2.GL_VERTEX_SHADER);
        int fragmentShader = gl.glCreateShader(GL2ES2.GL_FRAGMENT_SHADER);
        gl.glShaderSource(vertexShader, length1.length, vertexSource, length1, 0);
        gl.glShaderSource(fragmentShader, length2.length, fragmentSource, length2, 0);
        gl.glCompileShader(vertexShader);
        //Check compile status.
        int[] compiled = new int[1];
        gl.glGetShaderiv(vertexShader, GL2ES2.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] != 0) {
            System.out.println("vertex shader compiled");
        } else {
            int[] logLength = new int[1];
            gl.glGetShaderiv(vertexShader, GL2ES2.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(vertexShader, logLength[0], null, 0, log, 0);

            System.out.println("Error compiling the vertex shader:" + new String(log));
            System.exit(1);
        }
        gl.glCompileShader(fragmentShader);
        //Check compile status.
        gl.glGetShaderiv(fragmentShader, GL2ES2.GL_COMPILE_STATUS, compiled, 0);
        if (compiled[0] != 0) {
            System.out.println("fragment shader compiled");
        } else {
            int[] logLength = new int[1];
            gl.glGetShaderiv(fragmentShader, GL2ES2.GL_INFO_LOG_LENGTH, logLength, 0);

            byte[] log = new byte[logLength[0]];
            gl.glGetShaderInfoLog(fragmentShader, logLength[0], null, 0, log, 0);

            System.out.println("Error compiling the fragment shader:" + new String(log));
            System.exit(1);
        }
        program = gl.glCreateProgram();
        gl.glAttachShader(program, vertexShader);
        gl.glAttachShader(program, fragmentShader);
        gl.glLinkProgram(program);
        gl.glDeleteShader(vertexShader);//链接完成后删除shader
        gl.glDeleteShader(fragmentShader);

    }

    private String getSource(String path) {
        char[] buf = new char[1024];
        int count = 0;
        try {
            Reader in = new FileReader(path);
            count = in.read(buf);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(buf, 0, count);
    }

    private void initTexture(GL3 gl) {
        try {
            gl.glGenTextures(Data.TextureNum, TextureName);//生成纹理
            for (int i = 0; i < 10; i++) {
                initTexture(i, "/src/image/numberTexture/SORT-" + String.valueOf(i) + ".png", "png", gl);
            }
            initTexture(10, "/src/image/temp0.jpg", "jpg", gl);
            initTexture(11, "/src/image/blue.jpg", "jpg", gl);
            initTexture(12, "/src/image/pink.jpg", "jpg", gl);
            initTexture(13, "/src/image/temp1.jpg", "jpg", gl);
            initTexture(14, "/src/image/wo.jpg", "jpg", gl);
            initTexture(25, "/src/image/line2.png", "png", gl);
            initTexture(26, "/src/image/line.jpg", "jpg", gl);
            initTexture(27, "/src/image/temp1.jpg", "jpg", gl);
            initTexture(28, "/src/image/black.png", "png", gl);
            initTexture(29, "/src/image/CIRCLE.png", "png", gl);
            initTexture(30, "/src/image/point.png", "png", gl);
            initTexture(31, "/src/image/nearline.png", "png", gl);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
            gl.glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initTexture(int num, String path, String type, GL3 gl) {
        try {
            TextureData data = TextureIO.newTextureData(GLProfile.getDefault(), new File(
                    System.getProperty("user.dir") + path), false, type);
            gl.glActiveTexture(GL_TEXTURE0 + num);//开启num号通道
            gl.glBindTexture(GL_TEXTURE_2D, TextureName.get(num));//绑定
            gl.glPixelStorei(GL_UNPACK_ALIGNMENT, 1);//解决图像宽度不能整除4带来的缓冲区不足的问题
            gl.glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    data.getInternalFormat(),
                    data.getWidth(),
                    data.getHeight(),
                    data.getBorder(),
                    data.getPixelFormat(),
                    data.getPixelType(),
                    data.getBuffer()
            );
            gl.glGenerateMipmap(GL_TEXTURE_2D);
            data.destroy();//销毁data
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void initMatrix(GL3 gl) {
        //view.translate(0.2f,0.2f,-1.0f);
        camera = new Camera(new Vec3(0f, 0f, 2f),//观察点的位置
                (float) Math.toRadians(0f),//俯仰角
                (float) Math.toRadians(180f),//环视
                new Vec3(0, 1.0f, 0));
        view = camera.getViewMatrix();
        proj.perspective(45.0f, (float) 1200 / (float) 900, 0.1f, 100f);

    }

    private void initFrontVers() {
        initVers(FrontVers,testModel.Ts);
        //ArrayList<Float> frontV = new ArrayList<>();
        /**for (Tri tempT : testModel.Ts.tris.values()) {
            FrontVers.add((float) ((tempT.getV0().getX() - 70) * 0.05));
            FrontVers.add((float) ((tempT.getV0().getY() - 70) * 0.05));
            FrontVers.add((float) ((tempT.getV0().getZ() - 70) * 0.05));
            FrontVers.add(0.0f);
            FrontVers.add(0.0f);

            FrontVers.add((float) ((tempT.getV1().getX() - 70) * 0.05));
            FrontVers.add((float) ((tempT.getV1().getY() - 70) * 0.05));
            FrontVers.add((float) ((tempT.getV1().getZ() - 70) * 0.05));
            FrontVers.add(1.0f);
            FrontVers.add(0.0f);

            FrontVers.add((float) ((tempT.getV2().getX() - 70) * 0.05));
            FrontVers.add((float) ((tempT.getV2().getY() - 70) * 0.05));
            FrontVers.add((float) ((tempT.getV2().getZ() - 70) * 0.05));
            FrontVers.add(0.5f);
            FrontVers.add(1.0f);
        }
        for (int i = 0; i < testModel.Ts.tris.size() * 3 * 5; i++) {
            frontVers[i] = FrontVers.get(i);
        }**/
    }

    private void initBackVers() {
        //initVers(BackVers,testModel.Ts); //方向不一致，无法共用此方法
        //ArrayList<Float> backV = new ArrayList<Float>();
        BackVers.clear();
        for (Tri tempT : testModel.Ts.tris.values()) {
         BackVers.add((float) ((tempT.getV2().getX() - 70) * 0.05));
         BackVers.add((float) ((tempT.getV2().getY() - 70) * 0.05));
         BackVers.add((float) ((tempT.getV2().getZ() - 70) * 0.05));
         BackVers.add(0.5f);
         BackVers.add(1.0f);

         BackVers.add((float) ((tempT.getV1().getX() - 70) * 0.05));
         BackVers.add((float) ((tempT.getV1().getY() - 70) * 0.05));
         BackVers.add((float) ((tempT.getV1().getZ() - 70) * 0.05));
         BackVers.add(1.0f);
         BackVers.add(0.0f);

         BackVers.add((float) ((tempT.getV0().getX() - 70) * 0.05));
         BackVers.add((float) ((tempT.getV0().getY() - 70) * 0.05));
         BackVers.add((float) ((tempT.getV0().getZ() - 70) * 0.05));
         BackVers.add(0.0f);
         BackVers.add(0.0f);

        }

        //for (int i = 0; i < testModel.Ts.tris.size() * 3 * 5; i++) {
          //  backVers[i] = BackVers.get(i);
        //}
    }

    private void initDeleteVers() {
        ArrayList<Float> deleteV = new ArrayList<Float>();
        for (Tri tempT : testModel.deleteTs.tris.values()) {
            deleteV.add((float) ((tempT.getV0().getX() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV0().getY() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV0().getZ() - 70) * 0.05));
            deleteV.add(0.5f);
            deleteV.add(1.0f);

            deleteV.add((float) ((tempT.getV1().getX() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV1().getY() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV1().getZ() - 70) * 0.05));
            deleteV.add(1.0f);
            deleteV.add(0.0f);

            deleteV.add((float) ((tempT.getV2().getX() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV2().getY() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV2().getZ() - 70) * 0.05));
            deleteV.add(0.0f);
            deleteV.add(0.0f);

        }

        for (int i = 0; i < testModel.deleteTs.tris.size() * 3 * 5; i++) {
            deleteVers[i] = deleteV.get(i);
        }

        for (int i = 0; i < 11; i++) {
            initDeleteVersSort(i);
        }
    }

    private void initDeleteVersSort(int num) {
        ArrayList<Float> deleteV = new ArrayList<Float>();
        for (Tri tempT : testModel.sortDeleteTS[num].tris.values()) {
            deleteV.add((float) ((tempT.getV0().getX() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV0().getY() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV0().getZ() - 70) * 0.05));
            deleteV.add(0.5f);
            deleteV.add(1.0f);

            deleteV.add((float) ((tempT.getV1().getX() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV1().getY() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV1().getZ() - 70) * 0.05));
            deleteV.add(1.0f);
            deleteV.add(0.0f);

            deleteV.add((float) ((tempT.getV2().getX() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV2().getY() - 70) * 0.05));
            deleteV.add((float) ((tempT.getV2().getZ() - 70) * 0.05));
            deleteV.add(0.0f);
            deleteV.add(0.0f);

        }
        for (int i = 0; i < testModel.sortDeleteTS[num].tris.size() * 3 * 5; i++) {
            deleteVersSort[num][i] = deleteV.get(i);
        }

    }

    private void initHoleEdge() {
        for (int i = 0; i < 10; i++) {
            //initHoleEfgeSort(i);
            this.HoleVers.put(i,new ArrayList<Float>());
            initVers(this.HoleVers.get(i), this.testModel.sortHole_Tri[i]);
        }
    }

    private void initHoleVers(){
        this.Hole_Vers.clear();
        this.Hole_Plane_Vers.clear();
        this.Line.clear();
        this.PLANE.clear();
        this.CIRCLE.clear();
        for(Vertexs Vs:this.testModel.Hole_Vertexs.VERTEXSs.values()){
            this.PLANE.add((float)((Vs.LeftUp.getX()-70)*0.05));
            this.PLANE.add((float)((Vs.LeftUp.getY()-70)*0.05));
            this.PLANE.add((float)((Vs.LeftUp.getZ()-70)*0.05));
            this.PLANE.add(0.0f);
            this.PLANE.add(1.0f);

            this.PLANE.add((float)((Vs.LeftDown.getX()-70)*0.05));
            this.PLANE.add((float)((Vs.LeftDown.getY()-70)*0.05));
            this.PLANE.add((float)((Vs.LeftDown.getZ()-70)*0.05));
            this.PLANE.add(0.0f);
            this.PLANE.add(0.0f);

            this.PLANE.add((float)((Vs.RightDown.getX()-70)*0.05));
            this.PLANE.add((float)((Vs.RightDown.getY()-70)*0.05));
            this.PLANE.add((float)((Vs.RightDown.getZ()-70)*0.05));
            this.PLANE.add(1.0f);
            this.PLANE.add(0.0f);

            this.PLANE.add((float)((Vs.LeftUp.getX()-70)*0.05));
            this.PLANE.add((float)((Vs.LeftUp.getY()-70)*0.05));
            this.PLANE.add((float)((Vs.LeftUp.getZ()-70)*0.05));
            this.PLANE.add(0.0f);
            this.PLANE.add(1.0f);

            this.PLANE.add((float)((Vs.RightDown.getX()-70)*0.05));
            this.PLANE.add((float)((Vs.RightDown.getY()-70)*0.05));
            this.PLANE.add((float)((Vs.RightDown.getZ()-70)*0.05));
            this.PLANE.add(1.0f);
            this.PLANE.add(0.0f);

            this.PLANE.add((float)((Vs.RightUp.getX()-70)*0.05));
            this.PLANE.add((float)((Vs.RightUp.getY()-70)*0.05));
            this.PLANE.add((float)((Vs.RightUp.getZ()-70)*0.05));
            this.PLANE.add(1.0f);
            this.PLANE.add(1.0f);
            for(Vertex v:Vs.vertexs.values()){
                this.Hole_Vers.add((float)((v.getX()-70)*0.05));
                this.Hole_Vers.add((float)((v.getY()-70)*0.05));
                this.Hole_Vers.add((float)((v.getZ()-70)*0.05));
                this.Hole_Vers.add(0.0f);
                this.Hole_Vers.add(0.0f);

                this.Hole_Plane_Vers.add((float)((v.getX()-70)*0.05));
                this.Hole_Plane_Vers.add((float)((v.getY()-70)*0.05));
                this.Hole_Plane_Vers.add((float)((v.getPLANE_FITTING_Z()-70)*0.05));
                this.Hole_Plane_Vers.add(1.0f);
                this.Hole_Plane_Vers.add(0.0f);

                this.CIRCLE.add((float)((v.CIRCLE_X-70)*0.05));
                this.CIRCLE.add((float)((v.CIRCLE_Y-70)*0.05));
                this.CIRCLE.add((float)((v.CIRCLE_Z-70)*0.05));
                this.CIRCLE.add(0.0f);
                this.CIRCLE.add(0.0f);

                this.Line.add((float)((v.getX()-70)*0.05));
                this.Line.add((float)((v.getY()-70)*0.05));
                this.Line.add((float)((v.getZ()-70)*0.05));
                this.Line.add(0.0f);
                this.Line.add(0.0f);

                this.Line.add((float)((v.getX()-70)*0.05));
                this.Line.add((float)((v.getY()-70)*0.05));
                this.Line.add((float)((v.getPLANE_FITTING_Z()-70)*0.05));
                this.Line.add(1.0f);
                this.Line.add(0.0f);
            }
        }
        /**double []a= MyUtil.planeFitting(this.testModel.Hole_Vers);

        for(Vertex v:this.testModel.Hole_Vers.vertexs.values()){
            this.Hole_Vers.add((float)((v.getX()-70)*0.05));
            this.Hole_Vers.add((float)((v.getY()-70)*0.05));
            this.Hole_Vers.add((float)((v.getZ()-70)*0.05));
            this.Hole_Vers.add(0.0f);
            this.Hole_Vers.add(0.0f);
            Vertex planeFittingPoint=MyUtil.getPlaneFittingPoint(a[0],a[1],a[2],v);
            this.Hole_Vers.add((float)((planeFittingPoint.getX()-70)*0.05));
            this.Hole_Vers.add((float)((planeFittingPoint.getY()-70)*0.05));
            this.Hole_Vers.add((float)((planeFittingPoint.getZ()-70)*0.05));
            this.Hole_Vers.add(1.0f);
            this.Hole_Vers.add(0.0f);
        }**/
    }
    private void initHoleEdgeSort(int num) {
        //ArrayList<Float> deleteV = new ArrayList<Float>();
        for (Tri tempT : testModel.sortHole_Tri[num].tris.values()) {
            HoleVers.get(num).add((float) ((tempT.getV0().getX() - 70) * 0.05));
            HoleVers.get(num).add((float) ((tempT.getV0().getY() - 70) * 0.05));
            HoleVers.get(num).add((float) ((tempT.getV0().getZ() - 70) * 0.05));
            HoleVers.get(num).add(0.5f);
            HoleVers.get(num).add(1.0f);

            HoleVers.get(num).add((float) ((tempT.getV1().getX() - 70) * 0.05));
            HoleVers.get(num).add((float) ((tempT.getV1().getY() - 70) * 0.05));
            HoleVers.get(num).add((float) ((tempT.getV1().getZ() - 70) * 0.05));
            HoleVers.get(num).add(1.0f);
            HoleVers.get(num).add(0.0f);

            HoleVers.get(num).add((float) ((tempT.getV2().getX() - 70) * 0.05));
            HoleVers.get(num).add((float) ((tempT.getV2().getY() - 70) * 0.05));
            HoleVers.get(num).add((float) ((tempT.getV2().getZ() - 70) * 0.05));
            HoleVers.get(num).add(0.0f);
            HoleVers.get(num).add(0.0f);

        }
        for (int i = 0; i < testModel.sortHole_Tri[num].tris.size() * 3 * 5; i++) {
            hole_Tri[num][i] = HoleVers.get(num).get(i);
        }
    }

    private void initVers(ArrayList<Float> VersList, Tris Ts){
        VersList.clear();
        for (Tri tempT : Ts.tris.values()) {
            VersList.add((float) ((tempT.getV0().getX() - 70) * 0.05));
            VersList.add((float) ((tempT.getV0().getY() - 70) * 0.05));
            VersList.add((float) ((tempT.getV0().getZ() - 70) * 0.05));
            VersList.add(0.0f);
            VersList.add(0.0f);

            VersList.add((float) ((tempT.getV1().getX() - 70) * 0.05));
            VersList.add((float) ((tempT.getV1().getY() - 70) * 0.05));
            VersList.add((float) ((tempT.getV1().getZ() - 70) * 0.05));
            VersList.add(1.0f);
            VersList.add(0.0f);

            VersList.add((float) ((tempT.getV2().getX() - 70) * 0.05));
            VersList.add((float) ((tempT.getV2().getY() - 70) * 0.05));
            VersList.add((float) ((tempT.getV2().getZ() - 70) * 0.05));
            VersList.add(0.5f);
            VersList.add(1.0f);
        }
    }

    public Camera getCamera() {
        return camera;
    }

    public void deleteTri(int keyCode) {
        if (keyCode == KeyEvent.VK_R) {
            startDelete = true;
        }
    }

    public void repairTri(int keyCode) {
        if (keyCode == KeyEvent.VK_X) {
            startRepair = true;
        }
        if (keyCode == KeyEvent.VK_O) {
            this.testModel.Hole_Vers.setPlane();
            op=true;
        }
        if (keyCode == KeyEvent.VK_P) {
            this.testModel.Hole_Vers.setCircle();
            op=true;
        }
    }

    public void selectChange(String name) {
        if(this.testModel.Ts.tris.containsKey(name)){
            initSelect(this.testModel.Ts.tris.get(name));
            selectTri=this.testModel.Ts.tris.get(name);
            selectChange=true;
            System.out.println(name);
        }else{
            System.out.println("NO SELECTCHANGE");
        }

    }

    private void initSelect(Tri tri) {
        if(tri.getTag()=="no Tri") return;
        if(!this.testModel.Ts.tris.containsKey(tri.getTag())){
            for(int i=0;i<15;i++){
                selectVer[i]=0.0f;
            }
        }
        selectVer[0]=((float) ((tri.getV0().getX() - 70) * 0.05));
        selectVer[1]=((float) ((tri.getV0().getY() - 70) * 0.05));
        selectVer[2]=((float) ((tri.getV0().getZ() - 70) * 0.05));
        selectVer[3]=(0.0f);
        selectVer[4]=(0.0f);

        selectVer[5]=((float) ((tri.getV1().getX() - 70) * 0.05));
        selectVer[6]=((float) ((tri.getV1().getY() - 70) * 0.05));
        selectVer[7]=((float) ((tri.getV1().getZ() - 70) * 0.05));
        selectVer[8]=(1.0f);
        selectVer[9]=(0.0f);

        selectVer[10]=((float) ((tri.getV2().getX() - 70) * 0.05));
        selectVer[11]=((float) ((tri.getV2().getY() - 70) * 0.05));
        selectVer[12]=((float) ((tri.getV2().getZ() - 70) * 0.05));
        selectVer[13]=(0.5f);
        selectVer[14]=(1.0f);
    }
}
