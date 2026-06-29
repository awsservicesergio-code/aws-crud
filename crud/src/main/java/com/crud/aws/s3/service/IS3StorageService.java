package com.crud.aws.s3.service;

import com.crud.aws.dynamoDB.entity.Pessoa;

public interface IS3StorageService {
    void deleteFromS3(Pessoa pessoa);
}
