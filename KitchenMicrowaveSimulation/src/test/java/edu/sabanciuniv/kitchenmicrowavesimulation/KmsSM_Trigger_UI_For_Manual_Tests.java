package edu.sabanciuniv.kitchenmicrowavesimulation;

import edu.sabanciuniv.kitchenmicrowavesimulation.UI.KitchenMicrowaveUI;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import static org.junit.jupiter.api.Assertions.assertEquals;


@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class KmsSM_Trigger_UI_For_Manual_Tests {

    @Test
    void KMS_TC_00_Transition_State_to_READY_with_event_ac_plugged() throws InterruptedException {
        KitchenMicrowaveUI kmsUI = new KitchenMicrowaveUI();
        while (kmsUI != null) {
            Thread.sleep((1000));
        }
        assertEquals(kmsUI, null);
    }
}