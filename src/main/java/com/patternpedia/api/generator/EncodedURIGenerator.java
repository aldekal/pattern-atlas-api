package com.patternpedia.api.generator;

import com.patternpedia.api.entities.EntityWithURI;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class EncodedURIGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        try {
            return URLEncoder.encode(URLEncoder.encode(((EntityWithURI) o).getUri(), StandardCharsets.UTF_8.toString()), StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            throw new HibernateException(e);
        }
    }

}
