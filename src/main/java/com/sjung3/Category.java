package com.sjung3;

public enum Category {
    FOOD,
    ENTERTAINMENT,
    ACCOMMODATION,
    TRANSPORT,
    MISC;

    private Category() {
    }

    public String toString() {
        switch(this) {
            case FOOD:
                return "Food";
            case ENTERTAINMENT:
                return "Entertainment";
            case ACCOMMODATION:
                return "Accommodation";
            case TRANSPORT:
                return "Transport";
            case MISC:
                return "Misc";
            default:
                return "Unspecified";
        }
    }
}

