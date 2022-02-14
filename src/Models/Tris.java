package Models;

import java.util.HashMap;

public class Tris {
    public HashMap<String ,Tri>tris=new HashMap<String,Tri>();

    public void print(){
        for(Tri t:this.tris.values()){
            t.print();
        }
    }

    public void printNearTris(){
        for(Tri t:this.tris.values()){
            t.printnearTris();
        }
    }

    public void printTag(){
        for(Tri t:this.tris.values()){
            System.out.println(t.getTag());
        }
    }

    public void sort(Tris Ts){
        if(Ts.tris.isEmpty()) return;
        this.tris.clear();
        Tri targetT = new Tri();
        Tris newTs=new Tris();
        newTs.tris.putAll(Ts.tris);
        int size=newTs.tris.size();

        int num = 0;
        boolean isNear = false;
        targetT = newTs.tris.entrySet().iterator().next().getValue();
        this.tris.put(String.valueOf(num++), targetT);
        newTs.tris.remove(targetT.getTag());

        while (num > 0 && num < size) {
            //tris.size()会逐渐减少
            //先找有公共边的，然后再找有公共点的
            for (Tri tempT : targetT.hasCommonBorder.tris.values()) {
                if (newTs.tris.containsKey(tempT.getTag())) {
                    targetT = tempT;
                    isNear = true;
                    break;
                }
            }
            if(!isNear){
                for (Tri tempT : targetT.hasCommonPoint.tris.values()) {
                    if (newTs.tris.containsKey(tempT.getTag())) {
                        targetT = tempT;
                        isNear = true;
                        break;
                    }
                }
            }

            if (isNear) {
                this.tris.put(String.valueOf(num++), targetT);
                newTs.tris.remove(targetT.getTag());
                isNear = false;
            } else {
                num = -1;
            }
        }
        this.tris.putAll(newTs.tris);

    }
    public void sort(){
        Tri targetT = new Tri();
        Tris newTs = new Tris();
        int num = 0;
        boolean isNear = false;
        int size=this.tris.size();

        targetT = this.tris.entrySet().iterator().next().getValue();
        newTs.tris.put(String.valueOf(num++), targetT);
        this.tris.remove(targetT.getTag());


        while (num > 0 && num < size) {
            //tris.size()会逐渐减少
            //先找有公共边的，然后再找有公共点的
            for (Tri tempT : targetT.hasCommonBorder.tris.values()) {
                if (this.tris.containsKey(tempT.getTag())) {
                    targetT = tempT;
                    isNear = true;
                    break;
                }
            }
            if(!isNear){
                for (Tri tempT : targetT.hasCommonPoint.tris.values()) {
                    if (this.tris.containsKey(tempT.getTag())) {
                        targetT = tempT;
                        isNear = true;
                        break;
                    }
                }
            }

            if (isNear) {
                newTs.tris.put(String.valueOf(num++), targetT);
                this.tris.remove(targetT.getTag());
                isNear = false;
            } else {
                num = -1;
            }
        }
        this.tris.putAll(newTs.tris);
    }
}
