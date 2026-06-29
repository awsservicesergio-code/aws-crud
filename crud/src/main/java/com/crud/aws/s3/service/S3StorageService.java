package com.crud.aws.s3.service;

import com.crud.aws.dynamoDB.entity.Pessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;


@Service
@RequiredArgsConstructor
public class S3StorageService implements IS3StorageService {

    private final S3Client s3Client;

    @Override
    public void deleteFromS3(Pessoa pessoa) {
        s3Client.deleteObject(builder -> builder
                .bucket("pessoa-s3-local")
                .key(pessoa.getS3Key())
        );
    }
}
