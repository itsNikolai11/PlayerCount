package no.nkopperudmoen.DAL;

public class PlayerController {
    private PlayerRepository repo;

    public PlayerController(PlayerRepository repo) {
        this.repo = repo;
    }
}
