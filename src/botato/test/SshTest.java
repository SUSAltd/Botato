package botato.test;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;


public class SshTest {

	/**
	 * @param args
	 * @throws JSchException 
	 * @throws SftpException 
	 */
	public static void main(String[] args) throws JSchException, SftpException {
		String user = "susaltd";
		String host = "vergil.u.washington.edu";
		String password = "theg4me!-washington";
		int port = 22;
		
		String fileIn = "/nfs/bronfs/uwfs/dw00/d25/susaltd/fish/fish-irc.rizon.net-#lelandcs.json";
		String fileOut = "/nfs/bronfs/uwfs/dw00/d25/susaltd/fish/test.txt";
		
		JSch jsch = new JSch();
		Session session = jsch.getSession(user, host, port);
		session.setPassword(password);
        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();
        ChannelSftp sftpChannel = (ChannelSftp) session.openChannel("sftp");
		sftpChannel.connect();
        
		InputStream in = sftpChannel.get(fileIn);
		Scanner input = new Scanner(in);
		while (input.hasNextLine()) {
			System.out.println(input.nextLine());
		}
		input.close();
		
		OutputStream out = sftpChannel.put(fileOut);
		PrintStream output = new PrintStream(out);
		output.println("COME BACK BOTATO, WE AWAIT YOU");
		output.close();
	}

}
