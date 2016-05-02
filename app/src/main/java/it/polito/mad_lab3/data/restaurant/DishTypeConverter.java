package it.polito.mad_lab3.data.restaurant;

/**
 * Created by f.germano on 02/05/2016.
 */
public class DishTypeConverter {

    public static String fromEnumToString(DishType dishType){
        switch (dishType){
            case Dessert:
                return "Dessert";
            case MainCourses:
                return "Main courses";
            case SecondCourses:
                return "Second courses";
            case Other:
                return "Other";
            default:
                return null;
        }
    }

    public static DishType fromStringToEnum(String stringEnum){
        switch (stringEnum){
            case "Dessert":
                return DishType.Dessert;
            case "Main courses":
                return DishType.MainCourses;
            case "Second courses":
                return DishType.SecondCourses;
            case "Other":
                return DishType.Other;
            default:
                return null;
        }
    }
}
