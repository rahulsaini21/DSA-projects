import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import org.w3c.dom.Node;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.BreakIterator;

class PowerLine {
    String cityA;
    String cityB;

    public PowerLine(String cityA, String cityB) {
        this.cityA = cityA;
        this.cityB = cityB;
    }
}

// Students can define new classes here
class node{
    String name;
    int cityNo;
    node par;
    ArrayList<node> childs;
    int level;
    int highPoint;

    node(String name,node par, int cityNo, int level, int highPoint){
        this.name = name;
        this.par = par;
        this.cityNo = cityNo;
        this.level = level;
        this.highPoint = highPoint;
        childs = new ArrayList<>();
    }

}

public class PowerGrid {
    int numCities;
    int numLines;
    String[] cityNames;
    PowerLine[] powerLines;

    // Students can define private variables here
    private int min(Integer a, Integer b){
        if(a>=b) return b;
        return a;
    }
    // private int timer =1;
    private node root;
    
    
    private ArrayList<PowerLine> bridges = new ArrayList<>();

    private HashMap<String, Integer> mapping = new HashMap<>();
    private HashMap<Integer, String> mapping2 = new HashMap<>();

    private int[] vis;
    private node[] nodeAdd;
    private int cEdge[];
    private int vis2[];
    // int[] levels;
    // int[] highPoint;


    private void dfs(ArrayList<ArrayList<Integer>> graphList, int pos, int par, int level, int[] vis, node[] nodeAdd){

        vis[pos] = 1;
        node pNode = nodeAdd[pos];

        for(int i=0; i<graphList.get(pos).size(); i++){
            int temp = graphList.get(pos).get(i);
            if(vis[temp] == -1){
                
                nodeAdd[temp].par = pNode;
                nodeAdd[temp].level = level+1;
                nodeAdd[temp].highPoint = level+1;

                pNode.childs.add(nodeAdd[temp]);
                dfs(graphList, temp, pos, level+1, vis, nodeAdd);
                pNode.highPoint = min(pNode.highPoint, nodeAdd[temp].highPoint);

            }

            if(vis[temp] != -1 && temp != par ){

                pNode.highPoint = min(pNode.highPoint, nodeAdd[temp].level);
            }
        }
    }

    private ArrayList<PowerLine> makeBridge(node node){
        Queue<node> q = new LinkedList<>();
        q.add(node);
        // int pLevel = 

        while(!q.isEmpty()){
            int size = q.size();

            for(int i=0; i<size; i++){
                
                node temp = q.poll();
                if(temp.highPoint == temp.level && temp != root ){
                    bridges.add(new PowerLine(temp.par.name, temp.name));
                }
                
                int tempSize = temp.childs.size();
                for(int j=0; j<tempSize; j++){
                    q.add(temp.childs.get(j));
                    
                }
            }

        }


        return bridges;

    }

    private node lca(node root, String cityA, String cityB){
        if(root == null) return null;
        if (root.name.equals(cityA) || root.name.equals(cityB)) {
            return root;
        }

        ArrayList<node> list = new ArrayList<>();
        for(node child: root.childs){
            node get = lca(child, cityA, cityB);

            if(get != null){
                list.add(get);
            }
            if(list.size() == 2){
                return root;
            }
        }

        if(list.size() ==1){
            return list.get(0);
        }
        return null;
    }

    private void dfs(node root,int ed, int[] vis2, int[] cEdge){
        vis2[root.cityNo] = 1;

        for(int i=0; i<root.childs.size(); i++){
            node child = root.childs.get(i);
            if(vis2[child.cityNo] == -1){
                if(child.level == child.highPoint){
                    cEdge[child.cityNo] = ed+1;
                    dfs(child, ed+1, vis2,cEdge);
                }
                else{
                    cEdge[child.cityNo] = ed;
                    dfs(child, ed, vis2, cEdge);

                }

            }
        }


    }

    // public void high_bfs(node node, int newHighPoint, int pLevel, int tillLevel){

    //     Queue<node> q = new LinkedList<>();
    //     q.add(node);

    //     while(pLevel<=tillLevel){
    //         int size = q.size();

    //         for(int i=0; i<size; i++){
    //             node temp = q.poll();
    //             temp.highPoint = min(temp.highPoint, newHighPoint);
    //             int tempSize = temp.childs.size();
    //             for(int j=0; j<tempSize; j++){
    //                 q.add(temp.childs.get(j));
    //             }
    //         }
    //         pLevel++;

