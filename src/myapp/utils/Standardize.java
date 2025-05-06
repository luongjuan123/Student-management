/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package myapp.utils;

import java.util.Scanner;

/**
 *
 * @author juan
 */
    public class Standardize {
    
    public static String name(String s) {
        String res = "";
        Scanner sc = new Scanner(s);
        while (sc.hasNext()) {
            String tmp = sc.next();
            tmp = Character.toUpperCase(tmp.charAt(0)) + tmp.substring(1).toLowerCase();
            res += tmp +" ";
        }
        return res;
    }   
}

