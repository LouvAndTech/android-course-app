package com.example.calorie;

public enum Gender {
    MEN,
    WOMEN;

    public static Gender getGender(Integer value) throws IllegalArgumentException{
        switch (value){
            case 0:
                return MEN;
            default:
                return WOMEN;
        }
    }

    public String getPronom(){
        switch (this){
            case MEN:
                return "Sir";
            case WOMEN:
                return "Madam";
        }
        return null;
    }
}
