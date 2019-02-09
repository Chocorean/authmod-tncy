package io.chocorean.authmod.guard;

import io.chocorean.authmod.guard.authentication.LoginPayload;
import io.chocorean.authmod.guard.registration.RegistrationPayload;
import io.chocorean.authmod.model.IPlayer;
import io.chocorean.authmod.model.Player;

public class PlayerFactory {

    public static IPlayer createFromRegistrationPayload(RegistrationPayload payload) {
        IPlayer player = new Player();
        player.setUsername(payload.getUsername());
        player.setEmail(payload.getEmail());
        player.setUuid(payload.getUuid());
        player.setPassword(payload.getPassword());
        return player;
    }

    public static IPlayer createFromLoginPayload(LoginPayload payload) {
        IPlayer player = new Player();
        player.setUuid(payload.getUuid());
        player.setEmail(payload.getEmail());
        player.setUsername(payload.getUsername());
        player.setPassword(payload.getPassword());
        return player;
    }
}