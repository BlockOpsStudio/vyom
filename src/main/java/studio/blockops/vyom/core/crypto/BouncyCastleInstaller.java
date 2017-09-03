package studio.blockops.vyom.core.crypto;

import java.security.Provider;
import java.security.Security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class BouncyCastleInstaller implements com.google.inject.Provider<Provider> {
	
	@Override
	public Provider get() {
        Provider provider = new BouncyCastleProvider();
            
        provider.put("MessageDigest.ETH-KECCAK-256", "org.ethereum.crypto.cryptohash.Keccak256");
        provider.put("MessageDigest.ETH-KECCAK-512", "org.ethereum.crypto.cryptohash.Keccak512");

        Security.addProvider(provider);
        
        return provider;
	}

}
