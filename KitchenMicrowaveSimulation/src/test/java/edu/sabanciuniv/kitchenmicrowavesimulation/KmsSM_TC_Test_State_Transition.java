package edu.sabanciuniv.kitchenmicrowavesimulation;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.Alphanumeric.class)
public class KmsSM_TC_Test_State_Transition {

    KitchenMicrowave kms = KitchenMicrowave.getInstance();

    @Test
    void KMS_TC_01_Transition_State_to_READY_with_event_ac_plugged(){
        assertEquals(kms.getState(), KitchenMicrowave.State.READY);
    }

    @Test
    void KMS_TC_02_Transition_State_from_READY_to_COOKING_with_event_cooking_started(){
        // preparation
        backToReadyState();
        assertEquals(kms.getState(), KitchenMicrowave.State.READY);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.cooking_started), KitchenMicrowave.State.COOKING);
    }

    @Test
    void KMS_TC_03_Transition_State_from_READY_to_HATCH_OPEN_with_event_hatch_opened(){
        // preparation
        backToReadyState();
        assertEquals(kms.getState(), KitchenMicrowave.State.READY);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.hatch_opened), KitchenMicrowave.State.HATCH_OPEN);
    }

    @Test
    void KMS_TC_04_Transition_State_from_COOKING_to_READY_with_event_countdown_end(){
        // preparation
        backToReadyState();
        kms.handleEvent(KitchenMicrowave.Event.cooking_started);
        assertEquals(kms.getState(), KitchenMicrowave.State.COOKING);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.countdown_end), KitchenMicrowave.State.READY);
    }

    @Test
    void KMS_TC_05_Transition_State_from_COOKING_to_READY_with_event_countdown_cancelled(){
        // preparation
        backToReadyState();
        kms.handleEvent(KitchenMicrowave.Event.cooking_started);
        assertEquals(kms.getState(), KitchenMicrowave.State.COOKING);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.countdown_cancelled), KitchenMicrowave.State.READY);
    }

    @Test
    void KMS_TC_06_Transition_State_from_COOKING_to_COOKING_PAUSE_with_event_hatch_opened(){
        // preparation
        backToReadyState();
        kms.handleEvent(KitchenMicrowave.Event.cooking_started);
        assertEquals(kms.getState(), KitchenMicrowave.State.COOKING);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.hatch_opened), KitchenMicrowave.State.COOKING_PAUSE);
    }
    @Test
    void KMS_TC_07_Transition_State_from_HATCH_OPEN_to_READY_with_event_hatch_closed(){
        // preparation
        backToReadyState();
        kms.handleEvent(KitchenMicrowave.Event.hatch_opened);
        assertEquals(kms.getState(), KitchenMicrowave.State.HATCH_OPEN);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.hatch_closed), KitchenMicrowave.State.READY);
    }

    @Test
    void KMS_TC_08_Transition_State_from_COOKING_PAUSE_to_COOKING_with_event_hatch_closed(){
        // preparation
        backToReadyState();
        kms.handleEvent(KitchenMicrowave.Event.cooking_started);
        kms.handleEvent(KitchenMicrowave.Event.hatch_opened);
        assertEquals(kms.getState(), KitchenMicrowave.State.COOKING_PAUSE);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.hatch_closed), KitchenMicrowave.State.COOKING);
    }

    @Test
    void KMS_TC_09_Transition_State_from_COOKING_PAUSE_to_HATCH_OPEN_with_event_countdown_cancelled(){
        // preparation
        backToReadyState();
        kms.handleEvent(KitchenMicrowave.Event.cooking_started);
        kms.handleEvent(KitchenMicrowave.Event.hatch_opened);
        assertEquals(kms.getState(), KitchenMicrowave.State.COOKING_PAUSE);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.countdown_cancelled), KitchenMicrowave.State.HATCH_OPEN);
    }

    @Test
    void KMS_TC_10_Trigger_unsupported_events_for_READY_state(){
        // test data
        KitchenMicrowave.State initialState = KitchenMicrowave.State.READY;
        List<KitchenMicrowave.Event> supportedEvents = new ArrayList<>();
        supportedEvents.add(KitchenMicrowave.Event.hatch_opened);
        supportedEvents.add(KitchenMicrowave.Event.cooking_started);

        // preparation
        backToReadyState();
        assertEquals(kms.getState(), initialState);

        // test
        for (KitchenMicrowave.Event event : KitchenMicrowave.Event.values()) {
            if (!supportedEvents.contains(event)){
                kms.handleEvent(event);
                assertEquals(kms.getState(), initialState);
            }
        }
    }

    @Test
    void KMS_TC_11_Trigger_unsupported_events_for_COOKING_state(){
        // test data
        KitchenMicrowave.State initialState = KitchenMicrowave.State.COOKING;
        List<KitchenMicrowave.Event> supportedEvents = new ArrayList<>();
        supportedEvents.add(KitchenMicrowave.Event.hatch_opened);
        supportedEvents.add(KitchenMicrowave.Event.countdown_end);
        supportedEvents.add(KitchenMicrowave.Event.countdown_cancelled);

        // preparation
        backToReadyState();
        kms.handleEvent(KitchenMicrowave.Event.cooking_started);
        assertEquals(kms.getState(), initialState);

        // test
        for (KitchenMicrowave.Event event : KitchenMicrowave.Event.values()) {
            if (!supportedEvents.contains(event)){
                kms.handleEvent(event);
                assertEquals(kms.getState(), initialState);
            }
        }
    }

    @Test
    void KMS_TC_12_Trigger_unsupported_events_for_HATCH_OPEN_state(){
        // test data
        KitchenMicrowave.State initialState = KitchenMicrowave.State.HATCH_OPEN;
        List<KitchenMicrowave.Event> supportedEvents = new ArrayList<>();
        supportedEvents.add(KitchenMicrowave.Event.hatch_closed);

        // preparation
        backToReadyState();
        kms.handleEvent(KitchenMicrowave.Event.hatch_opened);
        assertEquals(kms.getState(), initialState);

        // test
        for (KitchenMicrowave.Event event : KitchenMicrowave.Event.values()) {
            if (!supportedEvents.contains(event)){
                kms.handleEvent(event);
                assertEquals(kms.getState(), initialState);
            }
        }
    }

    @Test
    void KMS_TC_13_Trigger_unsupported_events_for_COOKING_PAUSE_state(){
        // test data
        KitchenMicrowave.State initialState = KitchenMicrowave.State.COOKING_PAUSE;
        List<KitchenMicrowave.Event> supportedEvents = new ArrayList<>();
        supportedEvents.add(KitchenMicrowave.Event.hatch_closed);
        supportedEvents.add(KitchenMicrowave.Event.countdown_cancelled);

        // preparation
        backToReadyState();
        kms.handleEvent(KitchenMicrowave.Event.cooking_started);
        kms.handleEvent(KitchenMicrowave.Event.hatch_opened);
        assertEquals(kms.getState(), initialState);

        // test
        for (KitchenMicrowave.Event event : KitchenMicrowave.Event.values()) {
            if (!supportedEvents.contains(event)){
                kms.handleEvent(event);
                assertEquals(kms.getState(), initialState);
            }
        }
    }

    @Test
    void KMS_TC_14_set_timer_10_sec_wait_until_timeout() throws InterruptedException {
        // preparation
        int cookDuration = 10;
        backToReadyState();
        assertEquals(kms.getState(), KitchenMicrowave.State.READY);
        kms.setTimer(cookDuration);
        assertEquals(kms.getTimer(), cookDuration);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.cooking_started), KitchenMicrowave.State.COOKING);
        Thread.sleep(cookDuration*1000);

        Thread.sleep((500)); // just in case
        assertEquals(kms.getState(), KitchenMicrowave.State.READY);
    }

    @Test
    void KMS_TC_15_set_timer_10_sec_start_cooking_stop_before_timeout() throws InterruptedException {
        // preparation
        int cookDuration = 10;
        int halfCookDuration = cookDuration/2;

        backToReadyState();
        assertEquals(kms.getState(), KitchenMicrowave.State.READY);
        kms.setTimer(cookDuration);
        assertEquals(kms.getTimer(), cookDuration);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.cooking_started), KitchenMicrowave.State.COOKING);
        Thread.sleep(halfCookDuration*1000);
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.countdown_cancelled), KitchenMicrowave.State.READY);

        Thread.sleep((500)); // just in case
    }

    @Test
    void KMS_TC_16_set_timer_10_sec_stop_wait_and_start_again() throws InterruptedException {
        // preparation
        int cookDuration = 10;
        int halfCookDuration = cookDuration/2;

        backToReadyState();
        assertEquals(kms.getState(), KitchenMicrowave.State.READY);
        kms.setTimer(cookDuration);
        assertEquals(kms.getTimer(), cookDuration);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.cooking_started), KitchenMicrowave.State.COOKING);
        Thread.sleep(halfCookDuration*1000);
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.countdown_cancelled), KitchenMicrowave.State.READY);

        waitForaWhile();

        assertEquals(kms.handleEvent(KitchenMicrowave.Event.cooking_started), KitchenMicrowave.State.COOKING);
        Thread.sleep((halfCookDuration*1000));

        Thread.sleep((1000)); // just in case
        assertEquals(kms.getState(), KitchenMicrowave.State.READY);
    }

    @Test
    void KMS_TC_17_set_timer_10_sec_hatch_opened_wait_and_hatch_closed_again() throws InterruptedException {
        // preparation
        int cookDuration = 10;
        int halfCookDuration = cookDuration/2;

        backToReadyState();
        assertEquals(kms.getState(), KitchenMicrowave.State.READY);
        kms.setTimer(cookDuration);
        assertEquals(kms.getTimer(), cookDuration);

        // test
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.cooking_started), KitchenMicrowave.State.COOKING);
        Thread.sleep((halfCookDuration*1000));
        assertEquals(kms.handleEvent(KitchenMicrowave.Event.hatch_opened), KitchenMicrowave.State.COOKING_PAUSE);

        waitForaWhile();

        assertEquals(kms.handleEvent(KitchenMicrowave.Event.hatch_closed), KitchenMicrowave.State.COOKING);
        Thread.sleep((halfCookDuration*1000));

        Thread.sleep((1000)); // just in case
        assertEquals(kms.getState(), KitchenMicrowave.State.READY);
    }

    void backToReadyState(){
        System.out.println("\n...Backing to initial state for new TC...");
        switch (kms.getState()) {
            case COOKING:
                kms.handleEvent(KitchenMicrowave.Event.countdown_cancelled);
                break;
            case HATCH_OPEN:
                kms.handleEvent(KitchenMicrowave.Event.hatch_closed);
                break;
            case COOKING_PAUSE:
                kms.handleEvent(KitchenMicrowave.Event.countdown_cancelled);
                kms.handleEvent(KitchenMicrowave.Event.hatch_closed);
                break;
        }
    }
    void waitForaWhile() throws InterruptedException {
        int waitingTime = 3;
        System.out.flush();
        System.out.println("<<<Waiting " + waitingTime + " seconds...>>>");
        Thread.sleep((waitingTime*1000));
    }
}