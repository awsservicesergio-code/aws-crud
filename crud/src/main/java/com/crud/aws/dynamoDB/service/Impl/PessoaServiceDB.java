package com.crud.aws.dynamoDB.service.Impl;

import com.crud.aws.dynamoDB.entity.Pessoa;
import com.crud.aws.dynamoDB.repository.PessoaRepository;
import com.crud.aws.dynamoDB.service.IPessoaServiceDB;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PessoaServiceDB implements IPessoaServiceDB {

    private final PessoaRepository pessoaRepository;

    /**
     * Método responsável save/update Pesso no DynamoDB.
     * @param pessoa
     */
    @Override
    public void savePessoa(Pessoa pessoa) {
        pessoaRepository.salvar(pessoa);
    }
}
