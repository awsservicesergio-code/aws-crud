package com.crud.aws.dynamoDB.service;

import com.crud.aws.dynamoDB.entity.Pessoa;

public interface IPessoaServiceDB {
    void savePessoa(Pessoa pessoa);
    Pessoa buscarPessoaPorCpf(String cpf);
}
