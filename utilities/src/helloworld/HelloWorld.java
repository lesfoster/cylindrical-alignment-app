package helloworld;
 
import java.util.Arrays;
 
/**
 *
 * @author
 */
public class HelloWorld {
 
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
 
         int[] array = {5,2,10,6,12,3,20,8,7,9};
         
         System.out.println("unsorted: " + Arrays.toString(array));
         System.out.println(Arrays.toString(bubbleSortInt(array)));
    }
 
    private static int[] bubbleSortInt(int[] array){
         
        int arrayLength = array.length;
        int temp;
        int iterationTotal = 0;
         
        for(int i = 0; i < arrayLength; i++){
            int iteration = 0;
             
            for(int j = 1; j < arrayLength - i; j++){
                if(array[j] < array[j-1]){
					temp = array[j]; 
					array[j] = array[j-1];
					array[j-1] = temp;
                }
                 
                System.out.println((i+1)+"th:  " + Arrays.toString(array));
                iteration++; 
            }
         
            System.out.println("interation on " + (i+1) +"th: " + iteration);
            iterationTotal+=iteration;
        }
 
        System.out.println("total iterations:  " + iterationTotal);
        System.out.println("array sorted - ascending: ");
 
        return array;
    }
}
