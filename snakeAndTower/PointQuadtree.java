import java.util.*;

public class PointQuadtree {

    enum Quad {
        NW,
        NE,
        SW,
        SE
    }

    public PointQuadtreeNode root;

    public PointQuadtree() {
        this.root = null;
    }

    public boolean insert(CellTower a) {
        // TO be completed by students
        PointQuadtreeNode pqt = new PointQuadtreeNode(a);
        // System.out.println("insert "+ a.x+" "+a.y);
        if(root == null){
            root = pqt;
            return true;
        }

        PointQuadtreeNode cur1 = root;

        while(true){
            boolean x_axis = true;
            boolean y_axis = true;
            if(a.x< cur1.celltower.x){
                x_axis = false;
            }
            if(a.y< cur1.celltower.y){
                y_axis = false;
            }

            if(a.x == cur1.celltower.x && a.y == cur1.celltower.y){
                return false;
            }

            if(!x_axis && y_axis){
                if(cur1.quadrants[0] == null){
                    cur1.quadrants[0] = pqt;
                    return true;
                }else{
                    cur1 = cur1.quadrants[0];
                    continue;
                }
            }

            if(x_axis && y_axis){
                if(cur1.quadrants[1] == null){
                    cur1.quadrants[1] = pqt;
                    return true;

                }else{
                    cur1 = cur1.quadrants[1];
                    continue;
                }
            }

            if(x_axis && !y_axis){
                if(cur1.quadrants[3] == null){
                    cur1.quadrants[3] = pqt;
                    return true;

                }else{
                    cur1 = cur1.quadrants[3];
                    continue;
                }
            }
            
            if(!x_axis && !y_axis){
                if(cur1.quadrants[2] == null){
                    cur1.quadrants[2] = pqt;
                    return true;

                }else{
                    cur1 = cur1.quadrants[2];
                    continue;
                }
            }
            
        }

        // return true;
    }

    public boolean cellTowerAt(int x, int y) {
        // TO be completed by students
        // System.out.println("search "+ x+" "+y);
        PointQuadtreeNode cur1 = root;

        while(cur1!=null){
            int c_x = cur1.celltower.x;
            int c_y = cur1.celltower.y;

            int dx = c_x - x;
            int dy = c_y - y;
            
            if(c_x == x && c_y == y){
                return true;
            }

            else if(dx>=0 && dy<=0){
                cur1 = cur1.quadrants[0];
            }
            else if(dx<=0 && dy <=0){
                cur1 = cur1.quadrants[1];
            }
            else if( dx>=0 && dy >=0){
                cur1 = cur1.quadrants[2];
            }
            else{
                cur1 = cur1.quadrants[3];
            }

        }
        return false;
    }

    

    public CellTower chooseCellTower(int x, int y, int r) {
        // TO be completed by students

        PointQuadtreeNode cur1 = root;
        // System.out.println(" chooseCellTower "+ x+" "+y+" "+r);

        Queue<PointQuadtreeNode> q = new LinkedList<>();

        q.add(cur1);
        CellTower ans = null;
        int min_cost = Integer.MAX_VALUE;
        while(!q.isEmpty()){
            PointQuadtreeNode temp = q.remove();
            int r_x = temp.celltower.x;
            int r_y = temp.celltower.y;


            if((x-r_x)*(x-r_x) +(y-r_y)*(y-r_y) < r*r){
                if(min_cost > temp.celltower.cost ){
                    ans = temp.celltower;
                    min_cost = temp.celltower.cost;
                }
            }


            for(int i=1; i<5; i++){
                // System.out.println("root child ("+ r_x+"," + r_y+ ") " +i);
                if(intersect_check(temp, x, y, r, i) && temp.quadrants[i-1] !=null){
                    // System.out.println("added child");
                    q.add(temp.quadrants[i-1]);
                }
            }
        }

        // if(ans!= null){
        // System.out.println("min cell "+ ans.x+","+ans.y+","+ans.cost);
        // }else{
        //     System.out.println("min cell null ");
        // }



        return ans;
    }

