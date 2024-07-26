package cn.cutemic.konata.forge;

import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cn.cutemic.konata.event.EventDispatcher;
import cn.cutemic.konata.event.events.EventAttack;

public class ForgeEventAPI {

    @SubscribeEvent
    public void onAttack(AttackEntityEvent e){
        EventDispatcher.dispatchEvent(new EventAttack(e.target));
    }

}
