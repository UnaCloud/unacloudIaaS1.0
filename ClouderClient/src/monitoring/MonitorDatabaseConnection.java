/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoring;

import com.losandes.communication.messages.monitoring.MonitorInitialReport;
import com.losandes.communication.messages.monitoring.MonitorReport;
import com.losandes.utils.VariableManager;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jcadavid
 */
public class MonitorDatabaseConnection {

    private MonitorDatabaseConnection() {
    }
    private static Connection generateConnection() throws SQLException {
        Connection conexion;
        String ip = VariableManager.getStringValue("MONITORING_SERVER_IP");
        String name = VariableManager.getStringValue("MONITORING_DATABASE_NAME");
        String user = VariableManager.getStringValue("MONITORING_DATABASE_USER");
        String password = VariableManager.getStringValue("MONITORING_DATABASE_PASSWORD");
        String url = "jdbc:mysql://" + ip + ":3306/" + name;
        System.out.println(url);
        conexion = DriverManager.getConnection(url, user, password);
        conexion.setAutoCommit(false);
        return conexion;
    }

    public static void doInitialReport(MonitorInitialReport initialReport) {
        final String insertQuery = "INSERT INTO "
                + "Nodes "
                + "(UUID,Date,HostName,DomainName,OSName,OSVersion,OSArchitect"
                + ",CPUModel,CPUVendor,CPUCores,CPUSockets,CPUMhz,CPUCoresXSocket"
                + ",RAMMemorySize,SwapMemorySize,HDSpace,HDFileSystem"
                + ",NETMACAddress) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection con = generateConnection()) {
            try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
                pstmt.setString(1, initialReport.getUUID());
                pstmt.setTimestamp(2, initialReport.getTimest());
                pstmt.setString(3, initialReport.getHostname());
                pstmt.setString(4, initialReport.getDomain());
                pstmt.setString(5, initialReport.getOperatingSystemName());
                pstmt.setString(6, initialReport.getOperatingSystemVersion());
                pstmt.setString(7, initialReport.getOperatingSystemArchitect());
                pstmt.setString(8, initialReport.getcPUModel());
                pstmt.setString(9, initialReport.getcPUVendor());
                pstmt.setInt(10, initialReport.getcPUCores());
                pstmt.setInt(11, initialReport.getTotalSockets());
                pstmt.setString(12, initialReport.getcPUMhz());
                pstmt.setInt(13, initialReport.getCoresPerSocket());
                pstmt.setFloat(14, initialReport.getrAMMemorySize());
                pstmt.setFloat(15, initialReport.getSwapMemorySize());
                pstmt.setFloat(16, initialReport.getHardDiskSpace());
                pstmt.setString(17, initialReport.getHardDiskFileSystem());
                pstmt.setString(18, initialReport.getNetworkMACAddress());
                pstmt.executeUpdate();
                
            }
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(MonitorDatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(initialReport.getcPUModel().length()+" "+initialReport.getcPUModel());
    }

