package no.nkopperudmoen.Tasks;

import no.nkopperudmoen.DAL.PlayerController;
import no.nkopperudmoen.UTIL.MESSAGES;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

public class OntimeTaskManager {
    private static final HashMap<UUID, TimerTask> activeTasks = new HashMap<>();

    /*
    Create and run an ontime tracking task for the player
     */
    public static void createTask(UUID uuid) {
        TimerTask increaseOntime = new TimerTask() {
            @Override
            public void run() {
                PlayerController controller = PlayerController.getInstance();
                int ontime = controller.getTotalOntime(uuid);
                ontime += 5;
                controller.setTotalOntime(uuid, ontime);
            }
        };
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(increaseOntime, 300000, 300000);

        /*BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(MESSAGES.plugin, () -> {
            PlayerController controller = PlayerController.getInstance();
            int ontime = controller.getTotalOntime(uuid);
            ontime += 5;
            controller.setTotalOntime(uuid, ontime);
        }, 6000, 6000);*/
        activeTasks.put(uuid, increaseOntime);
    }

    /*
    Cancel any tracking tasks related to the player
     */
    public static void cancelTask(UUID uuid) {
        TimerTask task = activeTasks.get(uuid);
        task.cancel();
        activeTasks.remove(uuid);

    }
}
