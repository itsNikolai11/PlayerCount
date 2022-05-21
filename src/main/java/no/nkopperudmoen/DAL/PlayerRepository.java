package no.nkopperudmoen.DAL;

import java.sql.Connection;

public class PlayerRepository {
    private final Connection connection;

    public PlayerRepository(Connection connection) {
        this.connection = connection;
    }

}
