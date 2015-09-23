/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Statistics;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kostas
 */
public class HostStats {
   
    String Hostname;
    String Time;
    String Arch;
    String Physical_CPUs;
    String Count;
    String Running;
    String Blocked;
    String Paused;
    String Shutdown;
    String Shutoff;
    String Crashed;
    String Active;
    String Inactive;
    String CPU_percentage;
    String Total_hardware_memory_KB;
    String Total_memory_KB;
    String Total_guest_memory_KB;
    List<NetRateStats> netRates;

    public HostStats(){
        
        netRates=new ArrayList<>();
        netRates.add(new NetRateStats("eth1"));
        netRates.add(new NetRateStats("wlan0"));
    
    }
    
    
    public String getHostname() {
        return Hostname;
    }

    public void setHostname(String Hostname) {
        this.Hostname = Hostname;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String Time) {
        this.Time = Time;
    }

    public String getArch() {
        return Arch;
    }

    public void setArch(String Arch) {
        this.Arch = Arch;
    }

    public String getPhysical_CPUs() {
        return Physical_CPUs;
    }

    public void setPhysical_CPUs(String Physical_CPUs) {
        this.Physical_CPUs = Physical_CPUs;
    }

    public String getCount() {
        return Count;
    }

    public void setCount(String Count) {
        this.Count = Count;
    }

    public String getRunning() {
        return Running;
    }

    public void setRunning(String Running) {
        this.Running = Running;
    }

    public String getBlocked() {
        return Blocked;
    }

    public void setBlocked(String Blocked) {
        this.Blocked = Blocked;
    }

    public String getPaused() {
        return Paused;
    }

    public void setPaused(String Paused) {
        this.Paused = Paused;
    }

    public String getShutdown() {
        return Shutdown;
    }

    public void setShutdown(String Shutdown) {
        this.Shutdown = Shutdown;
    }

    public String getShutoff() {
        return Shutoff;
    }

    public void setShutoff(String Shutoff) {
        this.Shutoff = Shutoff;
    }

    public String getCrashed() {
        return Crashed;
    }

    public void setCrashed(String Crashed) {
        this.Crashed = Crashed;
    }

    public String getActive() {
        return Active;
    }

    public void setActive(String Active) {
        this.Active = Active;
    }

    public String getInactive() {
        return Inactive;
    }

    public void setInactive(String Inactive) {
        this.Inactive = Inactive;
    }

    public String getCPU_percentage() {
        return CPU_percentage;
    }

    public void setCPU_percentage(String CPU_percentage) {
        this.CPU_percentage = CPU_percentage;
    }

    public String getTotal_hardware_memory_KB() {
        return Total_hardware_memory_KB;
    }

    public void setTotal_hardware_memory_KB(String Total_hardware_memory_KB) {
        this.Total_hardware_memory_KB = Total_hardware_memory_KB;
    }

    public String getTotal_memory_KB() {
        return Total_memory_KB;
    }

    public void setTotal_memory_KB(String Total_memory_KB) {
        this.Total_memory_KB = Total_memory_KB;
    }

    public String getTotal_guest_memory_KB() {
        return Total_guest_memory_KB;
    }

    public void setTotal_guest_memory_KB(String Total_guest_memory_KB) {
        this.Total_guest_memory_KB = Total_guest_memory_KB;
    }

    public List<NetRateStats> getNetRates() {
        return netRates;
    }

    public void setNetRates(List<NetRateStats> netRates) {
        this.netRates = netRates;
    }
    
    
    
    
}
