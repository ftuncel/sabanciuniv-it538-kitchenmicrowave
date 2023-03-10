package edu.sabanciuniv.kitchenmicrowavesimulation;

// Kitchen Microwave State Machine - Model Version: 2023.03-1
// KmsSM is a Singleton class

public class KitchenMicrowave {

    //--------------------
    // static attributes
    private static KitchenMicrowave _instance = null; // for Singleton
    public static enum Event {
        ac_plugged,
        hatch_opened,
        hatch_closed,
        cooking_started,
        countdown_cancelled,
        countdown_end
    };
    public static enum State {
        READY,
        COOKING,
        HATCH_OPEN,
        COOKING_PAUSE
    }

    //--------------------
    // static methods
    public static KitchenMicrowave getInstance () {
        System.out.println("KmsSM getInstance method is called.");
        if (_instance == null) {
            _instance = new KitchenMicrowave();
            _instance.init(); // set state to POWER_OFF
        }
        return _instance;
    }
    //--------------------
    // instance attributes
    private State currentState;
    private CookingTimer cookingTimer;

    public int getTimer() {
        return cookingTimer.getTime();
    }

    public void setTimer(int second) {
        this.cookingTimer = new CookingTimer(second);
    }

    //--------------------
    // instance methots
    private KitchenMicrowave() { // default CTOR is restricted for Singleton
        System.out.println("KmsSM Singleton instance is created.");
    }

    private void init() {
        setState(State.READY); // default transition
        this.cookingTimer = new CookingTimer(0);
        System.out.println("Initial State: " + getState());
    }

    public State getState() {
        return this.currentState;   // default transition
    }

    protected boolean setState(State state) {
        // System.out.println("DEBUG setState: " + state);
        this.currentState = state;
        return true;
    }

    public State handleEvent(Event e) {
        boolean handlerResult = true;

        switch (this.currentState) {
            case READY:
                handlerResult = handleEventForReady(e);
                break;
            case COOKING:
                handlerResult = handleEventForCooking(e);
                break;
            case HATCH_OPEN:
                handlerResult = handleEventForHatchOpen(e);
                break;
            case COOKING_PAUSE:
                handlerResult = handleEventForCookingPause(e);
                break;
        }

        if (handlerResult) {
            print_success(e);
        } else {
            print_warning(e);
        }

        return getState();
    }

    public boolean handleEventForReady(Event e) {
        switch(e){
            case cooking_started:
                processCooking();
                break;
            case hatch_opened:
                setState(State.HATCH_OPEN);
                break;
            default:
                return false;
        }
        return true;
    }

    private void processCooking() {
        setState(State.COOKING);
        if (cookingTimer.getTime() > 0){
            cookingTimer.start();
        }
    }

    public boolean handleEventForCooking(Event e) {
        switch(e){
            case countdown_end:
                setState(State.READY);
                break;
            case countdown_cancelled:
                cookingTimer.pause();
                setState(State.READY);
                break;
            case hatch_opened:
                cookingTimer.pause();
                setState(State.COOKING_PAUSE);
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean handleEventForHatchOpen(Event e) {
        switch(e){
            case hatch_closed:
                setState(State.READY);
                break;
            default:
                return false;
        }
        return true;
    }

    public boolean handleEventForCookingPause(Event e) {
        switch(e){
            case hatch_closed:
                processCooking();
                break;
            case countdown_cancelled:
                setState(State.HATCH_OPEN);
                break;
            default:
                return false;
        }
        return true;
    }

    private void print_warning(Event e) {
        System.out.println("!!! Invalid event request: " + e + " for State: " + currentState);
    }

    private void print_success(Event e) {
        System.out.println("Event handled successfully: " + e + ". New State: " + currentState);
    }
}