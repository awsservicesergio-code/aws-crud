package com.crud.aws.dynamoDB.repository;

import com.crud.aws.dynamoDB.entity.Pessoa;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.enhanced.dynamodb.*;

@Repository
public class PessoaRepository {

    private final DynamoDbTable<Pessoa> table;

    /**
     * Método responsável por obter os dados da tabela Pessoa para o DynamoDB.
     * @param enhancedClient
     */
    public PessoaRepository(DynamoDbEnhancedClient enhancedClient) {
        this.table = enhancedClient.table("Pessoa", TableSchema.fromBean(Pessoa.class));
    }

    /**
     * Método responsável por save/update Pessoa no DynamoDB.
     * @param pessoa
     */
    public void salvar(Pessoa pessoa) {
        table.putItem(pessoa);
    }

    /**
     * Método responsável por buscar por cpf a pessoa no DynamoDB.
     * @param cpf
     * @return
     */
    public Pessoa buscarPorCpf(String cpf) {
        return table.getItem(r -> r.key(k -> k.partitionValue(cpf)));
    }

    /**
     * Método responsável por delete Pessoa pelo cpf no DynamoDB.
     * @param cpf
     */
    public void deletar(String cpf) {
        table.deleteItem(r -> r.key(k -> k.partitionValue(cpf)));
    }

}
