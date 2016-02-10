/**
 * Created by ian on 2/3/2016.
 */
public class DiceGame {

    public static final int N = 1000;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int     face1 = 0,
                face2 = 0,
                face3 = 0,
                face4 = 0,
                face5 = 0,
                face6 = 0;

        double p1 = 0, p2 = 0, p3 = 0, p4 = 0, p5 = 0, p6 = 0;


        for(int i = 1; i<= N; i++){
            double roll = Math.random();

            if(roll >= 0 && roll < 1.0/6.0){
                face1++;
            }
            else if(roll >= 1.0/6.0 && roll <2.0/6.0) {
                face2++;
            }
            else if(roll >= 2.0/6.0 && roll < 3.0/6.0){
                face3++;
            }
            else if(roll >= 3.0/6.0 && roll < 4.0/6.0){
                face4++;
            }
            else if(roll >= 4.0/6.0 && roll < 5.0/6.0){
                face5++;
            }
            else
                face6++;


        }

        // Calculate percentage of occurrence for each dice face
        p1 = (double) face1/ 10;
        p2 = (double) face2 /10;
        p3 = (double) face3 / 10;
        p4 = (double) face4 /10;
        p5 = (double) face5 /10;
        p6 = (double) face6 / 10;


        // Print out the results

        System.out.println("Face" +  "Frequency" + "Percentage");
        System.out.println("1" +"\t" +face1+"\t\t"+p1);
        System.out.println("2" +"\t" +face2+"\t\t"+p2);
        System.out.println("3" +"\t" +face3+"\t\t"+p3);
        System.out.println("4" +"\t" +face4+"\t\t"+p4);
        System.out.println("5" +"\t" +face5+"\t\t"+p5);
        System.out.println("6" +"\t" +face6+"\t\t"+p6);


        System.out.println("-----------------------------------------");
        System.out.println("\t"+"1000"+"\t"+"100");
    }
}
