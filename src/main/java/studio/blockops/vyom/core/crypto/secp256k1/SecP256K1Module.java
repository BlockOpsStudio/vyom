package studio.blockops.vyom.core.crypto.secp256k1;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import studio.blockops.vyom.core.crypto.BlockCipher;
import studio.blockops.vyom.core.crypto.CryptoEngine;
import studio.blockops.vyom.core.crypto.Curve;
import studio.blockops.vyom.core.crypto.KeyAnalyzer;
import studio.blockops.vyom.core.crypto.KeyGenerator;
import studio.blockops.vyom.core.crypto.Signer;

public class SecP256K1Module extends AbstractModule {
	
	@Override
	protected void configure() {
		install(new FactoryModuleBuilder()
			.implement(Signer.class, SecP256K1Signer.class)
			.implement(KeyGenerator.class, SecP256K1KeyGenerator.class)
			.implement(BlockCipher.class, SecP256K1BlockCipher.class)
			.implement(KeyAnalyzer.class, SecP256K1KeyAnalyzer.class)
			.build(CryptoEngine.class));
	}
	
	@Provides @Singleton
	Curve provideCurve() {
		final X9ECParameters params = SECNamedCurves.getByName("secp256k1");
		final ECDomainParameters ecParams = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
		return new SecP256K1Curve(ecParams, ecParams.getN().shiftRight(1));
	}

}
