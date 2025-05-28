package DBlogic;

import clientLog.ClientHandler;
import clientLog.PasswordHandler;
import managers.CollectionManager;
import models.*;

import java.security.SecureRandom;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class PostgreSQLManager implements DBmanager {
    public static void createBase() {
        String creationQuery = """
        -- Создаем последовательности
        CREATE SEQUENCE IF NOT EXISTS coordinates_seq INCREMENT BY 1 START WITH 1;
        CREATE SEQUENCE IF NOT EXISTS labwork_seq INCREMENT BY 1 START WITH 1;
        
        -- Таблица пользователей
        CREATE TABLE IF NOT EXISTS users (
            id BIGINT PRIMARY KEY DEFAULT nextval('labwork_seq'),
            name VARCHAR(255) NOT NULL UNIQUE,
            passwd_hash TEXT NOT NULL,
            passwd_salt TEXT NOT NULL
        );
        
        -- Таблица координат
        CREATE TABLE IF NOT EXISTS coordinates (
            id BIGINT PRIMARY KEY DEFAULT nextval('coordinates_seq'),
            x BIGINT NOT NULL,
            y DOUBLE PRECISION NOT NULL
        );
        
        -- ENUM-типы
        DO $$
        BEGIN
            IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'difficulty_enum') THEN
                CREATE TYPE difficulty_enum AS ENUM (
                    'EASY', 'INSANE', 'VERY_HARD', 'HOPELESS', 'TERRIBLE'
                );
            END IF;
            
            IF NOT EXISTS (SELECT 1 FROM pg_type WHERE typname = 'country_enum') THEN
                CREATE TYPE country_enum AS ENUM (
                    'UNITED_KINGDOM', 'CHINA', 'INDIA', 'SOUTH_KOREA', 'NORTH_KOREA'
                );
            END IF;
        END$$;
        
        -- Таблица лабораторных работ
        CREATE TABLE IF NOT EXISTS labwork (
            id BIGINT PRIMARY KEY DEFAULT nextval('labwork_seq'),
            name TEXT NOT NULL,
            coordinates_id BIGINT NOT NULL REFERENCES coordinates(id),
            creation_date TIMESTAMP WITH TIME ZONE NOT NULL,
            minimal_point BIGINT NOT NULL CHECK (minimal_point > 0),
            description TEXT,
            difficulty difficulty_enum NOT NULL,
            author TEXT NOT NULL,
            birthday DATE NOT NULL,
            height DOUBLE PRECISION CHECK (height > 0),
            nationality country_enum
        );
        
        -- Таблица создателей
        CREATE TABLE IF NOT EXISTS creator (
            user_id BIGINT NOT NULL REFERENCES users(id),
            labwork_id BIGINT NOT NULL REFERENCES labwork(id),
            PRIMARY KEY (user_id, labwork_id)
        );
        
        -- Индексы
        CREATE INDEX IF NOT EXISTS idx_labwork_name ON labwork(name);
        CREATE INDEX IF NOT EXISTS idx_user_name ON users(name);
        """;

        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(creationQuery);
            Logger.getLogger(PostgreSQLManager.class.getName()).info("Database tables created successfully");

        } catch (SQLException e) {
            Logger.getLogger(PostgreSQLManager.class.getName()).severe("Error creating database: " + e.getMessage());
            throw new RuntimeException("Failed to initialize database", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Vector<LabWork> getCollectionFromDB() {
        Vector<LabWork> data = new Vector<>();

        try (Connection connection = ConnectionFactory.getConnection();
             Statement statement = connection.createStatement()) {

            String query = "SELECT l.*, co.x, co.y " +
                    "FROM labwork l " +
                    "JOIN coordinates co ON l.coordinates_id = co.id";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                Coordinates coordinates = new Coordinates(resultSet.getInt("x"), resultSet.getInt("y"));
                ZonedDateTime creationDate = resultSet.getObject("creation_date", ZonedDateTime.class);
                long minimalpoint = resultSet.getLong("minimal_point");
                String description = resultSet.getString("description");
                Difficulty difficulty = Difficulty.valueOf(resultSet.getString("difficulty"));
                String author = resultSet.getString("author");
                LocalDate birthday = resultSet.getDate("birthday").toLocalDate();
                Double height = resultSet.getDouble("height");
                Country nationality = Country.valueOf(resultSet.getString("nationality"));
                Person person = new Person(author, birthday, height, nationality);
                LabWork labWork = new LabWork(id, name, coordinates, creationDate, minimalpoint, description, difficulty, person);
                data.add(labWork);
            }
            return data;
        } catch (SQLException e) {
            Logger.getLogger(PostgreSQLManager.class.getName()).warning("Something wrong" + e.getMessage());
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public long writeObjToDB(LabWork labWork) {
        long generatedId = -1;
        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);

            generatedId = addElementToDB(labWork, connection);
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(PostgreSQLManager.class.getName()).warning("Something wrong" + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return generatedId;
    }

    @Override
    public void writeCollectionToDB() {
        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);
            Vector<Long> existedLabWorkId = new Vector<>();
            String getLabWorkIdQuery = "SELECT id FROM labwork";
            PreparedStatement getLabWorkIdStatement = connection.prepareStatement(getLabWorkIdQuery);
            ResultSet labWorkIdresult = getLabWorkIdStatement.executeQuery();
            while (labWorkIdresult.next()) {
                existedLabWorkId.add(labWorkIdresult.getLong("id"));
            }
            for (LabWork labwork : CollectionManager.getInstance().getLabWorkCollection()) {
                if (!existedLabWorkId.contains(labwork.getId())) {
                    labwork.setId(addElementToDB(labwork, connection));
                }
            }
            connection.commit();
        } catch (SQLException e) {
            Logger.getLogger(PostgreSQLManager.class.getName()).warning("Something wrong" + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public long addElementToDB(LabWork labWork, Connection connection) throws SQLException {
        long generatedId = -1;
        PreparedStatement inCoordStatement = null;
        PreparedStatement inLabWorkStatement = null;
        PreparedStatement inCreatorStatement = null;

        try {
            connection.setAutoCommit(false);
            String inCoordQuery = "INSERT INTO Coordinates (x, y) VALUES (?, ?) RETURNING id";
            inCoordStatement = connection.prepareStatement(inCoordQuery);
            inCoordStatement.setLong(1, labWork.getCoordinates().getX());
            inCoordStatement.setDouble(2, labWork.getCoordinates().getY());
            ResultSet coordResultSet = inCoordStatement.executeQuery();
            Logger.getLogger(PostgreSQLManager.class.getName()).info("Coordinates were added");

            int coordId = -1;
            if (coordResultSet.next()) {
                coordId = coordResultSet.getInt(1);
            }


            ZonedDateTime creationDate = labWork.getCreationDate();
            Timestamp timestamp = creationDate != null
                    ? Timestamp.from(creationDate.toInstant())
                    : new Timestamp(System.currentTimeMillis());

            String inLabWorkQuery = "INSERT INTO labwork (name, coordinates_id, creation_date, minimal_point, description, difficulty, author, birthday, height, nationality)" +
                    "VALUES (?, ?, ?, ?, ?, CAST(? AS difficulty_enum), ?, ?, ?, CAST(? AS country_enum)) RETURNING id";
            inLabWorkStatement = connection.prepareStatement(inLabWorkQuery);
            inLabWorkStatement.setString(1, labWork.getName());
            inLabWorkStatement.setInt(2, coordId);
            inLabWorkStatement.setTimestamp(3, timestamp);
            inLabWorkStatement.setLong(4, labWork.getMinimalPoint());
            inLabWorkStatement.setString(5, labWork.getDescription());
            inLabWorkStatement.setString(6, labWork.getDifficulty().toString());
            Person author = labWork.getAuthor();
            inLabWorkStatement.setString(7, author.getName());
            inLabWorkStatement.setDate(8, java.sql.Date.valueOf(author.getBirthday()));
            inLabWorkStatement.setDouble(9, author.getHeight());
            inLabWorkStatement.setString(10, author.getNationality().toString());
            ResultSet labworkResultSet = inLabWorkStatement.executeQuery();
            Logger.getLogger(PostgreSQLManager.class.getName()).info("labwork was added");
            if (!labworkResultSet.next()) {
                throw new SQLException("Failed to get labwork ID");
            }
            generatedId = labworkResultSet.getLong(1);
            Logger.getLogger(PostgreSQLManager.class.getName()).info("Labwork added with ID: " + generatedId);
            String inCreatorQuery = "INSERT INTO creator (user_id, labwork_id) VALUES (?, ?) ";
            inCreatorStatement = connection.prepareStatement(inCreatorQuery);
            inCreatorStatement.setLong(1, ClientHandler.getUserId());
            inCreatorStatement.setLong(2, generatedId);
            inCreatorStatement.executeUpdate();
            Logger.getLogger(PostgreSQLManager.class.getName()).info("creator was added");


            connection.commit();
        }catch (SQLException e) {
            Logger.getLogger(PostgreSQLManager.class.getName()).warning("Error adding element to DB" + e.getMessage());
            try {
                if (connection != null) {
                    connection.rollback();
                }
            } catch (SQLException se) {
                Logger.getLogger(PostgreSQLManager.class.getName()).warning("Error on rollback: " + se.getMessage());
            }
        } finally {
            try {
                if (inCoordStatement != null) inCoordStatement.close();
                if (inLabWorkStatement != null) inLabWorkStatement.close();
                if (inCreatorStatement != null) inCreatorStatement.close();
            } catch (SQLException e) {
                Logger.getLogger(PostgreSQLManager.class.getName()).warning("Error on close: " + e.getMessage());
            }
        }
        return generatedId;
    }
    public boolean removeLabWorkById(long labworkId) {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false);

            // 1. Получаем координаты для удаления
            Long coordinatesId = null;
            String selectQuery = "SELECT coordinates_id FROM labwork WHERE id = ?";
            try (PreparedStatement selectStmt = connection.prepareStatement(selectQuery)) {
                selectStmt.setLong(1, labworkId);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    coordinatesId = rs.getLong("coordinates_id");
                }
            }

            // 2. Удаляем из creator
            String deleteCreatorQuery = "DELETE FROM creator WHERE labwork_id = ? AND user_id = ?";
            try (PreparedStatement deleteCreatorStmt = connection.prepareStatement(deleteCreatorQuery)) {
                deleteCreatorStmt.setLong(1, labworkId);
                deleteCreatorStmt.setLong(2, ClientHandler.getUserId());
                deleteCreatorStmt.executeUpdate();
            }

            // 3. Удаляем из labwork
            String deleteLabWorkQuery = "DELETE FROM labwork WHERE id = ?";
            int labworkDeleted = 0;
            try (PreparedStatement deleteLabworkStmt = connection.prepareStatement(deleteLabWorkQuery)) {
                deleteLabworkStmt.setLong(1, labworkId);
                labworkDeleted = deleteLabworkStmt.executeUpdate();
            }

            // 4. Удаляем координаты если labwork был удален
            if (labworkDeleted > 0 && coordinatesId != null) {
                String deleteCoordQuery = "DELETE FROM coordinates WHERE id = ?";
                try (PreparedStatement deleteCoordStmt = connection.prepareStatement(deleteCoordQuery)) {
                    deleteCoordStmt.setLong(1, coordinatesId);
                    deleteCoordStmt.executeUpdate();
                }
            }

            connection.commit();
            return labworkDeleted > 0;
        } catch (SQLException e) {
            try {
                if (connection != null) connection.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(PostgreSQLManager.class.getName()).warning("Error on rollback: " + ex.getMessage());
            }
            Logger.getLogger(PostgreSQLManager.class.getName()).warning("Error removing labwork: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found", e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
            }
        }
    }
    public boolean isLabworkOwnedByUser(long labworkId) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            String checkOwnerQuery = "SELECT 1 FROM creator WHERE labwork_id = ? AND user_id = ? LIMIT 1";
            try (PreparedStatement checkOwnerStatement = connection.prepareStatement(checkOwnerQuery)) {
                checkOwnerStatement.setLong(1, labworkId);
                checkOwnerStatement.setLong(2, ClientHandler.getUserId());
                try (ResultSet resultSet = checkOwnerStatement.executeQuery()) {
                    return resultSet.next(); // Возвращает true если есть хотя бы одна запись
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(PostgreSQLManager.class.getName())
                    .warning("Error checking labwork ownership: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found", e);
        }
    }
    public long authUser(String name, char[] passwd) {
        try (Connection connection = ConnectionFactory.getConnection()) {

            String selectUserQuery = "SELECT id, passwd_hash, passwd_salt FROM users WHERE name = ?";
            PreparedStatement selectUserStatement = connection.prepareStatement(selectUserQuery);
            selectUserStatement.setString(1, name);
            ResultSet resultSet = selectUserStatement.executeQuery();

            if (resultSet.next()) {
                String passwdHash = resultSet.getString("passwd_hash");
                String passwdSalt = resultSet.getString("passwd_salt");
                String inputPasswdHash = PasswordHandler.hashPassword(passwd, passwdSalt);

                if (passwdHash.equals(inputPasswdHash)) {
                    return resultSet.getLong("id");
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(PostgreSQLManager.class.getName()).warning("Something went wrong ");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
    public long regUser(String name, char[] passwd) {
        try (Connection connection = ConnectionFactory.getConnection()) {

            String selectUserQuery = "SELECT COUNT(*) FROM users WHERE name = ?";
            PreparedStatement selectUserStatement = connection.prepareStatement(selectUserQuery);
            selectUserStatement.setString(1, name);
            ResultSet resultSet = selectUserStatement.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                return -1;
            }

            SecureRandom random = new SecureRandom();
            byte[] saltBytes = new byte[16];
            random.nextBytes(saltBytes);
            String salt = Base64.getEncoder().encodeToString(saltBytes);

            String passwdHash = PasswordHandler.hashPassword(passwd, salt);

            String inUserQuery = "INSERT INTO users (name, passwd_hash, passwd_salt) VALUES (?, ?, ?)";
            PreparedStatement inUserStatement = connection.prepareStatement(inUserQuery, Statement.RETURN_GENERATED_KEYS);
            inUserStatement.setString(1, name);
            inUserStatement.setString(2, passwdHash);
            inUserStatement.setString(3, salt);

            int rowsAffected = inUserStatement.executeUpdate();
            if (rowsAffected > 0) {
                ResultSet generatedKeys = inUserStatement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    return generatedKeys.getLong(1);
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(PostgreSQLManager.class.getName()).warning("Something wrong");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
    public List<Long> clearLabWorkForUser() {
        long userId = ClientHandler.getUserId();
        List<Long> deletedLabworkIds = new ArrayList<>();
        Connection connection = null;

        try {
            connection = ConnectionFactory.getConnection();
            connection.setAutoCommit(false); // Начинаем транзакцию

            // 1. Получаем все labwork_id для данного пользователя
            String selectQuery = "SELECT labwork_id FROM creator WHERE user_id = ?";
            PreparedStatement selectStmt = connection.prepareStatement(selectQuery);
            selectStmt.setLong(1, userId);
            ResultSet rs = selectStmt.executeQuery();

            List<Long> labworkIds = new ArrayList<>();
            while (rs.next()) {
                long labworkId = rs.getLong("labwork_id");
                labworkIds.add(labworkId);
                deletedLabworkIds.add(labworkId);
            }

            if (!labworkIds.isEmpty()) {
                // 2. Сначала удаляем записи из таблицы creator
                String deleteCreatorQuery = "DELETE FROM creator WHERE user_id = ?";
                try (PreparedStatement deleteCreatorStmt = connection.prepareStatement(deleteCreatorQuery)) {
                    deleteCreatorStmt.setLong(1, userId);
                    int deletedCreatorRows = deleteCreatorStmt.executeUpdate();
                    System.out.println("Deleted from creator: " + deletedCreatorRows);
                }

                // 3. Теперь можем безопасно удалить из labwork
                String deleteLabWorkQuery = "DELETE FROM labwork WHERE id = ANY(?)";
                try (PreparedStatement deleteLabworkStmt = connection.prepareStatement(deleteLabWorkQuery)) {
                    deleteLabworkStmt.setArray(1,
                            connection.createArrayOf("bigint", labworkIds.toArray()));
                    int deletedLabworks = deleteLabworkStmt.executeUpdate();
                    System.out.println("Deleted from labwork: " + deletedLabworks);
                }

                // 4. Получаем coordinates_id для удаления
                String selectCoordsQuery = "SELECT coordinates_id FROM labwork WHERE id = ANY(?)";
                try (PreparedStatement selectCoordsStmt = connection.prepareStatement(selectCoordsQuery)) {
                    selectCoordsStmt.setArray(1,
                            connection.createArrayOf("bigint", labworkIds.toArray()));
                    ResultSet coordsRs = selectCoordsStmt.executeQuery();

                    List<Long> coordIds = new ArrayList<>();
                    while (coordsRs.next()) {
                        coordIds.add(coordsRs.getLong("coordinates_id"));
                    }

                    // 5. Удаляем координаты
                    if (!coordIds.isEmpty()) {
                        String deleteCoordsQuery = "DELETE FROM coordinates WHERE id = ANY(?)";
                        try (PreparedStatement deleteCoordsStmt = connection.prepareStatement(deleteCoordsQuery)) {
                            deleteCoordsStmt.setArray(1,
                                    connection.createArrayOf("bigint", coordIds.toArray()));
                            int deletedCoords = deleteCoordsStmt.executeUpdate();
                            System.out.println("Deleted from coordinates: " + deletedCoords);
                        }
                    }
                }
            }

            connection.commit(); // Подтверждаем транзакцию
            System.out.println("Successfully deleted " + deletedLabworkIds.size() + " labworks for user " + userId);

        } catch (SQLException e) {
            try {
                if (connection != null) {
                    connection.rollback(); // Откатываем при ошибке
                }
            } catch (SQLException ex) {
                Logger.getLogger(PostgreSQLManager.class.getName())
                        .warning("Rollback failed: " + ex.getMessage());
            }
            Logger.getLogger(PostgreSQLManager.class.getName())
                    .warning("Error clearing labworks: " + e.getMessage());
            deletedLabworkIds.clear();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found", e);
        } finally {
            try {
                if (connection != null) {
                    connection.setAutoCommit(true); // Возвращаем авто-коммит
                    connection.close();
                }
            } catch (SQLException e) {
                Logger.getLogger(PostgreSQLManager.class.getName())
                        .warning("Error closing connection: " + e.getMessage());
            }
        }

        return deletedLabworkIds;
    }
    public boolean updateLabWork(LabWork labWork) {
        try (Connection connection = ConnectionFactory.getConnection()) {
            connection.setAutoCommit(false);

            // 1. Обновляем координаты (исправленный запрос)
            String updateCoordQuery = "UPDATE coordinates SET x = ?, y = ? " +
                    "WHERE id = (SELECT coordinates_id FROM labwork WHERE id = ?)";
            try (PreparedStatement updateCoordStatement = connection.prepareStatement(updateCoordQuery)) {
                updateCoordStatement.setLong(1, labWork.getCoordinates().getX());
                updateCoordStatement.setDouble(2, labWork.getCoordinates().getY());
                updateCoordStatement.setLong(3, labWork.getId());
                updateCoordStatement.executeUpdate();
            }

            // 2. Обновляем labwork
            String updateLabWorkQuery = "UPDATE labwork SET name = ?, creation_date = ?, minimal_point = ?, " +
                    "description = ?, difficulty = CAST(? AS difficulty_enum), " +
                    "author = ?, birthday = ?, height = ?, nationality = CAST(? AS country_enum) " +
                    "WHERE id = ?";

            try (PreparedStatement updateLabWorkStatement = connection.prepareStatement(updateLabWorkQuery)) {
                // Преобразование ZonedDateTime в Timestamp
                Timestamp timestamp = labWork.getCreationDate() != null
                        ? Timestamp.from(labWork.getCreationDate().toInstant())
                        : new Timestamp(System.currentTimeMillis());

                Person author = labWork.getAuthor();

                updateLabWorkStatement.setString(1, labWork.getName());
                updateLabWorkStatement.setTimestamp(2, timestamp);
                updateLabWorkStatement.setLong(3, labWork.getMinimalPoint());
                updateLabWorkStatement.setString(4, labWork.getDescription());
                updateLabWorkStatement.setString(5, labWork.getDifficulty().toString());
                updateLabWorkStatement.setString(6, author.getName());
                updateLabWorkStatement.setDate(7, java.sql.Date.valueOf(author.getBirthday()));
                updateLabWorkStatement.setDouble(8, author.getHeight());
                updateLabWorkStatement.setString(9, author.getNationality() != null
                        ? author.getNationality().toString()
                        : null);
                updateLabWorkStatement.setLong(10, labWork.getId());

                updateLabWorkStatement.executeUpdate();
            }

            connection.commit();
            return true;
        } catch (SQLException e) {
            Logger.getLogger(PostgreSQLManager.class.getName())
                    .warning("Error updating labwork: " + e.getMessage());
            return false;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found", e);
        }
    }


}