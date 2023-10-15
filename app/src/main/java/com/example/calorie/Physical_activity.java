package com.example.calorie;

public enum Physical_activity {
    SEDENTARY,
    ACTIVE,
    SPORTIVE;

    public static Physical_activity getActive(Integer value) throws IllegalArgumentException{
        switch (value){
            case 0:
                return SEDENTARY;
            case 1:
                return ACTIVE;
            case 2:
                return SPORTIVE;
            default:
                throw new IllegalArgumentException("Invalid value for Physical_activity");
        }
    }

    public String toString(){
        switch (this){
            case SEDENTARY:
                return "Sedentary";
            case ACTIVE:
                return "Active";
            case SPORTIVE:
                return "Sportive";
            default:
                throw new IllegalArgumentException("Invalid value for Physical_activity");
        }
    }

}
