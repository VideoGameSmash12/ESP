package me.videogamesm12.esp;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.User;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import me.totalfreedom.totalfreedommod.TotalFreedomMod;

import java.util.UUID;

public final class EssentialsSpectatorPatch extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        getLogger().info("This is a temporary patch for the known TPToggle bypass exploit caused by Spectator Mode and BuildersUtilities.");
        getLogger().info("When we migrate to 1.19.4 with the permission nodes, do not use this plugin anymore.");
        getLogger().info("Kind regards, Video");

        Essentials essentials = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Client.SPECTATE)
        {
            @Override
            public void onPacketReceiving(PacketEvent event)
            {
                if (event.getPacketType() == PacketType.Play.Client.SPECTATE)
                {
                    UUID uuid = event.getPacket().getUUIDs().readSafely(0);
                    if (uuid == null)
                    {
                        return;
                    }

                    User target = essentials.getUser(uuid);
                    if (target == null)
                    {
                        return;
                    }

                    if (!target.isTeleportEnabled() && !TotalFreedomMod.getPlugin().al.isAdmin(event.getPlayer()))
                    {
                        String message = I18n.tl("teleportDisabled", target.getDisplayName());
                        event.getPlayer().sendMessage(I18n.tl("errorWithMessage", message));
                        //--
                        event.setCancelled(true);
                    }
                }
            }
        });
    }

    @Override
    public void onDisable()
    {
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
    }
}
