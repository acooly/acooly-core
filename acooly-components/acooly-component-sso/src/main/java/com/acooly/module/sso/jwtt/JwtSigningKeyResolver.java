
package com.acooly.module.sso.jwtt;

import java.security.Key;

import javax.crypto.spec.SecretKeySpec;


import com.acooly.module.sso.dic.AuthConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SigningKeyResolverAdapter;

/**
 * @author shuijing
 */
public class JwtSigningKeyResolver extends SigningKeyResolverAdapter {

    @Override
    public Key resolveSigningKey(JwsHeader header, Claims claims) {
        SignatureAlgorithm alg = SignatureAlgorithm.forName(header.getAlgorithm());
        byte[] keyBytes = this.resolveSigningKeyBytes(header, claims);
        return new SecretKeySpec(keyBytes, alg.getJcaName());
    }

    @Override
    public byte[] resolveSigningKeyBytes(JwsHeader header, Claims claims) {
        return AuthConstants.SIGN_KEY.getBytes();
    }

}
