/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package services;

import controllers.PageRouter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Vilde
 */
public class AddressLookup {

    private String apibase = "https://api.getaddress.io/v2/uk/";
    private String apikey = "?api-key=aEeWLfHZ30KZvofp8FmPQQ3887";
    private String pc = "/";
    private String no = "/";

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

            try {
                String output = "";
                JSONParser parser = new JSONParser();
                JSONObject json = (JSONObject) parser.parse(recvbuff);
                String[] address = json.get("Addresses").toString().split(", ");
                for (String s : address) {
                    if (!s.trim().isEmpty()) {
                        output += s.trim() + ", ";
                    }
                }
                output = output.substring(0, output.length() - 2).replace("[", "").replace("]", "").replace("\"", "");
                return output;
            } catch (ParseException ex) {
                Logger.getLogger(PageRouter.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
