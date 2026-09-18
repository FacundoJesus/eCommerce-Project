package com.ecommerce.project.payload;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
//Solicitud de categoría
public class CategoryDTO {

    @Schema(description = "Category ID of a particular category", example = "101") //Para schemas en Swagger
    private Long categoryId;
    @Schema(description = "Category Name for category you wish to create", example= "Laptops")
    private String categoryName;


}
