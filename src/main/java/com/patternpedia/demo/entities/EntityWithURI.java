package com.patternpedia.demo.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@MappedSuperclass
@Data
@NoArgsConstructor
class EntityWithURI {

    @Id
    private String encodedUri;

    private String uri;

    private String name;

    EntityWithURI(String uri, String name) throws UnsupportedEncodingException {
        this.encodedUri = URLEncoder.encode(URLEncoder.encode(uri, StandardCharsets.UTF_8.toString()), StandardCharsets.UTF_8.toString());
        this.uri = uri;
        this.name = name;
    }

}