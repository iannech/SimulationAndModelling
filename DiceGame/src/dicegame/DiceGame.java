
package dicegame;

/**
 *
 * @author Ian
 */
public class DiceGame {

     public static final int N = 1000;
     
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        int     d1 = 0,
                d2 = 0,
                d3 = 0,
                d4 = 0,
                d5 = 0,
                d6 = 0;
        
        double p1 = 0, p2 = 0, p3 = 0, p4 = 0, p5 = 0, p6 = 0;
        
        int frequency = 0;
        double percentage = 0;
        
        for(int i = 0; i<= N; i++){
            int roll = (int) (6.0 * Math.random());
            
            if(roll == 1)
                d1++;
            else if(roll == 2)
                d2++;
            else if(roll == 3)
                d3++;
            else if(roll == 4)
                d4++;
            else if(roll == 5)
                d5++;
            else if(roll == 6)
                d6++;
                        
        }
        
        // Calculate percentage of occurence for each dice face
        p1 = (double) d1/ 10;
        p2 = (double) d2 /10;
        p3 = (double) d3 / 10;
        p4 = (double) d4 /10;
        p5 = (double) d5 /10;
        p6 = (double) d6 / 10;
         
        for(int count = 1; count < 6; count++){
            switch(count){
                case 1:
                    frequency = d1;
                    percentage = p1;
                    break;
                case 2: 
                    frequency = d2;
                    percentage = p2;
                    break;
                case 3:
                    frequency = d3;
                    percentage = p3;
                    break;
                case 4:
                    frequency = d4;
                    percentage = p4;
                    break;
                case 5:
                    frequency = d5;
                    percentage = p5;
                    break;
                case 6:
                    frequency = d6;
                    percentage = p6;
                    break;
            }
            
            System.out.println(count + "\t"+frequency+"\t"+ percentage);
        }
        System.out.println("-----------------------------------------");
        System.out.println("\t"+"1000"+"\t"+"100");
    }
    
}
