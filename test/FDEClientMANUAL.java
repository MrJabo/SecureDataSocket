import cryptolib.CryptoSocketInterface;
import cryptolib.CryptoSocketInterface.Channel;
import cryptolib.CryptoSocketInterface.ChannelType;
import cryptolib.CryptoSocket;
import cryptolib.FDESocket;
import java.util.Arrays;

public class FDEClientMANUAL{
	public static void main(String [] args){
		try{
			/*FDESocket cs = new FDESocket(new Channel(ChannelType.MANUAL, "127.0.0.1:4711"));
			//never call setSharedSecret on both sides
			cs.setSharedSecret("00000000000000000000000000000000".getBytes());
			boolean test = cs.connect();
			if (!test){
				System.out.println("Failed!");
				return;
			}

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
			System.out.println("Write: "+cs.write(new SerializationObject(5)));*/
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
