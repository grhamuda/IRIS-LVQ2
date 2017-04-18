/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package TA_LVQ2_IRIS;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.File;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

/**
 *
 * @author user
 */
public class LVQ2 {
    
    private double[][] data_training = new double[50][5];
    private double[][] data_testing = new double[48][5];
    private double[] hasil_testing = new double[48];
    
    private String[] token;
    int row = 0;
    private double windowWidth = 0.15;
    
    private double[][] w = new double[2][4];
    
    String path_training, path_testing, path_bobot;
    
    private double alpha, theta, min_alpha; 
    private int epoch, max_epoch;
    private boolean e;
    
    private double d_1, d_2;
    private double c1, c2;
    
    private int count_benar = 0;
    
    private double[][] d_train = new double[50][2];
    private double[][] d_tes = new double[48][2];
    private double[] cj_train = new double[50];
    private double[][] w1_train_update = new double[75][4];
    private double[][] w2_train_update = new double[75][4];
    
    protected void set_LVQ(String path_training, String path_testing, String path_bobot, double alpha, double theta, double min_alpha, int max_epoch ) throws FileNotFoundException, IOException{
        
        this.path_training = path_training;
        this.path_testing = path_testing;
        this.path_bobot = path_bobot;
        this.alpha = alpha;
        this.theta = theta;
        this.min_alpha = min_alpha;
        this.max_epoch = max_epoch;
        this.e = false;
        
        FileReader input_training = new FileReader(path_training);
        BufferedReader br_training = new BufferedReader(input_training);
        String line_training; 
        
        row = 0;
        while((line_training=br_training.readLine())!=null){
            token = line_training.split(",");
            data_training[row][0] = Double.parseDouble(token[0]);
            data_training[row][1] = Double.parseDouble(token[1]);
            data_training[row][2] = Double.parseDouble(token[2]);
            data_training[row][3] = Double.parseDouble(token[3]);
            data_training[row][4] = Double.parseDouble(token[4]);
            row++;
        }
        
        FileReader input_testing = new FileReader(path_testing);
        BufferedReader br_testing = new BufferedReader(input_testing);
        String line_testing; 
        
        row = 0;
        while((line_testing=br_testing.readLine())!=null){
            token = line_testing.split(",");
            data_testing[row][0] = Double.parseDouble(token[0]);
            data_testing[row][1] = Double.parseDouble(token[1]);
            data_testing[row][2] = Double.parseDouble(token[2]);
            data_testing[row][3] = Double.parseDouble(token[3]);
            data_testing[row][4] = Double.parseDouble(token[4]);
            row++;
        }
        
        FileReader input_bobot = new FileReader(path_bobot);
        BufferedReader br_bobot = new BufferedReader(input_bobot);
        String line_bobot; 
        
        row = 0;
        while((line_bobot=br_bobot.readLine())!=null){
            token = line_bobot.split(",");
            w[row][0] = Double.parseDouble(token[0]);
            w[row][1] = Double.parseDouble(token[1]);
            w[row][2] = Double.parseDouble(token[2]);
            w[row][3] = Double.parseDouble(token[3]);
            row++;
        }
    }
    
