package uz.auth.auth.dto;

import lombok.Data;

@Data
public class DivisionDTO {
    private Long id;
    private String name;
    private String shortName;
    private String index;
    private boolean delete;
}