package uz.forall.telephone.payload;

import lombok.Data;

@Data
public class CategoryUpdateDto {
    private String name;
    private Long parentId;
}