    protected void train(){
        
        System.out.println(" ==========");
        System.out.println(" |TRAINING|");
        System.out.println(" ==========");
        
        System.out.println("");
        
        this.epoch = 0;
        
        while(e != true){
            
            epoch += 1;
            
            for(int i = 0; i<data_training.length; i++){
                
                //euclidian
                d_1 = Math.sqrt((Math.pow((data_training[i][0] - w[0][0]),2)) + (Math.pow((data_training[i][1] - w[0][1]),2)) + (Math.pow((data_training[i][2] - w[0][2]),2)) + (Math.pow((data_training[i][3] - w[0][3]),2)));
                d_2 = Math.sqrt((Math.pow((data_training[i][0] - w[1][0]),2)) + (Math.pow((data_training[i][1] - w[1][1]),2)) + (Math.pow((data_training[i][2] - w[1][2]),2)) + (Math.pow((data_training[i][3] - w[1][3]),2)));
                                
                if(d_1 < d_2){
                    c1 = 1;
                    c2 = 2;
                }
                else{
                    c1 = 2;
                    c2 = 1;
                }
                
                //update bobot lvq2
                if((d_1/d_2 > (1-windowWidth)) && (d_2/d_1 < (1+windowWidth)) && (data_training[i][4] != c1) && (data_training[i][4] == c2)){
                                        
                    if(c1 == 1){
                        
                        w[0][0] = w[0][0] - (alpha * (data_training[i][0] - w[0][0]));
                        w[0][1] = w[0][1] - (alpha * (data_training[i][1] - w[0][1]));
                        w[0][2] = w[0][2] - (alpha * (data_training[i][2] - w[0][2]));
                        w[0][3] = w[0][3] - (alpha * (data_training[i][3] - w[0][3]));

                        w[1][0] = w[1][0] + (alpha * (data_training[i][0] - w[1][0]));
                        w[1][1] = w[1][1] + (alpha * (data_training[i][1] - w[1][1]));
                        w[1][2] = w[1][2] + (alpha * (data_training[i][2] - w[1][2]));
                        w[1][3] = w[1][3] + (alpha * (data_training[i][3] - w[1][3]));
                    }
                    else{
                        w[0][0] = w[0][0] + (alpha * (data_training[i][0] - w[0][0]));
                        w[0][1] = w[0][1] + (alpha * (data_training[i][1] - w[0][1]));
                        w[0][2] = w[0][2] + (alpha * (data_training[i][2] - w[0][2]));
                        w[0][3] = w[0][3] + (alpha * (data_training[i][3] - w[0][3]));

                        w[1][0] = w[1][0] - (alpha * (data_training[i][0] - w[1][0]));
                        w[1][1] = w[1][1] - (alpha * (data_training[i][1] - w[1][1]));
                        w[1][2] = w[1][2] - (alpha * (data_training[i][2] - w[1][2]));
                        w[1][3] = w[1][3] - (alpha * (data_training[i][3] - w[1][3]));    
                    }
                                        
                }
                
                //kondisi tdk terpenuhi, update lvq
                else{
                    if(data_training[i][4] == c1){

                        if(c1 == 1){
                            w[0][0] = w[0][0] + (alpha * (data_training[i][0] - w[0][0]));
                            w[0][1] = w[0][1] + (alpha * (data_training[i][1] - w[0][1]));
                            w[0][2] = w[0][2] + (alpha * (data_training[i][2] - w[0][2]));
                            w[0][3] = w[0][3] + (alpha * (data_training[i][3] - w[0][3]));
                        }
                        else{
                            w[1][0] = w[1][0] + (alpha * (data_training[i][0] - w[1][0]));
                            w[1][1] = w[1][1] + (alpha * (data_training[i][1] - w[1][1]));
                            w[1][2] = w[1][2] + (alpha * (data_training[i][2] - w[1][2]));
                            w[1][3] = w[1][3] + (alpha * (data_training[i][3] - w[1][3]));
                        }
                    }
                    else{

                        if(c1 == 1){
                            w[0][0] = w[0][0] - (alpha * (data_training[i][0] - w[0][0]));
                            w[0][1] = w[0][1] - (alpha * (data_training[i][1] - w[0][1]));
                            w[0][2] = w[0][2] - (alpha * (data_training[i][2] - w[0][2]));
                            w[0][3] = w[0][3] - (alpha * (data_training[i][3] - w[0][3]));
                        }
                        else{
                            w[1][0] = w[1][0] - (alpha * (data_training[i][0] - w[1][0]));
                            w[1][1] = w[1][1] - (alpha * (data_training[i][1] - w[1][1]));
                            w[1][2] = w[1][2] - (alpha * (data_training[i][2] - w[1][2]));
                            w[1][3] = w[1][3] - (alpha * (data_training[i][3] - w[1][3]));
                        }
                    }    
                }
                
                
                                                
                d_train[i][0] = d_1;
                d_train[i][1] = d_2;
                
                cj_train[i] = c1;
                
                w1_train_update[i][0] = w[0][0];
                w1_train_update[i][1] = w[0][1];
                w1_train_update[i][2] = w[0][2];
                w1_train_update[i][3] = w[0][3];
                
                w2_train_update[i][0] = w[1][0];
                w2_train_update[i][1] = w[1][1];
                w2_train_update[i][2] = w[1][2];
                w2_train_update[i][3] = w[1][3];
                               
                
//                System.out.println(i);
//                System.out.println("d_1 = " + d_1);
//                System.out.println("d_2 = " + d_2);
//                System.out.println("d_3 = " + d_3);
//                System.out.println("Cj  = " + c);
//                System.out.println("T   = " + data_training[i][4] );
//                System.out.println("a   = " + alpha);
//                System.out.println("e   = " + epoch);

//                System.out.println("");
            }
                
                //menampilkan data
                System.out.println(" Training Epoch ke- "+epoch);
                System.out.println(" =================================================================================================================================================================================================================================================================================");
                System.out.println(" | no. \t | x1 \t | x2 \t | x3 \t | x4 \t | target |  \t d_1 \t \t | \t d_2 \t \t | cj \t | \t w[1][1]      |      w[1][2]       |      w[1][3]       |      w[1][4]       | \t    w[2][1]       |      w[2][2]       |      w[2][3]       |      w[2][4]       |");
                System.out.println(" =================================================================================================================================================================================================================================================================================");
                
                for(int i = 0; i < data_training.length; i++){
                    
                    int no = i+1;
                    
                    System.out.printf(" | "+no+" \t");
                    
                    for(int j = 0; j < data_training[i].length; j++ ){
                        System.out.printf(" | " + data_training[i][j]+"\t");
                    }
                    
                    System.out.printf(" ");
                    
                    for(int j = 0; j < d_train[i].length; j++ ){
                        System.out.printf(" | " + d_train[i][j]+"\t");
                    }
                    
                    System.out.printf(" | " + cj_train[i] +"\t");
                    
                    for(int k = 0; k < w1_train_update[i].length; k++){
                        System.out.printf(" | %.16f", w1_train_update[i][k],"\t \t");
                    }
                    
                    for(int k = 0; k < w2_train_update[i].length; k++){
                        System.out.printf(" | %.16f", w2_train_update[i][k],"\t \t");
                    }
                    
                    System.out.println(" | ");
                }
                
                System.out.println(" =================================================================================================================================================================================================================================================================================");
                System.out.println("");
                System.out.println("");
            
            alpha = alpha * theta;
            
            if(alpha <= min_alpha || epoch >= max_epoch){
                e = true;
            }
            else{
                
            }
        }
    }
    
