/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package matching;

/**
 *
 * @author quy
 * Algorithm: RabinKarpPatternSearch
 * Compute a hash of pat[0..M).
    Compute a hash of text[i..M+i) for each i.
    If pattern hash = text substring hash, check for a match.
 */
public class RabinKarp {
    private char[] pattern; //the pattern
    private int patternHash; // pattern hash value
    private int M; //pattern length
    private int Q = 8355967 ; //modulus - a large prime, but small enough to advpid 32-bit integer overflow
    private int R; // radix
    private int RM; // R^(M-1) %Q
    
    public RabinKarp(int R, char[] pattern){
        this.R = R;
        this.pattern = pattern;
        this.M = pattern.length;
        
        RM = 1;
        for (int i=1; i <= M-1; i++){
            RM = (R * RM) % Q; //precompute R^(M-1) mode Q
        }
        this.patternHash = hash(pattern); // The pattern signature is computed once: O(m).
    }
    
    //Compute the signature of the key using modulo-arithmetic. 
    private int hash (char[] key){
        int h =0;
        for(int j=0;j<M;j++){
            h = (R*h + key[j]%Q);
        }
        return h;
    }
    
    public int search(char[] text){
        int N = text.length;
        if(N<M) return N;
        int offset = hashSearch(text);
        if(offset == N) return N;
        
        for(int i =0; i <M; i++){
            if(pattern[i] != text[offset + i]){
                return N;
            }
        }
        return offset;
    }

    private int hashSearch(char[] text) {
        int N = text.length;
        int textHash = hash(text);
        if(patternHash == textHash) return 0;
        
        for(int i = M; i < N; i++){
            textHash = (textHash + Q - RM*text[i-M] % Q) % Q;
            textHash = (textHash*R + text[i]) %Q;
            if(patternHash == textHash){
                return i -M + 1;
            }
        }
        return N;
    }
}
