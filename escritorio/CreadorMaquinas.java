import java.util.Random;


public class CreadorMaquinas {

	public static void main(String[] args) {
		for(int e=0;e<30;e++){
			System.out.println(SQLMaquina(true,"LoniClient"+e,true,10,"E:/GRID/MasterSGE64-bit/.164-MasterSGE 64-bit (DavidMendez).vmx",
					1,512,"157.253.202."+(100+e),"ISC"+(301+e),5,1,17));
		}
	}
	
	public static String SQLMaquina(boolean stored,String name,boolean configured,int vmdisk,String vmPath,int vmCores,int vmMemory,String vmIP,String pmName,int securityCode,int hpCode, int templateCode){
		String h = "INSERT INTO clouder.virtualmachine (`VIRTUALMACHINESTATE`, `LOCALLYSTORED`, `VIRTUALMACHINENAME`, `CONFIGURED`, `VIRTUALMACHINEMAC`, `IPPOLICY`, `VIRTUALMACHINEHARDDISK`, `MACPOLICY`, `VIRTUALMACHINEPATH`, `VIRTUALMACHINECORES`, `VIRTUALMACHINERAMMEMORY`, `VIRTUALMACHINEIP`, `PHYSICALMACHINE_PHYSICALMACHINENAME`, `VIRTUALMACHINESECURITY_VIRTUALMACHINESECURITYCODE`, `HYPERVISOR_HYPERVISORCODE`, `TEMPLATE_TEMPLATECODE`) ";
		h+= "VALUES (0, ";
		h+=stored+", '"+name+"', "+configured+", '"+mac()+"', 0, "+vmdisk+", 0, ";
		h+="'"+vmPath+"', "+vmCores+", "+vmMemory+", '"+vmIP+"', '"+pmName+"', "+securityCode+", "+hpCode+", "+templateCode+");";
		return h;
	}
	
	private static String mac(){
		Random r = new Random();
		String h = Integer.toHexString(r.nextInt(256));
		while(h.length()<2)h="0"+h;
		for(int e=0;e<5;e++){
			String j = Integer.toHexString(r.nextInt(256));
			while(j.length()<2)j="0"+j;
			h+=":"+j;
		}
		return h;
	}
}
