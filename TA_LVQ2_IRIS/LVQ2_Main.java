/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package TA_LVQ2_IRIS;

import java.io.IOException;
import java.util.Scanner;

/**
 *
 * @author user
 */
public class LVQ2_Main {
   
    public static void main(String[] args) throws IOException {
        Scanner in = new Scanner(System.in);
        LVQ2 p = new LVQ2();
        
        System.out.println(" =======");
        System.out.println(" |INPUT|");
        System.out.println(" =======");
        
        System.out.print(" File data training      : ");
        String training = in.next();
        
        System.out.print(" File data testing       : ");
        String testing = in.next();
        
        System.out.print(" File inisialisasi bobot : ");
        String bobot = in.next();
        
        System.out.print(" alpha (learning rate)   : ");
        String a = in.next();
        Double alpha = Double.parseDouble(a);
        
        System.out.print(" alpha minimum           : ");
        String a_min = in.next();
        Double alpha_min = Double.parseDouble(a_min);
        
        System.out.print(" reduksi learning rate   : ");
        String r = in.next();
        Double reduksi = Double.parseDouble(r);
        
        System.out.print(" epoch maksimal          : ");
        String e = in.next();
        int epoch = Integer.parseInt(e);
        System.out.print(" Window Width            : 0.35 (fixed)");
        
        System.out.println("");
        System.out.println("");
        System.out.println("");
                
        
//        double alpha = 0.5;
//        double theta = 0;
//        int epoch = 20;
       
        String path_training = ; //your data training path, txt
        String path_testing = ; //your data testing path, txt
        String path_bobot = ; //your weight data path, txt
        
        p.set_LVQ(path_training, path_testing, path_bobot, alpha, reduksi, alpha_min, epoch);
        p.train();
        p.test();
        p.get_akurasi();
        //p.print();
   
    }
}