    protected void test(){
        
        System.out.println(" =========");
        System.out.println(" |TESTING|");
        System.out.println(" =========");
        
        for(int i = 0; i < data_testing.length; i++){
            
            d_1 = Math.sqrt((Math.pow((data_testing[i][0] - w[0][0]),2)) + (Math.pow((data_testing[i][1] - w[0][1]),2)) + (Math.pow((data_testing[i][2] - w[0][2]),2)) + (Math.pow((data_testing[i][3] - w[0][3]),2)));
            d_2 = Math.sqrt((Math.pow((data_testing[i][0] - w[1][0]),2)) + (Math.pow((data_testing[i][1] - w[1][1]),2)) + (Math.pow((data_testing[i][2] - w[1][2]),2)) + (Math.pow((data_testing[i][3] - w[1][3]),2)));
                    
            if(d_1 < d_2){
                hasil_testing[i] = 1;
            }
            else{
                hasil_testing[i] = 2;
            }
            
            
            d_tes[i][0] = d_1;
            d_tes[i][1] = d_2;
        }
        
                System.out.println(" =========================================================================================================");
                System.out.println(" | no. \t | x1 \t | x2 \t | x3 \t | x4 \t | target |  \t d_1 \t \t | \t d_2 \t \t | cj \t |");
                System.out.println(" =========================================================================================================");
                
                for(int i = 0; i < data_testing.length; i++){
                    
                    int no = i+1;
                    
                    System.out.printf(" | "+no+" \t");
                    
                    for(int j = 0; j < data_testing[i].length; j++ ){
                        System.out.printf(" | " + data_testing[i][j]+"\t");
                    }
                    
                    System.out.printf(" ");
                    
                    for(int j = 0; j < d_tes[i].length; j++ ){
                        System.out.printf(" | " + d_tes[i][j]+"\t");
                    }
                    
                    System.out.printf(" | " + hasil_testing[i] +"\t");
                    
                    if(hasil_testing[i] != data_testing[i][4]){
                        System.out.printf(" | //Salah, Cj dan Target berbeda \n");
                    }
                    else{
                        System.out.println(" | ");
                    }
                }
                
                System.out.println(" =========================================================================================================");
                System.out.println("");
    }
    
    protected void get_akurasi(){
        
        for(int i = 0; i < data_testing.length; i++){
            
            if(data_testing[i][4] == hasil_testing[i]){
                count_benar += 1;
                //System.out.println(data_testing[i][4] + " - " + hasil_testing[i]);
            }
            else{
                //System.out.println(data_testing[i][4] + " - " + hasil_testing[i] + " //Salah");
            }
            
        }
        
        double hasil_testing_benar = (double) count_benar;
        double total_data_testing = (double) data_testing.length;
        
        double akurasi = (hasil_testing_benar/total_data_testing)*100;
        
        System.out.println(" --------------------------------------------");
        System.out.println(" | Banyak Data testing = " + total_data_testing + "               |");
        System.out.println(" | Akurasi Program     = " + akurasi + "% |");
        System.out.println(" --------------------------------------------");
        System.out.println("");
    }
    
    protected void print(){
        
        for(int i = 0; i < data_testing.length; i++){
            
            for(int j = 0; j < data_testing[i].length; j++){
                System.out.printf(data_testing[i][j]+" \t");
            }
            System.out.println("");
        }
        
    }
}
