import cryptolib.CryptoSocketInterface;
import cryptolib.CryptoSocketInterface.Channel;
import cryptolib.CryptoSocketInterface.ChannelType;
import cryptolib.CryptoSocket;
import cryptolib.SecureDataSocket;
import java.util.Arrays;

public class SecureDataSocketClientQR{
	public static void main(String [] args){
		try{
			SecureDataSocket cs = new SecureDataSocket(4711);
			cs.setupClientWithCamera("192.168.178.34:4711:4Viw9UKdulNLSaxMujQ2/h9vogsfWp+d9D56XVJkYSk=");
			/*if (!test){
				System.out.println("Failed!");
				return;
			}*/

			byte[] testb = cs.read();
			System.out.println("Read!");
			System.out.println(new String(testb));
			//cs.verifiedOOB();
			System.out.println("Write: "+cs.write("Hallo"));
			int i = cs.readInt();
			System.out.println("Read!");
			System.out.println(i);
			System.out.println("Write: "+cs.write(3.45f));
			System.out.println("Read!");
			System.out.println(cs.readDouble());
			System.out.println("Write: "+cs.write(new SerializationObject(5)));
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