    //     }
    // }

    // public void bfs(node node){
    //     Queue<node> q = new LinkedList<>();
    //     q.add(node);

    //     while(!q.isEmpty()){
    //         int size = q.size();

    //         for(int i=0; i<size; i++){
    //             node temp = q.poll();
    //             System.out.print(temp.cityNo +","+temp.level+" "+temp.highPoint+"\t");
    //             int tempSize = temp.childs.size();
    //             for(int j=0; j<tempSize; j++){
    //                 q.add(temp.childs.get(j));
    //             }
    //         }
    //         System.out.println();
    //     }
    // }
    

    public PowerGrid(String filename) throws Exception {
        File file = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(file));
        numCities = Integer.parseInt(br.readLine());
        numLines = Integer.parseInt(br.readLine());
        cityNames = new String[numCities];
        for (int i = 0; i < numCities; i++) {
            cityNames[i] = br.readLine();
        }
        powerLines = new PowerLine[numLines];
        for (int i = 0; i < numLines; i++) {
            String[] line = br.readLine().split(" ");
            powerLines[i] = new PowerLine(line[0], line[1]);
        }

        // TO be completed by students

        vis = new int[numCities];
        nodeAdd = new node[numCities];
        ArrayList<ArrayList<Integer>> graphList = new ArrayList<>(numLines);

        for(int i=0; i<numCities ; i++){
            mapping.put(cityNames[i], i);
            mapping2.put(i, cityNames[i]);
            ArrayList<Integer> temp = new ArrayList<>();
            graphList.add(i, temp);
            vis[i] = -1;
            nodeAdd[i] = new node(cityNames[i], null, i, -1, -1);
        }

        for(int i=0; i<numLines; i++){
            int aCity = mapping.get(powerLines[i].cityA);
            int bCity = mapping.get(powerLines[i].cityB);

            graphList.get(aCity).add(bCity);
            graphList.get(bCity).add(aCity);

        }
      
        nodeAdd[0].highPoint = 0;
        nodeAdd[0].level = 0;

        root = nodeAdd[0];

        dfs(graphList, 0, 0, 0, vis, nodeAdd);

        

        makeBridge(root);

        
    }

    public ArrayList<PowerLine> criticalLines() {
        /*
         * Implement an efficient algorithm to compute the critical transmission lines
         * in the power grid.
         
         * Expected running time: O(m + n), where n is the number of cities and m is the
         * number of transmission lines.
         */

        return bridges;
    }


    

    public void preprocessImportantLines() {
        /*
         * Implement an efficient algorithm to preprocess the power grid and build
         * required data structures which you will use for the numImportantLines()
         * method. This function is called once before calling the numImportantLines()
         * method. You might want to define new classes and data structures for this
         * method.
         
         * Expected running time: O(n * logn), where n is the number of cities.
         */
        vis2 = new int[numCities];
        cEdge = new int[numCities];

        for(int i=0; i<numCities; i++){
            vis2[i] = -1;
            cEdge[i] = 0;
        }

        dfs(root, 0, vis2, cEdge);


        return;
    }


    
    public int numImportantLines(String cityA, String cityB) {
        /*
         * Implement an efficient algorithm to compute the number of important
         * transmission lines between two cities. Calls to numImportantLines will be
         * made only after calling the preprocessImportantLines() method once.
         
         * Expected running time: O(logn), where n is the number of cities.
         */

        // preprocessImportantLines();
        node lca = lca(root, cityA, cityB);
        
        int cA= mapping.get(cityA);
        int cB= mapping.get(cityB);

        int  res = (cEdge[cA]+cEdge[cB])- (2*cEdge[lca.cityNo]) ;
        
        return res;
    }
    // public static void main(String[] args) throws Exception {
    //     PowerGrid pg = new PowerGrid("E:/SEMESTERS/Semester 6th/COL106/Assignments/A6/v2/A6_starter_code_v2/Q1/test1.txt");
    //     ArrayList<PowerLine> cl = pg.criticalLines();
    //     for(PowerLine pl : cl){
    //         System.out.println(pl.cityA+" "+pl.cityB);  
    //     }
    //     // int ni = pg.numImportantLines("Kolkata", "Chennai");
    //     // System.out.println("imp line "+ni);
        
    // }
}