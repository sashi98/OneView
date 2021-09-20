package mydemo;

import java.util.HashMap;

public class HashmapDemo {
    public static void main(String[] args) {
        HashMap<Integer, Emp> hm = new HashMap();
        Emp emp = new Emp(1);
        hm.put(1, emp);
        hm.put(2, emp.setId(2));
        hm.put(3, emp.setId(3));
        hm.put(4, emp.setId(4));


        //hm.entrySet().forEach(ie->printLog("KEY VALUE:{}--{}", ie.getKey(), ie.getValue()));
        hm.put(2, new Emp(99));
        //printLog("--------------------------------------------------------");
        //hm.entrySet().forEach(ie->printLog("KEY VALUE:{}--{}", ie.getKey(), ie.getValue()));

    }

//    public static void printLog(String msg, Object... objects) {
//        final String regex = "\\{}";
//        for (Object object : objects) {
//            msg = msg.replaceFirst(regex, object.toString());
//        }
//        System.out.println(msg);
//    }
}

class Emp{
    private int id;
    public Emp(int id){
        this.id=id;
    }
    public Emp(Emp emp){
        this.id=emp.id;
    }

    public Emp setId(int i) {
        this.id=i;
        return this;
    }

    @Override
    public String toString() {
        return "Emp{" +
                "id=" + id +
                '}';
    }
}