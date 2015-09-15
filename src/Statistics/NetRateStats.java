/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Statistics;

/**
 *
 * @author kostas
 */
public class NetRate {
      
    String intrface;
    String timestamp;
    String Kbps_in;
    String Kbps_out;

    public String getIntrface() {
        return intrface;
    }

    public void setIntrface(String intrface) {
        this.intrface = intrface;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getKbps_in() {
        return Kbps_in;
    }

    public void setKbps_in(String Kbps_in) {
        this.Kbps_in = Kbps_in;
    }

    public String getKbps_out() {
        return Kbps_out;
    }

    public void setKbps_out(String Kbps_out) {
        this.Kbps_out = Kbps_out;
    }
    
    
}
