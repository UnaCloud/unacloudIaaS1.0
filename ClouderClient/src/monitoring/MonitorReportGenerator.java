package monitoring;

import com.losandes.communication.messages.monitoring.MonitorReport;
import com.losandes.communication.messages.monitoring.MonitorInitialReport;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;

import org.hyperic.sigar.Cpu;
import org.hyperic.sigar.CpuInfo;
import org.hyperic.sigar.CpuPerc;
import org.hyperic.sigar.Mem;
import org.hyperic.sigar.NetInterfaceStat;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.Uptime;
import org.hyperic.sigar.cmd.SigarCommandBase;

import physicalmachine.Network;
import physicalmachine.PhysicalMachine;

public class MonitorReportGenerator extends SigarCommandBase {
	private static MonitorReportGenerator instance = new MonitorReportGenerator();
	private static String UUID = java.util.UUID.randomUUID().toString();;
	private static int contadorRegistros;

	public static MonitorInitialReport generateInitialReport() {
		PhysicalMachine monitor = new PhysicalMachine();
		java.util.Date date;
		date = new Date();
		java.sql.Timestamp timest = new java.sql.Timestamp(date.getTime());
		// com.sun.security.auth.module.NTSystem NTSystem = new
		// com.sun.security.auth.module.NTSystem();
		// Evalua una sola CPU
                CpuInfo[] infos;
		try {
			infos = instance.sigar.getCpuInfoList();
                        RepetitionCounter cpuModel=new RepetitionCounter();
                        RepetitionCounter cpuVendor=new RepetitionCounter();
                        RepetitionCounter cpuMhz=new RepetitionCounter();
                        
                        for(CpuInfo cpu:infos){
                            cpuModel.add(cpu.getModel());
                            cpuVendor.add(cpu.getVendor());
                            cpuMhz.add(""+cpu.getMhz());
                            
                        }
                        
			org.hyperic.sigar.CpuInfo CPU1 = infos[0];
			// System.out.println("Iniciando Carga de Batch Inicial");
			String domain = "Corregir domain";
			return new MonitorInitialReport(UUID, timest,
					Network.getHostname(), domain,
					monitor.operatingSystem.getOperatingSystemName(),
					monitor.operatingSystem.getOperatingSystemVersion(),
					monitor.operatingSystem.getOperatingSystemArchitect(),
					cpuModel.toString(), cpuVendor.toString(),
					monitor.cpu.getCPUCores(), CPU1.getTotalSockets(),
					cpuMhz.toString(), CPU1.getCoresPerSocket(),
					monitor.memory.getRAMMemorySize(),
					monitor.memory.getSwapMemorySize(),
					monitor.hardDisk.getHardDiskSpace(),
					monitor.hardDisk.getHardDiskFileSystem(),
					monitor.network.getNetworkMACAddress());
		} catch (SigarException e) {
			e.printStackTrace();
		}
                return null;
	}
        private static class RepetitionCounter extends HashMap<String,Integer>{
            public void add(String input){
                Integer n=get(input);
                if(n==null)n=0;
                n++;
                put(input,n);
            }

            @Override
            public String toString() {
                String ret=null;
                for(java.util.Map.Entry<String,Integer> ent:entrySet()){
                    ret=(ret!=null?";":"")+ent.getKey()+" x"+ent.getValue();
                }
                return ret==null?"":ret;
            }
            
        }
        
	public static MonitorReport generateStateReport() {
		String username=getUserName();
		LinpackJava.Linpack CPUMflops = new LinpackJava.Linpack();
		CPUMflops.run_benchmark();
		PhysicalMachine monitor = new PhysicalMachine();
		java.util.Date date = new Date();
		java.sql.Timestamp timest = new java.sql.Timestamp(date.getTime());
		contadorRegistros++;
		try {
			Cpu cpu = instance.sigar.getCpu();
                        CpuPerc CPU2 = instance.sigar.getCpuPerc();
			Uptime UPTIME = instance.sigar.getUptime();
			Mem MEM = instance.sigar.getMem();
			NetInterfaceStat NET = instance.sigar
					.getNetInterfaceStat(monitor.network.getNetworkInterface());
			return new MonitorReport(UUID, timest, contadorRegistros, username,
					UPTIME.getUptime(), CPUMflops.getMflops(),
					CPUMflops.getTimeinSecs(), CPU2.getIdle() * 100,
					(100 - (CPU2.getIdle() * 100)), CPU2.getUser() * 100,
					CPU2.getSys() * 100, CPU2.getNice() * 100,
					CPU2.getWait() * 100, CPU2.getCombined() * 100, cpu.getUser(),
					cpu.getSys(), cpu.getNice(), cpu.getWait(), cpu.getIdle(),
					monitor.memory.getRAMMemoryFree(),
					monitor.memory.getRAMMemoryUsed(), MEM.getFreePercent(),
					MEM.getUsedPercent(), monitor.memory.getSwapMemoryFree(),
					monitor.memory.getSwapMemoryPageIn(),
					monitor.memory.getSwapMemoryPageOut(),
					monitor.memory.getSwapMemoryUsed(),
					monitor.hardDisk.getHardDiskFreeSpace(),
					monitor.hardDisk.getHardDiskUsedSpace(),
					monitor.network.getNetworkIPAddress(),
					monitor.network.getNetworkInterface(),
					monitor.network.getNetworkNetmask(),
					monitor.network.getNetworkGateway(), NET.getRxBytes(),
					NET.getTxBytes(), NET.getSpeed(), NET.getRxErrors(),
					NET.getTxErrors(), NET.getRxPackets(), NET.getTxPackets());
		} catch (SigarException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static String getUserName(){
		String UserName = null;
		String sFichero = "C:/dist/user.info";
		File fichero = new File(sFichero);
		try{
			if (fichero.exists()) {
				// System.out.println("El fichero existe");
				FileReader fr = new FileReader("C:/dist/user.info");
				BufferedReader bf = new BufferedReader(fr);
				String User = null;
				while ((User = bf.readLine()) != null) {
					UserName = User;
				}
				bf.close();
				fr.close();
			} else {
				// System.out.println("El fichero no existe");
				Process p = Runtime.getRuntime().exec("cmd.exe /c quser");
				InputStream is = p.getInputStream();
				BufferedReader br = new BufferedReader(new InputStreamReader(is));
				String linea;
				StringBuilder mensaje = new StringBuilder();
				while ((linea = br.readLine()) != null) {
					if (linea.startsWith(">")) {
						String[] campos = linea.split("\\s+");
						UserName = campos[0].substring(1, campos[0].length());
					}
					mensaje.append(linea + "\n");/* Concatenamos el mensaje */
				}
				// String aux = br.readLine();
			}
		}catch(IOException ex){
			
		}
		return UserName;
	}

	@Override
	public void output(String[] arg0) throws SigarException {
		// TODO Auto-generated method stub
	}
}
