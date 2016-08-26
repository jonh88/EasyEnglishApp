package com.jonh.easyenglish.Util;

//import com.jonh.easyenglish.domain.Token;
import com.nimbusds.jose.JOSEException;
//import com.nimbusds.jose.JWSAlgorithm;
//import com.nimbusds.jose.JWSHeader;
//import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
//import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
//import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
/**
 * Created by jonh on 16/08/16.
 */
public class TokenManager {
    private static final byte [] secret = "[B@70306774P@sSWord32EaSyenglisH".getBytes();

    public TokenManager(){}

    public int getUserFromToken(String token){

        // On the consumer side, parse the JWS and verify its HMAC
        SignedJWT signedJWTClient;
        try {
            signedJWTClient = SignedJWT.parse(token);

            JWSVerifier verifier = new MACVerifier(TokenManager.secret);
            if(!signedJWTClient.verify(verifier)){
                return -2;
            }
            int idUser = Integer.parseInt(signedJWTClient.getJWTClaimsSet().getSubject());

            return idUser;
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        } catch (JOSEException e) {
            e.printStackTrace();
            return -1;
        }

    }
}
