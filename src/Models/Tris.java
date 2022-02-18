package Models;

import java.util.HashMap;

public class Tris {
    public HashMap<String ,Tri>tris=new HashMap<String,Tri>();

    public HashMap<String,Tri>sortTris=new HashMap<String, Tri>();

    //public Tri head=new Tri();

    //public Tri tail=new Tri();

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
        this.sortTris.clear();
        Tri targetT = new Tri();
        Tris newTs=new Tris();
        newTs.tris.putAll(Ts.tris);
        int size=newTs.tris.size();

        int num = 0;
        boolean isNear = false;
        targetT = newTs.tris.entrySet().iterator().next().getValue();
        this.sortTris.put(String.valueOf(num++), targetT);
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
                this.sortTris.put(String.valueOf(num++), targetT);
                newTs.tris.remove(targetT.getTag());
                isNear = false;
            } else {
                num = -1;
            }
        }
        this.sortTris.putAll(newTs.tris);

    }

    public void sort(){
        this.sort(this);
    }

    public Tris divide(){//分割后再排序可能会出现无法串连起来的情况，所以应该在分割的时候就排好序
        if(this.tris.isEmpty()) return new Tris();
        Tris tempTs=new Tris();
        Tri targetT = new Tri();
        int size=this.tris.size();
        int num=0;

        boolean isNear = false;
        targetT = this.tris.entrySet().iterator().next().getValue();
        tempTs.tris.put(targetT.getTag(), targetT);
        tempTs.sortTris.put(String.valueOf(num++),targetT);
        this.tris.remove(targetT.getTag());

        while (num > 0 && num < size) {
            //先找有公共边的，然后再找有公共点的
            for (Tri T : targetT.hasCommonBorder.tris.values()) {
                if (this.tris.containsKey(T.getTag())) {
                    targetT = T;
                    isNear = true;
                    break;
                }
            }
            if(!isNear){
                for (Tri T : targetT.hasCommonPoint.tris.values()) {
                    if (this.tris.containsKey(T.getTag())) {
                        targetT = T;
                        isNear = true;
                        break;
                    }
                }
            }

            if (isNear) {
                tempTs.tris.put(targetT.getTag(), targetT);
                tempTs.sortTris.put(String.valueOf(num++),targetT);
                this.tris.remove(targetT.getTag());
                isNear = false;
            } else{
                num=-1;
            }
        }
        return tempTs;
    }

    public Tri getHead(){
        return this.sortTris.getOrDefault("0",new Tri("no head"));
    }

    public Tri getTail(){
        return this.sortTris.getOrDefault(
                String.valueOf(this.sortTris.size()-1),new Tri("no tail"));
    }
}
