package com.example.demo.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {

    private Integer id;
    private String account;
    private String name;
    private String position;
    private String email;
    private Boolean enabled;
}
