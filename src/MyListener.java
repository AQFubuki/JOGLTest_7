import Models.Model;
import Models.Tri;
import Models.Vertex;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.util.GLBuffers;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import glm.mat._4.Mat4;
import glm.vec._3.Vec3;

import java.awt.event.KeyEvent;
import java.io.*;
import java.lang.reflect.Array;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.jogamp.opengl.GL.*;
import static com.jogamp.opengl.GL2ES1.GL_PERSPECTIVE_CORRECTION_HINT;

public class MyListener implements GLEventListener {

    private final IntBuffer ArrayName = GLBuffers.newDirectIntBuffer(3);//VAO的buffer
    private final IntBuffer BufferName = GLBuffers.newDirectIntBuffer(3);//VBO的buffer
    private final IntBuffer TextureName = GLBuffers.newDirectIntBuffer(3);//纹理的buffer

    private Models.Model testModel=new Model(System.getProperty("user.dir") + "/src/stl/Twoman.stl");
    private float frontVers[]=new float[testModel.Ts.tris.size()*3*5];
    private float backVers[]=new float[testModel.Ts.tris.size()*3*5];
    private float deleteVers[]=new float[testModel.Ts.tris.size()*3*5];




    private  int program;
    private GLU glu;

    private final glm.mat._4.Mat4 model =new Mat4();
    private glm.mat._4.Mat4 view =new Mat4();//观察矩阵
    private final glm.mat._4.Mat4 proj =new Mat4();//投影矩阵

    private Camera camera=new Camera();

    long lastTime=0,currentTime;
    float deltaTime;

    boolean startDelete=false;
    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();//获取OpenGL上下文
        //glv=new GLU();
        gl.glClearColor(1.0f,1.0f,1.0f,0.0f);//设置背景颜色
        gl.glClearDepth(1.0f);//设置最远的深度值
        gl.glEnable(GL_DEPTH_TEST);//开启深度测试
        gl.glDepthFunc(GL_LEQUAL);//the type of depth test to do
        gl.glHint(GL_PERSPECTIVE_CORRECTION_HINT,GL_NICEST);//best perspective correction
        //---------以下为自定义初始化代码-----------
        initFrontVers();
        initBackVers();
        initDeleteVers();
        initBuffer(gl);

