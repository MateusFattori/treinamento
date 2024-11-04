package br.com.fiap.treinamento;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.fiap.treinamento.model.Cliente;
import br.com.fiap.treinamento.repository.ClienteRepository;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class TreinamentoModelo implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public void run(String... args) throws Exception {
        // Criar atributos
        ArrayList<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("pontos")); // Pontos acumulados
        attributes.add(new Attribute("idade")); // Idade do cliente
        attributes.add(new Attribute("tempoFiliacao")); // Tempo de filiação em meses

        // Criar atributo de classe
        ArrayList<String> classValues = new ArrayList<>();
        classValues.add("frequente");
        classValues.add("inativo");
        classValues.add("novo");
        Attribute classAttribute = new Attribute("perfilCompra", classValues);
        attributes.add(classAttribute);

        // Criar conjunto de dados
        Instances dataset = new Instances("clientes", attributes, 0);
        dataset.setClassIndex(dataset.numAttributes() - 1); // Último atributo é a classe

        // Buscar clientes do banco de dados
        List<Cliente> clientes = clienteRepository.findAll();

        // Adicionar instâncias (dados de treinamento)
        for (Cliente cliente : clientes) {
            double[] valores = new double[dataset.numAttributes()];
            valores[0] = cliente.getPontos(); // Pontos do cliente

            // Calcular idade do cliente em anos
            int idade = LocalDate.now().getYear() - cliente.getDt_nascimento().getYear();
            valores[1] = idade;

            // Calcular o tempo de filiação em meses usando o campo dt_filiacao
            int tempoFiliacao = (int) java.time.temporal.ChronoUnit.MONTHS.between(cliente.getDt_filiacao(), LocalDate.now());
            valores[2] = tempoFiliacao;

            // Determinar o valor da classe com base na fidelidade e outros critérios
            String perfilCompra = determinePerfilCompra(cliente, idade, tempoFiliacao, cliente.getPontos());
            if (classValues.contains(perfilCompra)) {
                valores[3] = classValues.indexOf(perfilCompra);
            } else {
                System.out.println("Valor de perfilCompra inválido para o cliente: " + cliente.getNome());
                continue; // Ignorar clientes com valor de perfilCompra inválido
            }

            Instance instance = new DenseInstance(1.0, valores);
            dataset.add(instance);
        }

        // Treinar o classificador
        Classifier classifier = new J48(); // Criar o classificador
        classifier.buildClassifier(dataset); // Treinar com o conjunto de dados

        // Salvar o modelo em um arquivo
        SerializationHelper.write("modeloTreinado.model", classifier);
        System.out.println("Modelo treinado e salvo com sucesso!");
    }

    private String determinePerfilCompra(Cliente cliente, int idade, int tempoFiliacao, int pontos) {
        // Exemplo de lógica para determinar perfil de compra (pode ser ajustada conforme necessário)
        if (pontos > 500 && tempoFiliacao > 12) {
            return "frequente";
        } else if (tempoFiliacao > 6 && pontos < 50) {
            return "inativo";
        } else {
            return "novo";
        }
    }
}
