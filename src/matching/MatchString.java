/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package matching;

/**
 *
 * @author quy
 */
public class MatchString {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
       
    }
    /*
    Check for pattern starting at each text position.
    @param: pattern and text
    Worst case: MN char compares (patterns and text are repetitive)
    */
    public static int searchBrutForce(char[] pattern, char[] text){
        int M = pattern.length;
        int N = text.length;
        for(int i=0; i <N-M;i++){
            int j;
            for(j=0;j<M;j++){
                if(text[i+j]!=pattern[j]){
                    break;
                }
            }
            if(j==M){
                return i;//index where pattern starts
            }
        }
        return N;//not found
    } 
    
    /* 
    

    */
    public static int searchBackup(char[] pattern, char[] text){
        int j, M = pattern.length;//j stores number of already-matched chars (end of sequence in pattern).
        int i, N = text.length;//i points to end of sequence of already-matched chars in text.
        for(i =0, j =0; i<N && j<M;i++){
            if(text[i]==pattern[j]) {
                j++;
            }else{
                i-=j;
                j=0;
            }
        }
        if(j==M){
            return i-M;
        }else{
            return N;
        }
    }
    
    
}