    public static void doBatchReport(MonitorReport... statusReports) {
        int[] results=null;
        final String insertQuery = "INSERT INTO "
                + "ResourcesMonitoring"
                + "(UUID,Id,Date,Counter,UserName,UpTime,CPUMflops,CPUMflopsTime"
                + ",CPUIdlePct,CPUUsedPct,CPUUserPct,CPUKernelPct,CPUNicePct,CPUWaitPct"
                + ",CPUCombinedPct,CPUUserTime,CPUKernelTime,CPUNiceTime,CPUWaitTime"
                + ",CPUIdleTime,RAMMemoryFree,RAMMemoryUsed,RAMMemoryFreePct,RAMMemoryUsedPct"
                + ",SwapMemoryFree,SwapMemoryPageIn,SwapMemoryPageOut,SwapMemoryUsed"
                + ",HDFreeSpace,HDUsedSpace,NETIPAddress,NETInterface,NETNetmask"
                + ",NETGateway,NETRxBytes,NETTxBytes,NETSpeed,NETRxErrors,NETTxErrors"
                + ",NETRxPackets,NETTxPackets) VALUES (?,?,?,?,?,?,?,?,"
                + "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try(PrintWriter pw=new PrintWriter(new FileOutputStream("monErr.txt",true))){
                pw.println("Start");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(MonitorDatabaseConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
        try (Connection con = generateConnection()) {
            try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
                for (MonitorReport statusReport : statusReports)if(statusReport!=null){
                    String ID = statusReport.getUserName() + '#' + statusReport.getTimest().getTime();
                    pstmt.setString(1, statusReport.getUUID());
                    pstmt.setString(2, ID);
                    pstmt.setTimestamp(3, statusReport.getTimest());
                    pstmt.setInt(4, statusReport.getContadorRegistros());
                    pstmt.setString(5, statusReport.getUserName());
                    pstmt.setDouble(6, statusReport.getUptime());
                    pstmt.setDouble(7, statusReport.getMflops());
                    pstmt.setDouble(8, statusReport.getTimeinSecs());
                    pstmt.setDouble(9, statusReport.getIdle());
                    pstmt.setDouble(10, statusReport.getD());
                    pstmt.setDouble(11, statusReport.getCPuser());
                    pstmt.setDouble(12, statusReport.getSys());
                    pstmt.setDouble(13, statusReport.getNice());
                    pstmt.setDouble(14, statusReport.getWait());
                    pstmt.setDouble(15, statusReport.getCombined());
                    pstmt.setLong(16, statusReport.getUser());
                    pstmt.setLong(17, statusReport.getSys0());
                    pstmt.setLong(18, statusReport.getNice0());
                    pstmt.setLong(19, statusReport.getWait0());
                    pstmt.setLong(20, statusReport.getIdle0());
                    pstmt.setFloat(21, statusReport.getrAMMemoryFree());
                    pstmt.setFloat(22, statusReport.getrAMMemoryUsed());
                    pstmt.setDouble(23, statusReport.getFreePercent());
                    pstmt.setDouble(24, statusReport.getUsedPercent());
                    pstmt.setFloat(25, statusReport.getSwapMemoryFree());
                    pstmt.setFloat(26, statusReport.getSwapMemoryPageIn());
                    pstmt.setFloat(27, statusReport.getSwapMemoryPageOut());
                    pstmt.setFloat(28, statusReport.getSwapMemoryUsed());
                    pstmt.setLong(29, statusReport.getHardDiskFreeSpace());
                    pstmt.setLong(30, statusReport.getHardDiskUsedSpace());
                    pstmt.setString(31, statusReport.getNetworkIPAddress());
                    pstmt.setString(32, statusReport.getNetworkInterface());
                    pstmt.setString(33, statusReport.getNetworkNetmask());
                    pstmt.setString(34, statusReport.getNetworkGateway());
                    pstmt.setLong(35, statusReport.getRxBytes());
                    pstmt.setLong(36, statusReport.getTxBytes());
                    pstmt.setLong(37, statusReport.getSpeed());
                    pstmt.setLong(38, statusReport.getRxErrors());
                    pstmt.setLong(39, statusReport.getTxErrors());
                    pstmt.setLong(40, statusReport.getRxPackets());
                    pstmt.setLong(41, statusReport.getTxPackets());
                    pstmt.addBatch();
                }
                results=pstmt.executeBatch();
                con.commit();
            }
        } catch (SQLException ex) {
            try(PrintWriter pw=new PrintWriter(new FileOutputStream("monErr.txt",true))){
                ex.printStackTrace(pw);
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(MonitorDatabaseConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
            Logger.getLogger(MonitorDatabaseConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
        try(PrintWriter pw=new PrintWriter(new FileOutputStream("monErr.txt",true))){
                pw.println("Done");
            } catch (FileNotFoundException ex1) {
                Logger.getLogger(MonitorDatabaseConnection.class.getName()).log(Level.SEVERE, null, ex1);
            }
        checkUpdateCounts(results);
    }

    private static void checkUpdateCounts(int[] updateCounts) {
        if(updateCounts==null)return;
        for (int i = 0; i < updateCounts.length; i++) {
            if (updateCounts[i] >= 0) {
                //System.out.println("OK; updateCount="+updateCounts[i]);
            } else if (updateCounts[i] == Statement.SUCCESS_NO_INFO) {
                System.out.println("OK; updateCount=Statement.SUCCESS_NO_INFO");
            } else if (updateCounts[i] == Statement.EXECUTE_FAILED) {
                System.out.println("Failure; updateCount=Statement.EXECUTE_FAILED");
            }
        }
    }
}
