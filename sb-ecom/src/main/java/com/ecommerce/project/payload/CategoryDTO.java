package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

//Solicitud de categoría
public class CategoryDTO {

    private Long categoryId;
    private String categoryName;

}
