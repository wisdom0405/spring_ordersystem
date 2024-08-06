package com.beyond.ordersystem.product.Service;
import com.beyond.ordersystem.common.service.StockInventoryService;
import com.beyond.ordersystem.product.Repository.ProductRepository;
import com.beyond.ordersystem.product.domain.Product;
import com.beyond.ordersystem.product.dto.ProductResDto;
import com.beyond.ordersystem.product.dto.ProductSaveReqDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final S3Client s3Client;
    private final StockInventoryService stockInventoryService;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Autowired
    public ProductService(ProductRepository productRepository, S3Client s3Client, StockInventoryService stockInventoryService){
        this.productRepository = productRepository;
        this.s3Client = s3Client;
        this.stockInventoryService = stockInventoryService;
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

            if(dto.getName().contains("sale")){
                // 만약 "sale" 상태라면 redis-increaseStock 메소드 실행
                stockInventoryService.increaseStock(product.getId(), dto.getStockQuantity());
            }
        }catch (IOException e){
            throw new RuntimeException("이미지 저장실패");
        }
        return product;
    }

    public Product productAwsCreate(ProductSaveReqDto dto){
        MultipartFile image = dto.getProductImage();
        Product product = null;

        try{
            product = productRepository.save(dto.toEntity());
            byte[] bytes = image.getBytes();
            String fileName = product.getId() + "_" + image.getOriginalFilename();
            // 경로지정
            Path path = Paths.get("/Users/wisdom/Documents/GitHub/spring_ordersystem/src/main/java/com/beyond/ordersystem/product/tmp/",fileName);

            // local PC에 임시저장 : 파일 쓰기
            Files.write(path, bytes, StandardOpenOption.CREATE, StandardOpenOption.WRITE); // 해당경로에 bytes 저장

            // aws에 pc에 저장된 파일을 업로드
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileName)
                    .build();

            PutObjectResponse putObjectResponse = s3Client.putObject(putObjectRequest, RequestBody.fromFile(path)); // path에 담겨있는 file을 가져다가 upload
            String s3Path = s3Client.utilities().getUrl(a->a.bucket(bucket).key(fileName)).toExternalForm(); // 어디서든 접근가능한 Url형태로 path를 얻어냄
            product.updateImagePath(s3Path);

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
