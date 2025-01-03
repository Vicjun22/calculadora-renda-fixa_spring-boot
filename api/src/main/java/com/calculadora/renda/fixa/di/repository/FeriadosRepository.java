package com.calculadora.renda.fixa.di.repository;

import com.calculadora.renda.fixa.di.domain.model.FeriadoModel;
import com.calculadora.renda.fixa.di.util.SQLiteUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class FeriadosRepository {

    private final SQLiteUtil db;

    public FeriadosRepository(@Value("${database.file.path}") String databaseFile) {
        this.db = new SQLiteUtil(databaseFile);
        initializeDatabase();
    }

    private void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS feriados (
                date TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                type TEXT NOT NULL
            );
        """;
        try {
            db.executeUpdate(createTableSQL);
        } catch (SQLException e) {
            throw new RuntimeException("Ocorreu um erro ao inicializar base de dados para Feriados.");
        }
    }

    public void inserirFeriadosEmLote(List<FeriadoModel> feriados, String ano) {
        String insertSQL = "INSERT OR IGNORE INTO feriados (date, name, type) VALUES (?, ?, ?);";

        try (Connection connection = db.connect();
             PreparedStatement statement = connection.prepareStatement(insertSQL)) {

            connection.setAutoCommit(false);

            for (FeriadoModel model : feriados) {
                statement.setString(1, model.getDate().toString());
                statement.setString(2, model.getName());
                statement.setString(3, model.getType());
                statement.addBatch();
            }

            statement.executeBatch();
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao inserir feriados referente ao ano de " + ano + ".");
        }
    }

    public List<FeriadoModel> listarFeriadosPorAno(String ano) {
        String query = "SELECT * FROM feriados WHERE strftime('%Y', date) = ? ORDER BY date;";
        List<FeriadoModel> feriados = new ArrayList<>();

        try (Connection connection = db.connect();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, ano);

            try (ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    FeriadoModel model = new FeriadoModel();
                    model.setDate(LocalDate.parse(rs.getString("date")));
                    model.setName(rs.getString("name"));
                    model.setType(rs.getString("type"));
                    feriados.add(model);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar feriados do ano " + ano + ".", e);
        }

        return feriados;
    }
}