    private boolean intersect_check(PointQuadtreeNode root, int x0, int y0, int r, int quad){

        if(root == null) return false;

        int arr_x[] = {x0 , x0-r, x0, x0+r, x0};
        int arr_y[] = {y0, y0, y0+r, y0, y0-r};

        int r_x = root.celltower.x;
        int r_y = root.celltower.y;

        if(quad == 1){
            for(int i=0; i<5; i++){
                if(r_x-arr_x[i]>=0 && r_y - arr_y[i] <=0){
                    return true;
                }
            }
        }

        if(quad == 2){
            for(int i=0; i<5; i++){
                if(r_x-arr_x[i] <=0 && r_y - arr_y[i]<=0){
                    return true;
                }
            }
        }

        if(quad == 3){
            for(int i=0; i<5; i++){
                if(r_x-arr_x[i]>=0 && r_y - arr_y[i]>=0){
                    return true;
                }
            }
        }

        if(quad == 4){
            for(int i=0; i<5; i++){
                if(r_x-arr_x[i]<= 0 && r_y - arr_y[i]>=0){
                    return true;
                }
            }
        }
        
        return false;
    }

    // private int dfs(PointQuadtreeNode root, int x, int y, int r){
    //     if(root == null){
    //         return -1;
    //     }
    //     int r_x = root.celltower.x;
    //     int r_y = root.celltower.y;

    //     System.out.println("root "+ r_x+" "+ r_y);

    //     if((x-r_x)*(x-r_x) +(y-r_y)*(y-r_y) < r*r){
    //         System.out.println("found "+ r_x+ " "+ r_y+ " "+ root.celltower.cost);
    //     }

    //     for(int i=1; i<5; i++){
    //         System.out.println("root child ("+ r_x+"," + r_y+ ") " +i);
    //         if(intersect_check(root, x, y, r, i)){
    //             dfs(root.quadrants[i-1], x, y, r);
    //         }
    //     }
    //     return 0;
    // }

    

    // private int abs(int a){
    //     if(a<0) return -a;
    //     return a;
    // }

    // private void print(){
    //     Queue<PointQuadtreeNode> q = new LinkedList<>();

    //     q.add(root);
    //     while(!q.isEmpty()){
    //         int size = q.size();
    //         for(int i=0; i<size; i++){
                
    //             PointQuadtreeNode temp = q.remove();
    //             if(temp!=null) System.out.print("("+ temp.celltower.x+","+temp.celltower.y+") \t");
    //             if(temp == null){
    //                 System.out.print("null \t");
    //                 continue;
    //             }
    //             for(int j=0; j<4; j++){
    //                 q.add(temp.quadrants[j]);
    //                 // if(temp.quadrants[j]!= null){
    //                 // }
    //             }  
    //         }
    //         System.out.println();
    //         // q.pop(); 
                     
    //     }

    // }


    // public static void main(String[] args) {
    //     PointQuadtree pt = new PointQuadtree();
    //     // CellTower ct = new CellTower(5, 0, 0);
    //     // CellTower ct1 = new CellTower(6, 2, 0);
    //     // CellTower ct2 = new CellTower(2, -2, 0);
    //     // CellTower ct3 = new CellTower(3, 0, 0);
    //     // CellTower ct4 = new CellTower(2, -2, 0);
    //     // pt.insert(ct);
    //     // pt.insert(ct1);
    //     // pt.insert(ct2);
    //     // pt.insert(ct3);

    //     CellTower c1 = new CellTower(0,0,5);
    //     CellTower c2 = new CellTower(-2,0,4);
    //     CellTower c3 = new CellTower(2,3,10);
    //     CellTower c4 = new CellTower(-4,6,9);
    //     pt.insert(c1);
    //     pt.insert(c2);
    //     pt.insert(c3);
    //     pt.insert(c4);


    //     // System.out.println(pt.cellTowerAt(-2, 0));
    //     // System.out.println(pt.cellTowerAt(2, 4));
    //     System.out.println(pt.cellTowerAt(-4, 6));



    //     // pt.chooseCellTower(0, 6, 5);
    //     // pt.insert(c4);
    //     // pt.chooseCellTower(0, 6, 5);


    //     // CellTower c5 = new CellTower(-3,7,5);
    //     // CellTower c6 = new CellTower(-3,3,4);
    //     // CellTower c7 = new CellTower(-6,7,2);
    //     // CellTower c8 = new CellTower(-5,4,9);
    //     // pt.insert(c5);
    //     // pt.insert(c6);
    //     // pt.insert(c7);
    //     // pt.insert(c8);
    //     // System.out.println(pt.insert(c3));
    //     // pt.chooseCellTower(-2, 6, 2); // returns c5                                                                


        
    //     // System.out.println(pt.insert(ct4));
    //     // pt.insert(ct4);
    //     // pt.print();

    //     // pt.chooseCellTower(1, -1, 4);


    // }
    
    

}
