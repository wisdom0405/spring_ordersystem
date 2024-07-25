package com.beyond.ordersystem.product.Service;
import com.beyond.ordersystem.product.Repository.ProductRepository;
import com.beyond.ordersystem.product.domain.Product;
import com.beyond.ordersystem.product.dto.ProductResDto;
import com.beyond.ordersystem.product.dto.ProductSaveReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public Product productCreate(ProductSaveReqDto dto){
        MultipartFile image = dto.getProductImage();
        Product product = null;
        try{
            product = productRepository.save(dto.toEntity());
            byte[] bytes = image.getBytes();
            // 경로지정
            Path path = Paths.get("/Users/wisdom/Documents/GitHub/spring_ordersystem/src/main/java/com/beyond/ordersystem/product/tmp/",
                    product.getId() +"_"+
                    image.getOriginalFilename());
            // 파일 쓰기
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE); // 해당경로에 bytes 저장
            product.updateImagePath(path.toString());

        }catch (IOException e){
            throw new RuntimeException("이미지 저장실패");
        }
        return product;
    }

    public Page<ProductResDto> productList(Pageable pageable){
        Page<Product> products = productRepository.findAll(pageable);
        Page<ProductResDto> productResDtos = products.map(a->a.fromEntity());
        return productResDtos;
    }


}
