package studio.blockops.vyom.core.crypto.secp256k1;

import org.bouncycastle.asn1.sec.SECNamedCurves;
import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.params.ECDomainParameters;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.assistedinject.FactoryModuleBuilder;

import studio.blockops.vyom.core.crypto.Curve;

public class SecP256K1Module extends AbstractModule {
	
	@Override
	protected void configure() {
		
	}
	
	@Provides @Singleton
	Curve provideCurve() {
		final X9ECParameters params = SECNamedCurves.getByName("secp256k1");
		final ECDomainParameters ecParams = new ECDomainParameters(params.getCurve(), params.getG(), params.getN(), params.getH());
		return new SecP256K1Curve(ecParams, ecParams.getN().shiftRight(1));
	}

}
