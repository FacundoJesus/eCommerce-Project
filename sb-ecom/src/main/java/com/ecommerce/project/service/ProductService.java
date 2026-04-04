package com.ecommerce.project.service;


import com.ecommerce.project.exceptions.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repositories.iCategoryRepository;
import com.ecommerce.project.repositories.iProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProductService implements iProductService{


    @Autowired
    private iProductRepository productRepository;
    @Autowired
    private iCategoryRepository categoryRepository;
    @Autowired
    private ModelMapper modelMapper;


    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category","categoryId",categoryId));

        Product product = modelMapper.map(productDTO, Product.class);

        product.setImage("image.jpg");
        product.setCategory(category);

        double specialPrice = product.getPrice() * (1 - product.getDiscount() / 100.0);

        product.setSpecialPrice(specialPrice);

        Product savedProduct =  productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);

    }

    @Override
    public ProductResponse getAllProducts() {

        List<Product> products = productRepository.findAll();

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
        String path = "images/";
        String fileName = uploadImage(path, image);

        //Actualizar el nombre del nuevo archivo al producto obtenido de la base de datos
        productFromDb.setImage(fileName);

        //Guardar el producto actualizado
        Product updatedProduct = productRepository.save(productFromDb);
        //Mappear el producto a DTO y retornarlo
        return modelMapper.map(updatedProduct,ProductDTO.class);
    }

    private String uploadImage(String path, MultipartFile file) throws IOException {
        //Obtener nombre del archivo original
        String originalFileName = file.getOriginalFilename();

        //nombrar el archivo de forma unica para evitar conflictos de nombres. (Generar un nombre unico)
        String randomId = UUID.randomUUID().toString();
        //mat.jpg --> 1234 --> 1234.jpg
        String fileName = randomId.concat(originalFileName.substring(originalFileName.lastIndexOf('.')));
        String filePath = path + File.separator + fileName;

        //Comprobar si la rutha existe y crearla
        File folder = new File(path);
        if(!folder.exists())
            folder.mkdir();

        //proceso de carga al servidor
        Files.copy(file.getInputStream(), Paths.get(filePath));

        //Retornar el nombre del archivo
        return fileName;

    }

}
