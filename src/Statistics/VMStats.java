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
public class VMStats {
     
        String hostName;
        String Domain_ID;
        String Domain_name;
        String CPU_ns;
        String CPU_percentage;
        String Mem_bytes;
        String Mem_percentage;
        String Block_RDRQ;
        String Block_WRRQ;
        String Net_RXBY;
        String Net_TXBY;
        
        int instance;
        int slot;
        
        NetRateStats netRates;

    public String getDomain_ID() {
        return Domain_ID;
    }

    public void setDomain_ID(String Domain_ID) {
        this.Domain_ID = Domain_ID;
    }

    public String getDomain_name() {
        return Domain_name;
    }

    public void setDomain_name(String Domain_name) {
        this.Domain_name = Domain_name;
    }

    public String getCPU_ns() {
        return CPU_ns;
    }

    public void setCPU_ns(String CPU_ns) {
        this.CPU_ns = CPU_ns;
    }

    public String getCPU_percentage() {
        return CPU_percentage;
    }

    public void setCPU_percentage(String CPU_percentage) {
        this.CPU_percentage = CPU_percentage;
    }

    public String getMem_bytes() {
        return Mem_bytes;
    }

    public void setMem_bytes(String Mem_bytes) {
        this.Mem_bytes = Mem_bytes;
    }

    public String getMem_percentage() {
        return Mem_percentage;
    }

    public void setMem_percentage(String Mem_percentage) {
        this.Mem_percentage = Mem_percentage;
    }

    public String getBlock_RDRQ() {
        return Block_RDRQ;
    }

    public void setBlock_RDRQ(String Block_RDRQ) {
        this.Block_RDRQ = Block_RDRQ;
    }

    public String getBlock_WRRQ() {
        return Block_WRRQ;
    }

    public void setBlock_WRRQ(String Block_WRRQ) {
        this.Block_WRRQ = Block_WRRQ;
    }

    public String getNet_RXBY() {
        return Net_RXBY;
    }

    public void setNet_RXBY(String Net_RXBY) {
        this.Net_RXBY = Net_RXBY;
    }

    public String getNet_TXBY() {
        return Net_TXBY;
    }

    public void setNet_TXBY(String Net_TXBY) {
        this.Net_TXBY = Net_TXBY;
    }

    public NetRateStats getNetRates() {
        return netRates;
    }

    public void setNetRates(NetRateStats netRates) {
        this.netRates = netRates;
    }

    public int getInstance() {
        return instance;
    }

    public void setInstance(int instance) {
        this.instance = instance;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }
        
        
}
