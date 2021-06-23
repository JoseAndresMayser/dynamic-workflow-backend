package bo.dynamicworkflow.dynamicworkflowbackend.app.access.token;

import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.dto.TokenRequest;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.dto.TokenResponse;
import bo.dynamicworkflow.dynamicworkflowbackend.app.access.token.exceptions.GenerateTokenException;
import com.google.gson.Gson;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Component
public class TokenManager implements Serializable {

    private final RsaKeyManager rsaKeyManager;

    public TokenManager(RsaKeyManager rsaKeyManager) {
        this.rsaKeyManager = rsaKeyManager;
    }

    public TokenResponse generateTokenResponse(TokenRequest<?> tokenRequest) throws GenerateTokenException {
        try {
            RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) rsaKeyManager.getPrivateKey();
            JWSSigner jwsSigner = new RSASSASigner(rsaPrivateKey);

            Calendar calendar = Calendar.getInstance();
            calendar.add(tokenRequest.getTimeInstanceType().getValue(), tokenRequest.getTokenAvailabilityTime());

            String username = tokenRequest.getUsername();
            String accountJson = new Gson().toJson(tokenRequest.getAccount());
            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(username)
                    .issuer(ISSUER)
                    .expirationTime(calendar.getTime())
                    .notBeforeTime(new Date())
                    .issueTime(new Date())
                    .jwtID(UUID.randomUUID().toString())
                    .claim(ACCOUNT_KEY, accountJson)
                    .build();

            JWSObject jwsObject =
                    new JWSObject(new JWSHeader(JWSAlgorithm.RS256), new Payload(claimsSet.toJSONObject()));
            jwsObject.sign(jwsSigner);

            return new TokenResponse(username, jwsObject.serialize(), TOKEN_TYPE, calendar.getTime());
        } catch (Exception e) {
            e.printStackTrace();
            throw new GenerateTokenException("Ocurrió un error al generar el Token: ".concat(e.getMessage()), e);
        }
    }

    public Boolean verifyToken(String token) {
        try {
            return (!isTokenExpired(token) && verifyIntegrity(token));
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean isTokenExpired(String token) throws ParseException {
        Date expirationDate = getExpirationDateFromToken(token);
        return expirationDate.before(new Date());
    }

    public Date getExpirationDateFromToken(String token) throws ParseException {
        JWTClaimsSet jwtClaimsSet = getClaimSetFromToken(token);
        return jwtClaimsSet.getExpirationTime();
    }

    public JWTClaimsSet getClaimSetFromToken(String token) throws ParseException {
        SignedJWT signedJWT = parseToken(token);
        return signedJWT.getJWTClaimsSet();
    }

    private SignedJWT parseToken(String token) throws ParseException {
        return SignedJWT.parse(token);
    }

    private boolean verifyIntegrity(String token) throws ParseException, JOSEException {
        SignedJWT signedJWT = parseToken(token);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) rsaKeyManager.getPublicKey();
        JWSVerifier verifier = new RSASSAVerifier(rsaPublicKey);
        return signedJWT.verify(verifier);
    }

    public <Account> Account getAccountFromToken(String token, Class<Account> accountClass) throws ParseException {
        JWTClaimsSet claimsSet = getClaimSetFromToken(token);
        String accountString = claimsSet.getClaim("account").toString();
        return new Gson().fromJson(accountString, accountClass);
    }

    private static final String ISSUER = "web";
    private static final String ACCOUNT_KEY = "account";
    private static final String TOKEN_TYPE = "Bearer";

}