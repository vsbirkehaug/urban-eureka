/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


/**
 *
 * @author Vilde
 */
public class AddressLookup {

    String apibase = "https://api.getaddress.io/v2/uk/";
    String apikey = "?api-key=aEeWLfHZ30KZvofp8FmPQQ3887";
    String pc = "/";
    String no = "/";

    public String getUrl(String pc, String no) {
        return apibase + pc + this.pc + no + this.no + apikey;
    }

    public String getAddress(String url) {
        try {
            String recv;
            String recvbuff = "";
            URL jsonpage = new URL(url);
            URLConnection urlcon = jsonpage.openConnection();
            BufferedReader buffread = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));

            while ((recv = buffread.readLine()) != null) {
                recvbuff += recv;
            }
            buffread.close();
            
            String[] address = recvbuff.split(",");
            String output = "";
            for(String s : address) {
                if(!s.trim().isEmpty()) {
                    output += s.trim() + ", ";                 
                }
            }
            output = output.substring(0, output.length()-2);
            
            return output;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
