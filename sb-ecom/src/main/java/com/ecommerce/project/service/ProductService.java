package com.ecommerce.project.service;


import com.ecommerce.project.exceptions.APIException;
import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.iCategoryRepository;
import com.ecommerce.project.repositories.iProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class ProductService implements iProductService{


    @Autowired
    private iProductRepository productRepository;
    @Autowired
    private iCategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${project.image}")
    private String path;


    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        //Chequear si el producto esta presente o no con el nombre.
        boolean isProductNotPresent = true;
        List<Product> products = category.getProducts();
        for(int i = 0; i<products.size(); i++) {
            if(products.get(i).getProductName().equals(productDTO.getProductName())) {
                isProductNotPresent = false;
                break;
            }
        }
        if(isProductNotPresent) {
            Product product = modelMapper.map(productDTO, Product.class);

            product.setImage("image.jpg");
            product.setCategory(category);

            double specialPrice = product.getPrice() * (1 - product.getDiscount() / 100.0);

            product.setSpecialPrice(specialPrice);

            Product savedProduct =  productRepository.save(product);

            return modelMapper.map(savedProduct, ProductDTO.class);
        }
        else {
            throw new APIException("Product already exist!");
        }
    }


    @Override
    public ProductResponse getAllProducts() {

        List<Product> products = productRepository.findAll();
        if(products.isEmpty())
            throw new APIException("No products Exist!");

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                        .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;

    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        //Importante. obtenemos todos los productos de esa categoría en específico.
        List<Product> products = productRepository.findByCategoryOrderByPriceAsc(category);

        if(products.isEmpty())
            throw new APIException("No products created till now.");

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductResponse searchProductByKeyword(String keyword) {

        List<Product> products = productRepository.findByProductNameLikeIgnoreCase('%' + keyword + '%');

        if(products.isEmpty())
            throw new APIException("No products created till now.");

        List<ProductDTO> productDTOS = products.stream()
                .map(product -> modelMapper.map(product,ProductDTO.class))
                .toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);

        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        Product product = modelMapper.map(productDTO,Product.class);

        productFromDb.setProductName(product.getProductName());
        productFromDb.setDescription(product.getDescription());
        productFromDb.setImage("image.jpg");
        productFromDb.setQuantity(product.getQuantity());
        productFromDb.setPrice(product.getPrice());
        productFromDb.setDiscount(product.getDiscount());
        productFromDb.setSpecialPrice(product.getSpecialPrice());

        Product updatedProduct = productRepository.save(productFromDb);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {

        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        productRepository.delete(productFromDb);

        return modelMapper.map(productFromDb,ProductDTO.class);

    }


    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        //Obtener el producto de la base de datos
        Product productFromDb = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product","productId",productId));

        //Subir la imagen al servidor y obtener el nombre de la imagen actualizada
        String fileName = fileService.uploadImage(path, image);

        //Actualizar el nombre del nuevo archivo al producto obtenido de la base de datos
        productFromDb.setImage(fileName);

        //Guardar el producto actualizado
        Product updatedProduct = productRepository.save(productFromDb);
        //Mappear el producto a DTO y retornarlo
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

}
