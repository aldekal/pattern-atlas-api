package com.patternpedia.api.entities;

import lombok.*;

import org.hibernate.annotations.GenericGenerator;


import javax.persistence.*;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private UUID id;

    private String fileName;

    private String fileType;

    @Lob
    private byte[] data;
}
