package com.example.CloudKeeper.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "storage")
public class File {

    public File(String name, String type, byte[] data, long size) {
        this.name = name;
        this.type = type;
        this.data = data;
        this.size = size;
    }

    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @JsonProperty("filename")
    @Column
    private String name;

    @JsonIgnore
    @Column
    private String type;

    @JsonIgnore
    @Lob
    private byte[] data;

    @Column
    Long size;

    @JsonIgnore
    @Column
    Long userId;

}
