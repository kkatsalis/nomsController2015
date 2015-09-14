/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Statistics;

import java.util.List;

/**
 *
 * @author kostas
 */
public class VM {
     
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
        
        List<NetRate> netRates;
}
