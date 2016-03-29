/**
 * Created by ian on 3/2/2016.
 */
public class MonteCarloSimulation {

    public static void main (String[] args){

        double  p1 = 0,
                p2 = 0,
                p3 = 0,
                p4 = 0,
                p5 = 0;


        p1 = monteCarlo(p1, 10);
        p2 = monteCarlo(p2, 20);
        p3 = monteCarlo(p3, 40);
        p4 = monteCarlo(p4, 80);
        p5 = monteCarlo(p5, 160);


        System.out.println("-------------------------------------------------");
        System.out.println("n" + " " + "\t\t"+"10"+"\t\t"+"20"+"\t\t" + "40"+ "\t\t" + "80"+"\t\t"+"160");
        System.out.println("-------------------------------------------------");
        System.out.printf("\n%s\t%.2f\t%.2f\t%.2f\t%.2f\t%.2f\n", "Y(n)", p1, p2, p3, p4, p5);
        System.out.println("-------------------------------------------------");


    }

    public static double monteCarlo(double point, int k){
        double a = 0;
        double b = 22.0/7.0;
        double  total = 0, approx;

        for(int i = 0; i < k; i++){
            point = Math.sin(a + (b - a) * Math.random());
            total += point;
        }

        approx = (total / k) * (b - a);
        return  approx;

    }
}
