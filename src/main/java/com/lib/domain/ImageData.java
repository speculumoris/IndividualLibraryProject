package com.lib.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "t_imagedata")
public class ImageData {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Lob
    private byte[] data;

    public ImageData(Long id) {
        this.id = id;
    }

    public ImageData(byte[] data) {
        this.data = data;
    }

}
