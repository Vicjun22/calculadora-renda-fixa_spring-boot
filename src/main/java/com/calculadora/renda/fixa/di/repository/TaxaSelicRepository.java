package com.calculadora.renda.fixa.di.repository;

import com.calculadora.renda.fixa.di.domain.model.TaxaSelicModel;
import com.calculadora.renda.fixa.di.util.SQLiteUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class TaxaSelicRepository {

    private final SQLiteUtil db;

    public TaxaSelicRepository(@Value("${database.file.path}") String databaseFile) {
        this.db = new SQLiteUtil(databaseFile);
        initializeDatabase();
    }

    private void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS selic (
                data TEXT PRIMARY KEY,
                valor TEXT NOT NULL
            );
        """;
        try {
            db.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Ocorreu um erro ao inicializar base de dados para verificar as Taxas Selic.");
        }
    }

    public void inserirTaxasEmLote(List<TaxaSelicModel> taxas) {
        String insertSQL = "INSERT OR IGNORE INTO selic (data, valor) VALUES (?, ?);";

        try (Connection connection = db.connect();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            connection.setAutoCommit(false);

            for (TaxaSelicModel model : taxas) {
                statement.setString(1, model.getData());
                statement.setString(2, model.getValor());
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir dados referentes à taxa selic");
        }
    }

    public TaxaSelicModel consultarUltimaDataInserida() {
        String query = "SELECT * FROM selic ORDER BY data DESC LIMIT 1;";
        TaxaSelicModel taxaSelic = null;

        try (Connection connection = db.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    TaxaSelicModel model = new TaxaSelicModel();
                    model.setData(rs.getString("data"));
                    model.setValor(rs.getString("valor"));
                    taxaSelic = model;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar taxas Selic no período de tempo", e);
        }

        return taxaSelic;
    }

    public List<TaxaSelicModel> listarTaxasPorPeriodo(String dataInicial, String dataFinal) {
        String query = "SELECT * FROM selic WHERE data >= ? AND data <= ? ORDER BY data;";
        List<TaxaSelicModel> taxaSelic = new ArrayList<>();

        try (Connection connection = db.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, dataInicial);
            statement.setString(2, dataFinal);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    TaxaSelicModel model = new TaxaSelicModel();
                    model.setData(rs.getString("data"));
                    model.setValor(rs.getString("valor"));
                    taxaSelic.add(model);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar taxas Selic no período de tempo", e);
        }

        return taxaSelic;
    }
}
