package com.maximsachok.author_identification_demo.Dto;

public class AuthorDto {
    private Long id;

    public AuthorDto(Long id) {
        this.id = id;
    }

    public AuthorDto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "AuthorDto{" +
                "id=" + id +
                '}';
    }
}