        initShader(gl);//初始化着色器
        initTexture(gl);//初始化纹理
        initMatrix(gl);
    }



    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();//获取上下文
        gl.glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);//清楚颜色和深度缓存

        if(startDelete){
            System.out.println("delete");
            if(!testModel.Ts.tris.isEmpty() &&testModel.deleteTs.tris.isEmpty()){
                testModel.deleteTs.tris.put("9.956773E+01 7.965430E+01 7.003226E+011.001268E+02 8.266519E+01 6.974493E+019.985004E+01 8.175720E+01 7.163765E+01"
                      ,testModel.Ts.tris.get("9.956773E+01 7.965430E+01 7.003226E+011.001268E+02 8.266519E+01 6.974493E+019.985004E+01 8.175720E+01 7.163765E+01"));
                //testModel.deleteTs.tris.put("4.979375E+01 6.685950E+01 1.083000E+005.330725E+01 6.951050E+01 1.204987E-015.820825E+01 6.739100E+01 7.550430E-02"
                  //      ,testModel.Ts.tris.get("4.979375E+01 6.685950E+01 1.083000E+005.330725E+01 6.951050E+01 1.204987E-015.820825E+01 6.739100E+01 7.550430E-02"));
            }
            testModel.modelDelete();
            initFrontVers();
            initBackVers();
            initDeleteVers();
            initBuffer(gl);
            startDelete=false;
        }

        currentTime=System.currentTimeMillis();
        deltaTime=currentTime-lastTime;
        camera.setSpeed(deltaTime*0.015f);
        lastTime=currentTime;
       // model.rotate(0.01f,1.0f,0.0f,0.0f);
        //model.rotate(0.01f,0.0f,1.0f,0.0f);
       // model.rotate(0.01f,0.0f,0.0f,1.0f);
        gl.glUseProgram(program);
        view=camera.getViewMatrix();
        //传入矩阵
        gl.glUniformMatrix4fv(gl.glGetUniformLocation(program,"model"),1,false,model.toFa_(),0);
        gl.glUniformMatrix4fv(gl.glGetUniformLocation(program,"view"),1,false,view.toFa_(),0);
        gl.glUniformMatrix4fv(gl.glGetUniformLocation(program,"proj"),1,false,proj.toFa_(),0);

        gl.glEnable(GL_CULL_FACE);//剔除背面

        gl.glActiveTexture(GL_TEXTURE0);
        gl.glBindTexture(GL_TEXTURE_2D,TextureName.get(0));
        gl.glUniform1i(gl.glGetUniformLocation(program,"ourTexture"),0);
        gl.glBindVertexArray(ArrayName.get(0));
        gl.glDrawArrays(GL_TRIANGLES,0,testModel.Ts.tris.size()*3);

        gl.glActiveTexture(GL_TEXTURE1);
        gl.glBindTexture(GL_TEXTURE_2D,TextureName.get(1));
        gl.glUniform1i(gl.glGetUniformLocation(program,"ourTexture"),1);
        gl.glBindVertexArray(ArrayName.get(1));
        gl.glDrawArrays(GL_TRIANGLES,0,testModel.Ts.tris.size()*3);

        gl.glActiveTexture(GL_TEXTURE2);
        gl.glBindTexture(GL_TEXTURE_2D,TextureName.get(2));
        gl.glUniform1i(gl.glGetUniformLocation(program,"ourTexture"),2);
        gl.glBindVertexArray(ArrayName.get(2));
        gl.glDrawArrays(GL_TRIANGLES,0,testModel.deleteTs.tris.size()*3);
    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
    }

    private void initBuffer(GL3 gl) {
        //设置VAO和VBO
        gl.glGenVertexArrays(3, ArrayName);
        gl.glGenBuffers(3, BufferName);

        gl.glBindVertexArray(ArrayName.get(0));
        FloatBuffer frontVertexBuffer = GLBuffers.newDirectFloatBuffer(frontVers);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, BufferName.get(0));
        gl.glBufferData(GL.GL_ARRAY_BUFFER,
                frontVertexBuffer.capacity() * Float.BYTES,
                frontVertexBuffer, GL.GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(0);//通道0读取顶点坐标
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(1);//通道1读取纹理坐标
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3*Float.BYTES);
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

        gl.glBindVertexArray(ArrayName.get(1));
        FloatBuffer vertexBuffer = GLBuffers.newDirectFloatBuffer(backVers);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, BufferName.get(1));
        gl.glBufferData(GL.GL_ARRAY_BUFFER,
                vertexBuffer.capacity() * Float.BYTES,
                vertexBuffer, GL.GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(0);//通道0读取顶点坐标
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(1);//通道1读取纹理坐标
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3*Float.BYTES);
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

        gl.glBindVertexArray(ArrayName.get(2));
        FloatBuffer deleteBuffer = GLBuffers.newDirectFloatBuffer(deleteVers);
        gl.glBindBuffer(GL.GL_ARRAY_BUFFER, BufferName.get(2));
        gl.glBufferData(GL.GL_ARRAY_BUFFER,
                deleteBuffer.capacity() * Float.BYTES,
                deleteBuffer, GL.GL_STATIC_DRAW);
        gl.glEnableVertexAttribArray(0);//通道0读取顶点坐标
        gl.glVertexAttribPointer(0, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
        gl.glEnableVertexAttribArray(1);//通道1读取纹理坐标
        gl.glVertexAttribPointer(1, 2, GL_FLOAT, false, 5 * Float.BYTES, 3*Float.BYTES);
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
        int[] compiled =new int[1];
        gl.glGetShaderiv(vertexShader,GL2ES2.GL_COMPILE_STATUS,compiled,0);
        if(compiled[0]!=0){
            System.out.println("vertex shader compiled");
        }else{
            int[] logLength=new int[1];
            gl.glGetShaderiv(vertexShader,GL2ES2.GL_INFO_LOG_LENGTH,logLength,0);

            byte[] log=new byte[logLength[0]];
            gl.glGetShaderInfoLog(vertexShader,logLength[0],null,0,log,0);

            System.out.println("Error compiling the vertex shader:"+new String(log));
            System.exit(1);
        }
        gl.glCompileShader(fragmentShader);
        //Check compile status.
        gl.glGetShaderiv(fragmentShader,GL2ES2.GL_COMPILE_STATUS,compiled,0);
        if(compiled[0]!=0){
            System.out.println("fragment shader compiled");
        }else{
            int[] logLength=new int[1];
            gl.glGetShaderiv(fragmentShader,GL2ES2.GL_INFO_LOG_LENGTH,logLength,0);

            byte[] log=new byte[logLength[0]];
            gl.glGetShaderInfoLog(fragmentShader,logLength[0],null,0,log,0);

            System.out.println("Error compiling the fragment shader:"+new String(log));
            System.exit(1);
        }
        program=gl.glCreateProgram();
        gl.glAttachShader(program,vertexShader);
        gl.glAttachShader(program,fragmentShader);
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
            gl.glGenTextures(3,TextureName);//生成纹理

            TextureData data= TextureIO.newTextureData(GLProfile.getDefault(),new File(
                    System.getProperty("user.dir") + "/src/image/pink.jpg"),false,TextureIO.JPG);
            gl.glActiveTexture(GL_TEXTURE0);//开启0号通道
            gl.glBindTexture(GL_TEXTURE_2D,TextureName.get(0));//绑定
            gl.glPixelStorei(GL_UNPACK_ALIGNMENT,1);//解决图像宽度不能整除4带来的缓冲区不足的问题
            //System.out.println(data.getBuffer());
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

            data= TextureIO.newTextureData(GLProfile.getDefault(),new File(
                    System.getProperty("user.dir") + "/src/image/blue.jpg"),false,TextureIO.JPG);
            gl.glActiveTexture(GL_TEXTURE1);
            gl.glBindTexture(GL_TEXTURE_2D,TextureName.get(1));//绑定
            gl.glPixelStorei(GL_UNPACK_ALIGNMENT,1);//解决图像宽度不能整除4带来的缓冲区不足的问题
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

            data= TextureIO.newTextureData(GLProfile.getDefault(),new File(
                    System.getProperty("user.dir") + "/src/image/temp1.jpg"),false,TextureIO.JPG);
            gl.glActiveTexture(GL_TEXTURE2);
            gl.glBindTexture(GL_TEXTURE_2D,TextureName.get(2));//绑定
            gl.glPixelStorei(GL_UNPACK_ALIGNMENT,1);//解决图像宽度不能整除4带来的缓冲区不足的问题
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

            gl.glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_S,GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_WRAP_T,GL_REPEAT);
            gl.glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MIN_FILTER,GL_LINEAR);
            gl.glTexParameteri(GL_TEXTURE_2D,GL_TEXTURE_MAG_FILTER,GL_LINEAR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initMatrix(GL3 gl) {
        //view.translate(0.2f,0.2f,-1.0f);
        camera=new Camera(new Vec3(0f,0f,2f),//观察点的位置
                (float)Math.toRadians(0f),//俯仰角
                (float)Math.toRadians(180f),//环视
                new Vec3(0,1.0f,0));
        view=camera.getViewMatrix();
        proj.perspective(45.0f,(float)1200/(float)900,0.1f,100f);

    }

    private void initFrontVers() {
        ArrayList<Float> frontV= new ArrayList<>();
        for(Tri tempT:testModel.Ts.tris.values()){
            frontV.add((float)((tempT.getV0().getX()-70)*0.05));
            frontV.add((float)((tempT.getV0().getY()-70)*0.05));
            frontV.add((float)((tempT.getV0().getZ()-70)*0.05));
            frontV.add(0.0f);
            frontV.add(0.0f);

            frontV.add((float)((tempT.getV1().getX()-70)*0.05));
            frontV.add((float)((tempT.getV1().getY()-70)*0.05));
            frontV.add((float)((tempT.getV1().getZ()-70)*0.05));
            frontV.add(1.0f);
            frontV.add(0.0f);

            frontV.add((float)((tempT.getV2().getX()-70)*0.05));
            frontV.add((float)((tempT.getV2().getY()-70)*0.05));
            frontV.add((float)((tempT.getV2().getZ()-70)*0.05));
            frontV.add(0.5f);
            frontV.add(1.0f);
        }

        //System.out.println(frontVers.length+"  "+frontV.size());
        for(int i=0;i<testModel.Ts.tris.size()*3*5;i++){
            frontVers[i]=frontV.get(i);
        }
    }

    private void initBackVers() {
        ArrayList<Float> backV=new ArrayList<Float> ();
        for(Tri tempT:testModel.Ts.tris.values()){
            backV.add((float)((tempT.getV2().getX()-70)*0.05));
            backV.add((float)((tempT.getV2().getY()-70)*0.05));
            backV.add((float)((tempT.getV2().getZ()-70)*0.05));
            backV.add(0.5f);
            backV.add(1.0f);

            backV.add((float)((tempT.getV1().getX()-70)*0.05));
            backV.add((float)((tempT.getV1().getY()-70)*0.05));
            backV.add((float)((tempT.getV1().getZ()-70)*0.05));
            backV.add(1.0f);
            backV.add(0.0f);

            backV.add((float)((tempT.getV0().getX()-70)*0.05));
            backV.add((float)((tempT.getV0().getY()-70)*0.05));
            backV.add((float)((tempT.getV0().getZ()-70)*0.05));
            backV.add(0.0f);
            backV.add(0.0f);

        }

        for(int i=0;i<testModel.Ts.tris.size()*3*5;i++){
            backVers[i]=backV.get(i);
        }


    }

    private void initDeleteVers() {
        ArrayList<Float> deleteV=new ArrayList<Float> ();
        for(Tri tempT:testModel.deleteTs.tris.values()){
            deleteV.add((float)((tempT.getV0().getX()-70)*0.05));
            deleteV.add((float)((tempT.getV0().getY()-70)*0.05));
            deleteV.add((float)((tempT.getV0().getZ()-70)*0.05));
            deleteV.add(0.5f);
            deleteV.add(1.0f);

            deleteV.add((float)((tempT.getV1().getX()-70)*0.05));
            deleteV.add((float)((tempT.getV1().getY()-70)*0.05));
            deleteV.add((float)((tempT.getV1().getZ()-70)*0.05));
            deleteV.add(1.0f);
            deleteV.add(0.0f);

            deleteV.add((float)((tempT.getV2().getX()-70)*0.05));
            deleteV.add((float)((tempT.getV2().getY()-70)*0.05));
            deleteV.add((float)((tempT.getV2().getZ()-70)*0.05));
            deleteV.add(0.0f);
            deleteV.add(0.0f);

        }

        for(int i=0;i<testModel.deleteTs.tris.size()*3*5;i++){
            deleteVers[i]=deleteV.get(i);
        }

    }


    public Camera getCamera(){
        return camera;
    }

    public void deleteTri(int keyCode) {
        if(keyCode== KeyEvent.VK_R){
            startDelete=true;
        }
    }
}
